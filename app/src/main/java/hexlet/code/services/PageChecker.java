package hexlet.code.services;

import hexlet.code.model.UrlCheck;
import hexlet.code.utils.UrlUtils;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public final class PageChecker {

    public UrlCheck check(String url) throws Exception {
        HttpResponse<String> response = Unirest.get(url).asString();
        int statusCode = response.getStatus();
        if (statusCode >= 400) {
            throw new Exception("HTTP error: " + statusCode);
        }
        String html = response.getBody();
        Document doc = Jsoup.parse(html);
        String title = doc.title();
        Element h1Element = doc.selectFirst("h1");
        String h1 = h1Element != null ? h1Element.text() : "";
        Element metaDesc = doc.selectFirst("meta[name=description]");
        String description = metaDesc != null ? metaDesc.attr("content") : "";
        UrlCheck check = new UrlCheck();
        check.setStatusCode(statusCode);
        check.setTitle(UrlUtils.truncate(title, 200));
        check.setH1(UrlUtils.truncate(h1, 200));
        check.setDescription(UrlUtils.truncate(description, 200));

        return check;
    }
}
