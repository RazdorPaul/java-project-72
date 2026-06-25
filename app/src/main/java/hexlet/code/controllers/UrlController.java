package hexlet.code.controllers;

import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.dto.PageResultCheck;
import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repositories.UrlCheckRepository;
import hexlet.code.repositories.UrlRepository;
import hexlet.code.services.PageChecker;
import hexlet.code.utils.NamedRoutes;
import hexlet.code.utils.UrlUtils;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import static io.javalin.rendering.template.TemplateUtil.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public final class UrlController {
    private final UrlRepository urlRepository;
    private final UrlCheckRepository checkRepository;
    private final PageChecker pageChecker;

    public UrlController(HikariDataSource data) {
        //urlRepository = new UrlRepository(data);
        //checkRepository = new UrlCheckRepository(data);
        //pageChecker = new PageChecker();
        this(data, new PageChecker());
    }

    UrlController(HikariDataSource data, PageChecker checker) {
        this.urlRepository = new UrlRepository(data);
        this.checkRepository = new UrlCheckRepository(data);
        this.pageChecker = checker;
    }

    public void index(Context ctx) throws SQLException {
        var urls = urlRepository.findAll();
        for (var url : urls) {
            var check = checkRepository.findLastByUrlId(url.getId());
            check.ifPresent(urlCheck -> url.setChecks(List.of(urlCheck)));
        }
        var page = new UrlsPage(urls, null, "Сайты");
        ctx.render(NamedRoutes.urlsIndexPath(), model("page", page));
    }

    public void show(Context ctx) throws SQLException {
        var url = urlRepository.findByIdWithChecks(ctx.pathParamAsClass("id", Long.class)
                .get(), checkRepository)
                .orElseThrow(() -> new NotFoundResponse("Такого сайта в базе нет!"));
        Map<String, String> flash = ctx.consumeSessionAttribute("flash");
        var title = url.getName();
        var page = new UrlPage(url, flash, title);
        var pageCheck = new PageResultCheck(url.getChecks());
        ctx.render(NamedRoutes.urlShowPath(), model("page", page, "pageCheck", pageCheck));
    }

    public void create(Context ctx) throws SQLException {
        var urlString = ctx.formParam("url");
        try {
            urlString = UrlUtils.getBaseUrl(urlString);
        } catch (Exception e) {
            ctx.sessionAttribute("flash", Map.of(
                    "message", "Некорректный URL",
                    "type", "danger"
            ));
            ctx.status(422);
            ctx.render(NamedRoutes.indexPath(),
                    model("flash", ctx.consumeSessionAttribute("flash")));
            return;
        }
        var existUrl = urlRepository.findByName(urlString);
        Long id;
        if (existUrl.isPresent()) {
            ctx.sessionAttribute("flash", Map.of(
                    "message", "Страница уже существует",
                    "type", "warning"
            ));
            id = existUrl.get().getId();
        } else {
            id = urlRepository.save(new Url(urlString));
            ctx.sessionAttribute("flash", Map.of(
                    "message", "Страница успешно добавлена",
                    "type", "success"
            ));
            try {
                var checker = this.pageChecker.check(urlString);
                checker.setUrlId(id);
                checker.setId(checkRepository.save(checker));
            } catch (Exception e) {
                System.err.println("Ошибка при первой проверке URL: " + e.getMessage());
            }
        }
        ctx.redirect(NamedRoutes.urlPath(id));
    }

    public void check(Context ctx) throws SQLException {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();
        var url = urlRepository.findById(urlId);
        try {
            var checker = this.pageChecker.check(url.get().getName());
            checker.setUrlId(urlId);
            checker.setId(checkRepository.save(checker));
            ctx.sessionAttribute("flash", Map.of(
                    "message", "Страница успешно проверена",
                    "type", "success"
            ));
        } catch (Exception e) {
            ctx.sessionAttribute("flash", Map.of(
                    "message", "Произошла ошибка при проверке",
                    "type", "danger"
            ));
            System.err.println("Ошибка при проверке URL: " + e.getMessage());
        }
        ctx.redirect(NamedRoutes.urlPath(urlId));
    }
}
