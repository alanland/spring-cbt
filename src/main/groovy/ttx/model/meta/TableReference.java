package ttx.model.meta;

/**
 * MetaTable 之间的关系
 * <p/>
 * ＠author 王成义
 *
 * @created 2014-12-19.
 */
public class TableReference implements ITableReference {
    private String principalColumnName;
    private String subordinateColumnName;

    public TableReference(String principalColumnName, String subordinateColumnName) {
        this.principalColumnName = principalColumnName;
        this.subordinateColumnName = subordinateColumnName;
    }

    public static TableReference defaultInstance() {
        return new TableReference("id", "header_id");
    }

    @Override
    public String getJoinString(String principalTable, String subordinateTable) {
        return String.format(" %s.%s=%s.%s", principalTable, principalColumnName, subordinateTable, subordinateColumnName);
    }
}
