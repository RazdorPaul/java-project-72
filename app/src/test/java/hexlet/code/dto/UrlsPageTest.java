package hexlet.code.dto;

import hexlet.code.model.Url;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class UrlsPageTest {

    @Test
    void testGetters() {
        var now = new Timestamp(System.currentTimeMillis());
        var url1 = new Url(1L, "https://example1.com", now);
        var url2 = new Url(2L, "https://example2.com", now);
        var urls = List.of(url1, url2);
        var flash = Map.of("message", " Тестовое сообщение");
        var title = "Список сайтов";

        var page = new UrlsPage(urls, flash, title);

        assertThat(page.getUrls()).hasSize(2);
        assertThat(page.getUrls().get(0).getName()).isEqualTo("https://example1.com");
        assertThat(page.getFlash()).isEqualTo(flash);
        assertThat(page.getTitle()).isEqualTo(title);
    }
}
