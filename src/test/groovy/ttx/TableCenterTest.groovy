package ttx

import ttx.model.meta.MetaTable
import ttx.model.TableRegister
import ttx.util.Pinyin

class TableCenterTest extends GroovyTestCase {

    void testLoad() {
        TableRegister.loadTables()
        MetaTable model = TableRegister.getModel('owner')
        assertEquals('owner', model.key)
        assertEquals(3, model.fields.size())

        def json = model.getJsonBuilder().toPrettyString()
        println model.getJsonBuilder().toString()
        new File('/home/journey/day/svn/cbt/src/main/resources/config/results/owner.json').write(json, 'utf-8')
    }

    void testPinyin() {
        assertEquals('zg02ah', Pinyin.toHintCode('中国02a号'))
    }
}
