package hexlet.code;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {
    private static Javalin createApp() {
        try {
            return App.getApp();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось создать приложение", e);
        }
    }

    @Test
    void testMainPage() {
        // Передаем уже созданный объект Javalin
        JavalinTest.test(createApp(), (server, client) -> {
            var response = client.get("/");

            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }
}
