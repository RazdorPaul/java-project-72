package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.repositories.BaseRepository;
import io.github.cdimascio.dotenv.Dotenv;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;

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
        BaseRepository.dataSource = new HikariDataSource(hikariConfig);
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte());
        });

        app.get("/", ctx -> ctx.result("Hello World"));

        return app;
    }

    private static String getDatabaseUrl() {
        var url = "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1";
        return System.getenv().getOrDefault("JDBC_DATABASE_URL", url);
    }
}


