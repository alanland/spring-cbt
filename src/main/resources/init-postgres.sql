CREATE TABLE ttx_user_role (id int4, version int4, user_code varchar(80), role_code varchar(80));
CREATE TABLE ttx_sequence ("key" varchar(80), sequence int4);
CREATE TABLE ttx_role (version int4, code varchar(80), name varchar(80));
CREATE TABLE ttx_user_profile (version int4, code varchar(80), password varchar(80));
CREATE TABLE ttx_action_right (version int4, tid varchar(80), oid varchar(80), nid varchar(80), structure varchar(20000), role varchar(80));
CREATE TABLE ttx_wso_data (version int4, tid varchar(80), nid varchar(80), structure varchar(20000));
CREATE TABLE ttx_navigator (version int4, "key" varchar(80), structure varchar(20000));
CREATE TABLE ttx_bill_model (version int4, "key" varchar(80), structure varchar(20000));
CREATE TABLE ttx_table_model (version int4, "key" varchar(80), structure varchar(20000));


INSERT INTO ttx_user(version, code, password) VALUES (0, 'admin', 'admin123');
INSERT INTO ttx_role(version, code, name) VALUES (0, 'admin', '系统管理');
INSERT INTO ttx_user_role(id, version, user_code, role_code) VALUES (-1, 0, 'admin', 'admin');
