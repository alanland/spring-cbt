package ttx.service.base

import org.springframework.jdbc.core.JdbcTemplate
import ttx.jdbc.JdbcUtil
import ttx.model.BaseModel

/**
 * Created by journey on 14-12-5.
 */
class BaseService {
    JdbcTemplate template

    BaseModel header
    BaseModel line

    BaseService() {
        template = JdbcUtil.getTemplate()
    }

    /**
     * List data
     * @return
     */
    List<Map<String, Object>> getList() {
        template.queryForList("select * from ${header.headerTableName} where 1=1 ")
    }

    /**
     * details data
     * @param billId
     * @return
     */
    List<Map<String, Object>> getLineList(long billId) {
        template.queryForList("select * from ${header.lineTableName} where bill_id=${billId} ")
    }

}
