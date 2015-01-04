package ttx.jdbc.rest.page;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by journey on 14-12-9.
 */
public class PostgresPagination extends Pagination {
    public PostgresPagination(String sql, JdbcTemplate template, int begin, int end, Object[] params) {
        super(sql, template, begin, end, params);
    }

    @Override
    String getPageSql(String queryString, int startIndex, int pageSize) {
        return queryString + " limit " + pageSize + " offset " + startIndex;
    }
}
