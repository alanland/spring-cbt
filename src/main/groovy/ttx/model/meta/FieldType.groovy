package ttx.model.meta

import java.sql.Types

/**
 * ＠author 王成义
 * @created 14-12-11.
 */
enum FieldType {
    string('string'),
    integer('int'),
    number('number'),
    date('date'),
    datetime('datetime'),
    select('select'),
    combo('combo')

    private String value;

    private FieldType(String value) {
        this.value = value;
    }

    public static FieldType getType(String value) {
        def map = [:]
        map.put('int4', integer)
        map.put('varchar', string)
        map.get(value, string);
//        valueOf(value)
    }

    public static FieldType getTypeByDbType(int type) {
        def map = [:]
        map.put(Types.INTEGER, number)
        map.get(type, string)
    }

    @Override
    String toString() {
        value
    }

}