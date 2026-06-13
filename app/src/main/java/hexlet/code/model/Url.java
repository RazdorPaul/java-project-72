package hexlet.code.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Url {
    @ToString.Exclude
    private Long id;

    private String name;
    private java.sql.Timestamp createdAt;

    public Url(String name) {
        this.name = name;
    }
}
