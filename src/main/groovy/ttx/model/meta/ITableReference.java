package ttx.model.meta;

/**
 * ＠author 王成义
 *
 * @created 2014-12-19.
 */
public interface ITableReference {
    public String getJoinString(String principalTable, String subordinateTable);
}
