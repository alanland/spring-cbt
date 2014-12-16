package ttx.jdbc.rest.page;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by journey on 14-12-9.
 */
public class PostgresPagination extends Pagination {
    public PostgresPagination(List<Map<String, Object>> rows) {
        super(rows);
    }

    public PostgresPagination(List<Map<String, Object>> rows, int total, int totalPages, int currentPage) {
        super(rows, total, totalPages, currentPage);
    }

    public PostgresPagination(String sql, NamedParameterJdbcTemplate template, Map params, int currentPage, int numPerPage, String dbType) {
        super(sql, template, params, currentPage, numPerPage);
    }

    @Override
    String getPageSql(String queryString, NamedParameterJdbcTemplate template, int startIndex, int pageSize) {
        return queryString + " limit " + pageSize + " offset " + startIndex;
    }
}
