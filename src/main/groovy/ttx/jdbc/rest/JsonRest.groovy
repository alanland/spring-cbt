package ttx.jdbc.rest

import org.springframework.jdbc.core.JdbcTemplate
import ttx.jdbc.QueryCriteria
import ttx.model.BaseModel
import ttx.service.RegistryCenter

/**
 * Created by journey on 14-12-9.
 */
class JsonRest {
    JdbcTemplate template

    JsonRest(JdbcTemplate template) {
        this.template = template
    }

    /**
     * 返回指定列
     * @param target
     * @param id
     * @param columns
     */
    def get(String target, long id, String... columns) {
        // TODO
    }

    /**
     * 返回所有列
     * @param target
     * @param id
     * @return
     */
    def get(String target, long id) {
        Map mapping = RegistryCenter.getMappingItem(target)
        BaseModel header = mapping.get('header', null)
        BaseModel line = mapping.get('line', null)
        String sql = """
select * from ${header.lineTableName} l
    left join ${header.headerTableName} h
        on h.bill_id=l.bill_id
where bill_id=$id
"""
        template.queryForList(sql, '')
    }

    // TODO 区分明细头
    def query(String target, List<QueryCriteria> criteriaList, String options) {
        Map mapping = RegistryCenter.getMappingItem(target)
        BaseModel header = mapping.get('header', null)
        StringBuffer sql = """
select * from ${header.lineTableName} l
    left join ${header.headerTableName} h
        on h.bill_id=l.bill_id
where 1==1
"""
        List holders = new ArrayList()
        for (QueryCriteria c in criteriaList) {
            sql.append(" ${c.field} ${c.operator} ${c.getValueHolder()}")
            holders.add(c.value)
        }
        sql.append(options)
        template.queryForList(sql.toString(), holders.toArray())
    }

    def remove(String target, Long id) {
        BaseModel header = RegistryCenter.getMappingItem(target).get('header', null)
        template.update("delete from ${header.headerTableName} where bill_id=$id")
        template.update("delete from ${header.lineTableName} where bill_id=$id")
    }

    // TODO 删除单据
    def removeAll(String target, List<Long> ids) {
        BaseModel header = RegistryCenter.getMappingItem(target).get('header', null)
        for (int i = 0; i < ids.size(); i += 1000) { // for in limit in some database system
            List<Long> list = ids.subList(i, i + 1000 - 1);
            template.update("delete from ${header.headerTableName} where bill_id in (list.join(','))")
            template.update("delete from ${header.lineTableName} where bill_id in (list.join(','))")
        }
    }
    // TODO 删除明细

    // TODO 头明细保存分开
    def put(Map<String,Object> obj,Long id){
        BaseModel header = RegistryCenter.getMappingItem(target).get('header', null)
        String sql = "update ${header.headerTableName}"
        List holders = new ArrayList()
        for (String k in obj.keySet()) {
            Object v = obj.get(k)
            sql.append(" ${c.field} ${c.operator} ${c.getValueHolder()}")
            holders.add(k)
        }

    }


}
