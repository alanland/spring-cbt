package ttx.service.base

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import paillard.florent.springframework.simplejdbcupdate.SimpleJdbcUpdate
import ttx.jdbc.JdbcUtil

/**
 * Created by journey on 14-12-5.
 */
class BaseService {
    @Autowired
    JdbcUtil jdbc

    BaseService() {}

    JdbcTemplate template(String db) {
        jdbc.template(db)
    }

    NamedParameterJdbcTemplate namedTemplate(String db) {
        jdbc.namedTemplate(db)
    }

    SimpleJdbcInsert insert(String db) {
        jdbc.insert(db)
    }

    SimpleJdbcUpdate update(String db) {
        jdbc.update(db)
    }


}
