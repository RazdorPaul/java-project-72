package hexlet.code.controllers;

import hexlet.code.App;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UrlControllerTest {

    private static Javalin createApp() {
        try {
            return App.getApp();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testIndex() {
        JavalinTest.test(createApp(), (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Сайты");
        });
    }

    @Test
    void testShow() {
        JavalinTest.test(createApp(), (server, client) -> {
            var requestBody = RequestBody.create(
                    "url=https://example.com",
                    MediaType.parse("application/x-www-form-urlencoded; charset=utf-8")
            );

            var url = "http://localhost:" + server.port() + "/urls";
            var request = new okhttp3.Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            client.request(request);

            var showResponse = client.get("/urls/1");
            assertThat(showResponse.code()).isEqualTo(200);
            assertThat(showResponse.body().string()).contains("https://example.com");
        });
    }

    @Test
    void testShowNotFound() {
        JavalinTest.test(createApp(), (server, client) -> {
            var response = client.get("/urls/999");
            assertThat(response.code()).isEqualTo(404);
        });
    }

    @Test
    void testCreateUrl() {
        JavalinTest.test(createApp(), (server, client) -> {
            // Создаём OkHttpClient с отключёнными редиректами
            var httpClient = new okhttp3.OkHttpClient.Builder()
                    .followRedirects(false)
                    .build();

            var requestBody = RequestBody.create(
                    "url=https://example.com",
                    MediaType.parse("application/x-www-form-urlencoded; charset=utf-8")
            );

            var url = "http://localhost:" + server.port() + "/urls";

            var request = new okhttp3.Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            try (var response = httpClient.newCall(request).execute()) {
                assertThat(response.code()).isEqualTo(302);
                assertThat(response.header("Location")).contains("/urls/");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void testCreateDuplicate() {
        JavalinTest.test(createApp(), (server, client) -> {
            // Создаём OkHttpClient с отключёнными редиректами
            var httpClient = new okhttp3.OkHttpClient.Builder()
                    .followRedirects(false)
                    .build();

            var url = "http://localhost:" + server.port() + "/urls";

            // Первый запрос
            var requestBody1 = RequestBody.create(
                    "url=https://example.com",
                    MediaType.parse("application/x-www-form-urlencoded; charset=utf-8")
            );

            var request1 = new okhttp3.Request.Builder()
                    .url(url)
                    .post(requestBody1)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            try (var response1 = httpClient.newCall(request1).execute()) {
                assertThat(response1.code()).isEqualTo(302);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // Второй запрос (дубликат)
            var requestBody2 = RequestBody.create(
                    "url=https://example.com",
                    MediaType.parse("application/x-www-form-urlencoded; charset=utf-8")
            );

            var request2 = new okhttp3.Request.Builder()
                    .url(url)
                    .post(requestBody2)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            try (var response2 = httpClient.newCall(request2).execute()) {
                assertThat(response2.code()).isEqualTo(302);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void testCreateInvalidUrl() {
        JavalinTest.test(createApp(), (server, client) -> {
            var requestBody = RequestBody.create(
                    "url=invalid-url",
                    MediaType.parse("application/x-www-form-urlencoded")
            );

            var response = client.post("/urls", requestBody);

            assertThat(response.code()).isEqualTo(422);
        });
    }
}
