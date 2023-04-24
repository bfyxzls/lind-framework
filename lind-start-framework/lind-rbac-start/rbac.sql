/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50727
 Source Host           : localhost:3306
 Source Schema         : rbac

 Target Server Type    : MySQL
 Target Server Version : 50727
 File Encoding         : 65001

 Date: 29/06/2022 14:35:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dictionary
-- ----------------------------
DROP TABLE IF EXISTS `dictionary`;
CREATE TABLE `dictionary`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'code码',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '名称',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '类型',
  `p_id` int(11) DEFAULT NULL COMMENT '父id',
  `front_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '前台的code码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '下拉框字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dictionary
-- ----------------------------
INSERT INTO `dictionary` VALUES (30, NULL, '操作类型', 'operateType', NULL, NULL);
INSERT INTO `dictionary` VALUES (31, '1', '登录', 'operateType', 30, '1');
INSERT INTO `dictionary` VALUES (32, '2', '登出', 'operateType', 30, '2');
INSERT INTO `dictionary` VALUES (33, '3', '添加', 'operateType', 30, '3');
INSERT INTO `dictionary` VALUES (34, '4', '编辑', 'operateType', 30, '4');
INSERT INTO `dictionary` VALUES (35, '5', '删除', 'operateType', 30, '5');
INSERT INTO `dictionary` VALUES (36, '6', '上架', 'operateType', 30, '6');
INSERT INTO `dictionary` VALUES (37, '7', '下架', 'operateType', 30, '7');
INSERT INTO `dictionary` VALUES (38, '10', '预览', 'operateType', 30, '10');
INSERT INTO `dictionary` VALUES (39, '9', '图片预览', 'operateType', 30, '9');
INSERT INTO `dictionary` VALUES (40, '10', '添加原稿', 'operateType', 30, '10');
INSERT INTO `dictionary` VALUES (41, '11', '添加译稿', 'operateType', 30, '11');
INSERT INTO `dictionary` VALUES (42, '12', '刷新', 'operateType', 30, '12');
INSERT INTO `dictionary` VALUES (43, '13', '导出', 'operateType', 30, '13');
INSERT INTO `dictionary` VALUES (44, '14', '目录导出', 'operateType', 30, '14');
INSERT INTO `dictionary` VALUES (45, '15', '分配权限', 'operateType', 30, '15');
INSERT INTO `dictionary` VALUES (46, '16', '前台隐藏', 'operateType', 30, '16');
INSERT INTO `dictionary` VALUES (47, '17', '批量下架', 'operateType', 30, '17');

-- ----------------------------
-- Table structure for operate_log
-- ----------------------------
DROP TABLE IF EXISTS `operate_log`;
CREATE TABLE `operate_log`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `data_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `module_type` int(11) DEFAULT NULL,
  `content` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `operate_type` int(11) DEFAULT NULL,
  `operator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `operator_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `operate_time` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `api_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '后台地址',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'url路径',
  `file_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'vue文件路径',
  `http_method` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'http请求方式',
  `sort_number` double(11, 0) NOT NULL DEFAULT 100,
  `type` int(11) DEFAULT NULL,
  `parent_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_time` datetime(0) DEFAULT NULL,
  `update_time` datetime(0) DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `del_flag` int(11) DEFAULT NULL,
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '图标',
  `operate_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '操作类型，对应字典里的OperateType',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES ('1', '北大英华', NULL, '', '', 'POST', 100, 0, '', '2022-04-15 13:05:36', '2022-04-15 13:05:41', NULL, NULL, 0, 'aggregation', NULL);
INSERT INTO `sys_permission` VALUES ('19', '角色管理', '/role/query**', '/sys/roleList', '<role-list/>', NULL, 100, 0, '6', '2022-04-16 23:18:46', '2022-04-16 23:18:46', 'admin', 'admin', 0, 'aggregation', NULL);
INSERT INTO `sys_permission` VALUES ('20', '用户管理', '/user/list**', '/sys/userList', '<user-list/>', NULL, 100, 0, '6', '2022-04-16 23:18:46', '2022-04-16 23:18:46', 'admin', 'admin', 0, 'aggregation', NULL);
INSERT INTO `sys_permission` VALUES ('21', '菜单管理', '/permission/list**', '/sys/menu', '<menu-list/>', 'GET', 100, 0, '6', '2022-04-16 23:18:46', '2022-04-16 23:18:46', 'admin', 'admin', 0, 'aggregation', NULL);
INSERT INTO `sys_permission` VALUES ('22', '操作日志', '/operateLog/**', '/operationList', '<operate-list/>', 'GET', 100, 0, '6', '2022-04-16 23:18:46', '2022-04-16 23:18:46', 'admin', 'admin', 0, 'aggregation', NULL);
INSERT INTO `sys_permission` VALUES ('23', '添加用户', '/user/add**', '', '', 'POST', 2, 1, '20', '2022-04-16 23:18:46', '2022-04-16 23:18:46', 'admin', 'admin', 0, 'aggregation', '3');
INSERT INTO `sys_permission` VALUES ('24', '编辑用户', '/user/update/**', '', '', 'PUT', 1, 1, '20', '2022-04-16 23:18:46', '2022-04-16 23:18:46', 'admin', 'admin', 0, 'aggregation', '4');
INSERT INTO `sys_permission` VALUES ('25', '删除用户', '/user/del/**', '', '', 'DELETE', 4, 1, '20', '2022-04-16 23:18:46', '2022-04-16 23:18:46', 'admin', 'admin', 0, 'aggregation', '4');
INSERT INTO `sys_permission` VALUES ('26', '批量删除用户', '/user/bulk-del**', '', '', 'POST', 3, 1, '20', '2022-04-16 23:18:46', '2022-04-16 23:18:46', 'admin', 'admin', 0, 'aggregation', '4');
INSERT INTO `sys_permission` VALUES ('27', '重置密码', '/user/bulk-reset-password**', '', '', 'PUT', 100, 1, '20', '2022-04-16 23:18:46', '2022-04-16 23:18:46', 'admin', 'admin', 0, 'aggregation', '4');
INSERT INTO `sys_permission` VALUES ('28', '编辑角色', '/role/update/**', '', '', 'PUT', 100, 1, '19', '2022-04-16 23:18:46', '2022-04-16 23:18:46', 'admin', 'admin', 0, 'aggregation', '4');
INSERT INTO `sys_permission` VALUES ('29', '删除角色', '/api/role/del/**', '', '', 'DELETE', 100, 1, '19', '2022-04-16 23:18:46', '2022-04-16 23:18:46', 'admin', 'admin', 0, 'aggregation', '4');
INSERT INTO `sys_permission` VALUES ('30', '批量删除角色', '/role/bulk-del**', '', '', 'POST', 100, 1, '19', '2022-04-16 23:18:46', '2022-04-16 23:18:46', 'admin', 'admin', 0, 'aggregation', '4');
INSERT INTO `sys_permission` VALUES ('31', '添加角色', '/role/add**', '', '', 'POST', 100, 1, '19', '2022-04-16 23:18:46', '2022-04-16 23:18:46', 'admin', 'admin', 0, 'aggregation', '3');
INSERT INTO `sys_permission` VALUES ('37', '删除菜单', '/permission/del/**', '', '', 'DELETE', 100, 1, '6', '2022-04-16 23:18:46', '2022-04-16 23:18:46', 'admin', 'admin', 0, 'aggregation', NULL);
INSERT INTO `sys_permission` VALUES ('5', '数据管理', NULL, '', NULL, 'GET', 1, 0, '1', '2022-04-15 13:05:36', '2022-04-15 13:05:41', NULL, NULL, 0, 'aggregation', NULL);
INSERT INTO `sys_permission` VALUES ('6', '系统管理', NULL, '', NULL, 'GET', 2, 0, '1', '2022-04-15 13:05:36', '2022-04-15 13:05:41', NULL, NULL, 0, 'aggregation', NULL);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '姓名',
  `create_time` datetime(0) DEFAULT NULL,
  `update_time` datetime(0) DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `del_flag` int(11) DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '管理员', NULL, NULL, NULL, NULL, 0, NULL);

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `role_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `permission_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `selected` bit(1) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES ('10', '1', '1', b'1');
INSERT INTO `sys_role_permission` VALUES ('11', '1', '24', b'0');
INSERT INTO `sys_role_permission` VALUES ('12', '1', '25', b'0');
INSERT INTO `sys_role_permission` VALUES ('13', '1', '26', b'0');
INSERT INTO `sys_role_permission` VALUES ('14', '1', '27', b'0');
INSERT INTO `sys_role_permission` VALUES ('15', '1', '28', b'0');
INSERT INTO `sys_role_permission` VALUES ('1506897376846630913', '1', '2', b'0');
INSERT INTO `sys_role_permission` VALUES ('1506897376855019521', '1', '3', b'0');
INSERT INTO `sys_role_permission` VALUES ('1506897376867602433', '1', '4', b'0');
INSERT INTO `sys_role_permission` VALUES ('1506897376875991042', '1', '5', b'0');
INSERT INTO `sys_role_permission` VALUES ('1514057482044018690', '1', '11', b'0');
INSERT INTO `sys_role_permission` VALUES ('1514057482056601602', '1', '12', b'0');
INSERT INTO `sys_role_permission` VALUES ('1514057482077573121', '1', '13', b'0');
INSERT INTO `sys_role_permission` VALUES ('1514057482081767425', '1', '14', b'0');
INSERT INTO `sys_role_permission` VALUES ('1514057482098544641', '1', '15', b'0');
INSERT INTO `sys_role_permission` VALUES ('1514884706477289474', '1', '16', b'0');
INSERT INTO `sys_role_permission` VALUES ('1515346543174053889', '1', '17', b'0');
INSERT INTO `sys_role_permission` VALUES ('1515346831633117186', '1', '18', b'0');
INSERT INTO `sys_role_permission` VALUES ('1515347060306571265', '1', '19', b'0');
INSERT INTO `sys_role_permission` VALUES ('1515348393243799553', '1', '20', b'0');
INSERT INTO `sys_role_permission` VALUES ('1515348981973086209', '1', '22', b'0');
INSERT INTO `sys_role_permission` VALUES ('16', '1', '29', b'0');
INSERT INTO `sys_role_permission` VALUES ('17', '1', '30', b'0');
INSERT INTO `sys_role_permission` VALUES ('18', '1', '31', b'0');
INSERT INTO `sys_role_permission` VALUES ('19', '1', '32', b'0');
INSERT INTO `sys_role_permission` VALUES ('20', '1', '33', b'0');
INSERT INTO `sys_role_permission` VALUES ('21', '1', '34', b'0');
INSERT INTO `sys_role_permission` VALUES ('22', '1', '35', b'0');
INSERT INTO `sys_role_permission` VALUES ('23', '1', '36', b'0');
INSERT INTO `sys_role_permission` VALUES ('24', '1', '21', b'0');
INSERT INTO `sys_role_permission` VALUES ('25', '1', '37', b'0');
INSERT INTO `sys_role_permission` VALUES ('26', '1', '27', b'0');
INSERT INTO `sys_role_permission` VALUES ('3', '1', '6', b'0');
INSERT INTO `sys_role_permission` VALUES ('4', '1', '7', b'0');
INSERT INTO `sys_role_permission` VALUES ('5', '1', '8', b'0');
INSERT INTO `sys_role_permission` VALUES ('6', '1', '9', b'0');
INSERT INTO `sys_role_permission` VALUES ('7', '1', '10', b'0');
INSERT INTO `sys_role_permission` VALUES ('8', '1', '23', b'0');
INSERT INTO `sys_role_permission` VALUES ('9', '1', '24', b'0');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `real_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_time` datetime(0) DEFAULT NULL,
  `update_time` datetime(0) DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `del_flag` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT 0 COMMENT '状态',
  `organization` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `job` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `gender` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '{bcrypt}$2a$10$vValqlVTQrdUS/UITOUcmuQbHdCZHpJBL9cgKkC6zpbo0YNN6nnQ.', 'admin@sina.com', '13521972990', 'Q9UaX//jLbPrwWV2SPZTu6EtPxQV3fytolaBrn3/vK3LO02xpXGaDSO1VBSpe4Lh4MkM5Sfpfca8ZaT1o9iwkazwAYAJ6CTdxiqQp9X65D6S49/86Bz5Y83GWJqc0nphT9oEGkwzRsLrEK0gjsJDorty5bfTJydQNr1SxP93kq4=', '2022-04-21 10:28:07', '2022-04-25 17:08:07', 'admin', 'system', 0, 0, 'LfrM9wHWO54smwxGvogJskq3DfY1qAQBeHUH68981iH34yCxYyRayr1rSaNYT2pBxouzUCADaLbCbAxxY4QIqNE1wYKvkUx9tN+Iya2T0Idj7LleksJi8OZeXwLpqS4prbCBhVqGHdV/Nc2qpewTN58bpgz0ituGsVORNfduoLk=', 'ShGiqh8uwLVhO2bq59F5GH9b6WC5G0H1OjgI8lhlN35nKnICwRdki526dSQSoz0Cb50CsGcuS16IVslqhvt7w2gygyYeuZs1TIoggvz2OJxC8NZkV/ywQnYyP2+2Cgvl8glL8vYopG+goePLcblUX32M1/GXz5L1AxUKF5HQpfk=', 1);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `role_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', '1', '1');

SET FOREIGN_KEY_CHECKS = 1;
