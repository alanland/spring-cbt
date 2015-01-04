package ttx.jdbc

import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import paillard.florent.springframework.simplejdbcupdate.SimpleJdbcUpdate
import ttx.model.outbound.ShipHeader

import java.sql.Driver
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.util.concurrent.atomic.AtomicLong

class JdbcUtil {

    static SimpleDriverDataSource dataSource = null
    static JdbcTemplate template = null
    static SimpleJdbcInsert insert = null
    static SimpleJdbcUpdate update = null

    private static final AtomicLong counter = new AtomicLong();

    static getDatabaseType(JdbcTemplate template) {
        // TODO data source for production
        SimpleDriverDataSource dataSource = template.getDataSource() as SimpleDriverDataSource
        Driver driver = dataSource.getDriver()
        // TODO database type detection
        def a = [1: 3]
        Map<Class, DatabaseType> typeMap = new HashMap<>();
        typeMap.put(org.postgresql.Driver, DatabaseType.postgres)
        typeMap.put(org.h2.Driver.class, DatabaseType.postgres)
        typeMap.get(driver.getClass(), DatabaseType.unknown)
    }


    static def getDataSource() {
        if (!dataSource) {
            dataSource = new SimpleDriverDataSource();
            dataSource.setDriverClass(org.h2.Driver.class);
            dataSource.setUsername("sa");
            dataSource.setUrl("jdbc:h2:mem");
            dataSource.setPassword("");

            dataSource = new SimpleDriverDataSource(
                    driverClass: org.postgresql.Driver.class,
                    username: 'wang',
                    password: 'wang',
                    url: 'jdbc:postgresql://localhost/wang'
            )
        }
        dataSource
    }

    static JdbcTemplate getTemplate() {
        if (!template) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
            template = jdbcTemplate
        }
        template
    }

    static SimpleJdbcInsert getInsert() {
//        if (!insert) {
//            insert = new SimpleJdbcInsert(dataSource)
//        }
//        insert
        new SimpleJdbcInsert(getDataSource())
    }

    static SimpleJdbcUpdate getUpdate() {
        new SimpleJdbcUpdate(getDataSource())
    }

    static void initData() {
        template = getTemplate()
        String sql = "insert into wm_ship_header(bill_id,bill_no,sh_owner,sh_count) values(?,?,?,?)"
        final int size = 100;
        List list = []
        Map map = [:] // bill_id : bill_no

        // 插入单头
        for (int i in 1..size) {
            long billId = counter.incrementAndGet()
            String billNo = '20141212_' + i
            map[billId] = billNo
            list.add([billId, billNo, 'owner_' + i, (int) Math.random() * 100])
        }
        template.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            void setValues(PreparedStatement ps, int i) throws SQLException {
                def item = list[i]
                item.eachWithIndex { entry, int index ->
                    ps.setObject(index + 1, entry)
                }
            }

            @Override
            int getBatchSize() {
                return size
            }
        });

        // 插入明细
        sql = "insert into wm_ship_line(bill_id,bill_no,line_id,sl_material,sl_spec,sl_qty) values(?,?,?,?,?,?)"
        list = []
        map.each { billId, billNo ->
            (1..5).each {
                list.add([billId, billNo, counter.incrementAndGet(), "material_" + it, "spec_" + it, (int) Math.random() * 10 + 1])
            }
        }
        template.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            void setValues(PreparedStatement ps, int i) throws SQLException {
                def item = list[i]
                item.eachWithIndex { entry, int index ->
                    ps.setObject(index + 1, entry)
                }
            }

            @Override
            int getBatchSize() {
                return list.size()
            }
        })


    }

    static void initData2() {
        template = getTemplate()
        template.execute("drop table ship_header if exists")
        template.execute("""
        create table ship_header(
            id serial,
            sh_no varchar(255),
            sh_owner varchar(255),
            sh_count int
        )""")

        template.execute("drop table ship_line if exists")
        template.execute("""
        create table ship_line(
            id serial,
            header_id int,
            header_no varchar(255),
            sl_material varchar(255),
            sl_spec varchar(255),
            sl_qty int
        )""")

        for (i in 1..333) {
            template.update(
                    "insert into ship_header(sh_no,sh_owner,sh_count) values (?,?,?)",
                    "no" + System.currentTimeMillis(),
                    "owner_i",
                    Math.random() * 30 as int
            )
        }
        List<ShipHeader> ships = template.query("select * from ship_header", new RowMapper() {
            @Override
            Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                new ShipHeader(
                        id: rs.getInt('id'),
                        no: rs.getString('sh_no'),
                        owner: rs.getString('sh_owner'),
                        count: rs.getInt('sh_count')
                )
            }
        })
        for (ShipHeader ship : ships) {
            for (i in 1..(int) Math.random() * 2) {
                template.update(
                        "insert into ship_line(header_id,header_no,sl_material,sl_spec,sl_qty) values(?,?,?,?,?)",
                        ship.id,
                        ship.no,
                        "material_$i",
                        "spec_$i",
                        Math.random() * 10 as int
                )
            }
        }
    }


}
