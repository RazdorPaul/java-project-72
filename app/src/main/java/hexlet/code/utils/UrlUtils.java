package hexlet.code.utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;

public class UrlUtils {

    public static String getBaseUrl(String urlString) throws URISyntaxException, MalformedURLException {
        var uri = new URI(urlString);
        try {
            var url = uri.toURL();
            var protocol = url.getProtocol();
            var host = url.getHost();
            var port = url.getPort();
            var baseUrl = protocol + "://" + host;
            if (port > 0) {
                baseUrl = baseUrl + ":" + port;
            }
            return baseUrl;
        } catch (IllegalArgumentException e) {
            throw new MalformedURLException("URI is not absolute: " + urlString);
        }

    }

    public static String truncate(String text, int maxlen) {
        if (text == null || text.isBlank()) {
            return null;
        } else if (text.length() <= maxlen) {
            return text;
        } else {
            return text.substring(0, maxlen) + "...";
        }
    }

    public static String formatDate(java.sql.Timestamp date) {
        if (date == null) {
            return "";
        }
        var formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return date.toLocalDateTime().format(formatter);
    }
}
