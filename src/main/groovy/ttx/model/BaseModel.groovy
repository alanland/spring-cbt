package ttx.model

/**
 * Created by journey on 14-12-3.
 */
@Deprecated
class BaseModel {
    String headerTableName
    String lineTableName

    Map tableFieldNameMapping

    long billId
    String billNo

    long lineId

    BaseModel(){
        tableFieldNameMapping=[:]
    }
}
