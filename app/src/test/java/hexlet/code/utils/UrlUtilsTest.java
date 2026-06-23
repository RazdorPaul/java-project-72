package hexlet.code.utils;

import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UrlUtilsTest {

    @Test
    void testGetBaseUrlSimple() throws URISyntaxException, MalformedURLException {
        var result = UrlUtils.getBaseUrl("https://example.com");
        assertThat(result).isEqualTo("https://example.com");
    }

    @Test
    void testGetBaseUrlWithPath() throws URISyntaxException, MalformedURLException {
        var result = UrlUtils.getBaseUrl("https://example.com/path/to/page");
        assertThat(result).isEqualTo("https://example.com");
    }

    @Test
    void testGetBaseUrlWithPort() throws URISyntaxException, MalformedURLException {
        var result = UrlUtils.getBaseUrl("http://localhost:8080/api");
        assertThat(result).isEqualTo("http://localhost:8080");
    }

    @Test
    void testGetBaseUrlInvalidSyntax() {
        // Строка с невалидным синтаксисом URI
        assertThatThrownBy(() -> UrlUtils.getBaseUrl("http://example.com/<invalid>"))
                .isInstanceOf(URISyntaxException.class);
    }

    @Test
    void testGetBaseUrlNotAbsolute() {
        // Строка без протокола (не абсолютный URI)
        assertThatThrownBy(() -> UrlUtils.getBaseUrl("not-a-url"))
                .isInstanceOf(MalformedURLException.class);
    }

    @Test
    void testTruncateNull() {
        var result = UrlUtils.truncate(null, 200);
        assertThat(result).isNull();
    }

    @Test
    void testTruncateShortString() {
        var result = UrlUtils.truncate("Short text", 200);
        assertThat(result).isEqualTo("Short text");
    }

    @Test
    void testTruncateExactLength() {
        var text = "a".repeat(200);
        var result = UrlUtils.truncate(text, 200);
        assertThat(result).isEqualTo(text);
        assertThat(result).hasSize(200);
    }

    @Test
    void testTruncateLongString() {
        var text = "a".repeat(250);
        var result = UrlUtils.truncate(text, 200);
        assertThat(result).hasSize(203); // 200 + "..."
        assertThat(result).endsWith("...");
        assertThat(result).startsWith("a".repeat(200));
    }
}
