package hexlet.code.dto;

import hexlet.code.model.Url;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

class UrlPageTest {

    @Test
    void testGetters() {
        var now = new Timestamp(System.currentTimeMillis());
        var url = new Url(1L, "https://example.com", now);
        var flash = "Страница успешно добавлена";
        var title = "Тестовая страница";

        var page = new UrlPage(url, flash, title);

        assertThat(page.getUrl()).isEqualTo(url);
        assertThat(page.getFlash()).isEqualTo(flash);
        assertThat(page.getTitle()).isEqualTo(title);
    }
}
