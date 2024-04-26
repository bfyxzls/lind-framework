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
    `like_list`            varchar(255)  DEFAULT NULL,
    `extension_info`       varchar(512)  DEFAULT NULL,
    PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `t_log`;
CREATE TABLE `t_log`
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
