package hexlet.code.utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class UrlUtils {

    public static String getBaseUrl(String urlString) throws URISyntaxException, MalformedURLException {
        var uri = new URI(urlString);
        var url = uri.toURL();
        var protocol = url.getProtocol();
        var host = url.getHost();
        var port = url.getPort();
        var baseUrl = protocol + "://" + host;
        if (port > 0) {
            baseUrl = baseUrl + ":" + port;
        }
        return baseUrl;
    }
}
