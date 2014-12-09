package ttx.jdbc.demo

import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import ttx.jdbc.JdbcUtil

import java.sql.PreparedStatement
import java.sql.SQLException
import java.util.concurrent.atomic.AtomicLong

/**
 * Created by journey on 14-12-8.
 */
class UserDatas {
    private static final AtomicLong counter = new AtomicLong();

    static List roleMap = ['Admin', 'Admin1', 'AdminUser', 'User', 'SJ', 'CG', 'DD']

    static void create() {
        JdbcTemplate template = JdbcUtil.getTemplate()
        String sql = '''insert into ttx_user(
us_usercode,us_username,us_department,us_password,us_role,us_active,us_start_date,us_end_date,us_allow_login
) values (?,?,?,?,?,?,?,?,?) '''
        final int size = 100;
        List list = []
        // 插入单头
        for (int i in 1..size) {
            list.add([
                    'code' + i,
                    'name' + i,
                    '部门' + (int) (Math.random() * 10 + 1),
                    '***',
                    roleMap.get((int) (Math.random() * roleMap.size())),
                    (int) (Math.random() * 2),
                    new java.sql.Date(new Date().getTime()),
                    new java.sql.Date(new Date().getTime()),
                    (int) (Math.random() * 2)
            ])
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
    }
}
