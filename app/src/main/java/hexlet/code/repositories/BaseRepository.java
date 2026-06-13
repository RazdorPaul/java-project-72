package hexlet.code.repositories;

import com.zaxxer.hikari.HikariDataSource;

public abstract class BaseRepository {
    protected final HikariDataSource dataSource;

    public BaseRepository(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }
}
