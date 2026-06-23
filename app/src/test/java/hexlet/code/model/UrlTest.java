package hexlet.code.model;

import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UrlTest {

    @Test
    void testAllargsConstructor() {
        var now = new Timestamp(System.currentTimeMillis());
        var url = new Url(1L, "https://example.com", now, List.of());
        assertThat(url.getId()).isEqualTo(1L);
        assertThat(url.getName()).isEqualTo("https://example.com");
        assertThat(url.getCreatedAt()).isEqualTo(now);
        assertThat(url.getChecks()).isEmpty();
    }

    @Test
    void testOneArgsConstructor() {
        var url = new Url("https://example.com");
        assertThat(url.getId()).isNull();
        assertThat(url.getName()).isEqualTo("https://example.com");
        assertThat(url.getCreatedAt()).isNull();
        assertThat(url.getChecks()).isEmpty();
    }

    @Test
    void testSetters() {
        var now = new Timestamp(System.currentTimeMillis());
        var url = new Url("https://example.com");
        url.setCreatedAt(now);
        url.setName("https://hexlet.com");
        url.setId(1L);
        url.setChecks(List.of());
        assertThat(url.getId()).isEqualTo(1L);
        assertThat(url.getName()).isEqualTo("https://hexlet.com");
        assertThat(url.getCreatedAt()).isEqualTo(now);
        assertThat(url.getChecks()).isEmpty();
    }
}
