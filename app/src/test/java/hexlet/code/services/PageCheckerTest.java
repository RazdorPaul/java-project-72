package hexlet.code.services;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PageCheckerTest {
    private static MockWebServer srv;
    private PageChecker check;

    @BeforeAll
    public static  void startServer() throws IOException {
        srv = new MockWebServer();
        srv.start();
    }

    @AfterAll
    public static void stopServer() throws IOException {
        srv.shutdown();
    }


    @BeforeEach
    public final void setUp() {
        check = new PageChecker();
    }

    @Test
    void testCheckSuccess() throws Exception {
        // HTML с h1, title, description
        var html = """
                <!DOCTYPE html>
                <html>
                    <head>
                        <title>Test Title</title>
                        <meta name="description" content="Test Description">
                    </head>
                    <body>
                        <h1>Test H1</h1>
                        <p>Some content</p>
                    </body>
                </html>
                """;

        srv.enqueue(new MockResponse().setResponseCode(200).setBody(html));

        var url = srv.url("/").toString();
        var result = check.check(url);

        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getTitle()).isEqualTo("Test Title");
        assertThat(result.getH1()).isEqualTo("Test H1");
        assertThat(result.getDescription()).isEqualTo("Test Description");
    }

    @Test
    void testCheckWithoutTitle() throws Exception {
        // HTML без title
        var html = """
            <!DOCTYPE html>
            <html>
                <head>
                    <meta name="description" content="Has description but no title">
                </head>
                <body>
                    <h1>H1 Present</h1>
                </body>
            </html>
            """;

        srv.enqueue(new MockResponse().setResponseCode(200).setBody(html));

        var url = srv.url("/").toString();
        var result = check.check(url);

        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getTitle()).isNullOrEmpty();
        assertThat(result.getDescription()).isEqualTo("Has description but no title");
        assertThat(result.getH1()).isEqualTo("H1 Present");
    }

    @Test
    void testCheckWithoutH1() throws Exception {
        // HTML без h1
        var html = """
                <!DOCTYPE html>
                <html>
                    <head>
                        <title>No H1 Page</title>
                    </head>
                    <body>
                        <p>Content without H1</p>
                    </body>
                </html>
                """;

        srv.enqueue(new MockResponse().setResponseCode(200).setBody(html));

        var url = srv.url("/").toString();
        var result = check.check(url);

        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getTitle()).isEqualTo("No H1 Page");
        assertThat(result.getH1()).isNullOrEmpty();
    }

    @Test
    void testCheckWithoutDescription() throws Exception {
        // HTML без meta description
        var html = """
                <!DOCTYPE html>
                <html>
                    <head>
                        <title>No Description</title>
                    </head>
                    <body>
                        <h1>H1 Present</h1>
                    </body>
                </html>
                """;

        srv.enqueue(new MockResponse().setResponseCode(200).setBody(html));

        var url = srv.url("/").toString();
        var result = check.check(url);

        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getDescription()).isNullOrEmpty();
    }

    @Test
    void testCheckNotFound() throws Exception {
        // Сервер возвращает 404
        srv.enqueue(new MockResponse().setResponseCode(404).setBody("Not Found"));

        var url = srv.url("/").toString();

        assertThatThrownBy(() -> check.check(url))
                .isInstanceOf(Exception.class);
    }

    @Test
    void testCheckServerError() throws Exception {
        // Сервер возвращает 500
        srv.enqueue(new MockResponse().setResponseCode(500).setBody("Internal Server Error"));

        var url = srv.url("/").toString();

        assertThatThrownBy(() -> check.check(url))
                .isInstanceOf(Exception.class);
    }

    @Test
    void testCheckLongText() throws Exception {
        // Очень длинный title (300 символов)
        var longTitle = "A".repeat(300);
        var html = String.format("""
                <!DOCTYPE html>
                <html>
                    <head>
                        <title>%s</title>
                    </head>
                    <body>
                        <h1>Short H1</h1>
                    </body>
                </html>
                """, longTitle);

        srv.enqueue(new MockResponse().setResponseCode(200).setBody(html));

        var url = srv.url("/").toString();
        var result = check.check(url);

        assertThat(result.getStatusCode()).isEqualTo(200);
        // Проверяем, что title обрезан до 200 символов + "..."
        assertThat(result.getTitle()).hasSize(203);
        assertThat(result.getTitle()).endsWith("...");
    }

    @Test
    void testCheckEmptyPage() throws Exception {
        // Пустая страница
        var html = "";

        srv.enqueue(new MockResponse().setResponseCode(200).setBody(html));

        var url = srv.url("/").toString();
        var result = check.check(url);

        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getTitle()).isNullOrEmpty();
        assertThat(result.getH1()).isNullOrEmpty();
        assertThat(result.getDescription()).isNullOrEmpty();
    }
}
