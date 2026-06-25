package hexlet.code.dto;

import hexlet.code.model.Url;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class UrlsPage {
    private List<Url> urls;
    private Map<String, String> flash;
    private String title;
}
