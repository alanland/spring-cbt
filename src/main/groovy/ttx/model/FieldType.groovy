package ttx.model

/**
 * Created by journey on 14-12-11.
 */
enum FieldType {
    string('string'),
    number('number'),
    date('date'),
    datetime('datetime'),
    select('select'),
    combo('combo')

    private String value;

    private FieldType(String value) {
        this.value = value;
    }

    public static FieldType getFieldType(String value) {
        FieldType.valueOf(value);
    }

    @Override
    String toString() {
        value
    }
}