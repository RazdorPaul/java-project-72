package hexlet.code.controllers;

import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repositories.UrlRepository;
import hexlet.code.utils.NamedRoutes;
import hexlet.code.utils.UrlUtils;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import static io.javalin.rendering.template.TemplateUtil.model;

import java.sql.SQLException;

public final class UrlController {
    private final UrlRepository urlRepository;

    public UrlController(HikariDataSource data) {
        urlRepository = new UrlRepository(data);
    }

    public void index(Context ctx) throws SQLException {
        var urls = urlRepository.findAll();
        var page = new UrlsPage(urls, "Сайты", null);
        ctx.render(NamedRoutes.urlsIndexPath(), model("page", page));
    }

    public void show(Context ctx) throws SQLException {
        var url = urlRepository.findById(ctx.pathParamAsClass("id", Long.class)
                .get()).orElseThrow(() -> new NotFoundResponse("Такого сайта в базе нет!"));
        String flash = ctx.consumeSessionAttribute("flash");
        var title = url.getName();
        var page = new UrlPage(url, flash, title);
        ctx.render(NamedRoutes.urlShowPath(), model("page", page));
    }

    public void create(Context ctx) throws SQLException {
        var urlString = ctx.formParam("url");
        System.out.println("DEBUG: urlString = " + urlString);
        try {
            urlString = UrlUtils.getBaseUrl(urlString);
        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.status(422);
            ctx.render(NamedRoutes.indexPath());
            return;
        }
        var existUrl = urlRepository.findByName(urlString);
        System.out.println("DEBUG: existUrl.isPresent() = " + existUrl.isPresent());
        Long id;
        if (existUrl.isPresent()) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            id = existUrl.get().getId();
        } else {
            id = urlRepository.save(new Url(urlString));
            ctx.sessionAttribute("flash", "Страница успешно добавлена");
        }
        ctx.redirect(NamedRoutes.urlPath(id));
    }
}
