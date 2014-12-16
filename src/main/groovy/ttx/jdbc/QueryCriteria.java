package ttx.jdbc;

public class QueryCriteria {
    public String field;
    public String operator;
    public Object value;

    public QueryCriteria(String field, String operator, Object obj) {
        this.field = field;
        this.operator = operator;
        this.value = obj;
    }

    public QueryCriteria(String field, Object obj) {
        this.field = field;
        this.operator = QueryOperator.Equal;
        this.value = obj;
    }

    public String getValueHolder() {
        if (operator.equalsIgnoreCase(QueryOperator.Like.trim())) {
            return "%?%";
        } else {
            return "?";
        }
    }

}
