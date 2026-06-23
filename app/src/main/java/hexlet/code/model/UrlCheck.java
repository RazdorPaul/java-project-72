package hexlet.code.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UrlCheck {
    @ToString.Exclude
    private Long id;

    private Long urlId;
    private Integer statusCode;
    private String h1;
    private String title;
    private String description;
    private java.sql.Timestamp createdAt;
}
