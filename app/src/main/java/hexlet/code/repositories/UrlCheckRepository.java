package hexlet.code.repositories;

import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.model.UrlCheck;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class UrlCheckRepository extends BaseRepository {

    public UrlCheckRepository(HikariDataSource dataSource) {
        super(dataSource);
    }

    public long save(UrlCheck check) throws SQLException {
        var sql = "INSERT INTO url_checks "
                + "(url_id, status_code, h1, title, description) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql,
                     Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, check.getUrlId());
            stmt.setObject(2, check.getStatusCode());
            stmt.setString(3, check.getH1());
            stmt.setString(4, check.getTitle());
            stmt.setString(5, check.getDescription());
            stmt.executeUpdate();
            try (var keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    var id = keys.getLong("id");
                    check.setId(id);
                    return id;
                } else {
                    throw new SQLException("Creating url_check failed, no ID obtained.");
                }
            }
        }
    }

    public List<UrlCheck> findByUrlId(Long urlId) throws SQLException {
        var sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY created_at DESC";
        var checks = new ArrayList<UrlCheck>();
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql);
        ) {
            stmt.setLong(1, urlId);
            try (var resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    var id = resultSet.getLong("id");
                    var idUrl = resultSet.getLong("url_id");
                    var status = (Integer) resultSet.getInt("status_code");
                    var h1 = resultSet.getString("h1");
                    var title = resultSet.getString("title");
                    var description = resultSet.getString("description");
                    var createdAt = resultSet.getTimestamp("created_at");
                    var check = new UrlCheck(id,
                            idUrl,
                            status,
                            h1,
                            title,
                            description,
                            createdAt);
                    checks.add(check);
                }
            }
        }
        return checks;
    }

    public Optional<UrlCheck> findLastByUrlId(Long id) throws SQLException {
        var checks = findByUrlId(id);
        if (!checks.isEmpty()) {
            return Optional.of(checks.getFirst());
        }
        return Optional.empty();
    }
}
