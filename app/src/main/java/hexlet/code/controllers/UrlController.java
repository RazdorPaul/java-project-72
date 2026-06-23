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

public final class UrlController {
    private final UrlRepository urlRepository;
    private final UrlCheckRepository checkRepository;

    public UrlController(HikariDataSource data) {
        urlRepository = new UrlRepository(data);
        checkRepository = new UrlCheckRepository(data);
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
        String flash = ctx.consumeSessionAttribute("flash");
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
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.status(422);
            ctx.render(NamedRoutes.indexPath(),
                    model("flash", ctx.consumeSessionAttribute("flash")));
            return;
        }
        var existUrl = urlRepository.findByName(urlString);
        Long id;
        if (existUrl.isPresent()) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            id = existUrl.get().getId();
        } else {
            id = urlRepository.save(new Url(urlString));
            ctx.sessionAttribute("flash", "Страница успешно добавлена");
            try {
                var checker = new PageChecker();
                var check = checker.check(urlString);
                check.setUrlId(id);
                check.setId(checkRepository.save(check));
            } catch (Exception e) {
                //ctx.sessionAttribute("flash", "Произошла ошибка при проверке");

            }
        }
        ctx.redirect(NamedRoutes.urlPath(id));
    }

    public void check(Context ctx) throws SQLException {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();
        var url = urlRepository.findById(urlId);
        var pageChecker = new PageChecker();
        try {
            var check = pageChecker.check(url.get().getName());
            check.setUrlId(urlId);
            check.setId(checkRepository.save(check));
            ctx.sessionAttribute("flash", "Страница успешно проверена");
        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Произошла ошибка при проверке");
        }
        ctx.redirect(NamedRoutes.urlPath(urlId));
    }
}
