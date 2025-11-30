package zin.rashidi.boot.jdbi.book;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Rashidi Zin
 */
public class AuthorRowMapper implements RowMapper<Author> {

    @Override
    public Author map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Author(rs.getLong("id"), rs.getString("name"));
    }

}
