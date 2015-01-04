package ttx.jdbc.rest.page;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

;

public abstract class Pagination implements Serializable {
    private static final long serialVersionUID = -2554565760258955645L;

    private int numPerPage;
    private int total;
    private int totalPages;
    private int currentPage;
    private int startIndex;
    private int lastIndex;

    private List<Map<String, Object>> rows;

    public Pagination(List<Map<String, Object>> rows) {
        this.rows = rows;
        this.total = rows.size();
        this.totalPages = 1;
        this.currentPage = 1;
    }

    public Pagination(List<Map<String, Object>> rows, int total, int totalPages, int currentPage) {
        this.rows = rows;
        this.total = total;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Pagination(String sql, NamedParameterJdbcTemplate template, Map params, int currentPage, int numPerPage) {
        if (template == null) {
            throw new IllegalArgumentException(
                    "template is null , pls initialize ... ");
        } else if (StringUtils.isEmpty(sql)) {
            throw new IllegalArgumentException("sql is blank , pls initialize ... ");
        }
        setNumPerPage(numPerPage);
        setCurrentPage(currentPage);
        setTotal(template.queryForObject(" select count(1) from ( " + sql + " ) t", params, Integer.class));
        setTotalPages();
        setStartIndex();
        setLastIndex();
        setRows(template.queryForList(getPageSql(sql, startIndex, numPerPage), params));
    }

    public Pagination(String sql, JdbcTemplate template, int begin, int end, Object[] params) {
        int numPerPage = end - begin + 1;
        int currentPage = end / numPerPage + 1;
        if (template == null) {
            throw new IllegalArgumentException(
                    "template is null , pls initialize ... ");
        } else if (StringUtils.isEmpty(sql)) {
            throw new IllegalArgumentException("sql is blank , pls initialize ... ");
        }
        setNumPerPage(numPerPage);
        setCurrentPage(currentPage);
        setTotal(template.queryForObject(" select count(1) from ( " + sql + " ) t", Integer.class, params));
        setTotalPages();
        setStartIndex();
        setLastIndex();
        setRows(template.queryForList(getPageSql(sql, startIndex, numPerPage), params));
    }

    abstract String getPageSql(String queryString, int startIndex, int pageSize);

    private void setTotalPages() {
        if (total % numPerPage == 0) {
            this.totalPages = total / numPerPage;
        } else {
            this.totalPages = (total / numPerPage) + 1;
        }
    }

    private void setStartIndex() {
        this.startIndex = (currentPage - 1) * numPerPage;
    }

    private void setLastIndex() {
        if (total < numPerPage) {
            this.lastIndex = total;
        } else if ((total % numPerPage == 0)
                || (total % numPerPage != 0 && currentPage < totalPages)) {
            this.lastIndex = currentPage * numPerPage;
        } else if (total % numPerPage != 0 && currentPage == totalPages) {
            this.lastIndex = total;
        }
    }

    //setter and getter
    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getNumPerPage() {
        return numPerPage;
    }

    public void setNumPerPage(int numPerPage) {
        this.numPerPage = numPerPage;
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, Object>> rows) {
        this.rows = rows;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getLastIndex() {
        return lastIndex;
    }

}
