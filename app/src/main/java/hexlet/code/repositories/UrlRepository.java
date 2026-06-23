package hexlet.code.repositories;

import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.model.Url;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class UrlRepository extends BaseRepository {

    public UrlRepository(HikariDataSource dataSource) {
        super(dataSource);
    }

    public Optional<Url> findById(Long id) throws SQLException {
        var sql = "SELECT * FROM urls WHERE id = ?";
        try (var conn = dataSource.getConnection();
                var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (var resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    var name = resultSet.getString("name");
                    var createdAt = resultSet.getTimestamp("created_at");
                    var url = new Url(id, name, createdAt);
                    return Optional.of(url);
                }
            }
            return Optional.empty();
        }
    }

    public long save(Url url) throws SQLException {
        var existingUrl = findByName(url.getName());
        if (existingUrl.isPresent()) {
            return existingUrl.get().getId();
        }
        var sql = "INSERT INTO urls (name) VALUES (?)";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, url.getName());
            stmt.executeUpdate();
            try (var keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                } else {
                    throw new SQLException("Creating url failed, no ID obtained.");
                }
            }
        }
    }

    public Optional<Url> findByName(String term) throws SQLException {
        var sql = "SELECT * FROM urls WHERE name = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, term);
            try (var resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    var name = resultSet.getString("name");
                    var createdAt = resultSet.getTimestamp("created_at");
                    var id = resultSet.getLong("id");
                    var url = new Url(id, name, createdAt);
                    return Optional.of(url);
                }
            }
            return Optional.empty();
        }
    }

    public List<Url> findAll() throws SQLException {
        var urls = new ArrayList<Url>();
        var sql = "SELECT * FROM urls ORDER BY created_at DESC";
        try (var conn = dataSource.getConnection();
            var stmt = conn.createStatement()) {
            try (var resultSet = stmt.executeQuery(sql)) {
                while (resultSet.next()) {
                    var id = resultSet.getLong("id");
                    var name = resultSet.getString("name");
                    var createdAt = resultSet.getTimestamp("created_at");
                    urls.add(new Url(id, name, createdAt));
                }
            }
        }
        return urls;
    }
    public Optional<Url> findByIdWithChecks(Long id, UrlCheckRepository repo) throws SQLException {
        var url = findById(id);
        url.ifPresent(value -> {
            try {
                value.setChecks(repo.findByUrlId(id));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return url;
    }
}
