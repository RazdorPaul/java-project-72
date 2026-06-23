package hexlet.code.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Url {
    @ToString.Exclude
    private Long id;

    private String name;
    private java.sql.Timestamp createdAt;

    @ToString.Exclude
    List<UrlCheck> checks = new ArrayList<>();

    public Url(String name) {
        this.name = name;
    }

    public Url(Long id, String name, java.sql.Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }
}
