package ttx.jdbc.rest.page;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by journey on 14-12-9.
 */
public class SqlServerPagination extends Pagination {
    public SqlServerPagination(List<Map<String, Object>> rows) {
        super(rows);
    }

    public SqlServerPagination(List<Map<String, Object>> rows, int total, int totalPages, int currentPage) {
        super(rows, total, totalPages, currentPage);
    }

    public SqlServerPagination(String sql, NamedParameterJdbcTemplate template, Map params, int currentPage, int numPerPage) {
        super(sql, template, params, currentPage, numPerPage);
    }

    @Override
    String getPageSql(String queryString, int startIndex, int pageSize) {
        return getMSSQLPageSQL(queryString, startIndex, pageSize, "Id");
    }

    public String getMSSQLPageSQL(String queryString, int startIndex, int pageSize, String pagingOrderBy) {
        StringBuilder sbSql = null;
        StringBuilder sbPageSql = null;
        int idxOfFrom = -1;

        idxOfFrom = queryString.indexOf(" from ");
        if (idxOfFrom < 0)
            return queryString;

        sbSql = new StringBuilder(512)
                .append(queryString.substring(0, idxOfFrom)).append(", row_number() over (order by ").append(pagingOrderBy).append(") as rank ")
                .append(queryString.substring(idxOfFrom));

        sbPageSql = new StringBuilder(512).append("select * from (").append(sbSql).append(") t where t.rank between ").append(startIndex).append(" and ").append(startIndex + pageSize);
        return sbPageSql.toString();
    }
}
