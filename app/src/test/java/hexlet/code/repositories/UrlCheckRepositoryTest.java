package hexlet.code.repositories;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.model.UrlCheck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class UrlCheckRepositoryTest {

    private HikariDataSource dataSource;
    private UrlCheckRepository repository;

    @BeforeEach
    void setUp() throws SQLException {
        // Настройка H2 базы данных для тестов
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:test;MODE=PostgreSQL");
        config.setUsername("sa");
        config.setPassword("");
        dataSource = new HikariDataSource(config);

        // Инициализация схемы (создание таблиц)
        try (var conn = dataSource.getConnection()) {
            try (var stmt = conn.createStatement()) {
                stmt.execute("DROP TABLE IF EXISTS url_checks");
                stmt.execute("DROP TABLE IF EXISTS urls");
                stmt.execute("CREATE TABLE IF NOT EXISTS urls ("
                        + "id BIGSERIAL PRIMARY KEY, "
                        + "name VARCHAR(255) NOT NULL UNIQUE, "
                        + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

                stmt.execute("CREATE TABLE IF NOT EXISTS url_checks ("
                        + "id BIGSERIAL PRIMARY KEY, "
                        + "url_id BIGINT NOT NULL, "
                        + "status_code INT, "
                        + "h1 VARCHAR(255), "
                        + "title VARCHAR(255), "
                        + "description TEXT, "
                        + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                        + "FOREIGN KEY (url_id) REFERENCES urls(id))");
                stmt.execute("INSERT INTO urls (name) "
                        + "VALUES ('https://example.com')");
            }
        }

        repository = new UrlCheckRepository(dataSource);
    }

    @Test
    void testSave() throws SQLException {
        var urlCheck = new UrlCheck();
        urlCheck.setUrlId(1L); // Предполагаем, что URL с ID 1 существует
        urlCheck.setStatusCode(200);
        urlCheck.setTitle("Test title");

        long id = repository.save(urlCheck);

        assertThat(id).isGreaterThan(0);
    }

    @Test
    void testFindByUrlId() throws SQLException {
        // Сначала сохраняем
        var urlCheck = new UrlCheck();
        urlCheck.setUrlId(1L);
        urlCheck.setStatusCode(200);
        urlCheck.setTitle("Test title");
        repository.save(urlCheck);

        // Проверяем поиск
        List<UrlCheck> checks = repository.findByUrlId(1L);

        assertThat(checks).hasSize(1);
        assertThat(checks.get(0).getStatusCode()).isEqualTo(200);
        assertThat(checks.get(0).getTitle()).isEqualTo("Test title");
    }

    @Test
    void testFindByUrlIdEmpty() throws SQLException {
        List<UrlCheck> checks = repository.findByUrlId(999L);
        assertThat(checks).isEmpty();
    }

    @Test
    void testFindLastByUrlId() throws SQLException {
        // Используем ID 1L, как в других тестах
        var urlId = 1L;

        var check1 = new UrlCheck();
        check1.setUrlId(urlId);
        check1.setStatusCode(200);
        check1.setTitle("First check");
        repository.save(check1);

        var check2 = new UrlCheck();
        check2.setUrlId(urlId);
        check2.setStatusCode(404);
        check2.setTitle("Second check");
        repository.save(check2);

        var lastCheck = repository.findLastByUrlId(urlId);

        assertThat(lastCheck).isPresent();
        assertThat(lastCheck.get().getStatusCode()).isEqualTo(404);
    }

    @Test
    void testFindLastByUrlIdEmpty() throws SQLException {
        var lastCheck = repository.findLastByUrlId(999L);
        assertThat(lastCheck).isEmpty();
    }
}
