package hexlet.code.dto;

import hexlet.code.model.Url;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;


@Getter
@AllArgsConstructor
public class UrlPage {
    private Url url;
    private Map<String, String> flash;
    private String title;
}
