package ttx.domain

/**
 * ＠author 王成义 
 * @created 2015-01-11.
 */
class User {
    Long id
    String username
    String password //TODO to char[]
    List<Role> roleList
}
