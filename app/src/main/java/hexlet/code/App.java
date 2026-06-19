package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import hexlet.code.controllers.UrlController;
import hexlet.code.utils.NamedRoutes;
import io.github.cdimascio.dotenv.Dotenv;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;

import gg.jte.resolve.ResourceCodeResolver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public final class App {

    /**
     * Поле содержит объект с переменными окружения.
     */
    private static final Dotenv DOTENV = Dotenv.
                    configure().
                    directory("../").
                    ignoreIfMissing().
                    load();

    private App() {

    }

    /**
     * Точка входа в приложение.
     * @param args
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
        var app = getApp();
        app.start(Integer.parseInt(DOTENV.get("PORT", "7070")));
    }

    /**
     * Метод создает настроенный Javalin.
     * @return Javalin app
     * @throws Exception
     */
    public static Javalin getApp() throws Exception {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getDatabaseUrl());
        var dataSource = new HikariDataSource(hikariConfig);
        var urlCtl = new UrlController(dataSource);
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            ClassLoader loader = App.class.getClassLoader();
            ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", loader);
            TemplateEngine engine = TemplateEngine.create(codeResolver, ContentType.Html);
            config.fileRenderer(new JavalinJte(engine));
        });
        var sql = readResourceSQL("schema.sql");
        try (var conn = dataSource.getConnection();
                var stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }

        app.get(NamedRoutes.rootPath(), ctx -> ctx.render(NamedRoutes.indexPath()));
        app.get(NamedRoutes.urlPath("{id}"), urlCtl::show);
        app.get(NamedRoutes.urlsPath(), urlCtl::index);
        app.post(NamedRoutes.urlsPath(), urlCtl::create);


        return app;
    }

    private static String getDatabaseUrl() {
        var url = "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1";
        return System.getenv().getOrDefault("JDBC_DATABASE_URL", url);
    }

    private static String readResourceSQL(String file) throws IOException {
        var inputStream = App.class.getClassLoader().getResourceAsStream(file);
        if (inputStream == null) {
            throw new FileNotFoundException("Resource not found: " + file);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
