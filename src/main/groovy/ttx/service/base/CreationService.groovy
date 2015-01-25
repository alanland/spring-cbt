package ttx.service.base

import com.gemstone.org.json.JSONArray
import com.gemstone.org.json.JSONObject
import groovy.json.JsonSlurper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.rowset.SqlRowSet
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData
import ttx.model.meta.FieldType
import ttx.redis.RedisUtil
import ttx.util.config.AppProfile

/**
 * Created by journey on 14-12-6.
 */
@Configuration
class CreationService extends BaseService {

    @Bean
    CreationService creationService() {
        new CreationService()
    }

    protected CreationService() {
        super()
    }

    def getSysNavigator(String db) {
        getNavigator(db, 'admin')
    }

    List getNavigator(String db, String key) {
        try {
            return new JsonSlurper().parseText(template(db).queryForMap('select * from ttx_navigator where key=?', key).structure)
        } catch (EmptyResultDataAccessException e) {
            return []
        }
    }

    def updateNavigator(String db, String key, List data) {
        key = key ?: ""
        JdbcTemplate template = template(db)
        int count = template.queryForObject("select count(*) from ttx_navigator where key=?", Integer.class, key)
        String sql = "update ttx_navigator set structure=? where key=?"
        if (count == 0)
            sql = "insert into ttx_navigator (version,structure,key) values(0,?,?)"
        def code = '0', desc = 'ok'
        try {
            template.update(sql, (data as JSONArray).toString(), key)
        } catch (e) {
            code = '1'
            desc = e.toString()
        }
        return [code: code, desc: desc]
    }

    // 所有表名称
    def getTables(String db) {
        def sql = '''
select relname as TABLE_NAME ,col_description(c.oid, 0) as COMMENTS from pg_class c
where relkind = 'r' and relname not like 'pg_%' and relname not like 'sql_%' order by relname
'''
        sql = '''
select relname as TABLE_NAME from pg_class c
where relkind = 'r' and relname not like 'pg_%' and relname not like 'sql_%' order by relname
'''
        List<String> list = template(db).queryForList(sql, String.class)
//        list.each {
//            new JsonSlurper().parseText(RedisUtil.get("$db:${AppProfile.TABLE_TABLE_MODEL}:$tableKey"))
//            RedisUtil.set("$db:${AppProfile.TABLE_TABLE_MODEL}:$tableKey", it)
//        }
        list
    }

    // 表字段
    def getFields(String db, String table) {
        SqlRowSet rowSet = template(db).queryForRowSet("select * from $table where 1=2 ")
        SqlRowSetMetaData meta = rowSet.getMetaData()
        (1..meta.columnCount).collect { index ->
            String columnName = meta.getColumnName(index)
            [
                    "id"   : columnName,
                    'field': columnName,
                    'name' : columnName,
                    'type' : FieldType.getType(meta.getColumnTypeName(index)) // TODO
            ]
        }
    }

    def getModel(String db, String table, String key) {
        template(db).queryForMap("select * from ${table} where key=?", key).structure
    }

    def getModels(String db, String table) {
        def list = template(db).queryForList("select * from $table")
        def models = list.collect { Map map ->
            RedisUtil.set("$db:$table:${map.key}", map.structure)
            new JsonSlurper().parseText(map.structure) as Map
        }
        models
    }

    def createModel(String db, String table, Map map) {
        JdbcTemplate template = template(db)
        String key = map['key']
        int count = template.queryForObject("select count(1) from ${table} where key=?", Integer.class, key)
        if (count > 0)
            return [code: '1', desc: "key [${key}] existed"]
        String sql = "insert into ${table}(version,key,structure) values(1,?,?)"
        def code = '0', desc = 'ok'
        try {
            template.update(sql, key, (map as JSONObject).toString())
        } catch (e) {
            code = '1'
            desc = e.toString()
        }
        getModels(db, table)//    update cache    #todo to change 不用update那么多的缓存
        [code: code, desc: desc]
    }

    def updateModel(String db, String table, Map map) {
        JdbcTemplate template = template(db)
        String key = map['key']
        int count = template.queryForObject("select count(key) from ${table} where key=?", Integer.class, key)
        if (count == 0)
            return [code: '1', desc: "key [${key}] not existed in ${table}"]
        if (count > 1)
            return [code: '1', desc: "key [${key}] more than one items in ${table}"]
        String sql = "update ${table} set structure=? where key=?"
        def code = '0', desc = 'ok'
        try {
            template.update(sql, (map as JSONObject).toString(), key)
        } catch (e) {
            code = '1'
            desc = e.toString()
        }
        getModels(db, table)//    update cache    #todo to change
        return [code: code, desc: desc]
    }

    def deleteModel(String db, String table, String key) {
        def code = '0', desc = 'ok'
        try {
            template(db).update("delete from ${table} where key=?", key)
        } catch (e) {
            code = '1'
            desc = e.toString()
        }
        RedisUtil.del("$db:$table:$key")
        return [code: code, desc: desc]
    }

    def getViewData(String db, String tid, String oid) {
        String table = AppProfile.TABLE_WSO_DATA
        try {
            def map = template(db).queryForMap("select * from $table where tid=? and oid=?", tid, oid)
            RedisUtil.set("$db:$table:${map.tid}:${map.oid}", map.structure)
            return new JsonSlurper().parseText(map.structure)
        } catch (EmptyResultDataAccessException e) {
            return []
        }
    }

    def getViewDatas(String db) {
        String table = AppProfile.TABLE_WSO_DATA
        def list = template(db).queryForList("select * from $table")
        def models = list.collect { Map map ->
            RedisUtil.set("$db:$table:${map.tid}:${map.oid}", map.structure)
            new JsonSlurper().parseText(map.structure) as Map
        }
        models
    }

    def getViewDatasByTid(String db, String tid) {
        String table = AppProfile.TABLE_WSO_DATA
        def list = template(db).queryForList("select * from $table where tid=?", tid)
        def models = list.collect { Map map ->
            RedisUtil.set("$db:$table:${map.tid}:${map.oid}", map.structure)
            new JsonSlurper().parseText(map.structure) as Map
        }
        models
    }

    def createViewData(String db, Map map) {
        String table = AppProfile.TABLE_WSO_DATA
        JdbcTemplate template = template(db)
        String tid = map.tid
        String oid = map.oid
        int count = template.queryForObject("select count(1) from ${table} where tid=? and oid=?", Integer.class, tid, oid)
        if (count > 0)
            return [code: '1', desc: "key [${tid},${oid}] existed"]
        String sql = "insert into ${table}(version,tid,oid,structure) values(0,?,?,?)"
        def code = '0', desc = 'ok'
        try {
            template.update(sql, tid, oid, (map as JSONObject).toString())
        } catch (e) {
            code = '1'
            desc = e.toString()
        }
        getViewData(db, tid, oid)//    update cache    #todo 直接更新不用sql
        [code: code, desc: desc]
    }

    def updateViewData(String db, Map map) {
        String table = AppProfile.TABLE_WSO_DATA
        JdbcTemplate template = template(db)
        String tid = map.tid
        String oid = map.oid
        int count = template.queryForObject("select count(1) from ${table} where tid=? and oid=?", Integer.class, tid, oid)
        if (count == 0)
            return [code: '1', desc: "key [${tid},${oid}] not existed in ${table}"]
        if (count > 1)
            return [code: '1', desc: "key [${tid},${oid}] more than one items in ${table}"]
        def code = '0', desc = 'ok'
        try {
            update(db).withTableName(table).execute([
                    structure: (map as JSONObject).toString()
            ], [
                    tid: tid,
                    oid: oid
            ])
        } catch (e) {
            code = '1'
            desc = e.toString()
        }
        getViewData(db, tid, oid)//    update cache    #todo 直接更新不用再sql
        [code: code, desc: desc]
    }

    def deleteViewData(String db, String tid, String oid) {
        String table = AppProfile.TABLE_WSO_DATA
        def code = '0', desc = 'ok'
        try {
            template(db).update("delete from ${table} where tid=? and oid=?", tid, oid)
        } catch (e) {
            code = '1'
            desc = e.toString()
        }
        RedisUtil.del("$db:$table:$tid:$oid")
        return [code: code, desc: desc]
    }
}
