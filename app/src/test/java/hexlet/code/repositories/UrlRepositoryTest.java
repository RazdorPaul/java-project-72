package hexlet.code.repositories;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class UrlRepositoryTest {

    private UrlRepository urlRepository;
    private HikariDataSource dataSource;

    @BeforeEach
    void setUp() throws Exception {
        var config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        dataSource = new HikariDataSource(config);
        var inputStream = getClass().getClassLoader().getResourceAsStream("schema.sql");
        var sql = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
        try (var conn = dataSource.getConnection();
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
        urlRepository = new UrlRepository(dataSource);
    }

    @AfterEach
    void tearDown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Test
    void testSave() throws SQLException {
        var url = new Url("https://example.com");
        var id = urlRepository.save(url);
        assertThat(id).isGreaterThan(0);
    }

    @Test
    void testSaveDuplicate() throws SQLException {
        var url1 = new Url("https://example.com");
        var id1 = urlRepository.save(url1);
        var url2 = new Url("https://example.com");
        var id2 = urlRepository.save(url2);
        assertThat(id1).isEqualTo(id2);
    }

    @Test
    void testFindById() throws SQLException {
        var url = new Url("https://example.com");
        var id = urlRepository.save(url);
        var found = urlRepository.findById(id);
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(id);
        assertThat(found.get().getName()).isEqualTo("https://example.com");
        assertThat(found.get().getCreatedAt()).isNotNull();
    }

    @Test
    void testFindByIdNotFound() throws SQLException {
        var found = urlRepository.findById(999L);
        assertThat(found).isEmpty();
    }

    @Test
    void testFindByName() throws SQLException {
        var url = new Url("https://example.com");
        urlRepository.save(url);

        var found = urlRepository.findByName("https://example.com");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("https://example.com");
    }

    @Test
    void testFindByNameNotFound() throws SQLException {
        var found = urlRepository.findByName("https://nonexistent.com");

        assertThat(found).isEmpty();
    }

    @Test
    void testFindAll() throws SQLException, InterruptedException {
        var url1 = new Url("https://example1.com");
        var url2 = new Url("https://example2.com");
        var url3 = new Url("https://example3.com");

        urlRepository.save(url1);
        Thread.sleep(10);
        urlRepository.save(url2);
        Thread.sleep(10);
        urlRepository.save(url3);
        var urls = urlRepository.findAll();
        assertThat(urls).hasSize(3);
        assertThat(urls.get(0).getName()).isEqualTo("https://example3.com");
        assertThat(urls.get(1).getName()).isEqualTo("https://example2.com");
        assertThat(urls.get(2).getName()).isEqualTo("https://example1.com");
    }

    @Test
    void testFindByIdWithChecks() throws SQLException, InterruptedException {
        var url = new Url("https://example.com");
        var urlId = urlRepository.save(url);

        // Создаём UrlCheckRepository для загрузки проверок
        var urlCheckRepository = new UrlCheckRepository(dataSource);

        // Создаём несколько проверок
        var check1 = new UrlCheck();
        check1.setUrlId(urlId);
        check1.setStatusCode(200);
        check1.setTitle("First check");
        urlCheckRepository.save(check1);

        var check2 = new UrlCheck();
        check2.setUrlId(urlId);
        check2.setStatusCode(404);
        check2.setTitle("Second check");
        urlCheckRepository.save(check2);

        // Вызываем метод с передачей UrlCheckRepository
        var found = urlRepository.findByIdWithChecks(urlId, urlCheckRepository);
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(urlId);
        assertThat(found.get().getChecks()).hasSize(2);
        assertThat(found.get().getChecks().get(0).getStatusCode()).isEqualTo(404);
        assertThat(found.get().getChecks().get(1).getStatusCode()).isEqualTo(200);
    }
}
