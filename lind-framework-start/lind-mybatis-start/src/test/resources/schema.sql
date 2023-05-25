----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`
(
    `id`                   varchar(255) NOT NULL,
    `create_by`            varchar(255)  DEFAULT NULL,
    `create_time`          datetime(0) DEFAULT NULL,
    `update_by`            varchar(255)  DEFAULT NULL,
    `update_time`          datetime(0) DEFAULT NULL,
    `del_flag`             int(11) DEFAULT NULL,

    `address`              varchar(255)  DEFAULT NULL,
    `avatar`               varchar(1000) DEFAULT NULL,
    `description`          varchar(255)  DEFAULT NULL,
    `email`                varchar(255)  DEFAULT NULL,
    `mobile`               varchar(255)  DEFAULT NULL,
    `password`             varchar(255)  DEFAULT NULL,
    `sex`                  int(11) DEFAULT NULL,
    `status`               int(11) DEFAULT NULL,
    `type`                 int(11) DEFAULT NULL,
    `username`             varchar(255)  DEFAULT NULL,
    `department_id`        varchar(255)  DEFAULT NULL,
    `create_department_id` varchar(255)  DEFAULT NULL,
    `street`               varchar(255)  DEFAULT NULL,
    `pass_strength`        varchar(2)    DEFAULT NULL,
    `nick_name`            varchar(255)  DEFAULT NULL,
    `account_type`         int(1) NOT NULL DEFAULT 0 COMMENT '0:主账户  1：子账户',
    `master_id`            varchar(64)   DEFAULT NULL COMMENT '主账户id',
    PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `t_log_20230525`;
CREATE TABLE `t_log_20230525`
(
    `id`          varchar(255) NOT NULL,
    `create_by`   varchar(255) DEFAULT NULL,
    `create_time` datetime(0) DEFAULT NULL,
    `update_by`   varchar(255) DEFAULT NULL,
    `update_time` datetime(0) DEFAULT NULL,
    `del_flag`    int(11) DEFAULT NULL,
    `message`     varchar(255) default NULL,
    PRIMARY KEY (`id`)
);

-- SET @table_name = CONCAT('t_log_', year(now()),month(now()),day(now()));
-- SET @delSql=CONCAT('DROP TABLE IF EXISTS ', @table_name);
-- PREPARE del FROM @delSql;
-- EXECUTE del;
--
-- SET @sql = CONCAT('CREATE TABLE ', @table_name, ' (
--     `id`          varchar(255) NOT NULL,
--     `create_by`   varchar(255) DEFAULT NULL,
--     `create_time` datetime(0) DEFAULT NULL,
--     `update_by`   varchar(255) DEFAULT NULL,
--     `update_time` datetime(0) DEFAULT NULL,
--     `del_flag`    int(11) DEFAULT NULL,
--     `message`     varchar(255) default NULL,
--     PRIMARY KEY (`id`)
-- )');
-- PREPARE stmt FROM @sql;
-- EXECUTE stmt;
