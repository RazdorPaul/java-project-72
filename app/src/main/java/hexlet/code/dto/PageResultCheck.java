package hexlet.code.dto;

import hexlet.code.model.UrlCheck;
import lombok.Getter;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Getter
public class PageResultCheck {
    private List<UrlCheck> checks;
}
