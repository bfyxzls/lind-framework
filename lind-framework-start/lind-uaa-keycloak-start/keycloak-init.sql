/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.119.131
 Source Server Type    : MySQL
 Source Server Version : 50732
 Source Host           : 192.168.119.131:3306
 Source Schema         : keycloak

 Target Server Type    : MySQL
 Target Server Version : 50732
 File Encoding         : 65001

 Date: 02/12/2020 11:23:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ADMIN_EVENT_ENTITY
-- ----------------------------
DROP TABLE IF EXISTS `ADMIN_EVENT_ENTITY`;
CREATE TABLE `ADMIN_EVENT_ENTITY`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `ADMIN_EVENT_TIME` bigint(20) DEFAULT NULL,
  `REALM_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `OPERATION_TYPE` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `AUTH_REALM_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `AUTH_CLIENT_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `AUTH_USER_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `IP_ADDRESS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `RESOURCE_PATH` varchar(2550) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `REPRESENTATION` text CHARACTER SET latin1 COLLATE latin1_swedish_ci,
  `ERROR` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `RESOURCE_TYPE` varchar(64) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ASSOCIATED_POLICY
-- ----------------------------
DROP TABLE IF EXISTS `ASSOCIATED_POLICY`;
CREATE TABLE `ASSOCIATED_POLICY`  (
  `POLICY_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `ASSOCIATED_POLICY_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`POLICY_ID`, `ASSOCIATED_POLICY_ID`) USING BTREE,
  INDEX `IDX_ASSOC_POL_ASSOC_POL_ID`(`ASSOCIATED_POLICY_ID`) USING BTREE,
  CONSTRAINT `FK_FRSR5S213XCX4WNKOG82SSRFY` FOREIGN KEY (`ASSOCIATED_POLICY_ID`) REFERENCES `RESOURCE_SERVER_POLICY` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_FRSRPAS14XCX4WNKOG82SSRFY` FOREIGN KEY (`POLICY_ID`) REFERENCES `RESOURCE_SERVER_POLICY` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ASSOCIATED_POLICY
-- ----------------------------
INSERT INTO `ASSOCIATED_POLICY` VALUES ('d4063bf0-5388-40c7-afa2-42ad44cf441e', '2486a449-2904-4b57-ab60-ef3dc73bf49e');

-- ----------------------------
-- Table structure for AUTHENTICATION_EXECUTION
-- ----------------------------
DROP TABLE IF EXISTS `AUTHENTICATION_EXECUTION`;
CREATE TABLE `AUTHENTICATION_EXECUTION`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `ALIAS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `AUTHENTICATOR` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `FLOW_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `REQUIREMENT` int(11) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `AUTHENTICATOR_FLOW` bit(1) NOT NULL DEFAULT b'0',
  `AUTH_FLOW_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `AUTH_CONFIG` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `IDX_AUTH_EXEC_REALM_FLOW`(`REALM_ID`, `FLOW_ID`) USING BTREE,
  INDEX `IDX_AUTH_EXEC_FLOW`(`FLOW_ID`) USING BTREE,
  CONSTRAINT `FK_AUTH_EXEC_FLOW` FOREIGN KEY (`FLOW_ID`) REFERENCES `AUTHENTICATION_FLOW` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_AUTH_EXEC_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of AUTHENTICATION_EXECUTION
-- ----------------------------
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('01189026-b2a6-471a-8957-159a5f332448', NULL, 'basic-auth-otp', 'master', 'fe76adc1-5919-4f82-8a15-5824d2f78030', 3, 20, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('0285eadd-832b-4982-a04a-02763ba65b67', NULL, NULL, 'demo', 'cbc582c8-1594-4065-9a40-a457792c894b', 1, 20, b'1', 'da8d37db-c914-434d-ab35-62074d59dfae', NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('07e0d78f-af81-41d6-8dc8-6ea44e1fb60a', NULL, NULL, 'master', 'ace5c5dd-66b6-4b57-b218-8a876c8dee75', 1, 20, b'1', '8f4b7bb3-9d23-4c0a-8435-fd0d40193295', NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('094e5388-a3e2-4a17-99c2-3563c890af7a', NULL, 'client-secret', 'master', '12b75397-babc-4d8a-9836-c4d9b373b53a', 2, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('09fdf3bf-f1c5-4c27-9b1e-026a0ca77654', NULL, 'conditional-user-configured', 'demo', 'da8d37db-c914-434d-ab35-62074d59dfae', 0, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('0a27494d-683a-42ef-8ac0-1bae4ab58f9d', NULL, 'reset-password', 'demo', 'd8e22d97-5a49-4841-ab5e-58f48b7b3752', 0, 30, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('0a8e29e9-a1f9-44e3-b0ab-705773399ed7', NULL, 'registration-recaptcha-action', 'demo', '221ee0fd-3223-4a75-a438-5b32d67b4c77', 3, 60, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('0e4b1bb8-01d6-4828-b7cf-b5d01094b384', NULL, NULL, 'demo', 'ad5470c8-4e0c-4144-98b6-37e8089c6d1a', 2, 30, b'1', 'cbc582c8-1594-4065-9a40-a457792c894b', NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('105e071d-75a9-40e4-bb22-4f80fcca4b1b', NULL, 'http-basic-authenticator', 'demo', 'ac5667b0-1adf-47ac-a57e-e10a8a74c63f', 0, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('10bc8e6c-b89a-41f5-9058-3b2dae76a157', NULL, 'auth-username-password-form', 'master', '944ed300-f7d5-4170-8e19-6f2d57f2502a', 0, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('12c7bb90-10c0-428e-a324-c50801b049d4', NULL, NULL, 'demo', '5eebb853-88b5-462d-be99-dea430b53459', 2, 20, b'1', '912bd7e6-2a89-4f49-8d84-8407a1eeeb61', NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('151552f8-9ba5-44f7-96ad-80a0df2cd023', NULL, 'client-jwt', 'demo', '45262779-1e0f-4bd2-907a-2c5c6203eb4c', 2, 20, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('161b9167-4436-467d-bb7b-bc32eaa236b5', NULL, NULL, 'demo', 'f29fecd0-54d5-4f35-8ad4-b5254c60fab1', 0, 20, b'1', '115f744c-e050-45ce-b4d3-9feb2e811ef5', NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('18660eb4-cb21-42ba-8736-1c391d89d8e8', NULL, 'auth-spnego', 'demo', 'ad5470c8-4e0c-4144-98b6-37e8089c6d1a', 3, 20, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('1c65fc64-6b63-4cd9-8e2e-5a64c3f37d8a', NULL, 'auth-otp-form', 'master', '4a9769aa-7eb8-473d-980b-5601355b1893', 0, 20, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('1da98a5a-559c-4339-806d-b09b38c5a289', NULL, 'reset-credential-email', 'demo', 'd8e22d97-5a49-4841-ab5e-58f48b7b3752', 0, 20, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('2337513f-0c8e-4f16-8f70-ada604b97efd', NULL, 'direct-grant-validate-username', 'demo', 'fefadc8e-45e8-4fb5-a00e-dfd976f05408', 0, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('26a02c8a-17d2-4367-a85e-88a8a84ebc9d', NULL, 'client-secret', 'demo', '45262779-1e0f-4bd2-907a-2c5c6203eb4c', 2, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('2dbf111e-14a4-4e5b-9267-4f849ee5b3de', NULL, NULL, 'master', '34fbdb0f-40b1-439c-9fbf-20c8a0cbb29d', 0, 20, b'1', '26b16f8b-19c0-4a92-9ebc-5dcba21c16e9', NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('2e1b47f0-495d-42d5-9a09-1425b4db7dc3', NULL, 'conditional-user-configured', 'demo', '88f1e9d4-4a33-48b6-8e6e-3167f3ecd7dd', 0, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('306dc5c7-bbf5-4ab8-9fd6-1f288729204a', NULL, 'auth-spnego', 'master', 'fe76adc1-5919-4f82-8a15-5824d2f78030', 3, 30, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('35f374e5-5d50-4c43-9ac2-2ae332fe3224', NULL, 'auth-otp-form', 'demo', 'da8d37db-c914-434d-ab35-62074d59dfae', 0, 20, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('363bd91e-b0c7-4b62-9dac-3d2e73f4b4f8', NULL, 'conditional-user-configured', 'master', '4a9769aa-7eb8-473d-980b-5601355b1893', 0, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('3b6e501c-7e49-461c-b4b7-6473e2e91029', NULL, 'auth-otp-form', 'master', '8f4b7bb3-9d23-4c0a-8435-fd0d40193295', 0, 20, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('3f6d0575-7de9-49bf-be07-492fd93c8377', NULL, 'reset-credential-email', 'master', 'bf641639-e075-479d-9a93-505405935d98', 0, 20, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('426caa76-d337-4c26-9614-edb68cabc03d', NULL, 'direct-grant-validate-otp', 'master', '4934d91e-a825-4d33-9ea1-b97786e4ca02', 0, 20, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('42945ff9-d03a-46d1-b7d6-616c280dde96', NULL, NULL, 'demo', 'd8e22d97-5a49-4841-ab5e-58f48b7b3752', 1, 40, b'1', '8ec6aeb1-e906-4608-997f-26f77e8b56fb', NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('4b958e3c-4c0c-4bc4-809b-6ab8e98d915b', NULL, 'client-secret-jwt', 'master', '12b75397-babc-4d8a-9836-c4d9b373b53a', 2, 30, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('4c73f7a8-73d9-4083-9958-cb2bbb57aa9f', NULL, 'conditional-user-configured', 'demo', '8ec6aeb1-e906-4608-997f-26f77e8b56fb', 0, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('55130b8d-b78c-4e34-8ceb-ab3de1eda89e', NULL, 'registration-password-action', 'demo', '221ee0fd-3223-4a75-a438-5b32d67b4c77', 0, 50, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('5816fa4a-b7b3-4ddd-b43c-72fd7d039bb8', NULL, NULL, 'master', 'de8b0690-ef7c-49e2-9b82-4b3049048872', 0, 20, b'1', 'fab46804-f7d7-4dcf-9e16-ceaccb42278f', NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('5c089e59-19d5-47e0-8468-94de8d8a154f', NULL, 'identity-provider-redirector', 'demo', 'ad5470c8-4e0c-4144-98b6-37e8089c6d1a', 2, 25, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('5eed2e9a-2e6f-41d2-853b-ff4504093b64', NULL, NULL, 'master', '26b16f8b-19c0-4a92-9ebc-5dcba21c16e9', 2, 20, b'1', 'de8b0690-ef7c-49e2-9b82-4b3049048872', NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('609560d8-9322-4fcb-b2da-970c56aa9400', NULL, NULL, 'demo', 'fefadc8e-45e8-4fb5-a00e-dfd976f05408', 1, 30, b'1', '88f1e9d4-4a33-48b6-8e6e-3167f3ecd7dd', NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('63ccafdd-542d-4c16-a9b3-0c364a901578', NULL, NULL, 'demo', '6445ae98-a3a3-4d5d-8aa4-8eb4d652c197', 0, 20, b'1', '5eebb853-88b5-462d-be99-dea430b53459', NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('670df691-401f-4acd-b208-2f6e490abfc0', NULL, 'idp-email-verification', 'demo', '5eebb853-88b5-462d-be99-dea430b53459', 2, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('6a32f37c-8b8d-47b6-a535-070657581222', NULL, 'idp-create-user-if-unique', 'master', '26b16f8b-19c0-4a92-9ebc-5dcba21c16e9', 2, 10, b'0', NULL, 'f9f7c86d-ff1d-402b-8840-a3e9bbae8739');
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('7097ec50-05dc-41ec-8aed-58ca81b55883', NULL, 'docker-http-basic-authenticator', 'master', 'ae8f6231-743e-43a7-8c1f-38a73df4e2f0', 0, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('71cd61f9-5614-4ffc-8005-b6b5d16f9cc9', NULL, 'registration-profile-action', 'demo', '221ee0fd-3223-4a75-a438-5b32d67b4c77', 0, 40, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('73d00ce9-d833-480e-9233-2d3ac33d0f7b', NULL, 'docker-http-basic-authenticator', 'demo', '7432f960-4836-4bbe-b4be-01943a80a813', 0, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('783037f4-0809-4b05-8dc5-e165994666eb', NULL, 'basic-auth-otp', 'demo', '115f744c-e050-45ce-b4d3-9feb2e811ef5', 3, 20, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('785e591e-8672-45a5-b7bc-7c1511ca387c', NULL, NULL, 'master', 'fab46804-f7d7-4dcf-9e16-ceaccb42278f', 2, 20, b'1', 'ace5c5dd-66b6-4b57-b218-8a876c8dee75', NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('794b1c42-dd1c-4107-881f-26a70648cc60', NULL, NULL, 'demo', '1443bd81-e268-462b-a73d-fe677929f7fc', 0, 20, b'1', '3c7dc103-0699-4928-8f9f-3c61216f74a5', NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('79b010b6-4ae1-4417-82c4-49a4ac7d9747', NULL, NULL, 'master', '944ed300-f7d5-4170-8e19-6f2d57f2502a', 1, 20, b'1', '4a9769aa-7eb8-473d-980b-5601355b1893', NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('7ae1af94-343e-4495-9cc1-2d7600afa411', NULL, 'idp-review-profile', 'demo', '1443bd81-e268-462b-a73d-fe677929f7fc', 0, 10, b'0', NULL, 'b7b0d1bd-b6f1-4bdd-be55-cde7a1af4fbc');
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('8005d087-b700-4093-8e23-1fb85ae02ef4', NULL, 'auth-cookie', 'demo', 'ad5470c8-4e0c-4144-98b6-37e8089c6d1a', 2, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('80a6dbc3-a004-4ce4-8d84-23406a7036dc', NULL, 'registration-recaptcha-action', 'master', '0de82f64-bd33-4dd6-b846-fe395410252c', 3, 60, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('80b54289-39c2-4981-9714-c29c4fbf632f', NULL, 'auth-username-password-form', 'demo', 'cbc582c8-1594-4065-9a40-a457792c894b', 0, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('83237dad-5b8f-4a6c-9f16-16aa45326e89', NULL, 'idp-username-password-form', 'master', 'ace5c5dd-66b6-4b57-b218-8a876c8dee75', 0, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('8465f196-f608-427e-8394-f420e9c3abf6', NULL, 'reset-credentials-choose-user', 'demo', 'd8e22d97-5a49-4841-ab5e-58f48b7b3752', 0, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('846efb22-d40a-4d79-bbdc-06806e9e489b', NULL, NULL, 'master', 'a9e306cb-8a4e-4db0-8bd5-e72962b1d275', 0, 20, b'1', 'fe76adc1-5919-4f82-8a15-5824d2f78030', NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('84c12aea-abcd-430b-84ce-fed29ea99eeb', NULL, 'registration-password-action', 'master', '0de82f64-bd33-4dd6-b846-fe395410252c', 0, 50, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('84fabaa2-e2d3-43e5-9d22-47a8be34c5d7', NULL, 'no-cookie-redirect', 'master', 'a9e306cb-8a4e-4db0-8bd5-e72962b1d275', 0, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('8af90a8b-cfed-41ce-914b-b2d5af5aad53', NULL, NULL, 'master', 'b5b8c015-7a28-44e9-9f44-ee412fc1ae94', 2, 30, b'1', '944ed300-f7d5-4170-8e19-6f2d57f2502a', NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('8d5932f9-ba7d-4d0d-8b58-45558241365e', NULL, 'idp-confirm-link', 'master', 'de8b0690-ef7c-49e2-9b82-4b3049048872', 0, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('93671802-744d-4f89-8f0a-2f8396fd3da7', NULL, 'registration-user-creation', 'master', '0de82f64-bd33-4dd6-b846-fe395410252c', 0, 20, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('9b63e36b-f66a-42f1-888e-b2747e758e04', NULL, 'idp-review-profile', 'master', '34fbdb0f-40b1-439c-9fbf-20c8a0cbb29d', 0, 10, b'0', NULL, '9549ec67-a72f-4d50-8c3c-f171dc098fcd');
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('9f5faba1-781c-4d3a-b98f-26659aba4aef', NULL, 'registration-user-creation', 'demo', '221ee0fd-3223-4a75-a438-5b32d67b4c77', 0, 20, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('a082e311-1f35-4464-9273-1f1a58e19bd6', NULL, 'conditional-user-configured', 'master', '4934d91e-a825-4d33-9ea1-b97786e4ca02', 0, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('a0b11bed-dbc1-450e-9926-dd0d0e79774a', NULL, 'no-cookie-redirect', 'demo', 'f29fecd0-54d5-4f35-8ad4-b5254c60fab1', 0, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('a2c266ef-9d2c-43f9-89ce-8cead34851c0', NULL, 'conditional-user-configured', 'demo', '3901f454-5095-40ec-81cd-b44eb96aac4c', 0, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('a82e1795-60fc-4358-8762-3708c3463306', NULL, 'auth-spnego', 'master', 'b5b8c015-7a28-44e9-9f44-ee412fc1ae94', 3, 20, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('ab294827-47c7-4018-9570-242a542c1698', NULL, 'idp-email-verification', 'master', 'fab46804-f7d7-4dcf-9e16-ceaccb42278f', 2, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('ab6fc2dc-f633-4f44-87d2-aa3aa22216cb', NULL, NULL, 'master', '4c22b971-6ad7-4660-90de-81e672a27497', 1, 30, b'1', '4934d91e-a825-4d33-9ea1-b97786e4ca02', NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('acb1a7c5-bf59-4ebb-9632-2bbd8c4b2189', NULL, 'identity-provider-redirector', 'master', 'b5b8c015-7a28-44e9-9f44-ee412fc1ae94', 2, 25, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('acdd2036-b5f1-489c-a666-85f848a28335', NULL, 'idp-username-password-form', 'demo', '912bd7e6-2a89-4f49-8d84-8407a1eeeb61', 0, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('ad915822-22fb-4362-b10e-3b87379ce28c', NULL, 'http-basic-authenticator', 'master', '3c67dffe-6163-46fe-aa0e-2ad52e042c9d', 0, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('adee9f6f-caba-4e79-bebb-74e742a74903', NULL, 'registration-profile-action', 'master', '0de82f64-bd33-4dd6-b846-fe395410252c', 0, 40, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('b2e57e66-02c3-42a0-9438-59c829c238a0', NULL, 'client-secret-jwt', 'demo', '45262779-1e0f-4bd2-907a-2c5c6203eb4c', 2, 30, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('b4de3d12-30dc-45e0-a104-68f2a2d0cdfe', NULL, 'direct-grant-validate-password', 'master', '4c22b971-6ad7-4660-90de-81e672a27497', 0, 20, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('bac9ff55-8560-42b6-abb7-cc3087ab82af', NULL, 'reset-credentials-choose-user', 'master', 'bf641639-e075-479d-9a93-505405935d98', 0, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('bc48f3d6-6f23-49c1-a6c4-f1f7057b7dc2', NULL, 'reset-password', 'master', 'bf641639-e075-479d-9a93-505405935d98', 0, 30, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('bc70ddd8-0209-40cc-975e-5df7b26a3570', NULL, 'reset-otp', 'master', '2bbce254-dad3-4482-983c-14a4e46bb26e', 0, 20, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('c4370309-75e3-4278-a2da-e5c68c80fb8d', NULL, 'auth-spnego', 'demo', '115f744c-e050-45ce-b4d3-9feb2e811ef5', 3, 30, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('c4ffe392-cc48-49e4-b5f3-58ec50f3da92', NULL, 'direct-grant-validate-password', 'demo', 'fefadc8e-45e8-4fb5-a00e-dfd976f05408', 0, 20, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('c8641cb2-4a3f-4520-8511-d59ddd2258c7', NULL, NULL, 'demo', '912bd7e6-2a89-4f49-8d84-8407a1eeeb61', 1, 20, b'1', '3901f454-5095-40ec-81cd-b44eb96aac4c', NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('d0c41f47-3f39-4286-a146-5c73a951a12c', NULL, 'client-x509', 'master', '12b75397-babc-4d8a-9836-c4d9b373b53a', 2, 40, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('d0e6b5e9-3ab5-45a4-9ea4-11958242db85', NULL, 'idp-create-user-if-unique', 'demo', '3c7dc103-0699-4928-8f9f-3c61216f74a5', 2, 10, b'0', NULL, 'c18a2454-4a38-41ce-982d-9b561bab46e2');
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('d1193309-5722-4487-9be5-a64e31222629', NULL, NULL, 'master', 'bf641639-e075-479d-9a93-505405935d98', 1, 40, b'1', '2bbce254-dad3-4482-983c-14a4e46bb26e', NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('d7160751-513a-4143-a43c-f5f3799124c6', NULL, 'conditional-user-configured', 'master', '2bbce254-dad3-4482-983c-14a4e46bb26e', 0, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('da3d3cc2-b1b9-4f49-a09f-5e7b476b19b8', NULL, 'direct-grant-validate-username', 'master', '4c22b971-6ad7-4660-90de-81e672a27497', 0, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('dbe63324-ab00-45cd-ac6c-95d2f6c7473d', NULL, 'client-x509', 'demo', '45262779-1e0f-4bd2-907a-2c5c6203eb4c', 2, 40, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('dc9b269e-52ab-4b1c-abeb-5234dcade640', NULL, 'registration-page-form', 'demo', '3844c363-faa4-497e-8b9d-45fbc407e479', 0, 10, b'1', '221ee0fd-3223-4a75-a438-5b32d67b4c77', NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('dee975ed-24ad-4cd5-bfd8-b881db014d32', NULL, 'registration-page-form', 'master', '0aa31c40-d28e-4442-b442-f871fdff5498', 0, 10, b'1', '0de82f64-bd33-4dd6-b846-fe395410252c', NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('e22ad837-7bbe-4bf5-9e3b-55cae85f0db4', NULL, 'reset-otp', 'demo', '8ec6aeb1-e906-4608-997f-26f77e8b56fb', 0, 20, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('eb8b4d6f-f3a7-4525-98ea-8ae1f3f33f74', NULL, 'auth-cookie', 'master', 'b5b8c015-7a28-44e9-9f44-ee412fc1ae94', 2, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('ee0d304c-a700-42cf-88da-a25d868b1211', NULL, 'direct-grant-validate-otp', 'demo', '88f1e9d4-4a33-48b6-8e6e-3167f3ecd7dd', 0, 20, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('ee8df345-3820-41a8-b52c-3c16d1402d65', NULL, 'client-jwt', 'master', '12b75397-babc-4d8a-9836-c4d9b373b53a', 2, 20, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('f0a51f62-f1a1-442d-bfe0-c67484103f11', NULL, 'conditional-user-configured', 'master', '8f4b7bb3-9d23-4c0a-8435-fd0d40193295', 0, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('f32fd5ad-c295-4c62-961c-1b431715035f', NULL, 'basic-auth', 'demo', '115f744c-e050-45ce-b4d3-9feb2e811ef5', 0, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('f3db032b-b4d0-49ef-8f4c-b184286d4d13', NULL, 'basic-auth', 'master', 'fe76adc1-5919-4f82-8a15-5824d2f78030', 0, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('f6ecc97c-920c-41bb-91ca-3c6cabf4e0d3', NULL, 'auth-otp-form', 'demo', '3901f454-5095-40ec-81cd-b44eb96aac4c', 0, 20, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('fa6e45fd-e7a5-466e-94fe-47eee2e14ca9', NULL, 'idp-confirm-link', 'demo', '6445ae98-a3a3-4d5d-8aa4-8eb4d652c197', 0, 10, b'0', NULL, NULL);
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('fd93111c-15ba-40cc-9f01-7a83ba940bf6', NULL, NULL, 'demo', '3c7dc103-0699-4928-8f9f-3c61216f74a5', 2, 20, b'1', '6445ae98-a3a3-4d5d-8aa4-8eb4d652c197', NULL);

-- ----------------------------
-- Table structure for AUTHENTICATION_FLOW
-- ----------------------------
DROP TABLE IF EXISTS `AUTHENTICATION_FLOW`;
CREATE TABLE `AUTHENTICATION_FLOW`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `ALIAS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `PROVIDER_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL DEFAULT 'basic-flow',
  `TOP_LEVEL` bit(1) NOT NULL DEFAULT b'0',
  `BUILT_IN` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `IDX_AUTH_FLOW_REALM`(`REALM_ID`) USING BTREE,
  CONSTRAINT `FK_AUTH_FLOW_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of AUTHENTICATION_FLOW
-- ----------------------------
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('0aa31c40-d28e-4442-b442-f871fdff5498', 'registration', 'registration flow', 'master', 'basic-flow', b'1', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('0de82f64-bd33-4dd6-b846-fe395410252c', 'registration form', 'registration form', 'master', 'form-flow', b'0', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('115f744c-e050-45ce-b4d3-9feb2e811ef5', 'Authentication Options', 'Authentication options.', 'demo', 'basic-flow', b'0', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('12b75397-babc-4d8a-9836-c4d9b373b53a', 'clients', 'Base authentication for clients', 'master', 'client-flow', b'1', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('1443bd81-e268-462b-a73d-fe677929f7fc', 'first broker login', 'Actions taken after first broker login with identity provider account, which is not yet linked to any Keycloak account', 'demo', 'basic-flow', b'1', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('221ee0fd-3223-4a75-a438-5b32d67b4c77', 'registration form', 'registration form', 'demo', 'form-flow', b'0', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('26b16f8b-19c0-4a92-9ebc-5dcba21c16e9', 'User creation or linking', 'Flow for the existing/non-existing user alternatives', 'master', 'basic-flow', b'0', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('2bbce254-dad3-4482-983c-14a4e46bb26e', 'Reset - Conditional OTP', 'Flow to determine if the OTP should be reset or not. Set to REQUIRED to force.', 'master', 'basic-flow', b'0', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('34fbdb0f-40b1-439c-9fbf-20c8a0cbb29d', 'first broker login', 'Actions taken after first broker login with identity provider account, which is not yet linked to any Keycloak account', 'master', 'basic-flow', b'1', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('3844c363-faa4-497e-8b9d-45fbc407e479', 'registration', 'registration flow', 'demo', 'basic-flow', b'1', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('3901f454-5095-40ec-81cd-b44eb96aac4c', 'First broker login - Conditional OTP', 'Flow to determine if the OTP is required for the authentication', 'demo', 'basic-flow', b'0', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('3c67dffe-6163-46fe-aa0e-2ad52e042c9d', 'saml ecp', 'SAML ECP Profile Authentication Flow', 'master', 'basic-flow', b'1', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('3c7dc103-0699-4928-8f9f-3c61216f74a5', 'User creation or linking', 'Flow for the existing/non-existing user alternatives', 'demo', 'basic-flow', b'0', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('45262779-1e0f-4bd2-907a-2c5c6203eb4c', 'clients', 'Base authentication for clients', 'demo', 'client-flow', b'1', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('4934d91e-a825-4d33-9ea1-b97786e4ca02', 'Direct Grant - Conditional OTP', 'Flow to determine if the OTP is required for the authentication', 'master', 'basic-flow', b'0', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('4a9769aa-7eb8-473d-980b-5601355b1893', 'Browser - Conditional OTP', 'Flow to determine if the OTP is required for the authentication', 'master', 'basic-flow', b'0', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('4c22b971-6ad7-4660-90de-81e672a27497', 'direct grant', 'OpenID Connect Resource Owner Grant', 'master', 'basic-flow', b'1', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('5eebb853-88b5-462d-be99-dea430b53459', 'Account verification options', 'Method with which to verity the existing account', 'demo', 'basic-flow', b'0', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('6445ae98-a3a3-4d5d-8aa4-8eb4d652c197', 'Handle Existing Account', 'Handle what to do if there is existing account with same email/username like authenticated identity provider', 'demo', 'basic-flow', b'0', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('7432f960-4836-4bbe-b4be-01943a80a813', 'docker auth', 'Used by Docker clients to authenticate against the IDP', 'demo', 'basic-flow', b'1', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('88f1e9d4-4a33-48b6-8e6e-3167f3ecd7dd', 'Direct Grant - Conditional OTP', 'Flow to determine if the OTP is required for the authentication', 'demo', 'basic-flow', b'0', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('8ec6aeb1-e906-4608-997f-26f77e8b56fb', 'Reset - Conditional OTP', 'Flow to determine if the OTP should be reset or not. Set to REQUIRED to force.', 'demo', 'basic-flow', b'0', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('8f4b7bb3-9d23-4c0a-8435-fd0d40193295', 'First broker login - Conditional OTP', 'Flow to determine if the OTP is required for the authentication', 'master', 'basic-flow', b'0', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('912bd7e6-2a89-4f49-8d84-8407a1eeeb61', 'Verify Existing Account by Re-authentication', 'Reauthentication of existing account', 'demo', 'basic-flow', b'0', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('944ed300-f7d5-4170-8e19-6f2d57f2502a', 'forms', 'Username, password, otp and other auth forms.', 'master', 'basic-flow', b'0', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('a9e306cb-8a4e-4db0-8bd5-e72962b1d275', 'http challenge', 'An authentication flow based on challenge-response HTTP Authentication Schemes', 'master', 'basic-flow', b'1', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('ac5667b0-1adf-47ac-a57e-e10a8a74c63f', 'saml ecp', 'SAML ECP Profile Authentication Flow', 'demo', 'basic-flow', b'1', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('ace5c5dd-66b6-4b57-b218-8a876c8dee75', 'Verify Existing Account by Re-authentication', 'Reauthentication of existing account', 'master', 'basic-flow', b'0', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('ad5470c8-4e0c-4144-98b6-37e8089c6d1a', 'browser', 'browser based authentication', 'demo', 'basic-flow', b'1', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('ae8f6231-743e-43a7-8c1f-38a73df4e2f0', 'docker auth', 'Used by Docker clients to authenticate against the IDP', 'master', 'basic-flow', b'1', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('b5b8c015-7a28-44e9-9f44-ee412fc1ae94', 'browser', 'browser based authentication', 'master', 'basic-flow', b'1', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('bf641639-e075-479d-9a93-505405935d98', 'reset credentials', 'Reset credentials for a user if they forgot their password or something', 'master', 'basic-flow', b'1', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('cbc582c8-1594-4065-9a40-a457792c894b', 'forms', 'Username, password, otp and other auth forms.', 'demo', 'basic-flow', b'0', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('d8e22d97-5a49-4841-ab5e-58f48b7b3752', 'reset credentials', 'Reset credentials for a user if they forgot their password or something', 'demo', 'basic-flow', b'1', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('da8d37db-c914-434d-ab35-62074d59dfae', 'Browser - Conditional OTP', 'Flow to determine if the OTP is required for the authentication', 'demo', 'basic-flow', b'0', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('de8b0690-ef7c-49e2-9b82-4b3049048872', 'Handle Existing Account', 'Handle what to do if there is existing account with same email/username like authenticated identity provider', 'master', 'basic-flow', b'0', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('f29fecd0-54d5-4f35-8ad4-b5254c60fab1', 'http challenge', 'An authentication flow based on challenge-response HTTP Authentication Schemes', 'demo', 'basic-flow', b'1', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('fab46804-f7d7-4dcf-9e16-ceaccb42278f', 'Account verification options', 'Method with which to verity the existing account', 'master', 'basic-flow', b'0', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('fe76adc1-5919-4f82-8a15-5824d2f78030', 'Authentication Options', 'Authentication options.', 'master', 'basic-flow', b'0', b'1');
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('fefadc8e-45e8-4fb5-a00e-dfd976f05408', 'direct grant', 'OpenID Connect Resource Owner Grant', 'demo', 'basic-flow', b'1', b'1');

-- ----------------------------
-- Table structure for AUTHENTICATOR_CONFIG
-- ----------------------------
DROP TABLE IF EXISTS `AUTHENTICATOR_CONFIG`;
CREATE TABLE `AUTHENTICATOR_CONFIG`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `ALIAS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `IDX_AUTH_CONFIG_REALM`(`REALM_ID`) USING BTREE,
  CONSTRAINT `FK_AUTH_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of AUTHENTICATOR_CONFIG
-- ----------------------------
INSERT INTO `AUTHENTICATOR_CONFIG` VALUES ('9549ec67-a72f-4d50-8c3c-f171dc098fcd', 'review profile config', 'master');
INSERT INTO `AUTHENTICATOR_CONFIG` VALUES ('b7b0d1bd-b6f1-4bdd-be55-cde7a1af4fbc', 'review profile config', 'demo');
INSERT INTO `AUTHENTICATOR_CONFIG` VALUES ('c18a2454-4a38-41ce-982d-9b561bab46e2', 'create unique user config', 'demo');
INSERT INTO `AUTHENTICATOR_CONFIG` VALUES ('f9f7c86d-ff1d-402b-8840-a3e9bbae8739', 'create unique user config', 'master');

-- ----------------------------
-- Table structure for AUTHENTICATOR_CONFIG_ENTRY
-- ----------------------------
DROP TABLE IF EXISTS `AUTHENTICATOR_CONFIG_ENTRY`;
CREATE TABLE `AUTHENTICATOR_CONFIG_ENTRY`  (
  `AUTHENTICATOR_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VALUE` longtext CHARACTER SET latin1 COLLATE latin1_swedish_ci,
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`AUTHENTICATOR_ID`, `NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of AUTHENTICATOR_CONFIG_ENTRY
-- ----------------------------
INSERT INTO `AUTHENTICATOR_CONFIG_ENTRY` VALUES ('9549ec67-a72f-4d50-8c3c-f171dc098fcd', 'missing', 'update.profile.on.first.login');
INSERT INTO `AUTHENTICATOR_CONFIG_ENTRY` VALUES ('b7b0d1bd-b6f1-4bdd-be55-cde7a1af4fbc', 'missing', 'update.profile.on.first.login');
INSERT INTO `AUTHENTICATOR_CONFIG_ENTRY` VALUES ('c18a2454-4a38-41ce-982d-9b561bab46e2', 'false', 'require.password.update.after.registration');
INSERT INTO `AUTHENTICATOR_CONFIG_ENTRY` VALUES ('f9f7c86d-ff1d-402b-8840-a3e9bbae8739', 'false', 'require.password.update.after.registration');

-- ----------------------------
-- Table structure for BROKER_LINK
-- ----------------------------
DROP TABLE IF EXISTS `BROKER_LINK`;
CREATE TABLE `BROKER_LINK`  (
  `IDENTITY_PROVIDER` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `STORAGE_PROVIDER_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `BROKER_USER_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `BROKER_USERNAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `TOKEN` text CHARACTER SET latin1 COLLATE latin1_swedish_ci,
  `USER_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`IDENTITY_PROVIDER`, `USER_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for CLIENT
-- ----------------------------
DROP TABLE IF EXISTS `CLIENT`;
CREATE TABLE `CLIENT`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `FULL_SCOPE_ALLOWED` bit(1) NOT NULL DEFAULT b'0',
  `CLIENT_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `NOT_BEFORE` int(11) DEFAULT NULL,
  `PUBLIC_CLIENT` bit(1) NOT NULL DEFAULT b'0',
  `SECRET` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `BASE_URL` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `BEARER_ONLY` bit(1) NOT NULL DEFAULT b'0',
  `MANAGEMENT_URL` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `SURROGATE_AUTH_REQUIRED` bit(1) NOT NULL DEFAULT b'0',
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `PROTOCOL` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `NODE_REREG_TIMEOUT` int(11) DEFAULT 0,
  `FRONTCHANNEL_LOGOUT` bit(1) NOT NULL DEFAULT b'0',
  `CONSENT_REQUIRED` bit(1) NOT NULL DEFAULT b'0',
  `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `SERVICE_ACCOUNTS_ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `CLIENT_AUTHENTICATOR_TYPE` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `ROOT_URL` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `REGISTRATION_TOKEN` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `STANDARD_FLOW_ENABLED` bit(1) NOT NULL DEFAULT b'1',
  `IMPLICIT_FLOW_ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `DIRECT_ACCESS_GRANTS_ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `ALWAYS_DISPLAY_IN_CONSOLE` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `UK_B71CJLBENV945RB6GCON438AT`(`REALM_ID`, `CLIENT_ID`) USING BTREE,
  INDEX `IDX_CLIENT_ID`(`CLIENT_ID`) USING BTREE,
  CONSTRAINT `FK_P56CTINXXB9GSK57FO49F9TAC` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of CLIENT
-- ----------------------------
INSERT INTO `CLIENT` VALUES ('1ea2d246-2e72-440b-8236-0d9284d8a739', b'1', b'0', 'broker', 0, b'0', '9b48b879-79b3-4ae5-b9e8-8afab3f4ea68', NULL, b'0', NULL, b'0', 'demo', 'openid-connect', 0, b'0', b'0', '${client_broker}', b'0', 'client-secret', NULL, NULL, NULL, b'1', b'0', b'0', b'0');
INSERT INTO `CLIENT` VALUES ('23979e0b-c766-42bc-aaa3-f320a171837a', b'1', b'0', 'account-console', 0, b'1', 'ad06d808-2c4f-452e-850c-1e1e079d5e10', '/realms/master/account/', b'0', NULL, b'0', 'master', 'openid-connect', 0, b'0', b'0', '${client_account-console}', b'0', 'client-secret', '${authBaseUrl}', NULL, NULL, b'1', b'0', b'0', b'0');
INSERT INTO `CLIENT` VALUES ('605a5c15-5cfb-4453-b771-313bb7239afd', b'1', b'0', 'security-admin-console', 0, b'1', '0acc9ef6-7b79-4dee-a4aa-4eadeacb4776', '/admin/master/console/', b'0', NULL, b'0', 'master', 'openid-connect', 0, b'0', b'0', '${client_security-admin-console}', b'0', 'client-secret', '${authAdminUrl}', NULL, NULL, b'1', b'0', b'0', b'0');
INSERT INTO `CLIENT` VALUES ('7515bb98-a373-4166-b03c-22aa4f14cea4', b'1', b'1', 'demo-realm', 0, b'0', '52dfdcdf-43d6-47be-abe9-fb0036b1f366', NULL, b'1', NULL, b'0', 'master', NULL, 0, b'0', b'0', 'demo Realm', b'0', 'client-secret', NULL, NULL, NULL, b'1', b'0', b'0', b'0');
INSERT INTO `CLIENT` VALUES ('7b9dd7b9-71ad-470d-b499-3f9d598eaec0', b'1', b'0', 'admin-cli', 0, b'1', '83d953e2-6926-455a-9129-7cb293669cee', NULL, b'0', NULL, b'0', 'demo', 'openid-connect', 0, b'0', b'0', '${client_admin-cli}', b'0', 'client-secret', NULL, NULL, NULL, b'0', b'0', b'1', b'0');
INSERT INTO `CLIENT` VALUES ('7bdba359-bb16-4b34-8b43-c62aae593ea6', b'1', b'0', 'admin-cli', 0, b'1', 'aaa01796-d58e-4a5e-b6ef-3ffea1a13d5d', NULL, b'0', NULL, b'0', 'master', 'openid-connect', 0, b'0', b'0', '${client_admin-cli}', b'0', 'client-secret', NULL, NULL, NULL, b'0', b'0', b'1', b'0');
INSERT INTO `CLIENT` VALUES ('80e2ea2a-23ba-4fed-af92-cabc4d2ea4ed', b'1', b'0', 'account-console', 0, b'1', '30de2f59-de72-401d-9167-e6a10d453e2b', '/realms/demo/account/', b'0', NULL, b'0', 'demo', 'openid-connect', 0, b'0', b'0', '${client_account-console}', b'0', 'client-secret', '${authBaseUrl}', NULL, NULL, b'1', b'0', b'0', b'0');
INSERT INTO `CLIENT` VALUES ('8cedc74f-6b51-42a1-ba12-e2b80c11d39a', b'1', b'1', 'democlient', 0, b'0', '58e23419-c53f-49bc-bad6-b696756bba26', NULL, b'0', NULL, b'0', 'demo', 'openid-connect', -1, b'0', b'0', NULL, b'1', 'client-secret', NULL, NULL, NULL, b'1', b'0', b'1', b'0');
INSERT INTO `CLIENT` VALUES ('8f04cb27-025c-495c-ab99-916ad43d5957', b'1', b'0', 'broker', 0, b'0', 'e168d605-b049-4ea9-9ba3-746f6bc3f13b', NULL, b'0', NULL, b'0', 'master', 'openid-connect', 0, b'0', b'0', '${client_broker}', b'0', 'client-secret', NULL, NULL, NULL, b'1', b'0', b'0', b'0');
INSERT INTO `CLIENT` VALUES ('9f327fb4-6a74-4b8f-8a22-03c07dcc6475', b'1', b'0', 'account', 0, b'0', 'bebea4a3-ac4c-43ec-b467-6ce0ca630ac6', '/realms/master/account/', b'0', NULL, b'0', 'master', 'openid-connect', 0, b'0', b'0', '${client_account}', b'0', 'client-secret', '${authBaseUrl}', NULL, NULL, b'1', b'0', b'0', b'0');
INSERT INTO `CLIENT` VALUES ('aa33da79-41ff-42be-9518-96b12bcd8055', b'1', b'0', 'account', 0, b'0', 'b797d261-5dbe-4078-9d3d-64e8d8882350', '/realms/demo/account/', b'0', NULL, b'0', 'demo', 'openid-connect', 0, b'0', b'0', '${client_account}', b'0', 'client-secret', '${authBaseUrl}', NULL, NULL, b'1', b'0', b'0', b'0');
INSERT INTO `CLIENT` VALUES ('b6c158d8-d49c-4a26-aa0a-c4ead414c72f', b'1', b'0', 'realm-management', 0, b'0', '3923f83c-4967-431e-8ade-b9dbe1115d32', NULL, b'1', NULL, b'0', 'demo', 'openid-connect', 0, b'0', b'0', '${client_realm-management}', b'0', 'client-secret', NULL, NULL, NULL, b'1', b'0', b'0', b'0');
INSERT INTO `CLIENT` VALUES ('b8e29510-4692-4ac8-8aba-fc6882c30fc7', b'1', b'1', 'master-realm', 0, b'0', 'a165b115-ca72-4628-b624-d408d8d4c008', NULL, b'1', NULL, b'0', 'master', NULL, 0, b'0', b'0', 'master Realm', b'0', 'client-secret', NULL, NULL, NULL, b'1', b'0', b'0', b'0');
INSERT INTO `CLIENT` VALUES ('f2fba72e-4130-482a-9ac3-50674c17fe38', b'1', b'0', 'security-admin-console', 0, b'1', '9de2d4b1-b25b-4a88-97f4-dd8e6c22a2db', '/admin/demo/console/', b'0', NULL, b'0', 'demo', 'openid-connect', 0, b'0', b'0', '${client_security-admin-console}', b'0', 'client-secret', '${authAdminUrl}', NULL, NULL, b'1', b'0', b'0', b'0');

-- ----------------------------
-- Table structure for CLIENT_ATTRIBUTES
-- ----------------------------
DROP TABLE IF EXISTS `CLIENT_ATTRIBUTES`;
CREATE TABLE `CLIENT_ATTRIBUTES`  (
  `CLIENT_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VALUE` varchar(4000) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`CLIENT_ID`, `NAME`) USING BTREE,
  CONSTRAINT `FK3C47C64BEACCA966` FOREIGN KEY (`CLIENT_ID`) REFERENCES `CLIENT` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of CLIENT_ATTRIBUTES
-- ----------------------------
INSERT INTO `CLIENT_ATTRIBUTES` VALUES ('23979e0b-c766-42bc-aaa3-f320a171837a', 'S256', 'pkce.code.challenge.method');
INSERT INTO `CLIENT_ATTRIBUTES` VALUES ('605a5c15-5cfb-4453-b771-313bb7239afd', 'S256', 'pkce.code.challenge.method');
INSERT INTO `CLIENT_ATTRIBUTES` VALUES ('80e2ea2a-23ba-4fed-af92-cabc4d2ea4ed', 'S256', 'pkce.code.challenge.method');
INSERT INTO `CLIENT_ATTRIBUTES` VALUES ('8cedc74f-6b51-42a1-ba12-e2b80c11d39a', '3600', 'access.token.lifespan');
INSERT INTO `CLIENT_ATTRIBUTES` VALUES ('8cedc74f-6b51-42a1-ba12-e2b80c11d39a', 'false', 'display.on.consent.screen');
INSERT INTO `CLIENT_ATTRIBUTES` VALUES ('8cedc74f-6b51-42a1-ba12-e2b80c11d39a', 'false', 'exclude.session.state.from.auth.response');
INSERT INTO `CLIENT_ATTRIBUTES` VALUES ('8cedc74f-6b51-42a1-ba12-e2b80c11d39a', 'false', 'saml.assertion.signature');
INSERT INTO `CLIENT_ATTRIBUTES` VALUES ('8cedc74f-6b51-42a1-ba12-e2b80c11d39a', 'false', 'saml.authnstatement');
INSERT INTO `CLIENT_ATTRIBUTES` VALUES ('8cedc74f-6b51-42a1-ba12-e2b80c11d39a', 'false', 'saml.client.signature');
INSERT INTO `CLIENT_ATTRIBUTES` VALUES ('8cedc74f-6b51-42a1-ba12-e2b80c11d39a', 'false', 'saml.encrypt');
INSERT INTO `CLIENT_ATTRIBUTES` VALUES ('8cedc74f-6b51-42a1-ba12-e2b80c11d39a', 'false', 'saml.force.post.binding');
INSERT INTO `CLIENT_ATTRIBUTES` VALUES ('8cedc74f-6b51-42a1-ba12-e2b80c11d39a', 'false', 'saml.multivalued.roles');
INSERT INTO `CLIENT_ATTRIBUTES` VALUES ('8cedc74f-6b51-42a1-ba12-e2b80c11d39a', 'false', 'saml.onetimeuse.condition');
INSERT INTO `CLIENT_ATTRIBUTES` VALUES ('8cedc74f-6b51-42a1-ba12-e2b80c11d39a', 'false', 'saml.server.signature');
INSERT INTO `CLIENT_ATTRIBUTES` VALUES ('8cedc74f-6b51-42a1-ba12-e2b80c11d39a', 'false', 'saml.server.signature.keyinfo.ext');
INSERT INTO `CLIENT_ATTRIBUTES` VALUES ('8cedc74f-6b51-42a1-ba12-e2b80c11d39a', 'false', 'saml_force_name_id_format');
INSERT INTO `CLIENT_ATTRIBUTES` VALUES ('8cedc74f-6b51-42a1-ba12-e2b80c11d39a', 'false', 'tls.client.certificate.bound.access.tokens');
INSERT INTO `CLIENT_ATTRIBUTES` VALUES ('f2fba72e-4130-482a-9ac3-50674c17fe38', 'S256', 'pkce.code.challenge.method');

-- ----------------------------
-- Table structure for CLIENT_AUTH_FLOW_BINDINGS
-- ----------------------------
DROP TABLE IF EXISTS `CLIENT_AUTH_FLOW_BINDINGS`;
CREATE TABLE `CLIENT_AUTH_FLOW_BINDINGS`  (
  `CLIENT_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `FLOW_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `BINDING_NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`CLIENT_ID`, `BINDING_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for CLIENT_DEFAULT_ROLES
-- ----------------------------
DROP TABLE IF EXISTS `CLIENT_DEFAULT_ROLES`;
CREATE TABLE `CLIENT_DEFAULT_ROLES`  (
  `CLIENT_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `ROLE_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`CLIENT_ID`, `ROLE_ID`) USING BTREE,
  UNIQUE INDEX `UK_8AELWNIBJI49AVXSRTUF6XJOW`(`ROLE_ID`) USING BTREE,
  INDEX `IDX_CLIENT_DEF_ROLES_CLIENT`(`CLIENT_ID`) USING BTREE,
  CONSTRAINT `FK_8AELWNIBJI49AVXSRTUF6XJOW` FOREIGN KEY (`ROLE_ID`) REFERENCES `KEYCLOAK_ROLE` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_NUILTS7KLWQW2H8M2B5JOYTKY` FOREIGN KEY (`CLIENT_ID`) REFERENCES `CLIENT` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of CLIENT_DEFAULT_ROLES
-- ----------------------------
INSERT INTO `CLIENT_DEFAULT_ROLES` VALUES ('9f327fb4-6a74-4b8f-8a22-03c07dcc6475', '09b53e53-331f-4fbd-8c4d-87c6c62da264');
INSERT INTO `CLIENT_DEFAULT_ROLES` VALUES ('9f327fb4-6a74-4b8f-8a22-03c07dcc6475', '2ba43097-0c34-4cfa-a7ea-f291fb5fd016');
INSERT INTO `CLIENT_DEFAULT_ROLES` VALUES ('aa33da79-41ff-42be-9518-96b12bcd8055', '63c27012-2e75-4b40-a03b-1538f37bca36');
INSERT INTO `CLIENT_DEFAULT_ROLES` VALUES ('aa33da79-41ff-42be-9518-96b12bcd8055', '900d9963-6141-47fb-9cf0-fb02d628a7ca');

-- ----------------------------
-- Table structure for CLIENT_INITIAL_ACCESS
-- ----------------------------
DROP TABLE IF EXISTS `CLIENT_INITIAL_ACCESS`;
CREATE TABLE `CLIENT_INITIAL_ACCESS`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `TIMESTAMP` int(11) DEFAULT NULL,
  `EXPIRATION` int(11) DEFAULT NULL,
  `COUNT` int(11) DEFAULT NULL,
  `REMAINING_COUNT` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `IDX_CLIENT_INIT_ACC_REALM`(`REALM_ID`) USING BTREE,
  CONSTRAINT `FK_CLIENT_INIT_ACC_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for CLIENT_NODE_REGISTRATIONS
-- ----------------------------
DROP TABLE IF EXISTS `CLIENT_NODE_REGISTRATIONS`;
CREATE TABLE `CLIENT_NODE_REGISTRATIONS`  (
  `CLIENT_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VALUE` int(11) DEFAULT NULL,
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`CLIENT_ID`, `NAME`) USING BTREE,
  CONSTRAINT `FK4129723BA992F594` FOREIGN KEY (`CLIENT_ID`) REFERENCES `CLIENT` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for CLIENT_SCOPE
-- ----------------------------
DROP TABLE IF EXISTS `CLIENT_SCOPE`;
CREATE TABLE `CLIENT_SCOPE`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `PROTOCOL` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `UK_CLI_SCOPE`(`REALM_ID`, `NAME`) USING BTREE,
  INDEX `IDX_REALM_CLSCOPE`(`REALM_ID`) USING BTREE,
  CONSTRAINT `FK_REALM_CLI_SCOPE` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of CLIENT_SCOPE
-- ----------------------------
INSERT INTO `CLIENT_SCOPE` VALUES ('0e9473b7-de09-484d-82e0-d88097d14b5d', 'email', 'demo', 'OpenID Connect built-in scope: email', 'openid-connect');
INSERT INTO `CLIENT_SCOPE` VALUES ('21c0b943-990d-4c75-95db-e033ba10989d', 'read', 'demo', '测试使用的scope,直接赋在字段上，配置jackson完成授权', 'openid-connect');
INSERT INTO `CLIENT_SCOPE` VALUES ('459e53e1-17bb-4dc9-bc04-9eea36319a60', 'email', 'master', 'OpenID Connect built-in scope: email', 'openid-connect');
INSERT INTO `CLIENT_SCOPE` VALUES ('4c2b4fcb-5f1e-4c78-a341-fd27dfab38e0', 'roles', 'demo', 'OpenID Connect scope for add user roles to the access token', 'openid-connect');
INSERT INTO `CLIENT_SCOPE` VALUES ('60240200-c0bd-4105-9f83-6dfb5d862eea', 'role_list', 'demo', 'SAML role list', 'saml');
INSERT INTO `CLIENT_SCOPE` VALUES ('63a96a62-31d2-4089-92a2-97e9f43c281f', 'profile', 'master', 'OpenID Connect built-in scope: profile', 'openid-connect');
INSERT INTO `CLIENT_SCOPE` VALUES ('7e69eefb-5f29-45cb-b249-3285b708f4e9', 'phone', 'demo', 'OpenID Connect built-in scope: phone', 'openid-connect');
INSERT INTO `CLIENT_SCOPE` VALUES ('81734f9f-7835-4af1-87f7-4625aa7ce31f', 'address', 'master', 'OpenID Connect built-in scope: address', 'openid-connect');
INSERT INTO `CLIENT_SCOPE` VALUES ('85983e59-945c-4f81-b2b3-b2f91ce56db9', 'role_list', 'master', 'SAML role list', 'saml');
INSERT INTO `CLIENT_SCOPE` VALUES ('91791326-87e9-4bfd-a5b3-b6752ef1bfc6', 'phone', 'master', 'OpenID Connect built-in scope: phone', 'openid-connect');
INSERT INTO `CLIENT_SCOPE` VALUES ('948be43b-bd24-4840-ac17-8b0477b17a77', 'web-origins', 'master', 'OpenID Connect scope for add allowed web origins to the access token', 'openid-connect');
INSERT INTO `CLIENT_SCOPE` VALUES ('9d697e2e-581d-4e01-92b5-09baef8c98f4', 'microprofile-jwt', 'master', 'Microprofile - JWT built-in scope', 'openid-connect');
INSERT INTO `CLIENT_SCOPE` VALUES ('a78bcbbd-ddfb-4c36-a43d-0b026769e79b', 'offline_access', 'demo', 'OpenID Connect built-in scope: offline_access', 'openid-connect');
INSERT INTO `CLIENT_SCOPE` VALUES ('b092e195-d133-4604-af92-27c80735ad3a', 'microprofile-jwt', 'demo', 'Microprofile - JWT built-in scope', 'openid-connect');
INSERT INTO `CLIENT_SCOPE` VALUES ('b4d036d9-3470-4960-a3b0-5f6365b99c8c', 'roles', 'master', 'OpenID Connect scope for add user roles to the access token', 'openid-connect');
INSERT INTO `CLIENT_SCOPE` VALUES ('ef20b41d-2b0a-4ae4-986f-84c02d5c0832', 'address', 'demo', 'OpenID Connect built-in scope: address', 'openid-connect');
INSERT INTO `CLIENT_SCOPE` VALUES ('f59745e4-deef-4a93-82d1-eccdd7b4eff1', 'profile', 'demo', 'OpenID Connect built-in scope: profile', 'openid-connect');
INSERT INTO `CLIENT_SCOPE` VALUES ('f82637b5-3526-4226-a37f-682ca3153857', 'web-origins', 'demo', 'OpenID Connect scope for add allowed web origins to the access token', 'openid-connect');
INSERT INTO `CLIENT_SCOPE` VALUES ('f8f75121-3841-4a0c-8428-107b6aac8cdd', 'offline_access', 'master', 'OpenID Connect built-in scope: offline_access', 'openid-connect');

-- ----------------------------
-- Table structure for CLIENT_SCOPE_ATTRIBUTES
-- ----------------------------
DROP TABLE IF EXISTS `CLIENT_SCOPE_ATTRIBUTES`;
CREATE TABLE `CLIENT_SCOPE_ATTRIBUTES`  (
  `SCOPE_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VALUE` varchar(2048) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`SCOPE_ID`, `NAME`) USING BTREE,
  INDEX `IDX_CLSCOPE_ATTRS`(`SCOPE_ID`) USING BTREE,
  CONSTRAINT `FK_CL_SCOPE_ATTR_SCOPE` FOREIGN KEY (`SCOPE_ID`) REFERENCES `CLIENT_SCOPE` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of CLIENT_SCOPE_ATTRIBUTES
-- ----------------------------
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('0e9473b7-de09-484d-82e0-d88097d14b5d', '${emailScopeConsentText}', 'consent.screen.text');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('0e9473b7-de09-484d-82e0-d88097d14b5d', 'true', 'display.on.consent.screen');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('0e9473b7-de09-484d-82e0-d88097d14b5d', 'true', 'include.in.token.scope');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('21c0b943-990d-4c75-95db-e033ba10989d', 'true', 'display.on.consent.screen');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('21c0b943-990d-4c75-95db-e033ba10989d', 'true', 'include.in.token.scope');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('459e53e1-17bb-4dc9-bc04-9eea36319a60', '${emailScopeConsentText}', 'consent.screen.text');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('459e53e1-17bb-4dc9-bc04-9eea36319a60', 'true', 'display.on.consent.screen');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('459e53e1-17bb-4dc9-bc04-9eea36319a60', 'true', 'include.in.token.scope');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('4c2b4fcb-5f1e-4c78-a341-fd27dfab38e0', '${rolesScopeConsentText}', 'consent.screen.text');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('4c2b4fcb-5f1e-4c78-a341-fd27dfab38e0', 'true', 'display.on.consent.screen');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('4c2b4fcb-5f1e-4c78-a341-fd27dfab38e0', 'false', 'include.in.token.scope');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('60240200-c0bd-4105-9f83-6dfb5d862eea', '${samlRoleListScopeConsentText}', 'consent.screen.text');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('60240200-c0bd-4105-9f83-6dfb5d862eea', 'true', 'display.on.consent.screen');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('63a96a62-31d2-4089-92a2-97e9f43c281f', '${profileScopeConsentText}', 'consent.screen.text');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('63a96a62-31d2-4089-92a2-97e9f43c281f', 'true', 'display.on.consent.screen');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('63a96a62-31d2-4089-92a2-97e9f43c281f', 'true', 'include.in.token.scope');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('7e69eefb-5f29-45cb-b249-3285b708f4e9', '${phoneScopeConsentText}', 'consent.screen.text');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('7e69eefb-5f29-45cb-b249-3285b708f4e9', 'true', 'display.on.consent.screen');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('7e69eefb-5f29-45cb-b249-3285b708f4e9', 'true', 'include.in.token.scope');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('81734f9f-7835-4af1-87f7-4625aa7ce31f', '${addressScopeConsentText}', 'consent.screen.text');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('81734f9f-7835-4af1-87f7-4625aa7ce31f', 'true', 'display.on.consent.screen');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('81734f9f-7835-4af1-87f7-4625aa7ce31f', 'true', 'include.in.token.scope');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('85983e59-945c-4f81-b2b3-b2f91ce56db9', '${samlRoleListScopeConsentText}', 'consent.screen.text');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('85983e59-945c-4f81-b2b3-b2f91ce56db9', 'true', 'display.on.consent.screen');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('91791326-87e9-4bfd-a5b3-b6752ef1bfc6', '${phoneScopeConsentText}', 'consent.screen.text');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('91791326-87e9-4bfd-a5b3-b6752ef1bfc6', 'true', 'display.on.consent.screen');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('91791326-87e9-4bfd-a5b3-b6752ef1bfc6', 'true', 'include.in.token.scope');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('948be43b-bd24-4840-ac17-8b0477b17a77', '', 'consent.screen.text');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('948be43b-bd24-4840-ac17-8b0477b17a77', 'false', 'display.on.consent.screen');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('948be43b-bd24-4840-ac17-8b0477b17a77', 'false', 'include.in.token.scope');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('9d697e2e-581d-4e01-92b5-09baef8c98f4', 'false', 'display.on.consent.screen');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('9d697e2e-581d-4e01-92b5-09baef8c98f4', 'true', 'include.in.token.scope');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('a78bcbbd-ddfb-4c36-a43d-0b026769e79b', '${offlineAccessScopeConsentText}', 'consent.screen.text');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('a78bcbbd-ddfb-4c36-a43d-0b026769e79b', 'true', 'display.on.consent.screen');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('b092e195-d133-4604-af92-27c80735ad3a', 'false', 'display.on.consent.screen');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('b092e195-d133-4604-af92-27c80735ad3a', 'true', 'include.in.token.scope');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('b4d036d9-3470-4960-a3b0-5f6365b99c8c', '${rolesScopeConsentText}', 'consent.screen.text');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('b4d036d9-3470-4960-a3b0-5f6365b99c8c', 'true', 'display.on.consent.screen');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('b4d036d9-3470-4960-a3b0-5f6365b99c8c', 'false', 'include.in.token.scope');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('ef20b41d-2b0a-4ae4-986f-84c02d5c0832', '${addressScopeConsentText}', 'consent.screen.text');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('ef20b41d-2b0a-4ae4-986f-84c02d5c0832', 'true', 'display.on.consent.screen');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('ef20b41d-2b0a-4ae4-986f-84c02d5c0832', 'true', 'include.in.token.scope');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('f59745e4-deef-4a93-82d1-eccdd7b4eff1', '${profileScopeConsentText}', 'consent.screen.text');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('f59745e4-deef-4a93-82d1-eccdd7b4eff1', 'true', 'display.on.consent.screen');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('f59745e4-deef-4a93-82d1-eccdd7b4eff1', 'true', 'include.in.token.scope');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('f82637b5-3526-4226-a37f-682ca3153857', '', 'consent.screen.text');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('f82637b5-3526-4226-a37f-682ca3153857', 'false', 'display.on.consent.screen');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('f82637b5-3526-4226-a37f-682ca3153857', 'false', 'include.in.token.scope');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('f8f75121-3841-4a0c-8428-107b6aac8cdd', '${offlineAccessScopeConsentText}', 'consent.screen.text');
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('f8f75121-3841-4a0c-8428-107b6aac8cdd', 'true', 'display.on.consent.screen');

-- ----------------------------
-- Table structure for CLIENT_SCOPE_CLIENT
-- ----------------------------
DROP TABLE IF EXISTS `CLIENT_SCOPE_CLIENT`;
CREATE TABLE `CLIENT_SCOPE_CLIENT`  (
  `CLIENT_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `SCOPE_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `DEFAULT_SCOPE` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`CLIENT_ID`, `SCOPE_ID`) USING BTREE,
  INDEX `IDX_CLSCOPE_CL`(`CLIENT_ID`) USING BTREE,
  INDEX `IDX_CL_CLSCOPE`(`SCOPE_ID`) USING BTREE,
  CONSTRAINT `FK_C_CLI_SCOPE_CLIENT` FOREIGN KEY (`CLIENT_ID`) REFERENCES `CLIENT` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_C_CLI_SCOPE_SCOPE` FOREIGN KEY (`SCOPE_ID`) REFERENCES `CLIENT_SCOPE` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of CLIENT_SCOPE_CLIENT
-- ----------------------------
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('1ea2d246-2e72-440b-8236-0d9284d8a739', '0e9473b7-de09-484d-82e0-d88097d14b5d', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('1ea2d246-2e72-440b-8236-0d9284d8a739', '4c2b4fcb-5f1e-4c78-a341-fd27dfab38e0', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('1ea2d246-2e72-440b-8236-0d9284d8a739', '60240200-c0bd-4105-9f83-6dfb5d862eea', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('1ea2d246-2e72-440b-8236-0d9284d8a739', '7e69eefb-5f29-45cb-b249-3285b708f4e9', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('1ea2d246-2e72-440b-8236-0d9284d8a739', 'a78bcbbd-ddfb-4c36-a43d-0b026769e79b', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('1ea2d246-2e72-440b-8236-0d9284d8a739', 'b092e195-d133-4604-af92-27c80735ad3a', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('1ea2d246-2e72-440b-8236-0d9284d8a739', 'ef20b41d-2b0a-4ae4-986f-84c02d5c0832', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('1ea2d246-2e72-440b-8236-0d9284d8a739', 'f59745e4-deef-4a93-82d1-eccdd7b4eff1', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('1ea2d246-2e72-440b-8236-0d9284d8a739', 'f82637b5-3526-4226-a37f-682ca3153857', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('23979e0b-c766-42bc-aaa3-f320a171837a', '459e53e1-17bb-4dc9-bc04-9eea36319a60', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('23979e0b-c766-42bc-aaa3-f320a171837a', '63a96a62-31d2-4089-92a2-97e9f43c281f', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('23979e0b-c766-42bc-aaa3-f320a171837a', '81734f9f-7835-4af1-87f7-4625aa7ce31f', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('23979e0b-c766-42bc-aaa3-f320a171837a', '85983e59-945c-4f81-b2b3-b2f91ce56db9', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('23979e0b-c766-42bc-aaa3-f320a171837a', '91791326-87e9-4bfd-a5b3-b6752ef1bfc6', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('23979e0b-c766-42bc-aaa3-f320a171837a', '948be43b-bd24-4840-ac17-8b0477b17a77', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('23979e0b-c766-42bc-aaa3-f320a171837a', '9d697e2e-581d-4e01-92b5-09baef8c98f4', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('23979e0b-c766-42bc-aaa3-f320a171837a', 'b4d036d9-3470-4960-a3b0-5f6365b99c8c', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('23979e0b-c766-42bc-aaa3-f320a171837a', 'f8f75121-3841-4a0c-8428-107b6aac8cdd', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('605a5c15-5cfb-4453-b771-313bb7239afd', '459e53e1-17bb-4dc9-bc04-9eea36319a60', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('605a5c15-5cfb-4453-b771-313bb7239afd', '63a96a62-31d2-4089-92a2-97e9f43c281f', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('605a5c15-5cfb-4453-b771-313bb7239afd', '81734f9f-7835-4af1-87f7-4625aa7ce31f', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('605a5c15-5cfb-4453-b771-313bb7239afd', '85983e59-945c-4f81-b2b3-b2f91ce56db9', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('605a5c15-5cfb-4453-b771-313bb7239afd', '91791326-87e9-4bfd-a5b3-b6752ef1bfc6', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('605a5c15-5cfb-4453-b771-313bb7239afd', '948be43b-bd24-4840-ac17-8b0477b17a77', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('605a5c15-5cfb-4453-b771-313bb7239afd', '9d697e2e-581d-4e01-92b5-09baef8c98f4', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('605a5c15-5cfb-4453-b771-313bb7239afd', 'b4d036d9-3470-4960-a3b0-5f6365b99c8c', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('605a5c15-5cfb-4453-b771-313bb7239afd', 'f8f75121-3841-4a0c-8428-107b6aac8cdd', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('7515bb98-a373-4166-b03c-22aa4f14cea4', '459e53e1-17bb-4dc9-bc04-9eea36319a60', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('7515bb98-a373-4166-b03c-22aa4f14cea4', '63a96a62-31d2-4089-92a2-97e9f43c281f', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('7515bb98-a373-4166-b03c-22aa4f14cea4', '81734f9f-7835-4af1-87f7-4625aa7ce31f', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('7515bb98-a373-4166-b03c-22aa4f14cea4', '85983e59-945c-4f81-b2b3-b2f91ce56db9', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('7515bb98-a373-4166-b03c-22aa4f14cea4', '91791326-87e9-4bfd-a5b3-b6752ef1bfc6', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('7515bb98-a373-4166-b03c-22aa4f14cea4', '948be43b-bd24-4840-ac17-8b0477b17a77', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('7515bb98-a373-4166-b03c-22aa4f14cea4', '9d697e2e-581d-4e01-92b5-09baef8c98f4', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('7515bb98-a373-4166-b03c-22aa4f14cea4', 'b4d036d9-3470-4960-a3b0-5f6365b99c8c', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('7515bb98-a373-4166-b03c-22aa4f14cea4', 'f8f75121-3841-4a0c-8428-107b6aac8cdd', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('7b9dd7b9-71ad-470d-b499-3f9d598eaec0', '0e9473b7-de09-484d-82e0-d88097d14b5d', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('7b9dd7b9-71ad-470d-b499-3f9d598eaec0', '4c2b4fcb-5f1e-4c78-a341-fd27dfab38e0', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('7b9dd7b9-71ad-470d-b499-3f9d598eaec0', '60240200-c0bd-4105-9f83-6dfb5d862eea', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('7b9dd7b9-71ad-470d-b499-3f9d598eaec0', '7e69eefb-5f29-45cb-b249-3285b708f4e9', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('7b9dd7b9-71ad-470d-b499-3f9d598eaec0', 'a78bcbbd-ddfb-4c36-a43d-0b026769e79b', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('7b9dd7b9-71ad-470d-b499-3f9d598eaec0', 'b092e195-d133-4604-af92-27c80735ad3a', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('7b9dd7b9-71ad-470d-b499-3f9d598eaec0', 'ef20b41d-2b0a-4ae4-986f-84c02d5c0832', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('7b9dd7b9-71ad-470d-b499-3f9d598eaec0', 'f59745e4-deef-4a93-82d1-eccdd7b4eff1', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('7b9dd7b9-71ad-470d-b499-3f9d598eaec0', 'f82637b5-3526-4226-a37f-682ca3153857', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('7bdba359-bb16-4b34-8b43-c62aae593ea6', '459e53e1-17bb-4dc9-bc04-9eea36319a60', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('7bdba359-bb16-4b34-8b43-c62aae593ea6', '63a96a62-31d2-4089-92a2-97e9f43c281f', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('7bdba359-bb16-4b34-8b43-c62aae593ea6', '81734f9f-7835-4af1-87f7-4625aa7ce31f', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('7bdba359-bb16-4b34-8b43-c62aae593ea6', '85983e59-945c-4f81-b2b3-b2f91ce56db9', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('7bdba359-bb16-4b34-8b43-c62aae593ea6', '91791326-87e9-4bfd-a5b3-b6752ef1bfc6', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('7bdba359-bb16-4b34-8b43-c62aae593ea6', '948be43b-bd24-4840-ac17-8b0477b17a77', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('7bdba359-bb16-4b34-8b43-c62aae593ea6', '9d697e2e-581d-4e01-92b5-09baef8c98f4', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('7bdba359-bb16-4b34-8b43-c62aae593ea6', 'b4d036d9-3470-4960-a3b0-5f6365b99c8c', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('7bdba359-bb16-4b34-8b43-c62aae593ea6', 'f8f75121-3841-4a0c-8428-107b6aac8cdd', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('80e2ea2a-23ba-4fed-af92-cabc4d2ea4ed', '0e9473b7-de09-484d-82e0-d88097d14b5d', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('80e2ea2a-23ba-4fed-af92-cabc4d2ea4ed', '4c2b4fcb-5f1e-4c78-a341-fd27dfab38e0', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('80e2ea2a-23ba-4fed-af92-cabc4d2ea4ed', '60240200-c0bd-4105-9f83-6dfb5d862eea', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('80e2ea2a-23ba-4fed-af92-cabc4d2ea4ed', '7e69eefb-5f29-45cb-b249-3285b708f4e9', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('80e2ea2a-23ba-4fed-af92-cabc4d2ea4ed', 'a78bcbbd-ddfb-4c36-a43d-0b026769e79b', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('80e2ea2a-23ba-4fed-af92-cabc4d2ea4ed', 'b092e195-d133-4604-af92-27c80735ad3a', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('80e2ea2a-23ba-4fed-af92-cabc4d2ea4ed', 'ef20b41d-2b0a-4ae4-986f-84c02d5c0832', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('80e2ea2a-23ba-4fed-af92-cabc4d2ea4ed', 'f59745e4-deef-4a93-82d1-eccdd7b4eff1', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('80e2ea2a-23ba-4fed-af92-cabc4d2ea4ed', 'f82637b5-3526-4226-a37f-682ca3153857', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('8cedc74f-6b51-42a1-ba12-e2b80c11d39a', '0e9473b7-de09-484d-82e0-d88097d14b5d', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('8cedc74f-6b51-42a1-ba12-e2b80c11d39a', '4c2b4fcb-5f1e-4c78-a341-fd27dfab38e0', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('8cedc74f-6b51-42a1-ba12-e2b80c11d39a', '60240200-c0bd-4105-9f83-6dfb5d862eea', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('8cedc74f-6b51-42a1-ba12-e2b80c11d39a', '7e69eefb-5f29-45cb-b249-3285b708f4e9', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('8cedc74f-6b51-42a1-ba12-e2b80c11d39a', 'a78bcbbd-ddfb-4c36-a43d-0b026769e79b', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('8cedc74f-6b51-42a1-ba12-e2b80c11d39a', 'b092e195-d133-4604-af92-27c80735ad3a', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('8cedc74f-6b51-42a1-ba12-e2b80c11d39a', 'ef20b41d-2b0a-4ae4-986f-84c02d5c0832', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('8cedc74f-6b51-42a1-ba12-e2b80c11d39a', 'f59745e4-deef-4a93-82d1-eccdd7b4eff1', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('8cedc74f-6b51-42a1-ba12-e2b80c11d39a', 'f82637b5-3526-4226-a37f-682ca3153857', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('8f04cb27-025c-495c-ab99-916ad43d5957', '459e53e1-17bb-4dc9-bc04-9eea36319a60', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('8f04cb27-025c-495c-ab99-916ad43d5957', '63a96a62-31d2-4089-92a2-97e9f43c281f', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('8f04cb27-025c-495c-ab99-916ad43d5957', '81734f9f-7835-4af1-87f7-4625aa7ce31f', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('8f04cb27-025c-495c-ab99-916ad43d5957', '85983e59-945c-4f81-b2b3-b2f91ce56db9', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('8f04cb27-025c-495c-ab99-916ad43d5957', '91791326-87e9-4bfd-a5b3-b6752ef1bfc6', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('8f04cb27-025c-495c-ab99-916ad43d5957', '948be43b-bd24-4840-ac17-8b0477b17a77', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('8f04cb27-025c-495c-ab99-916ad43d5957', '9d697e2e-581d-4e01-92b5-09baef8c98f4', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('8f04cb27-025c-495c-ab99-916ad43d5957', 'b4d036d9-3470-4960-a3b0-5f6365b99c8c', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('8f04cb27-025c-495c-ab99-916ad43d5957', 'f8f75121-3841-4a0c-8428-107b6aac8cdd', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('9f327fb4-6a74-4b8f-8a22-03c07dcc6475', '459e53e1-17bb-4dc9-bc04-9eea36319a60', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('9f327fb4-6a74-4b8f-8a22-03c07dcc6475', '63a96a62-31d2-4089-92a2-97e9f43c281f', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('9f327fb4-6a74-4b8f-8a22-03c07dcc6475', '81734f9f-7835-4af1-87f7-4625aa7ce31f', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('9f327fb4-6a74-4b8f-8a22-03c07dcc6475', '85983e59-945c-4f81-b2b3-b2f91ce56db9', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('9f327fb4-6a74-4b8f-8a22-03c07dcc6475', '91791326-87e9-4bfd-a5b3-b6752ef1bfc6', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('9f327fb4-6a74-4b8f-8a22-03c07dcc6475', '948be43b-bd24-4840-ac17-8b0477b17a77', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('9f327fb4-6a74-4b8f-8a22-03c07dcc6475', '9d697e2e-581d-4e01-92b5-09baef8c98f4', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('9f327fb4-6a74-4b8f-8a22-03c07dcc6475', 'b4d036d9-3470-4960-a3b0-5f6365b99c8c', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('9f327fb4-6a74-4b8f-8a22-03c07dcc6475', 'f8f75121-3841-4a0c-8428-107b6aac8cdd', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('aa33da79-41ff-42be-9518-96b12bcd8055', '0e9473b7-de09-484d-82e0-d88097d14b5d', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('aa33da79-41ff-42be-9518-96b12bcd8055', '4c2b4fcb-5f1e-4c78-a341-fd27dfab38e0', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('aa33da79-41ff-42be-9518-96b12bcd8055', '60240200-c0bd-4105-9f83-6dfb5d862eea', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('aa33da79-41ff-42be-9518-96b12bcd8055', '7e69eefb-5f29-45cb-b249-3285b708f4e9', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('aa33da79-41ff-42be-9518-96b12bcd8055', 'a78bcbbd-ddfb-4c36-a43d-0b026769e79b', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('aa33da79-41ff-42be-9518-96b12bcd8055', 'b092e195-d133-4604-af92-27c80735ad3a', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('aa33da79-41ff-42be-9518-96b12bcd8055', 'ef20b41d-2b0a-4ae4-986f-84c02d5c0832', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('aa33da79-41ff-42be-9518-96b12bcd8055', 'f59745e4-deef-4a93-82d1-eccdd7b4eff1', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('aa33da79-41ff-42be-9518-96b12bcd8055', 'f82637b5-3526-4226-a37f-682ca3153857', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('b6c158d8-d49c-4a26-aa0a-c4ead414c72f', '0e9473b7-de09-484d-82e0-d88097d14b5d', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('b6c158d8-d49c-4a26-aa0a-c4ead414c72f', '4c2b4fcb-5f1e-4c78-a341-fd27dfab38e0', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('b6c158d8-d49c-4a26-aa0a-c4ead414c72f', '60240200-c0bd-4105-9f83-6dfb5d862eea', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('b6c158d8-d49c-4a26-aa0a-c4ead414c72f', '7e69eefb-5f29-45cb-b249-3285b708f4e9', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('b6c158d8-d49c-4a26-aa0a-c4ead414c72f', 'a78bcbbd-ddfb-4c36-a43d-0b026769e79b', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('b6c158d8-d49c-4a26-aa0a-c4ead414c72f', 'b092e195-d133-4604-af92-27c80735ad3a', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('b6c158d8-d49c-4a26-aa0a-c4ead414c72f', 'ef20b41d-2b0a-4ae4-986f-84c02d5c0832', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('b6c158d8-d49c-4a26-aa0a-c4ead414c72f', 'f59745e4-deef-4a93-82d1-eccdd7b4eff1', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('b6c158d8-d49c-4a26-aa0a-c4ead414c72f', 'f82637b5-3526-4226-a37f-682ca3153857', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('b8e29510-4692-4ac8-8aba-fc6882c30fc7', '459e53e1-17bb-4dc9-bc04-9eea36319a60', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('b8e29510-4692-4ac8-8aba-fc6882c30fc7', '63a96a62-31d2-4089-92a2-97e9f43c281f', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('b8e29510-4692-4ac8-8aba-fc6882c30fc7', '81734f9f-7835-4af1-87f7-4625aa7ce31f', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('b8e29510-4692-4ac8-8aba-fc6882c30fc7', '85983e59-945c-4f81-b2b3-b2f91ce56db9', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('b8e29510-4692-4ac8-8aba-fc6882c30fc7', '91791326-87e9-4bfd-a5b3-b6752ef1bfc6', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('b8e29510-4692-4ac8-8aba-fc6882c30fc7', '948be43b-bd24-4840-ac17-8b0477b17a77', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('b8e29510-4692-4ac8-8aba-fc6882c30fc7', '9d697e2e-581d-4e01-92b5-09baef8c98f4', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('b8e29510-4692-4ac8-8aba-fc6882c30fc7', 'b4d036d9-3470-4960-a3b0-5f6365b99c8c', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('b8e29510-4692-4ac8-8aba-fc6882c30fc7', 'f8f75121-3841-4a0c-8428-107b6aac8cdd', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('f2fba72e-4130-482a-9ac3-50674c17fe38', '0e9473b7-de09-484d-82e0-d88097d14b5d', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('f2fba72e-4130-482a-9ac3-50674c17fe38', '4c2b4fcb-5f1e-4c78-a341-fd27dfab38e0', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('f2fba72e-4130-482a-9ac3-50674c17fe38', '60240200-c0bd-4105-9f83-6dfb5d862eea', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('f2fba72e-4130-482a-9ac3-50674c17fe38', '7e69eefb-5f29-45cb-b249-3285b708f4e9', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('f2fba72e-4130-482a-9ac3-50674c17fe38', 'a78bcbbd-ddfb-4c36-a43d-0b026769e79b', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('f2fba72e-4130-482a-9ac3-50674c17fe38', 'b092e195-d133-4604-af92-27c80735ad3a', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('f2fba72e-4130-482a-9ac3-50674c17fe38', 'ef20b41d-2b0a-4ae4-986f-84c02d5c0832', b'0');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('f2fba72e-4130-482a-9ac3-50674c17fe38', 'f59745e4-deef-4a93-82d1-eccdd7b4eff1', b'1');
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('f2fba72e-4130-482a-9ac3-50674c17fe38', 'f82637b5-3526-4226-a37f-682ca3153857', b'1');

-- ----------------------------
-- Table structure for CLIENT_SCOPE_ROLE_MAPPING
-- ----------------------------
DROP TABLE IF EXISTS `CLIENT_SCOPE_ROLE_MAPPING`;
CREATE TABLE `CLIENT_SCOPE_ROLE_MAPPING`  (
  `SCOPE_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `ROLE_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`SCOPE_ID`, `ROLE_ID`) USING BTREE,
  INDEX `IDX_CLSCOPE_ROLE`(`SCOPE_ID`) USING BTREE,
  INDEX `IDX_ROLE_CLSCOPE`(`ROLE_ID`) USING BTREE,
  CONSTRAINT `FK_CL_SCOPE_RM_ROLE` FOREIGN KEY (`ROLE_ID`) REFERENCES `KEYCLOAK_ROLE` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_CL_SCOPE_RM_SCOPE` FOREIGN KEY (`SCOPE_ID`) REFERENCES `CLIENT_SCOPE` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of CLIENT_SCOPE_ROLE_MAPPING
-- ----------------------------
INSERT INTO `CLIENT_SCOPE_ROLE_MAPPING` VALUES ('a78bcbbd-ddfb-4c36-a43d-0b026769e79b', '90aa7861-6913-4f70-b72d-c0a5cbcef83f');
INSERT INTO `CLIENT_SCOPE_ROLE_MAPPING` VALUES ('f8f75121-3841-4a0c-8428-107b6aac8cdd', '07b566ca-e0e1-4032-98cb-5bbd0ffe0ae4');

-- ----------------------------
-- Table structure for CLIENT_SESSION
-- ----------------------------
DROP TABLE IF EXISTS `CLIENT_SESSION`;
CREATE TABLE `CLIENT_SESSION`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `CLIENT_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `REDIRECT_URI` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `STATE` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `TIMESTAMP` int(11) DEFAULT NULL,
  `SESSION_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `AUTH_METHOD` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `REALM_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `AUTH_USER_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `CURRENT_ACTION` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `IDX_CLIENT_SESSION_SESSION`(`SESSION_ID`) USING BTREE,
  CONSTRAINT `FK_B4AO2VCVAT6UKAU74WBWTFQO1` FOREIGN KEY (`SESSION_ID`) REFERENCES `USER_SESSION` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for CLIENT_SESSION_AUTH_STATUS
-- ----------------------------
DROP TABLE IF EXISTS `CLIENT_SESSION_AUTH_STATUS`;
CREATE TABLE `CLIENT_SESSION_AUTH_STATUS`  (
  `AUTHENTICATOR` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `STATUS` int(11) DEFAULT NULL,
  `CLIENT_SESSION` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`CLIENT_SESSION`, `AUTHENTICATOR`) USING BTREE,
  CONSTRAINT `AUTH_STATUS_CONSTRAINT` FOREIGN KEY (`CLIENT_SESSION`) REFERENCES `CLIENT_SESSION` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for CLIENT_SESSION_NOTE
-- ----------------------------
DROP TABLE IF EXISTS `CLIENT_SESSION_NOTE`;
CREATE TABLE `CLIENT_SESSION_NOTE`  (
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VALUE` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `CLIENT_SESSION` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`CLIENT_SESSION`, `NAME`) USING BTREE,
  CONSTRAINT `FK5EDFB00FF51C2736` FOREIGN KEY (`CLIENT_SESSION`) REFERENCES `CLIENT_SESSION` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for CLIENT_SESSION_PROT_MAPPER
-- ----------------------------
DROP TABLE IF EXISTS `CLIENT_SESSION_PROT_MAPPER`;
CREATE TABLE `CLIENT_SESSION_PROT_MAPPER`  (
  `PROTOCOL_MAPPER_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `CLIENT_SESSION` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`CLIENT_SESSION`, `PROTOCOL_MAPPER_ID`) USING BTREE,
  CONSTRAINT `FK_33A8SGQW18I532811V7O2DK89` FOREIGN KEY (`CLIENT_SESSION`) REFERENCES `CLIENT_SESSION` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for CLIENT_SESSION_ROLE
-- ----------------------------
DROP TABLE IF EXISTS `CLIENT_SESSION_ROLE`;
CREATE TABLE `CLIENT_SESSION_ROLE`  (
  `ROLE_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `CLIENT_SESSION` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`CLIENT_SESSION`, `ROLE_ID`) USING BTREE,
  CONSTRAINT `FK_11B7SGQW18I532811V7O2DV76` FOREIGN KEY (`CLIENT_SESSION`) REFERENCES `CLIENT_SESSION` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for CLIENT_USER_SESSION_NOTE
-- ----------------------------
DROP TABLE IF EXISTS `CLIENT_USER_SESSION_NOTE`;
CREATE TABLE `CLIENT_USER_SESSION_NOTE`  (
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VALUE` varchar(2048) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `CLIENT_SESSION` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`CLIENT_SESSION`, `NAME`) USING BTREE,
  CONSTRAINT `FK_CL_USR_SES_NOTE` FOREIGN KEY (`CLIENT_SESSION`) REFERENCES `CLIENT_SESSION` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for COMPONENT
-- ----------------------------
DROP TABLE IF EXISTS `COMPONENT`;
CREATE TABLE `COMPONENT`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `PARENT_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `PROVIDER_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `PROVIDER_TYPE` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `SUB_TYPE` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `IDX_COMPONENT_REALM`(`REALM_ID`) USING BTREE,
  INDEX `IDX_COMPONENT_PROVIDER_TYPE`(`PROVIDER_TYPE`) USING BTREE,
  CONSTRAINT `FK_COMPONENT_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of COMPONENT
-- ----------------------------
INSERT INTO `COMPONENT` VALUES ('2233a69e-99c5-4f7c-ae94-0b0a24339a60', 'Consent Required', 'demo', 'consent-required', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'demo', 'anonymous');
INSERT INTO `COMPONENT` VALUES ('2600f294-5e9a-4267-b8a3-c346cd324c39', 'hmac-generated', 'demo', 'hmac-generated', 'org.keycloak.keys.KeyProvider', 'demo', NULL);
INSERT INTO `COMPONENT` VALUES ('2b191558-cf56-40e7-b7e5-f1a1cef57d91', 'Max Clients Limit', 'demo', 'max-clients', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'demo', 'anonymous');
INSERT INTO `COMPONENT` VALUES ('4814b9c7-271d-48f8-a158-a103f96cfeaa', 'Allowed Client Scopes', 'master', 'allowed-client-templates', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'master', 'anonymous');
INSERT INTO `COMPONENT` VALUES ('4a58c620-515a-4450-96dc-4c7894254cec', 'Max Clients Limit', 'master', 'max-clients', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'master', 'anonymous');
INSERT INTO `COMPONENT` VALUES ('5ef799ff-154e-475a-a105-d002fb870e53', 'Allowed Protocol Mapper Types', 'master', 'allowed-protocol-mappers', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'master', 'anonymous');
INSERT INTO `COMPONENT` VALUES ('6979896f-1bd2-476c-bbab-7dfd69818003', 'Allowed Client Scopes', 'master', 'allowed-client-templates', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'master', 'authenticated');
INSERT INTO `COMPONENT` VALUES ('700dfe45-452d-4b40-a39d-d5a3616999b2', 'Trusted Hosts', 'demo', 'trusted-hosts', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'demo', 'anonymous');
INSERT INTO `COMPONENT` VALUES ('7700ea10-d441-4599-841f-790d03ce1185', 'Allowed Protocol Mapper Types', 'demo', 'allowed-protocol-mappers', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'demo', 'anonymous');
INSERT INTO `COMPONENT` VALUES ('7cffe64c-02b7-4771-8375-e59cea225065', 'Full Scope Disabled', 'demo', 'scope', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'demo', 'anonymous');
INSERT INTO `COMPONENT` VALUES ('861cee0a-d873-44f6-8950-3ad5a814545c', 'fallback-HS256', 'master', 'hmac-generated', 'org.keycloak.keys.KeyProvider', 'master', NULL);
INSERT INTO `COMPONENT` VALUES ('8c0c5b6c-5379-41dc-9a0e-fab7b3932f0c', 'aes-generated', 'demo', 'aes-generated', 'org.keycloak.keys.KeyProvider', 'demo', NULL);
INSERT INTO `COMPONENT` VALUES ('90efc9f3-0af0-4472-9a88-61890c53e158', 'Allowed Protocol Mapper Types', 'demo', 'allowed-protocol-mappers', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'demo', 'authenticated');
INSERT INTO `COMPONENT` VALUES ('93e04b13-2605-451a-976f-8528ac9dd1a7', 'rsa-generated', 'demo', 'rsa-generated', 'org.keycloak.keys.KeyProvider', 'demo', NULL);
INSERT INTO `COMPONENT` VALUES ('948bb714-bbaf-448f-b483-beaa0e39b678', 'Allowed Protocol Mapper Types', 'master', 'allowed-protocol-mappers', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'master', 'authenticated');
INSERT INTO `COMPONENT` VALUES ('95756fb1-6f2f-4d5b-a888-e89d481d9229', 'Allowed Client Scopes', 'demo', 'allowed-client-templates', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'demo', 'anonymous');
INSERT INTO `COMPONENT` VALUES ('98cfa735-0519-46a1-aa71-3a18373ec490', 'Consent Required', 'master', 'consent-required', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'master', 'anonymous');
INSERT INTO `COMPONENT` VALUES ('9a411a35-992d-401e-99f1-cf91f6fba851', 'Allowed Client Scopes', 'demo', 'allowed-client-templates', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'demo', 'authenticated');
INSERT INTO `COMPONENT` VALUES ('aaf3f98d-d6ef-4161-a0fd-ee545be11394', 'fallback-RS256', 'master', 'rsa-generated', 'org.keycloak.keys.KeyProvider', 'master', NULL);
INSERT INTO `COMPONENT` VALUES ('b31fedaf-47dd-4f3c-96a2-5ba3e16ff957', 'Full Scope Disabled', 'master', 'scope', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'master', 'anonymous');
INSERT INTO `COMPONENT` VALUES ('e31ce4f5-9410-40ea-9bd0-bc5a4a5827e8', 'Trusted Hosts', 'master', 'trusted-hosts', 'org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy', 'master', 'anonymous');

-- ----------------------------
-- Table structure for COMPONENT_CONFIG
-- ----------------------------
DROP TABLE IF EXISTS `COMPONENT_CONFIG`;
CREATE TABLE `COMPONENT_CONFIG`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `COMPONENT_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VALUE` varchar(4000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `IDX_COMPO_CONFIG_COMPO`(`COMPONENT_ID`) USING BTREE,
  CONSTRAINT `FK_COMPONENT_CONFIG` FOREIGN KEY (`COMPONENT_ID`) REFERENCES `COMPONENT` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of COMPONENT_CONFIG
-- ----------------------------
INSERT INTO `COMPONENT_CONFIG` VALUES ('04d55171-a5b5-49fd-8ac0-d9ee7b431135', '4a58c620-515a-4450-96dc-4c7894254cec', 'max-clients', '200');
INSERT INTO `COMPONENT_CONFIG` VALUES ('06885fa3-646d-4f0f-bcf4-aeb7940c05f4', '5ef799ff-154e-475a-a105-d002fb870e53', 'allowed-protocol-mapper-types', 'saml-user-attribute-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('0a2a26e5-8abf-489d-a24e-1bb4fa9f32a3', '7700ea10-d441-4599-841f-790d03ce1185', 'allowed-protocol-mapper-types', 'oidc-sha256-pairwise-sub-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('0d2f3d52-4dc8-43e2-850f-7dd51f55beaf', '7700ea10-d441-4599-841f-790d03ce1185', 'allowed-protocol-mapper-types', 'saml-user-attribute-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('0dcf2e0a-e0c6-44af-9539-e64af471495b', '2600f294-5e9a-4267-b8a3-c346cd324c39', 'priority', '100');
INSERT INTO `COMPONENT_CONFIG` VALUES ('0f3c6d88-6659-494d-bfa7-39e7f65304c5', '861cee0a-d873-44f6-8950-3ad5a814545c', 'kid', '9267d1fe-f820-4629-b057-72e540060004');
INSERT INTO `COMPONENT_CONFIG` VALUES ('129b3488-29af-4e00-acc6-3114b6c75150', '7700ea10-d441-4599-841f-790d03ce1185', 'allowed-protocol-mapper-types', 'saml-role-list-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('1415045b-958f-4f7f-901c-bb6ae8a22be7', '861cee0a-d873-44f6-8950-3ad5a814545c', 'priority', '-100');
INSERT INTO `COMPONENT_CONFIG` VALUES ('1e8e60bc-2e06-4902-bbf5-548085277e9b', '948bb714-bbaf-448f-b483-beaa0e39b678', 'allowed-protocol-mapper-types', 'oidc-usermodel-property-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('239f174d-bdd6-4144-b5a5-c0d59fb63862', '700dfe45-452d-4b40-a39d-d5a3616999b2', 'client-uris-must-match', 'true');
INSERT INTO `COMPONENT_CONFIG` VALUES ('2b56ab4e-037c-42c1-8503-6ee3fbee309d', '948bb714-bbaf-448f-b483-beaa0e39b678', 'allowed-protocol-mapper-types', 'oidc-full-name-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('32b335a0-cac1-4f6a-93da-569809a1c587', '861cee0a-d873-44f6-8950-3ad5a814545c', 'algorithm', 'HS256');
INSERT INTO `COMPONENT_CONFIG` VALUES ('33f3a002-9651-45cc-bc42-6a60443fde5e', '5ef799ff-154e-475a-a105-d002fb870e53', 'allowed-protocol-mapper-types', 'oidc-full-name-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('402b30ed-8e72-44c4-adbf-ecc973361990', '8c0c5b6c-5379-41dc-9a0e-fab7b3932f0c', 'kid', '0520e2ab-43ea-479a-892b-e674c393f4ce');
INSERT INTO `COMPONENT_CONFIG` VALUES ('48a1e488-316f-43a6-b8c3-eb90f0be5a71', '6979896f-1bd2-476c-bbab-7dfd69818003', 'allow-default-scopes', 'true');
INSERT INTO `COMPONENT_CONFIG` VALUES ('498e2212-ed86-4ad5-8d4e-e662950dd5b9', '7700ea10-d441-4599-841f-790d03ce1185', 'allowed-protocol-mapper-types', 'saml-user-property-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('4d3fa3bd-2b93-40a8-9eea-965fb4b10549', 'aaf3f98d-d6ef-4161-a0fd-ee545be11394', 'certificate', 'MIICmzCCAYMCBgF1mzp++TANBgkqhkiG9w0BAQsFADARMQ8wDQYDVQQDDAZtYXN0ZXIwHhcNMjAxMTA2MDE0MzIzWhcNMzAxMTA2MDE0NTAzWjARMQ8wDQYDVQQDDAZtYXN0ZXIwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDGPTSaByvwaI+BW8uts/wAcOyjIIn9t05qsju5ftM4sT96UhXKrBMXJMNe1Igqi57p7dZpsl96Htde/I/qdrlvq+Vyb3aZy05+T77ZddKa8EtwfMwro+ww59gXrGWA3i57Zxi0QzA3QUhkmcIEquvN67UYuCTawmOzg1IrGZ2WqMgc/SCIdhNwzYb9OjA5wUCsNja0s2THWKE5/1oBQTBCENHFEhU1ke+S3A4GAgz7xIdRiZUBHBvzHhagMQOMxnRKGrBmY3/heiek5VcNYi0IBSM4r9Cr1+1snWoW3iekc5mNU5eZxIrm6Di8XyKLM0HyydZCd+ys5w1PG8l6IO/jAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAEjynFfGo3uP4OhIevxswP4NoX3kv2W7WvOyqpjVi7qASFuzWyctUQN2dgZGIfBXPx1hPYhnnv6QOqYqm9j7P2p8HS+lPNiyqsda3eP0l06sNBkO8AHuJ0aScUh7yr2ykhOHhq3jAQ2Pf67AC89jQBmeAkXXw8UBRRE/MMfufur5FPZ7lXZUycFmcZ9MvYOItYwc7T779IfP8TddylJPdmkQn4aB6LStKBzJs31NcERzhBKK3cgVSQzcW0rZI8MIFkKMIOoDOVWV8WPgmYrLCzRs3LLYYIqfFIaBfl386NZFt8opzruZOQYOlrtVDDWIYygIQhSlDN/Ej5+lGYzvO8M=');
INSERT INTO `COMPONENT_CONFIG` VALUES ('59f8f0e5-3fd1-4220-ac3c-74d963bc10a2', '90efc9f3-0af0-4472-9a88-61890c53e158', 'allowed-protocol-mapper-types', 'oidc-usermodel-attribute-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('5a7e9f1b-4baf-4cea-9aab-9de4eb76a795', '948bb714-bbaf-448f-b483-beaa0e39b678', 'allowed-protocol-mapper-types', 'saml-user-attribute-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('5aff4381-e651-47de-a1ed-f56829eabd56', '2600f294-5e9a-4267-b8a3-c346cd324c39', 'secret', '9lWzMoin4eJzY29nga8GNNcR9A_abOFFnIO6xCeAzvtlZhrKYMc2cFxRh3RwkeXKHnsbosxNBtEjCrk1jBbNCQ');
INSERT INTO `COMPONENT_CONFIG` VALUES ('5c8f7f26-076e-4e16-8777-8c87fb3e1582', '7700ea10-d441-4599-841f-790d03ce1185', 'allowed-protocol-mapper-types', 'oidc-usermodel-property-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('5d03e64d-5ffb-4091-b89d-21c69ced4023', '93e04b13-2605-451a-976f-8528ac9dd1a7', 'priority', '100');
INSERT INTO `COMPONENT_CONFIG` VALUES ('6218f9cd-5f7d-417a-aba9-18df02f1a84d', '8c0c5b6c-5379-41dc-9a0e-fab7b3932f0c', 'priority', '100');
INSERT INTO `COMPONENT_CONFIG` VALUES ('63510559-e77b-452b-9ebf-1752952526db', '2600f294-5e9a-4267-b8a3-c346cd324c39', 'algorithm', 'HS256');
INSERT INTO `COMPONENT_CONFIG` VALUES ('699b5304-b46c-4874-b98a-1510eca12ff4', '93e04b13-2605-451a-976f-8528ac9dd1a7', 'privateKey', 'MIIEpQIBAAKCAQEA2lVFKVy5b+5HhZfeGJeM9k5xMacgzxbFyRCfkDqghTrjD+bA91m6Da6fatu7plWNs3F/qbvPxfdnENc5MjSRL1c3RLD6vhwxOakON8j39TqBigPsmwQPkimtaU5mSlBLn30utttGtSjzbP97Yp57hJT6Jes7Og4zrFpE8Jf7cUVpIkFpb57WZMWY1hROsdAKh6nuXMk0jTNk+M9MHtYXv0dAz462ZZCu+rSy2dWwVJQ9E33Wk5zHSjXwVw1p4OHG463Aj6KuafYzeI+DqkMdU18JvIWx9SAbIXl1x8rIhn/Ay6OrKVNrOZBT985E1KQ5vviv7tcCATOyDGWj+NcexwIDAQABAoIBACi11ZuMypbwWy2xz5vORl526exUOW3j1SVUkCwYgm3VORlx8XEtrd93PAp0xlyRf0aCV0eS2X8UTod5WhTecGPfY1LPnTFtmFAkEZ6dPn7fVeAiMzAEZdLfKpKEJ9abej5W3SsDX//PjvQ3/w24d1eVX79KSVTd6QyuGES0UhIggtddsAGbGVC3fxYjyIwqptVtILx8vpYw6S71miPXSu+AtsStGHJWgs55bSxgWUoMeXgZ5Ww2QI2QmaxOSV9td6W4UPlX1Kziwwjjlxh7/UN088BGvMlsJusm4LrSaltBeeigxqBWVvrcNDa/69vQlyzx5u+CKLIDC4OyuWMkCAECgYEA+i2fQEFh2w945bV/KjgtAQekSIP9ikN2P23ngss9kJpDtU6tYhGOnMF/0xQKSbwa+69Wv4Id3flt2KokCEchhvIfbBk5HEC1au4nIgqRDqyXAAlbnL1UwTBHmfe9KYTXkmNlLmAP20omqunpbyOpdv3WhFMrlye+tlozmBLVO8cCgYEA32nwMGbnXSDTXZ64iEdKpvP/fD/rMTk9ycmcbgqUHL9gF9WaFBFT1UhI/ic394OnsGkGtmf7KpjoHnGrAEa7fiNVCkRz1fAdgafZbIQPyh36aqCX1bJJNRYHH+dzU3hmoKmMr4s73gJIv12/6M12DCDkFEhvqnKkty0ppN9xBQECgYEAiOEHPa/IK4PWoM/hyKa9sM+OigJ1aP2qJ+ApB73NbF2K6Bso7/wtLAUOu90+F8Ose8IeU1+MmdOLzzyfxEuuw1eb65gNUDDzUZMFpDFl1kKFx4ZXMnXAdobE1i/etmWxUK3DQVFbu55hc6fRiXZYlPsjMHkU43nidYD6s5yyQCcCgYEAuQZ/iVZv3kzFQA+nPBXu2X3R5RD6s3V4mpmCGBBiwbws1RBt3pjLGSQYb4QDcdVzF0Kj8IUFGi4wbqYsvLnXpYuVbj9qv49woGRmSNzGRtm0/oGpva09u91WYTHJ7SMVu5EvlmJ1XlP9856sxR0D2t+ShMnI0RzJFXsttgHnFAECgYEAopeFVwH2F9DanvNNTEc81RbqleortyUeSNDgMyyQRIkvB4yyz2qDZk+rW/uGNOlZeJW3TfgCjePLoCLJYEF+L5o8MaZvw+dm1tfs+jCf+SQTxpgt1xOp3DaTiPmYuIXpDjI7p38olDDx1X2CWTgBBbfR++CxbQvDT8SKUFOmRXU=');
INSERT INTO `COMPONENT_CONFIG` VALUES ('6fdc48ea-0733-438c-9d27-68ada90af294', 'aaf3f98d-d6ef-4161-a0fd-ee545be11394', 'privateKey', 'MIIEowIBAAKCAQEAxj00mgcr8GiPgVvLrbP8AHDsoyCJ/bdOarI7uX7TOLE/elIVyqwTFyTDXtSIKoue6e3WabJfeh7XXvyP6na5b6vlcm92mctOfk++2XXSmvBLcHzMK6PsMOfYF6xlgN4ue2cYtEMwN0FIZJnCBKrrzeu1GLgk2sJjs4NSKxmdlqjIHP0giHYTcM2G/TowOcFArDY2tLNkx1ihOf9aAUEwQhDRxRIVNZHvktwOBgIM+8SHUYmVARwb8x4WoDEDjMZ0ShqwZmN/4XonpOVXDWItCAUjOK/Qq9ftbJ1qFt4npHOZjVOXmcSK5ug4vF8iizNB8snWQnfsrOcNTxvJeiDv4wIDAQABAoIBAQCagxAh0d8shcL8l5jcbgHIMjwvNWh4qcxkG0Gz5icp8/U/GQXFL3O9wTjKDCuJxcN6H5fUUGRcofdKIN7nZ9xHLxsqo7g0TSOEDhH6GvypKQwTxCG7DgxFXh+u6/89f2+JXaUCs6+8EZUkXDe+PZ6HpOVFPBn1rDfbrkp4L4IF72zL3+lIPLKFYEUQ0DVUBGOsdzxK1xVkgTb9ulw4e/qfQckox3EiVnc9ehKbZhG4yIubRqdRy3lCfK4w/5LUW1MfIwtd9wLNon8GJ9bo8XcZsARfhn0Tbgzr2veDJOsFReMRh4ZFQEJJgddRn6tbWkoDeWsSrLbUc5R+HSYEuRn5AoGBAPp2tPZaBVADrkrirsa9yfPDJ0jtaYULtXPp9uClRCWZLsIykei36U9sXfVP3CsCP2lEtR4QxFZ5XeZVBUImnxeZB9+9EeHaYSH/Z+lwe+IHJfP6lZ4HUEbPEPafTKfWo9XFrnp150PHcCHkZrkFgakoUYMhHhq8AtpRnYkEm25VAoGBAMqe+fPDoeD1o+5VH9YYdg58a9iS5Rk70Tn/VXK+ptnMyhpic3Rk4hODhvjD0P1jnsj7uVBC/vmIImxvXcCly8BzhQSbaiP0ra3k7E44lFzCR99fzm3AH5ZT+mt3yWX5E93IgQak2LRaXD7RuIyCKYx+Vf4AgCWKGba+MqaBw61XAoGAcRaeOMmGso0m6ksV7UiOYhEQsN35kggqYu8V+HLGpU9YDHk25mP75U+h0PzBBW5bYVWTjcK2U/Ey06g52peDp+0B4CUHaPnod6hRq9aNKQ5fNZ1EZvkYZxBhsWlV/EZu7Tv3vvVzeWgYmkT9mLcCx3Ub8OBILnwLsbW+Mrqwz40CgYAJbGtjku2scMA3DW51np2e/tplMC/HhKiucdmfhI5uAsBDieCkwSxii/NzDmhh4Ig6ALsVqIgDQvmW4XcSxqM7SCQfmrrNAUwykjsLSEZeKgr9sI8BO7Y8X5t99AxB2du6rOCTVoddm/Y+VbKnlkYoapfFQHC1Tj7z6NSiOdbH8QKBgHMr39E6/G5lyZcPRoag5htOaDu5EaB4CHOYohKKncItAqn/+EtaGSTxyr7BKhypb8O1IQ3iGxiNQJgTYBOHyO9cZ1ejPyJJwOphBIUXCW2PWlgy3hykAHRrMEyG9ChnzQ8BcKuXRsHxDOx1xFL469O+4nsR8gFsqSQEO/ZmjtsF');
INSERT INTO `COMPONENT_CONFIG` VALUES ('78f2d8e7-f594-4557-96b9-3f3fe18e36f8', '90efc9f3-0af0-4472-9a88-61890c53e158', 'allowed-protocol-mapper-types', 'saml-user-property-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('7f0ae193-7d09-4ec1-92ba-7adc35f73247', '90efc9f3-0af0-4472-9a88-61890c53e158', 'allowed-protocol-mapper-types', 'saml-user-attribute-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('7f4b63a8-ab6c-48e9-8286-486225ae0313', '90efc9f3-0af0-4472-9a88-61890c53e158', 'allowed-protocol-mapper-types', 'saml-role-list-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('7f7162bd-0e8d-4cfe-9111-140856c8733d', '8c0c5b6c-5379-41dc-9a0e-fab7b3932f0c', 'secret', '7VLib1Vv4xXMb0G_cpVing');
INSERT INTO `COMPONENT_CONFIG` VALUES ('80bd4302-170b-434a-8524-6c128124b5d9', '5ef799ff-154e-475a-a105-d002fb870e53', 'allowed-protocol-mapper-types', 'oidc-usermodel-attribute-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('8e045083-ec72-487a-937b-ef65d0425074', '948bb714-bbaf-448f-b483-beaa0e39b678', 'allowed-protocol-mapper-types', 'oidc-sha256-pairwise-sub-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('90a193c5-2596-4c6b-8644-fd185750c138', '90efc9f3-0af0-4472-9a88-61890c53e158', 'allowed-protocol-mapper-types', 'oidc-usermodel-property-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('918ea99f-ad2c-473f-bced-4b48aa16f459', '90efc9f3-0af0-4472-9a88-61890c53e158', 'allowed-protocol-mapper-types', 'oidc-full-name-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('98823d63-c4f0-4357-946a-8c1c7f7f670c', '4814b9c7-271d-48f8-a158-a103f96cfeaa', 'allow-default-scopes', 'true');
INSERT INTO `COMPONENT_CONFIG` VALUES ('990bc1c0-129f-4a20-8f5a-58dbab031d06', '5ef799ff-154e-475a-a105-d002fb870e53', 'allowed-protocol-mapper-types', 'oidc-usermodel-property-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('9bd9cd87-2e91-494d-a592-4d16cc25d5d3', '7700ea10-d441-4599-841f-790d03ce1185', 'allowed-protocol-mapper-types', 'oidc-usermodel-attribute-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('9c99024e-d973-42f7-b7ea-470467eb98bf', '5ef799ff-154e-475a-a105-d002fb870e53', 'allowed-protocol-mapper-types', 'oidc-sha256-pairwise-sub-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('9f7d9483-2a05-4bc7-9b68-e14a447d7039', '948bb714-bbaf-448f-b483-beaa0e39b678', 'allowed-protocol-mapper-types', 'saml-user-property-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('a1b0f1e3-dc3f-4758-b642-67e4c8f4728b', '90efc9f3-0af0-4472-9a88-61890c53e158', 'allowed-protocol-mapper-types', 'oidc-address-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('a25574fd-5a39-4f4a-a9cd-73f654e314e5', '948bb714-bbaf-448f-b483-beaa0e39b678', 'allowed-protocol-mapper-types', 'saml-role-list-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('a3d77a5a-2a78-48f5-a6c1-c7bec591e0f5', 'e31ce4f5-9410-40ea-9bd0-bc5a4a5827e8', 'client-uris-must-match', 'true');
INSERT INTO `COMPONENT_CONFIG` VALUES ('a9b7718a-6d81-498d-974e-d63f9e0809e8', '861cee0a-d873-44f6-8950-3ad5a814545c', 'secret', 'I3lkON_Ezvph4g1sw4HWJVEtniL1kRIK7JytMAhI36D8xHdf-GUVh3VCioScJpg5TypI9R80b3pKby9fJnR9JQ');
INSERT INTO `COMPONENT_CONFIG` VALUES ('ac298c19-388e-4bd2-8b66-2b349cee54f9', '5ef799ff-154e-475a-a105-d002fb870e53', 'allowed-protocol-mapper-types', 'saml-user-property-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('b1df60ea-71fb-409d-b158-21d75cd8813e', '93e04b13-2605-451a-976f-8528ac9dd1a7', 'certificate', 'MIIClzCCAX8CBgF2CLLUuDANBgkqhkiG9w0BAQsFADAPMQ0wCwYDVQQDDARkZW1vMB4XDTIwMTEyNzA3NTMyNloXDTMwMTEyNzA3NTUwNlowDzENMAsGA1UEAwwEZGVtbzCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBANpVRSlcuW/uR4WX3hiXjPZOcTGnIM8WxckQn5A6oIU64w/mwPdZug2un2rbu6ZVjbNxf6m7z8X3ZxDXOTI0kS9XN0Sw+r4cMTmpDjfI9/U6gYoD7JsED5IprWlOZkpQS599LrbbRrUo82z/e2Kee4SU+iXrOzoOM6xaRPCX+3FFaSJBaW+e1mTFmNYUTrHQCoep7lzJNI0zZPjPTB7WF79HQM+OtmWQrvq0stnVsFSUPRN91pOcx0o18FcNaeDhxuOtwI+irmn2M3iPg6pDHVNfCbyFsfUgGyF5dcfKyIZ/wMujqylTazmQU/fORNSkOb74r+7XAgEzsgxlo/jXHscCAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAvq1WZbxXSZhbEE8NCZACVgga4VwYEOHUOZhrpclWmgUBUvJLC5EJ53cpxQn6zYt858xjiSiP4OszxkJNFtMQhmpQC+l0IRfSadD4O6ie1FkUO4TUPMJCE43jBmmTU7+DALje9cjOIJ3u1M1HOT58KGO8gXee8tB1pnVxtN5WuVtE/Gxy56+pIDYzRlNj5acpmlLJ3lTgITAVvCjSlGkL5GNG1l6/ZlL0dI7k9VRifO/NHTxm9ln3lwgJbNWCDsV9wO+1VQmA7c6qfKycvG2bIFSBQWuk7rqUjrOkkv7u7LSJ5eHCniYXec0msWkyYi/iMgpMrcyZRiBG1uqg77Upbw==');
INSERT INTO `COMPONENT_CONFIG` VALUES ('b441d467-12b9-4291-8ba7-5693d92ebc8f', 'e31ce4f5-9410-40ea-9bd0-bc5a4a5827e8', 'host-sending-registration-request-must-match', 'true');
INSERT INTO `COMPONENT_CONFIG` VALUES ('b47bd6e8-3b4b-4e83-8093-93f953b68670', '2b191558-cf56-40e7-b7e5-f1a1cef57d91', 'max-clients', '200');
INSERT INTO `COMPONENT_CONFIG` VALUES ('b765c3e2-1160-487f-ab75-47c909ca7ccd', '7700ea10-d441-4599-841f-790d03ce1185', 'allowed-protocol-mapper-types', 'oidc-full-name-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('b9417a6b-310f-4b61-9a1f-0ee50eb40d20', '948bb714-bbaf-448f-b483-beaa0e39b678', 'allowed-protocol-mapper-types', 'oidc-address-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('c00a0413-c082-4c9d-8e57-314b1ac999b4', '5ef799ff-154e-475a-a105-d002fb870e53', 'allowed-protocol-mapper-types', 'oidc-address-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('c1074c83-f006-4c1d-85f7-3c52739f0198', '948bb714-bbaf-448f-b483-beaa0e39b678', 'allowed-protocol-mapper-types', 'oidc-usermodel-attribute-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('cd482cd6-525d-48ec-91ec-ec93008a7024', 'aaf3f98d-d6ef-4161-a0fd-ee545be11394', 'algorithm', 'RS256');
INSERT INTO `COMPONENT_CONFIG` VALUES ('d290eb5a-9542-4ffd-8deb-6da1bd3592dd', '2600f294-5e9a-4267-b8a3-c346cd324c39', 'kid', 'f9e5baea-8a92-407c-888b-489a76c7f353');
INSERT INTO `COMPONENT_CONFIG` VALUES ('dba5475d-eeda-4434-8f50-029f320b082c', 'aaf3f98d-d6ef-4161-a0fd-ee545be11394', 'priority', '-100');
INSERT INTO `COMPONENT_CONFIG` VALUES ('dc2a1780-2a4e-4b13-80bf-9f6acf86997d', '9a411a35-992d-401e-99f1-cf91f6fba851', 'allow-default-scopes', 'true');
INSERT INTO `COMPONENT_CONFIG` VALUES ('e61e13d0-7e31-430d-bc29-d7bf587d80f4', '700dfe45-452d-4b40-a39d-d5a3616999b2', 'host-sending-registration-request-must-match', 'true');
INSERT INTO `COMPONENT_CONFIG` VALUES ('f03e1fc0-ccb8-4184-acc4-0a068ef93ee3', '5ef799ff-154e-475a-a105-d002fb870e53', 'allowed-protocol-mapper-types', 'saml-role-list-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('f22dc578-fd89-4c8f-af58-4494eed5152d', '95756fb1-6f2f-4d5b-a888-e89d481d9229', 'allow-default-scopes', 'true');
INSERT INTO `COMPONENT_CONFIG` VALUES ('f51ac94c-c19b-4210-9273-fe1c3d481c5f', '90efc9f3-0af0-4472-9a88-61890c53e158', 'allowed-protocol-mapper-types', 'oidc-sha256-pairwise-sub-mapper');
INSERT INTO `COMPONENT_CONFIG` VALUES ('fe2efbe0-2f35-4919-8df7-015f46d5a47c', '7700ea10-d441-4599-841f-790d03ce1185', 'allowed-protocol-mapper-types', 'oidc-address-mapper');

-- ----------------------------
-- Table structure for COMPOSITE_ROLE
-- ----------------------------
DROP TABLE IF EXISTS `COMPOSITE_ROLE`;
CREATE TABLE `COMPOSITE_ROLE`  (
  `COMPOSITE` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `CHILD_ROLE` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`COMPOSITE`, `CHILD_ROLE`) USING BTREE,
  INDEX `IDX_COMPOSITE`(`COMPOSITE`) USING BTREE,
  INDEX `IDX_COMPOSITE_CHILD`(`CHILD_ROLE`) USING BTREE,
  CONSTRAINT `FK_A63WVEKFTU8JO1PNJ81E7MCE2` FOREIGN KEY (`COMPOSITE`) REFERENCES `KEYCLOAK_ROLE` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_GR7THLLB9LU8Q4VQA4524JJY8` FOREIGN KEY (`CHILD_ROLE`) REFERENCES `KEYCLOAK_ROLE` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of COMPOSITE_ROLE
-- ----------------------------
INSERT INTO `COMPOSITE_ROLE` VALUES ('09b53e53-331f-4fbd-8c4d-87c6c62da264', 'eb124d23-1a6c-4ea4-9cd5-75678d68e4b5');
INSERT INTO `COMPOSITE_ROLE` VALUES ('0cd1b5a1-21a0-4ffd-8fbc-aed0c4686cb2', '0f9556b9-85ad-4d3c-92b3-14ad1f62df2a');
INSERT INTO `COMPOSITE_ROLE` VALUES ('0cd1b5a1-21a0-4ffd-8fbc-aed0c4686cb2', '83fe4665-b793-42ce-80f3-90cead33083a');
INSERT INTO `COMPOSITE_ROLE` VALUES ('34607a1c-9ff9-41d7-879b-2e269b2c0463', '1b6845b2-6179-4bc5-bce7-a4003c0e4b70');
INSERT INTO `COMPOSITE_ROLE` VALUES ('34607a1c-9ff9-41d7-879b-2e269b2c0463', 'd349ced9-620c-4785-939b-c9509f87a785');
INSERT INTO `COMPOSITE_ROLE` VALUES ('52a1662f-134e-4b3f-8c23-c8631015555a', 'dc968acd-5a95-4f29-bdfa-0c8c442978d7');
INSERT INTO `COMPOSITE_ROLE` VALUES ('63c27012-2e75-4b40-a03b-1538f37bca36', '5d97bb63-7297-4d11-a8c4-199fb99d98a5');
INSERT INTO `COMPOSITE_ROLE` VALUES ('63f0407a-9b7f-4d25-98eb-f2b44b7cc3ed', '35b49764-cf02-4b7c-a940-556497fee2ee');
INSERT INTO `COMPOSITE_ROLE` VALUES ('63f0407a-9b7f-4d25-98eb-f2b44b7cc3ed', '3f4f695a-b51f-458f-b77f-a77171c3460c');
INSERT INTO `COMPOSITE_ROLE` VALUES ('6c55ef31-d139-48f0-83cd-a8dde67d3dac', 'f7c33908-c4dc-4e78-adbe-e880c54107b8');
INSERT INTO `COMPOSITE_ROLE` VALUES ('9b0c4138-0ba3-4b20-8564-6a912bd781f1', '3f38e7db-8c56-4080-a919-f71899ce84ea');
INSERT INTO `COMPOSITE_ROLE` VALUES ('c41dfeb5-11e0-40eb-bd18-c5bf97247ba3', 'b87fc7fe-2465-4fe1-9b2a-22db0ff27e05');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '0201a972-0596-404f-b0a7-cfa9cf60232c');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '0666da58-e96d-4637-add1-bdcc8ae12a32');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '06c2e966-851b-4615-a30a-b13b2eb82e8d');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '09936ec5-8e0a-416b-a8d2-0f26c22b5282');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '0cd1b5a1-21a0-4ffd-8fbc-aed0c4686cb2');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '0f9556b9-85ad-4d3c-92b3-14ad1f62df2a');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '1e1843b1-adb7-4352-a6f5-e23b895a0c7f');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '268ce6a3-36ef-41ab-83c4-58e170d27906');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '2ded2bef-068f-4420-ba62-3b90d89345ae');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '3188b407-f30e-4518-bd4b-1be481167170');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '31e47454-1409-4b07-bb24-bc2a6ab747af');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '35b49764-cf02-4b7c-a940-556497fee2ee');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '3f38e7db-8c56-4080-a919-f71899ce84ea');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '3f4f695a-b51f-458f-b77f-a77171c3460c');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '47d5fdc6-8d94-4d34-a471-d2b50da5de1a');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '55b1ad60-1d20-47ed-9c36-a627b9455990');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '5d75cc0e-69e8-452e-9660-dc0498bd2c4f');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '5f9b0558-4144-4afc-8825-2231765f96ed');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '63f0407a-9b7f-4d25-98eb-f2b44b7cc3ed');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '64010059-7dde-4bf0-a42d-3f69aedd4d97');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '6897532e-c1e0-4bd9-871e-7bb68127ffea');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '6c55ef31-d139-48f0-83cd-a8dde67d3dac');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '83fe4665-b793-42ce-80f3-90cead33083a');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '8aae6535-a6eb-4d5d-82dd-aff060990931');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '8d5d7a8c-9547-4165-bb0f-d736ee4783ed');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '9175e7d8-71e6-4091-99bd-5084e9e1f84f');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '967d4a6e-d1e4-42c2-8b8c-c178660ac9a7');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', '9b0c4138-0ba3-4b20-8564-6a912bd781f1');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', 'a23e8432-6a65-49dd-9f43-458f2dab7709');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', 'a7cb0ea5-35ae-4e08-9258-ed8fef6587ac');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', 'ad632e6b-0dee-452d-9b80-915d2082a169');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', 'af5cc799-4d1b-4d20-9524-a232ad1d9bf2');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', 'bc2c8d50-2b9f-46b3-a358-44223e839b10');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', 'ce73e1ee-c8b6-46ac-857e-073f1ae6df7b');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', 'cec52398-64c6-46a0-8bf2-4510569b9a65');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', 'f74b4777-198e-49df-a8a0-1494f1529ee5');
INSERT INTO `COMPOSITE_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', 'f7c33908-c4dc-4e78-adbe-e880c54107b8');
INSERT INTO `COMPOSITE_ROLE` VALUES ('eeaf3173-45ec-4429-89b1-48326ba45773', '8b79da3a-7b0e-4ca5-8bbd-7cee6b18bfae');
INSERT INTO `COMPOSITE_ROLE` VALUES ('fedb960b-5157-494b-b631-b1959b5ff411', '05b04ac1-ccb3-4d44-ad17-7ebc1772fdff');
INSERT INTO `COMPOSITE_ROLE` VALUES ('fedb960b-5157-494b-b631-b1959b5ff411', '0fb0c7f5-e526-4cda-a961-77f98d7e1e2e');
INSERT INTO `COMPOSITE_ROLE` VALUES ('fedb960b-5157-494b-b631-b1959b5ff411', '1b6845b2-6179-4bc5-bce7-a4003c0e4b70');
INSERT INTO `COMPOSITE_ROLE` VALUES ('fedb960b-5157-494b-b631-b1959b5ff411', '1f0ef3b5-9176-43fa-b0d8-6b1dc236cfae');
INSERT INTO `COMPOSITE_ROLE` VALUES ('fedb960b-5157-494b-b631-b1959b5ff411', '29661820-f292-43e5-9bf4-5c1154db2e09');
INSERT INTO `COMPOSITE_ROLE` VALUES ('fedb960b-5157-494b-b631-b1959b5ff411', '34607a1c-9ff9-41d7-879b-2e269b2c0463');
INSERT INTO `COMPOSITE_ROLE` VALUES ('fedb960b-5157-494b-b631-b1959b5ff411', '758dfba3-fafc-4e80-8f6b-722be450c700');
INSERT INTO `COMPOSITE_ROLE` VALUES ('fedb960b-5157-494b-b631-b1959b5ff411', '81f241a8-b2cd-4123-85b5-e107dc1dc396');
INSERT INTO `COMPOSITE_ROLE` VALUES ('fedb960b-5157-494b-b631-b1959b5ff411', '8b79da3a-7b0e-4ca5-8bbd-7cee6b18bfae');
INSERT INTO `COMPOSITE_ROLE` VALUES ('fedb960b-5157-494b-b631-b1959b5ff411', 'b0fe80d4-56b1-45f2-937f-211c4e9da6e9');
INSERT INTO `COMPOSITE_ROLE` VALUES ('fedb960b-5157-494b-b631-b1959b5ff411', 'd050e62b-1743-4f2c-b185-534928e3b723');
INSERT INTO `COMPOSITE_ROLE` VALUES ('fedb960b-5157-494b-b631-b1959b5ff411', 'd349ced9-620c-4785-939b-c9509f87a785');
INSERT INTO `COMPOSITE_ROLE` VALUES ('fedb960b-5157-494b-b631-b1959b5ff411', 'd64c7bd9-6989-49cb-96d4-565581f8fea1');
INSERT INTO `COMPOSITE_ROLE` VALUES ('fedb960b-5157-494b-b631-b1959b5ff411', 'dac63842-a2a5-4d9a-8dee-ad210e14ca2f');
INSERT INTO `COMPOSITE_ROLE` VALUES ('fedb960b-5157-494b-b631-b1959b5ff411', 'e6d0965c-35dd-4c71-8d7d-bf16a56fa7ca');
INSERT INTO `COMPOSITE_ROLE` VALUES ('fedb960b-5157-494b-b631-b1959b5ff411', 'eeaf3173-45ec-4429-89b1-48326ba45773');
INSERT INTO `COMPOSITE_ROLE` VALUES ('fedb960b-5157-494b-b631-b1959b5ff411', 'f65f6c2d-cc8f-4d28-82dd-c240be8d4b8b');
INSERT INTO `COMPOSITE_ROLE` VALUES ('fedb960b-5157-494b-b631-b1959b5ff411', 'ffc496ba-4cb2-435e-83b6-17de9cc45c80');

-- ----------------------------
-- Table structure for CREDENTIAL
-- ----------------------------
DROP TABLE IF EXISTS `CREDENTIAL`;
CREATE TABLE `CREDENTIAL`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `SALT` tinyblob,
  `TYPE` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `USER_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `CREATED_DATE` bigint(20) DEFAULT NULL,
  `USER_LABEL` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `SECRET_DATA` longtext CHARACTER SET latin1 COLLATE latin1_swedish_ci,
  `CREDENTIAL_DATA` longtext CHARACTER SET latin1 COLLATE latin1_swedish_ci,
  `PRIORITY` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `IDX_USER_CREDENTIAL`(`USER_ID`) USING BTREE,
  CONSTRAINT `FK_PFYR0GLASQYL0DEI3KL69R6V0` FOREIGN KEY (`USER_ID`) REFERENCES `USER_ENTITY` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of CREDENTIAL
-- ----------------------------
INSERT INTO `CREDENTIAL` VALUES ('ab4f0a41-9069-4c8d-9a78-279f644dfadb', NULL, 'password', '9854c0b7-3d91-4a7e-9e74-c2f7438081ae', 1606724092238, NULL, '{\"value\":\"5M07Y2ySo6eIaY/AqEykCM7aEcd9g4rYBpNcdtSyGYBDF8SFbl9E23dh10MwIUwtOlBVdr9S6Vsg/Z+jNDQ1nw==\",\"salt\":\"QQv9wdUf/A6vWW5/WJxYGQ==\"}', '{\"hashIterations\":27500,\"algorithm\":\"pbkdf2-sha256\"}', 10);
INSERT INTO `CREDENTIAL` VALUES ('c08bfd12-494c-468c-b381-68a4d2e3d1c9', NULL, 'password', 'bf1fccf1-0a50-4f64-aa43-0f71b8a16c0c', 1604626608755, NULL, '{\"value\":\"96IOlf2LlnQMQwK1ZuvYoNN6Xa+coygi42jNeq1MoCUIELsLQTEhnnk+zfVi7iFgAM/uUe7uBbgXNz4bV1ZF5g==\",\"salt\":\"lMSETavcoD22jh237+MEEA==\"}', '{\"hashIterations\":27500,\"algorithm\":\"pbkdf2-sha256\"}', 10);

-- ----------------------------
-- Table structure for DATABASECHANGELOG
-- ----------------------------
DROP TABLE IF EXISTS `DATABASECHANGELOG`;
CREATE TABLE `DATABASECHANGELOG`  (
  `ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `AUTHOR` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `FILENAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `DATEEXECUTED` datetime(0) NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `MD5SUM` varchar(35) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `COMMENTS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `TAG` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `LIQUIBASE` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `CONTEXTS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `LABELS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of DATABASECHANGELOG
-- ----------------------------
INSERT INTO `DATABASECHANGELOG` VALUES ('1.0.0.Final-KEYCLOAK-5461', 'sthorger@redhat.com', 'META-INF/jpa-changelog-1.0.0.Final.xml', '2020-11-06 01:36:36', 1, 'EXECUTED', '7:4e70412f24a3f382c82183742ec79317', 'createTable tableName=APPLICATION_DEFAULT_ROLES; createTable tableName=CLIENT; createTable tableName=CLIENT_SESSION; createTable tableName=CLIENT_SESSION_ROLE; createTable tableName=COMPOSITE_ROLE; createTable tableName=CREDENTIAL; createTable tab...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('1.0.0.Final-KEYCLOAK-5461', 'sthorger@redhat.com', 'META-INF/db2-jpa-changelog-1.0.0.Final.xml', '2020-11-06 01:36:36', 2, 'MARK_RAN', '7:cb16724583e9675711801c6875114f28', 'createTable tableName=APPLICATION_DEFAULT_ROLES; createTable tableName=CLIENT; createTable tableName=CLIENT_SESSION; createTable tableName=CLIENT_SESSION_ROLE; createTable tableName=COMPOSITE_ROLE; createTable tableName=CREDENTIAL; createTable tab...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('1.1.0.Beta1', 'sthorger@redhat.com', 'META-INF/jpa-changelog-1.1.0.Beta1.xml', '2020-11-06 01:36:36', 3, 'EXECUTED', '7:0310eb8ba07cec616460794d42ade0fa', 'delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION; createTable tableName=CLIENT_ATTRIBUTES; createTable tableName=CLIENT_SESSION_NOTE; createTable tableName=APP_NODE_REGISTRATIONS; addColumn table...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('1.1.0.Final', 'sthorger@redhat.com', 'META-INF/jpa-changelog-1.1.0.Final.xml', '2020-11-06 01:36:36', 4, 'EXECUTED', '7:5d25857e708c3233ef4439df1f93f012', 'renameColumn newColumnName=EVENT_TIME, oldColumnName=TIME, tableName=EVENT_ENTITY', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('1.2.0.Beta1', 'psilva@redhat.com', 'META-INF/jpa-changelog-1.2.0.Beta1.xml', '2020-11-06 01:36:37', 5, 'EXECUTED', '7:c7a54a1041d58eb3817a4a883b4d4e84', 'delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION; createTable tableName=PROTOCOL_MAPPER; createTable tableName=PROTOCOL_MAPPER_CONFIG; createTable tableName=...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('1.2.0.Beta1', 'psilva@redhat.com', 'META-INF/db2-jpa-changelog-1.2.0.Beta1.xml', '2020-11-06 01:36:37', 6, 'MARK_RAN', '7:2e01012df20974c1c2a605ef8afe25b7', 'delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION; createTable tableName=PROTOCOL_MAPPER; createTable tableName=PROTOCOL_MAPPER_CONFIG; createTable tableName=...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('1.2.0.RC1', 'bburke@redhat.com', 'META-INF/jpa-changelog-1.2.0.CR1.xml', '2020-11-06 01:36:37', 7, 'EXECUTED', '7:0f08df48468428e0f30ee59a8ec01a41', 'delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete tableName=USER_SESSION; createTable tableName=MIGRATION_MODEL; createTable tableName=IDENTITY_P...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('1.2.0.RC1', 'bburke@redhat.com', 'META-INF/db2-jpa-changelog-1.2.0.CR1.xml', '2020-11-06 01:36:37', 8, 'MARK_RAN', '7:a77ea2ad226b345e7d689d366f185c8c', 'delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete tableName=USER_SESSION; createTable tableName=MIGRATION_MODEL; createTable tableName=IDENTITY_P...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('1.2.0.Final', 'keycloak', 'META-INF/jpa-changelog-1.2.0.Final.xml', '2020-11-06 01:36:37', 9, 'EXECUTED', '7:a3377a2059aefbf3b90ebb4c4cc8e2ab', 'update tableName=CLIENT; update tableName=CLIENT; update tableName=CLIENT', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('1.3.0', 'bburke@redhat.com', 'META-INF/jpa-changelog-1.3.0.xml', '2020-11-06 01:36:37', 10, 'EXECUTED', '7:04c1dbedc2aa3e9756d1a1668e003451', 'delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_PROT_MAPPER; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete tableName=USER_SESSION; createTable tableName=ADMI...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('1.4.0', 'bburke@redhat.com', 'META-INF/jpa-changelog-1.4.0.xml', '2020-11-06 01:36:37', 11, 'EXECUTED', '7:36ef39ed560ad07062d956db861042ba', 'delete tableName=CLIENT_SESSION_AUTH_STATUS; delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_PROT_MAPPER; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete table...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('1.4.0', 'bburke@redhat.com', 'META-INF/db2-jpa-changelog-1.4.0.xml', '2020-11-06 01:36:37', 12, 'MARK_RAN', '7:d909180b2530479a716d3f9c9eaea3d7', 'delete tableName=CLIENT_SESSION_AUTH_STATUS; delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_PROT_MAPPER; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete table...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('1.5.0', 'bburke@redhat.com', 'META-INF/jpa-changelog-1.5.0.xml', '2020-11-06 01:36:37', 13, 'EXECUTED', '7:cf12b04b79bea5152f165eb41f3955f6', 'delete tableName=CLIENT_SESSION_AUTH_STATUS; delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_PROT_MAPPER; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete table...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('1.6.1_from15', 'mposolda@redhat.com', 'META-INF/jpa-changelog-1.6.1.xml', '2020-11-06 01:36:38', 14, 'EXECUTED', '7:7e32c8f05c755e8675764e7d5f514509', 'addColumn tableName=REALM; addColumn tableName=KEYCLOAK_ROLE; addColumn tableName=CLIENT; createTable tableName=OFFLINE_USER_SESSION; createTable tableName=OFFLINE_CLIENT_SESSION; addPrimaryKey constraintName=CONSTRAINT_OFFL_US_SES_PK2, tableName=...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('1.6.1_from16-pre', 'mposolda@redhat.com', 'META-INF/jpa-changelog-1.6.1.xml', '2020-11-06 01:36:38', 15, 'MARK_RAN', '7:980ba23cc0ec39cab731ce903dd01291', 'delete tableName=OFFLINE_CLIENT_SESSION; delete tableName=OFFLINE_USER_SESSION', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('1.6.1_from16', 'mposolda@redhat.com', 'META-INF/jpa-changelog-1.6.1.xml', '2020-11-06 01:36:38', 16, 'MARK_RAN', '7:2fa220758991285312eb84f3b4ff5336', 'dropPrimaryKey constraintName=CONSTRAINT_OFFLINE_US_SES_PK, tableName=OFFLINE_USER_SESSION; dropPrimaryKey constraintName=CONSTRAINT_OFFLINE_CL_SES_PK, tableName=OFFLINE_CLIENT_SESSION; addColumn tableName=OFFLINE_USER_SESSION; update tableName=OF...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('1.6.1', 'mposolda@redhat.com', 'META-INF/jpa-changelog-1.6.1.xml', '2020-11-06 01:36:38', 17, 'EXECUTED', '7:d41d8cd98f00b204e9800998ecf8427e', 'empty', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('1.7.0', 'bburke@redhat.com', 'META-INF/jpa-changelog-1.7.0.xml', '2020-11-06 01:36:38', 18, 'EXECUTED', '7:91ace540896df890cc00a0490ee52bbc', 'createTable tableName=KEYCLOAK_GROUP; createTable tableName=GROUP_ROLE_MAPPING; createTable tableName=GROUP_ATTRIBUTE; createTable tableName=USER_GROUP_MEMBERSHIP; createTable tableName=REALM_DEFAULT_GROUPS; addColumn tableName=IDENTITY_PROVIDER; ...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('1.8.0', 'mposolda@redhat.com', 'META-INF/jpa-changelog-1.8.0.xml', '2020-11-06 01:36:38', 19, 'EXECUTED', '7:c31d1646dfa2618a9335c00e07f89f24', 'addColumn tableName=IDENTITY_PROVIDER; createTable tableName=CLIENT_TEMPLATE; createTable tableName=CLIENT_TEMPLATE_ATTRIBUTES; createTable tableName=TEMPLATE_SCOPE_MAPPING; dropNotNullConstraint columnName=CLIENT_ID, tableName=PROTOCOL_MAPPER; ad...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('1.8.0-2', 'keycloak', 'META-INF/jpa-changelog-1.8.0.xml', '2020-11-06 01:36:38', 20, 'EXECUTED', '7:df8bc21027a4f7cbbb01f6344e89ce07', 'dropDefaultValue columnName=ALGORITHM, tableName=CREDENTIAL; update tableName=CREDENTIAL', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('1.8.0', 'mposolda@redhat.com', 'META-INF/db2-jpa-changelog-1.8.0.xml', '2020-11-06 01:36:38', 21, 'MARK_RAN', '7:f987971fe6b37d963bc95fee2b27f8df', 'addColumn tableName=IDENTITY_PROVIDER; createTable tableName=CLIENT_TEMPLATE; createTable tableName=CLIENT_TEMPLATE_ATTRIBUTES; createTable tableName=TEMPLATE_SCOPE_MAPPING; dropNotNullConstraint columnName=CLIENT_ID, tableName=PROTOCOL_MAPPER; ad...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('1.8.0-2', 'keycloak', 'META-INF/db2-jpa-changelog-1.8.0.xml', '2020-11-06 01:36:38', 22, 'MARK_RAN', '7:df8bc21027a4f7cbbb01f6344e89ce07', 'dropDefaultValue columnName=ALGORITHM, tableName=CREDENTIAL; update tableName=CREDENTIAL', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('1.9.0', 'mposolda@redhat.com', 'META-INF/jpa-changelog-1.9.0.xml', '2020-11-06 01:36:38', 23, 'EXECUTED', '7:ed2dc7f799d19ac452cbcda56c929e47', 'update tableName=REALM; update tableName=REALM; update tableName=REALM; update tableName=REALM; update tableName=CREDENTIAL; update tableName=CREDENTIAL; update tableName=CREDENTIAL; update tableName=REALM; update tableName=REALM; customChange; dr...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('1.9.1', 'keycloak', 'META-INF/jpa-changelog-1.9.1.xml', '2020-11-06 01:36:38', 24, 'EXECUTED', '7:80b5db88a5dda36ece5f235be8757615', 'modifyDataType columnName=PRIVATE_KEY, tableName=REALM; modifyDataType columnName=PUBLIC_KEY, tableName=REALM; modifyDataType columnName=CERTIFICATE, tableName=REALM', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('1.9.1', 'keycloak', 'META-INF/db2-jpa-changelog-1.9.1.xml', '2020-11-06 01:36:38', 25, 'MARK_RAN', '7:1437310ed1305a9b93f8848f301726ce', 'modifyDataType columnName=PRIVATE_KEY, tableName=REALM; modifyDataType columnName=CERTIFICATE, tableName=REALM', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('1.9.2', 'keycloak', 'META-INF/jpa-changelog-1.9.2.xml', '2020-11-06 01:36:38', 26, 'EXECUTED', '7:b82ffb34850fa0836be16deefc6a87c4', 'createIndex indexName=IDX_USER_EMAIL, tableName=USER_ENTITY; createIndex indexName=IDX_USER_ROLE_MAPPING, tableName=USER_ROLE_MAPPING; createIndex indexName=IDX_USER_GROUP_MAPPING, tableName=USER_GROUP_MEMBERSHIP; createIndex indexName=IDX_USER_CO...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('authz-2.0.0', 'psilva@redhat.com', 'META-INF/jpa-changelog-authz-2.0.0.xml', '2020-11-06 01:36:38', 27, 'EXECUTED', '7:9cc98082921330d8d9266decdd4bd658', 'createTable tableName=RESOURCE_SERVER; addPrimaryKey constraintName=CONSTRAINT_FARS, tableName=RESOURCE_SERVER; addUniqueConstraint constraintName=UK_AU8TT6T700S9V50BU18WS5HA6, tableName=RESOURCE_SERVER; createTable tableName=RESOURCE_SERVER_RESOU...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('authz-2.5.1', 'psilva@redhat.com', 'META-INF/jpa-changelog-authz-2.5.1.xml', '2020-11-06 01:36:38', 28, 'EXECUTED', '7:03d64aeed9cb52b969bd30a7ac0db57e', 'update tableName=RESOURCE_SERVER_POLICY', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('2.1.0-KEYCLOAK-5461', 'bburke@redhat.com', 'META-INF/jpa-changelog-2.1.0.xml', '2020-11-06 01:36:38', 29, 'EXECUTED', '7:f1f9fd8710399d725b780f463c6b21cd', 'createTable tableName=BROKER_LINK; createTable tableName=FED_USER_ATTRIBUTE; createTable tableName=FED_USER_CONSENT; createTable tableName=FED_USER_CONSENT_ROLE; createTable tableName=FED_USER_CONSENT_PROT_MAPPER; createTable tableName=FED_USER_CR...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('2.2.0', 'bburke@redhat.com', 'META-INF/jpa-changelog-2.2.0.xml', '2020-11-06 01:36:38', 30, 'EXECUTED', '7:53188c3eb1107546e6f765835705b6c1', 'addColumn tableName=ADMIN_EVENT_ENTITY; createTable tableName=CREDENTIAL_ATTRIBUTE; createTable tableName=FED_CREDENTIAL_ATTRIBUTE; modifyDataType columnName=VALUE, tableName=CREDENTIAL; addForeignKeyConstraint baseTableName=FED_CREDENTIAL_ATTRIBU...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('2.3.0', 'bburke@redhat.com', 'META-INF/jpa-changelog-2.3.0.xml', '2020-11-06 01:36:38', 31, 'EXECUTED', '7:d6e6f3bc57a0c5586737d1351725d4d4', 'createTable tableName=FEDERATED_USER; addPrimaryKey constraintName=CONSTR_FEDERATED_USER, tableName=FEDERATED_USER; dropDefaultValue columnName=TOTP, tableName=USER_ENTITY; dropColumn columnName=TOTP, tableName=USER_ENTITY; addColumn tableName=IDE...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('2.4.0', 'bburke@redhat.com', 'META-INF/jpa-changelog-2.4.0.xml', '2020-11-06 01:36:38', 32, 'EXECUTED', '7:454d604fbd755d9df3fd9c6329043aa5', 'customChange', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('2.5.0', 'bburke@redhat.com', 'META-INF/jpa-changelog-2.5.0.xml', '2020-11-06 01:36:38', 33, 'EXECUTED', '7:57e98a3077e29caf562f7dbf80c72600', 'customChange; modifyDataType columnName=USER_ID, tableName=OFFLINE_USER_SESSION', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('2.5.0-unicode-oracle', 'hmlnarik@redhat.com', 'META-INF/jpa-changelog-2.5.0.xml', '2020-11-06 01:36:38', 34, 'MARK_RAN', '7:e4c7e8f2256210aee71ddc42f538b57a', 'modifyDataType columnName=DESCRIPTION, tableName=AUTHENTICATION_FLOW; modifyDataType columnName=DESCRIPTION, tableName=CLIENT_TEMPLATE; modifyDataType columnName=DESCRIPTION, tableName=RESOURCE_SERVER_POLICY; modifyDataType columnName=DESCRIPTION,...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('2.5.0-unicode-other-dbs', 'hmlnarik@redhat.com', 'META-INF/jpa-changelog-2.5.0.xml', '2020-11-06 01:36:38', 35, 'EXECUTED', '7:09a43c97e49bc626460480aa1379b522', 'modifyDataType columnName=DESCRIPTION, tableName=AUTHENTICATION_FLOW; modifyDataType columnName=DESCRIPTION, tableName=CLIENT_TEMPLATE; modifyDataType columnName=DESCRIPTION, tableName=RESOURCE_SERVER_POLICY; modifyDataType columnName=DESCRIPTION,...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('2.5.0-duplicate-email-support', 'slawomir@dabek.name', 'META-INF/jpa-changelog-2.5.0.xml', '2020-11-06 01:36:38', 36, 'EXECUTED', '7:26bfc7c74fefa9126f2ce702fb775553', 'addColumn tableName=REALM', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('2.5.0-unique-group-names', 'hmlnarik@redhat.com', 'META-INF/jpa-changelog-2.5.0.xml', '2020-11-06 01:36:38', 37, 'EXECUTED', '7:a161e2ae671a9020fff61e996a207377', 'addUniqueConstraint constraintName=SIBLING_NAMES, tableName=KEYCLOAK_GROUP', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('2.5.1', 'bburke@redhat.com', 'META-INF/jpa-changelog-2.5.1.xml', '2020-11-06 01:36:38', 38, 'EXECUTED', '7:37fc1781855ac5388c494f1442b3f717', 'addColumn tableName=FED_USER_CONSENT', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('3.0.0', 'bburke@redhat.com', 'META-INF/jpa-changelog-3.0.0.xml', '2020-11-06 01:36:38', 39, 'EXECUTED', '7:13a27db0dae6049541136adad7261d27', 'addColumn tableName=IDENTITY_PROVIDER', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('3.2.0-fix', 'keycloak', 'META-INF/jpa-changelog-3.2.0.xml', '2020-11-06 01:36:38', 40, 'MARK_RAN', '7:550300617e3b59e8af3a6294df8248a3', 'addNotNullConstraint columnName=REALM_ID, tableName=CLIENT_INITIAL_ACCESS', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('3.2.0-fix-with-keycloak-5416', 'keycloak', 'META-INF/jpa-changelog-3.2.0.xml', '2020-11-06 01:36:38', 41, 'MARK_RAN', '7:e3a9482b8931481dc2772a5c07c44f17', 'dropIndex indexName=IDX_CLIENT_INIT_ACC_REALM, tableName=CLIENT_INITIAL_ACCESS; addNotNullConstraint columnName=REALM_ID, tableName=CLIENT_INITIAL_ACCESS; createIndex indexName=IDX_CLIENT_INIT_ACC_REALM, tableName=CLIENT_INITIAL_ACCESS', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('3.2.0-fix-offline-sessions', 'hmlnarik', 'META-INF/jpa-changelog-3.2.0.xml', '2020-11-06 01:36:39', 42, 'EXECUTED', '7:72b07d85a2677cb257edb02b408f332d', 'customChange', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('3.2.0-fixed', 'keycloak', 'META-INF/jpa-changelog-3.2.0.xml', '2020-11-06 01:36:39', 43, 'EXECUTED', '7:a72a7858967bd414835d19e04d880312', 'addColumn tableName=REALM; dropPrimaryKey constraintName=CONSTRAINT_OFFL_CL_SES_PK2, tableName=OFFLINE_CLIENT_SESSION; dropColumn columnName=CLIENT_SESSION_ID, tableName=OFFLINE_CLIENT_SESSION; addPrimaryKey constraintName=CONSTRAINT_OFFL_CL_SES_P...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('3.3.0', 'keycloak', 'META-INF/jpa-changelog-3.3.0.xml', '2020-11-06 01:36:39', 44, 'EXECUTED', '7:94edff7cf9ce179e7e85f0cd78a3cf2c', 'addColumn tableName=USER_ENTITY', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('authz-3.4.0.CR1-resource-server-pk-change-part1', 'glavoie@gmail.com', 'META-INF/jpa-changelog-authz-3.4.0.CR1.xml', '2020-11-06 01:36:39', 45, 'EXECUTED', '7:6a48ce645a3525488a90fbf76adf3bb3', 'addColumn tableName=RESOURCE_SERVER_POLICY; addColumn tableName=RESOURCE_SERVER_RESOURCE; addColumn tableName=RESOURCE_SERVER_SCOPE', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('authz-3.4.0.CR1-resource-server-pk-change-part2-KEYCLOAK-6095', 'hmlnarik@redhat.com', 'META-INF/jpa-changelog-authz-3.4.0.CR1.xml', '2020-11-06 01:36:39', 46, 'EXECUTED', '7:e64b5dcea7db06077c6e57d3b9e5ca14', 'customChange', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('authz-3.4.0.CR1-resource-server-pk-change-part3-fixed', 'glavoie@gmail.com', 'META-INF/jpa-changelog-authz-3.4.0.CR1.xml', '2020-11-06 01:36:39', 47, 'MARK_RAN', '7:fd8cf02498f8b1e72496a20afc75178c', 'dropIndex indexName=IDX_RES_SERV_POL_RES_SERV, tableName=RESOURCE_SERVER_POLICY; dropIndex indexName=IDX_RES_SRV_RES_RES_SRV, tableName=RESOURCE_SERVER_RESOURCE; dropIndex indexName=IDX_RES_SRV_SCOPE_RES_SRV, tableName=RESOURCE_SERVER_SCOPE', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('authz-3.4.0.CR1-resource-server-pk-change-part3-fixed-nodropindex', 'glavoie@gmail.com', 'META-INF/jpa-changelog-authz-3.4.0.CR1.xml', '2020-11-06 01:36:39', 48, 'EXECUTED', '7:542794f25aa2b1fbabb7e577d6646319', 'addNotNullConstraint columnName=RESOURCE_SERVER_CLIENT_ID, tableName=RESOURCE_SERVER_POLICY; addNotNullConstraint columnName=RESOURCE_SERVER_CLIENT_ID, tableName=RESOURCE_SERVER_RESOURCE; addNotNullConstraint columnName=RESOURCE_SERVER_CLIENT_ID, ...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('authn-3.4.0.CR1-refresh-token-max-reuse', 'glavoie@gmail.com', 'META-INF/jpa-changelog-authz-3.4.0.CR1.xml', '2020-11-06 01:36:39', 49, 'EXECUTED', '7:edad604c882df12f74941dac3cc6d650', 'addColumn tableName=REALM', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('3.4.0', 'keycloak', 'META-INF/jpa-changelog-3.4.0.xml', '2020-11-06 01:36:39', 50, 'EXECUTED', '7:0f88b78b7b46480eb92690cbf5e44900', 'addPrimaryKey constraintName=CONSTRAINT_REALM_DEFAULT_ROLES, tableName=REALM_DEFAULT_ROLES; addPrimaryKey constraintName=CONSTRAINT_COMPOSITE_ROLE, tableName=COMPOSITE_ROLE; addPrimaryKey constraintName=CONSTR_REALM_DEFAULT_GROUPS, tableName=REALM...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('3.4.0-KEYCLOAK-5230', 'hmlnarik@redhat.com', 'META-INF/jpa-changelog-3.4.0.xml', '2020-11-06 01:36:39', 51, 'EXECUTED', '7:d560e43982611d936457c327f872dd59', 'createIndex indexName=IDX_FU_ATTRIBUTE, tableName=FED_USER_ATTRIBUTE; createIndex indexName=IDX_FU_CONSENT, tableName=FED_USER_CONSENT; createIndex indexName=IDX_FU_CONSENT_RU, tableName=FED_USER_CONSENT; createIndex indexName=IDX_FU_CREDENTIAL, t...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('3.4.1', 'psilva@redhat.com', 'META-INF/jpa-changelog-3.4.1.xml', '2020-11-06 01:36:39', 52, 'EXECUTED', '7:c155566c42b4d14ef07059ec3b3bbd8e', 'modifyDataType columnName=VALUE, tableName=CLIENT_ATTRIBUTES', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('3.4.2', 'keycloak', 'META-INF/jpa-changelog-3.4.2.xml', '2020-11-06 01:36:39', 53, 'EXECUTED', '7:b40376581f12d70f3c89ba8ddf5b7dea', 'update tableName=REALM', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('3.4.2-KEYCLOAK-5172', 'mkanis@redhat.com', 'META-INF/jpa-changelog-3.4.2.xml', '2020-11-06 01:36:39', 54, 'EXECUTED', '7:a1132cc395f7b95b3646146c2e38f168', 'update tableName=CLIENT', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('4.0.0-KEYCLOAK-6335', 'bburke@redhat.com', 'META-INF/jpa-changelog-4.0.0.xml', '2020-11-06 01:36:39', 55, 'EXECUTED', '7:d8dc5d89c789105cfa7ca0e82cba60af', 'createTable tableName=CLIENT_AUTH_FLOW_BINDINGS; addPrimaryKey constraintName=C_CLI_FLOW_BIND, tableName=CLIENT_AUTH_FLOW_BINDINGS', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('4.0.0-CLEANUP-UNUSED-TABLE', 'bburke@redhat.com', 'META-INF/jpa-changelog-4.0.0.xml', '2020-11-06 01:36:39', 56, 'EXECUTED', '7:7822e0165097182e8f653c35517656a3', 'dropTable tableName=CLIENT_IDENTITY_PROV_MAPPING', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('4.0.0-KEYCLOAK-6228', 'bburke@redhat.com', 'META-INF/jpa-changelog-4.0.0.xml', '2020-11-06 01:36:39', 57, 'EXECUTED', '7:c6538c29b9c9a08f9e9ea2de5c2b6375', 'dropUniqueConstraint constraintName=UK_JKUWUVD56ONTGSUHOGM8UEWRT, tableName=USER_CONSENT; dropNotNullConstraint columnName=CLIENT_ID, tableName=USER_CONSENT; addColumn tableName=USER_CONSENT; addUniqueConstraint constraintName=UK_JKUWUVD56ONTGSUHO...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('4.0.0-KEYCLOAK-5579-fixed', 'mposolda@redhat.com', 'META-INF/jpa-changelog-4.0.0.xml', '2020-11-06 01:36:39', 58, 'EXECUTED', '7:6d4893e36de22369cf73bcb051ded875', 'dropForeignKeyConstraint baseTableName=CLIENT_TEMPLATE_ATTRIBUTES, constraintName=FK_CL_TEMPL_ATTR_TEMPL; renameTable newTableName=CLIENT_SCOPE_ATTRIBUTES, oldTableName=CLIENT_TEMPLATE_ATTRIBUTES; renameColumn newColumnName=SCOPE_ID, oldColumnName...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('authz-4.0.0.CR1', 'psilva@redhat.com', 'META-INF/jpa-changelog-authz-4.0.0.CR1.xml', '2020-11-06 01:36:39', 59, 'EXECUTED', '7:57960fc0b0f0dd0563ea6f8b2e4a1707', 'createTable tableName=RESOURCE_SERVER_PERM_TICKET; addPrimaryKey constraintName=CONSTRAINT_FAPMT, tableName=RESOURCE_SERVER_PERM_TICKET; addForeignKeyConstraint baseTableName=RESOURCE_SERVER_PERM_TICKET, constraintName=FK_FRSRHO213XCX4WNKOG82SSPMT...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('authz-4.0.0.Beta3', 'psilva@redhat.com', 'META-INF/jpa-changelog-authz-4.0.0.Beta3.xml', '2020-11-06 01:36:40', 60, 'EXECUTED', '7:2b4b8bff39944c7097977cc18dbceb3b', 'addColumn tableName=RESOURCE_SERVER_POLICY; addColumn tableName=RESOURCE_SERVER_PERM_TICKET; addForeignKeyConstraint baseTableName=RESOURCE_SERVER_PERM_TICKET, constraintName=FK_FRSRPO2128CX4WNKOG82SSRFY, referencedTableName=RESOURCE_SERVER_POLICY', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('authz-4.2.0.Final', 'mhajas@redhat.com', 'META-INF/jpa-changelog-authz-4.2.0.Final.xml', '2020-11-06 01:36:40', 61, 'EXECUTED', '7:2aa42a964c59cd5b8ca9822340ba33a8', 'createTable tableName=RESOURCE_URIS; addForeignKeyConstraint baseTableName=RESOURCE_URIS, constraintName=FK_RESOURCE_SERVER_URIS, referencedTableName=RESOURCE_SERVER_RESOURCE; customChange; dropColumn columnName=URI, tableName=RESOURCE_SERVER_RESO...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('authz-4.2.0.Final-KEYCLOAK-9944', 'hmlnarik@redhat.com', 'META-INF/jpa-changelog-authz-4.2.0.Final.xml', '2020-11-06 01:36:40', 62, 'EXECUTED', '7:9ac9e58545479929ba23f4a3087a0346', 'addPrimaryKey constraintName=CONSTRAINT_RESOUR_URIS_PK, tableName=RESOURCE_URIS', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('4.2.0-KEYCLOAK-6313', 'wadahiro@gmail.com', 'META-INF/jpa-changelog-4.2.0.xml', '2020-11-06 01:36:40', 63, 'EXECUTED', '7:14d407c35bc4fe1976867756bcea0c36', 'addColumn tableName=REQUIRED_ACTION_PROVIDER', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('4.3.0-KEYCLOAK-7984', 'wadahiro@gmail.com', 'META-INF/jpa-changelog-4.3.0.xml', '2020-11-06 01:36:40', 64, 'EXECUTED', '7:241a8030c748c8548e346adee548fa93', 'update tableName=REQUIRED_ACTION_PROVIDER', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('4.6.0-KEYCLOAK-7950', 'psilva@redhat.com', 'META-INF/jpa-changelog-4.6.0.xml', '2020-11-06 01:36:40', 65, 'EXECUTED', '7:7d3182f65a34fcc61e8d23def037dc3f', 'update tableName=RESOURCE_SERVER_RESOURCE', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('4.6.0-KEYCLOAK-8377', 'keycloak', 'META-INF/jpa-changelog-4.6.0.xml', '2020-11-06 01:36:40', 66, 'EXECUTED', '7:b30039e00a0b9715d430d1b0636728fa', 'createTable tableName=ROLE_ATTRIBUTE; addPrimaryKey constraintName=CONSTRAINT_ROLE_ATTRIBUTE_PK, tableName=ROLE_ATTRIBUTE; addForeignKeyConstraint baseTableName=ROLE_ATTRIBUTE, constraintName=FK_ROLE_ATTRIBUTE_ID, referencedTableName=KEYCLOAK_ROLE...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('4.6.0-KEYCLOAK-8555', 'gideonray@gmail.com', 'META-INF/jpa-changelog-4.6.0.xml', '2020-11-06 01:36:40', 67, 'EXECUTED', '7:3797315ca61d531780f8e6f82f258159', 'createIndex indexName=IDX_COMPONENT_PROVIDER_TYPE, tableName=COMPONENT', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('4.7.0-KEYCLOAK-1267', 'sguilhen@redhat.com', 'META-INF/jpa-changelog-4.7.0.xml', '2020-11-06 01:36:40', 68, 'EXECUTED', '7:c7aa4c8d9573500c2d347c1941ff0301', 'addColumn tableName=REALM', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('4.7.0-KEYCLOAK-7275', 'keycloak', 'META-INF/jpa-changelog-4.7.0.xml', '2020-11-06 01:36:40', 69, 'EXECUTED', '7:b207faee394fc074a442ecd42185a5dd', 'renameColumn newColumnName=CREATED_ON, oldColumnName=LAST_SESSION_REFRESH, tableName=OFFLINE_USER_SESSION; addNotNullConstraint columnName=CREATED_ON, tableName=OFFLINE_USER_SESSION; addColumn tableName=OFFLINE_USER_SESSION; customChange; createIn...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('4.8.0-KEYCLOAK-8835', 'sguilhen@redhat.com', 'META-INF/jpa-changelog-4.8.0.xml', '2020-11-06 01:36:40', 70, 'EXECUTED', '7:ab9a9762faaba4ddfa35514b212c4922', 'addNotNullConstraint columnName=SSO_MAX_LIFESPAN_REMEMBER_ME, tableName=REALM; addNotNullConstraint columnName=SSO_IDLE_TIMEOUT_REMEMBER_ME, tableName=REALM', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('authz-7.0.0-KEYCLOAK-10443', 'psilva@redhat.com', 'META-INF/jpa-changelog-authz-7.0.0.xml', '2020-11-06 01:36:40', 71, 'EXECUTED', '7:b9710f74515a6ccb51b72dc0d19df8c4', 'addColumn tableName=RESOURCE_SERVER', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('8.0.0-adding-credential-columns', 'keycloak', 'META-INF/jpa-changelog-8.0.0.xml', '2020-11-06 01:36:40', 72, 'EXECUTED', '7:ec9707ae4d4f0b7452fee20128083879', 'addColumn tableName=CREDENTIAL; addColumn tableName=FED_USER_CREDENTIAL', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('8.0.0-updating-credential-data-not-oracle', 'keycloak', 'META-INF/jpa-changelog-8.0.0.xml', '2020-11-06 01:36:40', 73, 'EXECUTED', '7:03b3f4b264c3c68ba082250a80b74216', 'update tableName=CREDENTIAL; update tableName=CREDENTIAL; update tableName=CREDENTIAL; update tableName=FED_USER_CREDENTIAL; update tableName=FED_USER_CREDENTIAL; update tableName=FED_USER_CREDENTIAL', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('8.0.0-updating-credential-data-oracle', 'keycloak', 'META-INF/jpa-changelog-8.0.0.xml', '2020-11-06 01:36:40', 74, 'MARK_RAN', '7:64c5728f5ca1f5aa4392217701c4fe23', 'update tableName=CREDENTIAL; update tableName=CREDENTIAL; update tableName=CREDENTIAL; update tableName=FED_USER_CREDENTIAL; update tableName=FED_USER_CREDENTIAL; update tableName=FED_USER_CREDENTIAL', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('8.0.0-credential-cleanup-fixed', 'keycloak', 'META-INF/jpa-changelog-8.0.0.xml', '2020-11-06 01:36:40', 75, 'EXECUTED', '7:b48da8c11a3d83ddd6b7d0c8c2219345', 'dropDefaultValue columnName=COUNTER, tableName=CREDENTIAL; dropDefaultValue columnName=DIGITS, tableName=CREDENTIAL; dropDefaultValue columnName=PERIOD, tableName=CREDENTIAL; dropDefaultValue columnName=ALGORITHM, tableName=CREDENTIAL; dropColumn ...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('8.0.0-resource-tag-support', 'keycloak', 'META-INF/jpa-changelog-8.0.0.xml', '2020-11-06 01:36:40', 76, 'EXECUTED', '7:a73379915c23bfad3e8f5c6d5c0aa4bd', 'addColumn tableName=MIGRATION_MODEL; createIndex indexName=IDX_UPDATE_TIME, tableName=MIGRATION_MODEL', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('9.0.0-always-display-client', 'keycloak', 'META-INF/jpa-changelog-9.0.0.xml', '2020-11-06 01:36:40', 77, 'EXECUTED', '7:39e0073779aba192646291aa2332493d', 'addColumn tableName=CLIENT', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('9.0.0-drop-constraints-for-column-increase', 'keycloak', 'META-INF/jpa-changelog-9.0.0.xml', '2020-11-06 01:36:40', 78, 'MARK_RAN', '7:81f87368f00450799b4bf42ea0b3ec34', 'dropUniqueConstraint constraintName=UK_FRSR6T700S9V50BU18WS5PMT, tableName=RESOURCE_SERVER_PERM_TICKET; dropUniqueConstraint constraintName=UK_FRSR6T700S9V50BU18WS5HA6, tableName=RESOURCE_SERVER_RESOURCE; dropPrimaryKey constraintName=CONSTRAINT_O...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('9.0.0-increase-column-size-federated-fk', 'keycloak', 'META-INF/jpa-changelog-9.0.0.xml', '2020-11-06 01:36:40', 79, 'EXECUTED', '7:20b37422abb9fb6571c618148f013a15', 'modifyDataType columnName=CLIENT_ID, tableName=FED_USER_CONSENT; modifyDataType columnName=CLIENT_REALM_CONSTRAINT, tableName=KEYCLOAK_ROLE; modifyDataType columnName=OWNER, tableName=RESOURCE_SERVER_POLICY; modifyDataType columnName=CLIENT_ID, ta...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('9.0.0-recreate-constraints-after-column-increase', 'keycloak', 'META-INF/jpa-changelog-9.0.0.xml', '2020-11-06 01:36:40', 80, 'MARK_RAN', '7:1970bb6cfb5ee800736b95ad3fb3c78a', 'addNotNullConstraint columnName=CLIENT_ID, tableName=OFFLINE_CLIENT_SESSION; addNotNullConstraint columnName=OWNER, tableName=RESOURCE_SERVER_PERM_TICKET; addNotNullConstraint columnName=REQUESTER, tableName=RESOURCE_SERVER_PERM_TICKET; addNotNull...', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('9.0.1-add-index-to-client.client_id', 'keycloak', 'META-INF/jpa-changelog-9.0.1.xml', '2020-11-06 01:36:40', 81, 'EXECUTED', '7:45d9b25fc3b455d522d8dcc10a0f4c80', 'createIndex indexName=IDX_CLIENT_ID, tableName=CLIENT', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('9.0.1-KEYCLOAK-12579-drop-constraints', 'keycloak', 'META-INF/jpa-changelog-9.0.1.xml', '2020-11-06 01:36:40', 82, 'MARK_RAN', '7:890ae73712bc187a66c2813a724d037f', 'dropUniqueConstraint constraintName=SIBLING_NAMES, tableName=KEYCLOAK_GROUP', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('9.0.1-KEYCLOAK-12579-add-not-null-constraint', 'keycloak', 'META-INF/jpa-changelog-9.0.1.xml', '2020-11-06 01:36:40', 83, 'EXECUTED', '7:0a211980d27fafe3ff50d19a3a29b538', 'addNotNullConstraint columnName=PARENT_GROUP, tableName=KEYCLOAK_GROUP', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('9.0.1-KEYCLOAK-12579-recreate-constraints', 'keycloak', 'META-INF/jpa-changelog-9.0.1.xml', '2020-11-06 01:36:40', 84, 'MARK_RAN', '7:a161e2ae671a9020fff61e996a207377', 'addUniqueConstraint constraintName=SIBLING_NAMES, tableName=KEYCLOAK_GROUP', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('9.0.1-add-index-to-events', 'keycloak', 'META-INF/jpa-changelog-9.0.1.xml', '2020-11-06 01:36:40', 85, 'EXECUTED', '7:01c49302201bdf815b0a18d1f98a55dc', 'createIndex indexName=IDX_EVENT_TIME, tableName=EVENT_ENTITY', '', NULL, '3.5.4', NULL, NULL, '4626595450');
INSERT INTO `DATABASECHANGELOG` VALUES ('map-remove-ri', 'keycloak', 'META-INF/jpa-changelog-11.0.0.xml', '2020-11-06 01:36:40', 86, 'EXECUTED', '7:3dace6b144c11f53f1ad2c0361279b86', 'dropForeignKeyConstraint baseTableName=REALM, constraintName=FK_TRAF444KK6QRKMS7N56AIWQ5Y; dropForeignKeyConstraint baseTableName=KEYCLOAK_ROLE, constraintName=FK_KJHO5LE2C0RAL09FL8CM9WFW9', '', NULL, '3.5.4', NULL, NULL, '4626595450');

-- ----------------------------
-- Table structure for DATABASECHANGELOGLOCK
-- ----------------------------
DROP TABLE IF EXISTS `DATABASECHANGELOGLOCK`;
CREATE TABLE `DATABASECHANGELOGLOCK`  (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime(0) DEFAULT NULL,
  `LOCKEDBY` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of DATABASECHANGELOGLOCK
-- ----------------------------
INSERT INTO `DATABASECHANGELOGLOCK` VALUES (1, b'0', NULL, NULL);
INSERT INTO `DATABASECHANGELOGLOCK` VALUES (1000, b'0', NULL, NULL);
INSERT INTO `DATABASECHANGELOGLOCK` VALUES (1001, b'0', NULL, NULL);

-- ----------------------------
-- Table structure for DEFAULT_CLIENT_SCOPE
-- ----------------------------
DROP TABLE IF EXISTS `DEFAULT_CLIENT_SCOPE`;
CREATE TABLE `DEFAULT_CLIENT_SCOPE`  (
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `SCOPE_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `DEFAULT_SCOPE` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`REALM_ID`, `SCOPE_ID`) USING BTREE,
  INDEX `IDX_DEFCLS_REALM`(`REALM_ID`) USING BTREE,
  INDEX `IDX_DEFCLS_SCOPE`(`SCOPE_ID`) USING BTREE,
  CONSTRAINT `FK_R_DEF_CLI_SCOPE_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_R_DEF_CLI_SCOPE_SCOPE` FOREIGN KEY (`SCOPE_ID`) REFERENCES `CLIENT_SCOPE` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of DEFAULT_CLIENT_SCOPE
-- ----------------------------
INSERT INTO `DEFAULT_CLIENT_SCOPE` VALUES ('demo', '0e9473b7-de09-484d-82e0-d88097d14b5d', b'1');
INSERT INTO `DEFAULT_CLIENT_SCOPE` VALUES ('demo', '4c2b4fcb-5f1e-4c78-a341-fd27dfab38e0', b'1');
INSERT INTO `DEFAULT_CLIENT_SCOPE` VALUES ('demo', '60240200-c0bd-4105-9f83-6dfb5d862eea', b'1');
INSERT INTO `DEFAULT_CLIENT_SCOPE` VALUES ('demo', '7e69eefb-5f29-45cb-b249-3285b708f4e9', b'0');
INSERT INTO `DEFAULT_CLIENT_SCOPE` VALUES ('demo', 'a78bcbbd-ddfb-4c36-a43d-0b026769e79b', b'0');
INSERT INTO `DEFAULT_CLIENT_SCOPE` VALUES ('demo', 'b092e195-d133-4604-af92-27c80735ad3a', b'0');
INSERT INTO `DEFAULT_CLIENT_SCOPE` VALUES ('demo', 'ef20b41d-2b0a-4ae4-986f-84c02d5c0832', b'0');
INSERT INTO `DEFAULT_CLIENT_SCOPE` VALUES ('demo', 'f59745e4-deef-4a93-82d1-eccdd7b4eff1', b'1');
INSERT INTO `DEFAULT_CLIENT_SCOPE` VALUES ('demo', 'f82637b5-3526-4226-a37f-682ca3153857', b'1');
INSERT INTO `DEFAULT_CLIENT_SCOPE` VALUES ('master', '459e53e1-17bb-4dc9-bc04-9eea36319a60', b'1');
INSERT INTO `DEFAULT_CLIENT_SCOPE` VALUES ('master', '63a96a62-31d2-4089-92a2-97e9f43c281f', b'1');
INSERT INTO `DEFAULT_CLIENT_SCOPE` VALUES ('master', '81734f9f-7835-4af1-87f7-4625aa7ce31f', b'0');
INSERT INTO `DEFAULT_CLIENT_SCOPE` VALUES ('master', '85983e59-945c-4f81-b2b3-b2f91ce56db9', b'1');
INSERT INTO `DEFAULT_CLIENT_SCOPE` VALUES ('master', '91791326-87e9-4bfd-a5b3-b6752ef1bfc6', b'0');
INSERT INTO `DEFAULT_CLIENT_SCOPE` VALUES ('master', '948be43b-bd24-4840-ac17-8b0477b17a77', b'1');
INSERT INTO `DEFAULT_CLIENT_SCOPE` VALUES ('master', '9d697e2e-581d-4e01-92b5-09baef8c98f4', b'0');
INSERT INTO `DEFAULT_CLIENT_SCOPE` VALUES ('master', 'b4d036d9-3470-4960-a3b0-5f6365b99c8c', b'1');
INSERT INTO `DEFAULT_CLIENT_SCOPE` VALUES ('master', 'f8f75121-3841-4a0c-8428-107b6aac8cdd', b'0');

-- ----------------------------
-- Table structure for EVENT_ENTITY
-- ----------------------------
DROP TABLE IF EXISTS `EVENT_ENTITY`;
CREATE TABLE `EVENT_ENTITY`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `CLIENT_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `DETAILS_JSON` varchar(2550) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `ERROR` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `IP_ADDRESS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `REALM_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `SESSION_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `EVENT_TIME` bigint(20) DEFAULT NULL,
  `TYPE` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `USER_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `IDX_EVENT_TIME`(`REALM_ID`, `EVENT_TIME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for FEDERATED_IDENTITY
-- ----------------------------
DROP TABLE IF EXISTS `FEDERATED_IDENTITY`;
CREATE TABLE `FEDERATED_IDENTITY`  (
  `IDENTITY_PROVIDER` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `FEDERATED_USER_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `FEDERATED_USERNAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `TOKEN` text CHARACTER SET latin1 COLLATE latin1_swedish_ci,
  `USER_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`IDENTITY_PROVIDER`, `USER_ID`) USING BTREE,
  INDEX `IDX_FEDIDENTITY_USER`(`USER_ID`) USING BTREE,
  INDEX `IDX_FEDIDENTITY_FEDUSER`(`FEDERATED_USER_ID`) USING BTREE,
  CONSTRAINT `FK404288B92EF007A6` FOREIGN KEY (`USER_ID`) REFERENCES `USER_ENTITY` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for FEDERATED_USER
-- ----------------------------
DROP TABLE IF EXISTS `FEDERATED_USER`;
CREATE TABLE `FEDERATED_USER`  (
  `ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `STORAGE_PROVIDER_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for FED_USER_ATTRIBUTE
-- ----------------------------
DROP TABLE IF EXISTS `FED_USER_ATTRIBUTE`;
CREATE TABLE `FED_USER_ATTRIBUTE`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `USER_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `STORAGE_PROVIDER_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `VALUE` varchar(2024) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `IDX_FU_ATTRIBUTE`(`USER_ID`, `REALM_ID`, `NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for FED_USER_CONSENT
-- ----------------------------
DROP TABLE IF EXISTS `FED_USER_CONSENT`;
CREATE TABLE `FED_USER_CONSENT`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `CLIENT_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `USER_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `STORAGE_PROVIDER_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `CREATED_DATE` bigint(20) DEFAULT NULL,
  `LAST_UPDATED_DATE` bigint(20) DEFAULT NULL,
  `CLIENT_STORAGE_PROVIDER` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `EXTERNAL_CLIENT_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `IDX_FU_CONSENT`(`USER_ID`, `CLIENT_ID`) USING BTREE,
  INDEX `IDX_FU_CONSENT_RU`(`REALM_ID`, `USER_ID`) USING BTREE,
  INDEX `IDX_FU_CNSNT_EXT`(`USER_ID`, `CLIENT_STORAGE_PROVIDER`, `EXTERNAL_CLIENT_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for FED_USER_CONSENT_CL_SCOPE
-- ----------------------------
DROP TABLE IF EXISTS `FED_USER_CONSENT_CL_SCOPE`;
CREATE TABLE `FED_USER_CONSENT_CL_SCOPE`  (
  `USER_CONSENT_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `SCOPE_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`USER_CONSENT_ID`, `SCOPE_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for FED_USER_CREDENTIAL
-- ----------------------------
DROP TABLE IF EXISTS `FED_USER_CREDENTIAL`;
CREATE TABLE `FED_USER_CREDENTIAL`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `SALT` tinyblob,
  `TYPE` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `CREATED_DATE` bigint(20) DEFAULT NULL,
  `USER_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `STORAGE_PROVIDER_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `USER_LABEL` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `SECRET_DATA` longtext CHARACTER SET latin1 COLLATE latin1_swedish_ci,
  `CREDENTIAL_DATA` longtext CHARACTER SET latin1 COLLATE latin1_swedish_ci,
  `PRIORITY` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `IDX_FU_CREDENTIAL`(`USER_ID`, `TYPE`) USING BTREE,
  INDEX `IDX_FU_CREDENTIAL_RU`(`REALM_ID`, `USER_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for FED_USER_GROUP_MEMBERSHIP
-- ----------------------------
DROP TABLE IF EXISTS `FED_USER_GROUP_MEMBERSHIP`;
CREATE TABLE `FED_USER_GROUP_MEMBERSHIP`  (
  `GROUP_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `USER_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `STORAGE_PROVIDER_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`GROUP_ID`, `USER_ID`) USING BTREE,
  INDEX `IDX_FU_GROUP_MEMBERSHIP`(`USER_ID`, `GROUP_ID`) USING BTREE,
  INDEX `IDX_FU_GROUP_MEMBERSHIP_RU`(`REALM_ID`, `USER_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for FED_USER_REQUIRED_ACTION
-- ----------------------------
DROP TABLE IF EXISTS `FED_USER_REQUIRED_ACTION`;
CREATE TABLE `FED_USER_REQUIRED_ACTION`  (
  `REQUIRED_ACTION` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL DEFAULT ' ',
  `USER_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `STORAGE_PROVIDER_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`REQUIRED_ACTION`, `USER_ID`) USING BTREE,
  INDEX `IDX_FU_REQUIRED_ACTION`(`USER_ID`, `REQUIRED_ACTION`) USING BTREE,
  INDEX `IDX_FU_REQUIRED_ACTION_RU`(`REALM_ID`, `USER_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for FED_USER_ROLE_MAPPING
-- ----------------------------
DROP TABLE IF EXISTS `FED_USER_ROLE_MAPPING`;
CREATE TABLE `FED_USER_ROLE_MAPPING`  (
  `ROLE_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `USER_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `STORAGE_PROVIDER_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`ROLE_ID`, `USER_ID`) USING BTREE,
  INDEX `IDX_FU_ROLE_MAPPING`(`USER_ID`, `ROLE_ID`) USING BTREE,
  INDEX `IDX_FU_ROLE_MAPPING_RU`(`REALM_ID`, `USER_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for GROUP_ATTRIBUTE
-- ----------------------------
DROP TABLE IF EXISTS `GROUP_ATTRIBUTE`;
CREATE TABLE `GROUP_ATTRIBUTE`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL DEFAULT 'sybase-needs-something-here',
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VALUE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `GROUP_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `IDX_GROUP_ATTR_GROUP`(`GROUP_ID`) USING BTREE,
  CONSTRAINT `FK_GROUP_ATTRIBUTE_GROUP` FOREIGN KEY (`GROUP_ID`) REFERENCES `KEYCLOAK_GROUP` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for GROUP_ROLE_MAPPING
-- ----------------------------
DROP TABLE IF EXISTS `GROUP_ROLE_MAPPING`;
CREATE TABLE `GROUP_ROLE_MAPPING`  (
  `ROLE_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `GROUP_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`ROLE_ID`, `GROUP_ID`) USING BTREE,
  INDEX `IDX_GROUP_ROLE_MAPP_GROUP`(`GROUP_ID`) USING BTREE,
  CONSTRAINT `FK_GROUP_ROLE_GROUP` FOREIGN KEY (`GROUP_ID`) REFERENCES `KEYCLOAK_GROUP` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_GROUP_ROLE_ROLE` FOREIGN KEY (`ROLE_ID`) REFERENCES `KEYCLOAK_ROLE` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for IDENTITY_PROVIDER
-- ----------------------------
DROP TABLE IF EXISTS `IDENTITY_PROVIDER`;
CREATE TABLE `IDENTITY_PROVIDER`  (
  `INTERNAL_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `PROVIDER_ALIAS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `PROVIDER_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `STORE_TOKEN` bit(1) NOT NULL DEFAULT b'0',
  `AUTHENTICATE_BY_DEFAULT` bit(1) NOT NULL DEFAULT b'0',
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `ADD_TOKEN_ROLE` bit(1) NOT NULL DEFAULT b'1',
  `TRUST_EMAIL` bit(1) NOT NULL DEFAULT b'0',
  `FIRST_BROKER_LOGIN_FLOW_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `POST_BROKER_LOGIN_FLOW_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `PROVIDER_DISPLAY_NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `LINK_ONLY` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`INTERNAL_ID`) USING BTREE,
  UNIQUE INDEX `UK_2DAELWNIBJI49AVXSRTUF6XJ33`(`PROVIDER_ALIAS`, `REALM_ID`) USING BTREE,
  INDEX `IDX_IDENT_PROV_REALM`(`REALM_ID`) USING BTREE,
  CONSTRAINT `FK2B4EBC52AE5C3B34` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for IDENTITY_PROVIDER_CONFIG
-- ----------------------------
DROP TABLE IF EXISTS `IDENTITY_PROVIDER_CONFIG`;
CREATE TABLE `IDENTITY_PROVIDER_CONFIG`  (
  `IDENTITY_PROVIDER_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VALUE` longtext CHARACTER SET latin1 COLLATE latin1_swedish_ci,
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`IDENTITY_PROVIDER_ID`, `NAME`) USING BTREE,
  CONSTRAINT `FKDC4897CF864C4E43` FOREIGN KEY (`IDENTITY_PROVIDER_ID`) REFERENCES `IDENTITY_PROVIDER` (`INTERNAL_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for IDENTITY_PROVIDER_MAPPER
-- ----------------------------
DROP TABLE IF EXISTS `IDENTITY_PROVIDER_MAPPER`;
CREATE TABLE `IDENTITY_PROVIDER_MAPPER`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `IDP_ALIAS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `IDP_MAPPER_NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `IDX_ID_PROV_MAPP_REALM`(`REALM_ID`) USING BTREE,
  CONSTRAINT `FK_IDPM_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for IDP_MAPPER_CONFIG
-- ----------------------------
DROP TABLE IF EXISTS `IDP_MAPPER_CONFIG`;
CREATE TABLE `IDP_MAPPER_CONFIG`  (
  `IDP_MAPPER_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VALUE` longtext CHARACTER SET latin1 COLLATE latin1_swedish_ci,
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`IDP_MAPPER_ID`, `NAME`) USING BTREE,
  CONSTRAINT `FK_IDPMCONFIG` FOREIGN KEY (`IDP_MAPPER_ID`) REFERENCES `IDENTITY_PROVIDER_MAPPER` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for KEYCLOAK_GROUP
-- ----------------------------
DROP TABLE IF EXISTS `KEYCLOAK_GROUP`;
CREATE TABLE `KEYCLOAK_GROUP`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `PARENT_GROUP` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `SIBLING_NAMES`(`REALM_ID`, `PARENT_GROUP`, `NAME`) USING BTREE,
  CONSTRAINT `FK_GROUP_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for KEYCLOAK_ROLE
-- ----------------------------
DROP TABLE IF EXISTS `KEYCLOAK_ROLE`;
CREATE TABLE `KEYCLOAK_ROLE`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `CLIENT_REALM_CONSTRAINT` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `CLIENT_ROLE` bit(1) DEFAULT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `REALM_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `CLIENT` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `REALM` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `UK_J3RWUVD56ONTGSUHOGM184WW2-2`(`NAME`, `CLIENT_REALM_CONSTRAINT`) USING BTREE,
  INDEX `IDX_KEYCLOAK_ROLE_CLIENT`(`CLIENT`) USING BTREE,
  INDEX `IDX_KEYCLOAK_ROLE_REALM`(`REALM`) USING BTREE,
  CONSTRAINT `FK_6VYQFE4CN4WLQ8R6KT5VDSJ5C` FOREIGN KEY (`REALM`) REFERENCES `REALM` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of KEYCLOAK_ROLE
-- ----------------------------
INSERT INTO `KEYCLOAK_ROLE` VALUES ('0201a972-0596-404f-b0a7-cfa9cf60232c', '7515bb98-a373-4166-b03c-22aa4f14cea4', b'1', '${role_manage-authorization}', 'manage-authorization', 'master', '7515bb98-a373-4166-b03c-22aa4f14cea4', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('05b04ac1-ccb3-4d44-ad17-7ebc1772fdff', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', b'1', '${role_impersonation}', 'impersonation', 'demo', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('0666da58-e96d-4637-add1-bdcc8ae12a32', '7515bb98-a373-4166-b03c-22aa4f14cea4', b'1', '${role_create-client}', 'create-client', 'master', '7515bb98-a373-4166-b03c-22aa4f14cea4', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('06c2e966-851b-4615-a30a-b13b2eb82e8d', '7515bb98-a373-4166-b03c-22aa4f14cea4', b'1', '${role_view-identity-providers}', 'view-identity-providers', 'master', '7515bb98-a373-4166-b03c-22aa4f14cea4', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('07b566ca-e0e1-4032-98cb-5bbd0ffe0ae4', 'master', b'0', '${role_offline-access}', 'offline_access', 'master', NULL, 'master');
INSERT INTO `KEYCLOAK_ROLE` VALUES ('09936ec5-8e0a-416b-a8d2-0f26c22b5282', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', b'1', '${role_create-client}', 'create-client', 'master', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('09b53e53-331f-4fbd-8c4d-87c6c62da264', '9f327fb4-6a74-4b8f-8a22-03c07dcc6475', b'1', '${role_manage-account}', 'manage-account', 'master', '9f327fb4-6a74-4b8f-8a22-03c07dcc6475', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('0cd1b5a1-21a0-4ffd-8fbc-aed0c4686cb2', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', b'1', '${role_view-users}', 'view-users', 'master', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('0f9556b9-85ad-4d3c-92b3-14ad1f62df2a', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', b'1', '${role_query-users}', 'query-users', 'master', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('0fb0c7f5-e526-4cda-a961-77f98d7e1e2e', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', b'1', '${role_manage-clients}', 'manage-clients', 'demo', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('1b6845b2-6179-4bc5-bce7-a4003c0e4b70', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', b'1', '${role_query-groups}', 'query-groups', 'demo', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('1e1843b1-adb7-4352-a6f5-e23b895a0c7f', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', b'1', '${role_impersonation}', 'impersonation', 'master', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('1f0ef3b5-9176-43fa-b0d8-6b1dc236cfae', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', b'1', '${role_manage-identity-providers}', 'manage-identity-providers', 'demo', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('222b5675-80e1-4051-a932-4ca289d85906', '9f327fb4-6a74-4b8f-8a22-03c07dcc6475', b'1', '${role_view-applications}', 'view-applications', 'master', '9f327fb4-6a74-4b8f-8a22-03c07dcc6475', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('255644dd-b96e-4ff9-a1d2-6a9d1be655ad', '8cedc74f-6b51-42a1-ba12-e2b80c11d39a', b'1', NULL, 'uma_protection', 'demo', '8cedc74f-6b51-42a1-ba12-e2b80c11d39a', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('268ce6a3-36ef-41ab-83c4-58e170d27906', '7515bb98-a373-4166-b03c-22aa4f14cea4', b'1', '${role_manage-clients}', 'manage-clients', 'master', '7515bb98-a373-4166-b03c-22aa4f14cea4', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('29661820-f292-43e5-9bf4-5c1154db2e09', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', b'1', '${role_view-realm}', 'view-realm', 'demo', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('2ba43097-0c34-4cfa-a7ea-f291fb5fd016', '9f327fb4-6a74-4b8f-8a22-03c07dcc6475', b'1', '${role_view-profile}', 'view-profile', 'master', '9f327fb4-6a74-4b8f-8a22-03c07dcc6475', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('2ded2bef-068f-4420-ba62-3b90d89345ae', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', b'1', '${role_view-realm}', 'view-realm', 'master', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('3188b407-f30e-4518-bd4b-1be481167170', '7515bb98-a373-4166-b03c-22aa4f14cea4', b'1', '${role_view-realm}', 'view-realm', 'master', '7515bb98-a373-4166-b03c-22aa4f14cea4', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('31e47454-1409-4b07-bb24-bc2a6ab747af', '7515bb98-a373-4166-b03c-22aa4f14cea4', b'1', '${role_manage-events}', 'manage-events', 'master', '7515bb98-a373-4166-b03c-22aa4f14cea4', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('34607a1c-9ff9-41d7-879b-2e269b2c0463', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', b'1', '${role_view-users}', 'view-users', 'demo', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('35b49764-cf02-4b7c-a940-556497fee2ee', '7515bb98-a373-4166-b03c-22aa4f14cea4', b'1', '${role_query-users}', 'query-users', 'master', '7515bb98-a373-4166-b03c-22aa4f14cea4', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('3f38e7db-8c56-4080-a919-f71899ce84ea', '7515bb98-a373-4166-b03c-22aa4f14cea4', b'1', '${role_query-clients}', 'query-clients', 'master', '7515bb98-a373-4166-b03c-22aa4f14cea4', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('3f4f695a-b51f-458f-b77f-a77171c3460c', '7515bb98-a373-4166-b03c-22aa4f14cea4', b'1', '${role_query-groups}', 'query-groups', 'master', '7515bb98-a373-4166-b03c-22aa4f14cea4', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('47d5fdc6-8d94-4d34-a471-d2b50da5de1a', '7515bb98-a373-4166-b03c-22aa4f14cea4', b'1', '${role_manage-realm}', 'manage-realm', 'master', '7515bb98-a373-4166-b03c-22aa4f14cea4', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('52a1662f-134e-4b3f-8c23-c8631015555a', 'aa33da79-41ff-42be-9518-96b12bcd8055', b'1', '${role_manage-consent}', 'manage-consent', 'demo', 'aa33da79-41ff-42be-9518-96b12bcd8055', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('55b1ad60-1d20-47ed-9c36-a627b9455990', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', b'1', '${role_view-authorization}', 'view-authorization', 'master', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('5d75cc0e-69e8-452e-9660-dc0498bd2c4f', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', b'1', '${role_manage-events}', 'manage-events', 'master', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('5d97bb63-7297-4d11-a8c4-199fb99d98a5', 'aa33da79-41ff-42be-9518-96b12bcd8055', b'1', '${role_manage-account-links}', 'manage-account-links', 'demo', 'aa33da79-41ff-42be-9518-96b12bcd8055', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('5f9b0558-4144-4afc-8825-2231765f96ed', '7515bb98-a373-4166-b03c-22aa4f14cea4', b'1', '${role_query-realms}', 'query-realms', 'master', '7515bb98-a373-4166-b03c-22aa4f14cea4', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('63c27012-2e75-4b40-a03b-1538f37bca36', 'aa33da79-41ff-42be-9518-96b12bcd8055', b'1', '${role_manage-account}', 'manage-account', 'demo', 'aa33da79-41ff-42be-9518-96b12bcd8055', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('63f0407a-9b7f-4d25-98eb-f2b44b7cc3ed', '7515bb98-a373-4166-b03c-22aa4f14cea4', b'1', '${role_view-users}', 'view-users', 'master', '7515bb98-a373-4166-b03c-22aa4f14cea4', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('64010059-7dde-4bf0-a42d-3f69aedd4d97', '7515bb98-a373-4166-b03c-22aa4f14cea4', b'1', '${role_manage-identity-providers}', 'manage-identity-providers', 'master', '7515bb98-a373-4166-b03c-22aa4f14cea4', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('6897532e-c1e0-4bd9-871e-7bb68127ffea', 'master', b'0', '${role_create-realm}', 'create-realm', 'master', NULL, 'master');
INSERT INTO `KEYCLOAK_ROLE` VALUES ('6c55ef31-d139-48f0-83cd-a8dde67d3dac', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', b'1', '${role_view-clients}', 'view-clients', 'master', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('72667532-066f-413f-9a6c-ec295fb37ddf', '8f04cb27-025c-495c-ab99-916ad43d5957', b'1', '${role_read-token}', 'read-token', 'master', '8f04cb27-025c-495c-ab99-916ad43d5957', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('758dfba3-fafc-4e80-8f6b-722be450c700', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', b'1', '${role_view-identity-providers}', 'view-identity-providers', 'demo', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('78b57dc9-5c09-477c-9faa-363e6320aa62', '1ea2d246-2e72-440b-8236-0d9284d8a739', b'1', '${role_read-token}', 'read-token', 'demo', '1ea2d246-2e72-440b-8236-0d9284d8a739', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('81f241a8-b2cd-4123-85b5-e107dc1dc396', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', b'1', '${role_manage-users}', 'manage-users', 'demo', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('83fe4665-b793-42ce-80f3-90cead33083a', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', b'1', '${role_query-groups}', 'query-groups', 'master', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('8aae6535-a6eb-4d5d-82dd-aff060990931', '7515bb98-a373-4166-b03c-22aa4f14cea4', b'1', '${role_view-events}', 'view-events', 'master', '7515bb98-a373-4166-b03c-22aa4f14cea4', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('8b79da3a-7b0e-4ca5-8bbd-7cee6b18bfae', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', b'1', '${role_query-clients}', 'query-clients', 'demo', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('8d5d7a8c-9547-4165-bb0f-d736ee4783ed', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', b'1', '${role_manage-clients}', 'manage-clients', 'master', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('900d9963-6141-47fb-9cf0-fb02d628a7ca', 'aa33da79-41ff-42be-9518-96b12bcd8055', b'1', '${role_view-profile}', 'view-profile', 'demo', 'aa33da79-41ff-42be-9518-96b12bcd8055', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('90aa7861-6913-4f70-b72d-c0a5cbcef83f', 'demo', b'0', '${role_offline-access}', 'offline_access', 'demo', NULL, 'demo');
INSERT INTO `KEYCLOAK_ROLE` VALUES ('9175e7d8-71e6-4091-99bd-5084e9e1f84f', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', b'1', '${role_manage-identity-providers}', 'manage-identity-providers', 'master', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('93e446e1-b50c-4261-9e26-a1b9e8989a77', 'demo', b'0', '${role_uma_authorization}', 'uma_authorization', 'demo', NULL, 'demo');
INSERT INTO `KEYCLOAK_ROLE` VALUES ('967d4a6e-d1e4-42c2-8b8c-c178660ac9a7', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', b'1', '${role_query-realms}', 'query-realms', 'master', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('9b0c4138-0ba3-4b20-8564-6a912bd781f1', '7515bb98-a373-4166-b03c-22aa4f14cea4', b'1', '${role_view-clients}', 'view-clients', 'master', '7515bb98-a373-4166-b03c-22aa4f14cea4', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('a23e8432-6a65-49dd-9f43-458f2dab7709', '7515bb98-a373-4166-b03c-22aa4f14cea4', b'1', '${role_manage-users}', 'manage-users', 'master', '7515bb98-a373-4166-b03c-22aa4f14cea4', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('a7cb0ea5-35ae-4e08-9258-ed8fef6587ac', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', b'1', '${role_manage-realm}', 'manage-realm', 'master', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('ad632e6b-0dee-452d-9b80-915d2082a169', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', b'1', '${role_manage-authorization}', 'manage-authorization', 'master', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('af5cc799-4d1b-4d20-9524-a232ad1d9bf2', '7515bb98-a373-4166-b03c-22aa4f14cea4', b'1', '${role_view-authorization}', 'view-authorization', 'master', '7515bb98-a373-4166-b03c-22aa4f14cea4', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('b0fe80d4-56b1-45f2-937f-211c4e9da6e9', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', b'1', '${role_manage-realm}', 'manage-realm', 'demo', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('b87fc7fe-2465-4fe1-9b2a-22db0ff27e05', '9f327fb4-6a74-4b8f-8a22-03c07dcc6475', b'1', '${role_view-consent}', 'view-consent', 'master', '9f327fb4-6a74-4b8f-8a22-03c07dcc6475', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('bc2c8d50-2b9f-46b3-a358-44223e839b10', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', b'1', '${role_view-events}', 'view-events', 'master', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('c41dfeb5-11e0-40eb-bd18-c5bf97247ba3', '9f327fb4-6a74-4b8f-8a22-03c07dcc6475', b'1', '${role_manage-consent}', 'manage-consent', 'master', '9f327fb4-6a74-4b8f-8a22-03c07dcc6475', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('ce73e1ee-c8b6-46ac-857e-073f1ae6df7b', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', b'1', '${role_view-identity-providers}', 'view-identity-providers', 'master', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('cec52398-64c6-46a0-8bf2-4510569b9a65', '7515bb98-a373-4166-b03c-22aa4f14cea4', b'1', '${role_impersonation}', 'impersonation', 'master', '7515bb98-a373-4166-b03c-22aa4f14cea4', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('cfe52ad4-e562-469a-878e-6930aadac1a1', 'master', b'0', '${role_uma_authorization}', 'uma_authorization', 'master', NULL, 'master');
INSERT INTO `KEYCLOAK_ROLE` VALUES ('d050e62b-1743-4f2c-b185-534928e3b723', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', b'1', '${role_view-authorization}', 'view-authorization', 'demo', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('d349ced9-620c-4785-939b-c9509f87a785', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', b'1', '${role_query-users}', 'query-users', 'demo', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', 'master', b'0', '${role_admin}', 'admin', 'master', NULL, 'master');
INSERT INTO `KEYCLOAK_ROLE` VALUES ('d64c7bd9-6989-49cb-96d4-565581f8fea1', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', b'1', '${role_manage-events}', 'manage-events', 'demo', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('dac63842-a2a5-4d9a-8dee-ad210e14ca2f', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', b'1', '${role_manage-authorization}', 'manage-authorization', 'demo', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('dc968acd-5a95-4f29-bdfa-0c8c442978d7', 'aa33da79-41ff-42be-9518-96b12bcd8055', b'1', '${role_view-consent}', 'view-consent', 'demo', 'aa33da79-41ff-42be-9518-96b12bcd8055', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('e6d0965c-35dd-4c71-8d7d-bf16a56fa7ca', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', b'1', '${role_view-events}', 'view-events', 'demo', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('ea057541-326d-417f-8a5f-ac3ad7ff3ed8', 'aa33da79-41ff-42be-9518-96b12bcd8055', b'1', '${role_view-applications}', 'view-applications', 'demo', 'aa33da79-41ff-42be-9518-96b12bcd8055', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('eb124d23-1a6c-4ea4-9cd5-75678d68e4b5', '9f327fb4-6a74-4b8f-8a22-03c07dcc6475', b'1', '${role_manage-account-links}', 'manage-account-links', 'master', '9f327fb4-6a74-4b8f-8a22-03c07dcc6475', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('eeaf3173-45ec-4429-89b1-48326ba45773', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', b'1', '${role_view-clients}', 'view-clients', 'demo', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('f65f6c2d-cc8f-4d28-82dd-c240be8d4b8b', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', b'1', '${role_create-client}', 'create-client', 'demo', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('f74b4777-198e-49df-a8a0-1494f1529ee5', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', b'1', '${role_manage-users}', 'manage-users', 'master', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('f7c33908-c4dc-4e78-adbe-e880c54107b8', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', b'1', '${role_query-clients}', 'query-clients', 'master', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('fedb960b-5157-494b-b631-b1959b5ff411', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', b'1', '${role_realm-admin}', 'realm-admin', 'demo', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', NULL);
INSERT INTO `KEYCLOAK_ROLE` VALUES ('ffc496ba-4cb2-435e-83b6-17de9cc45c80', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', b'1', '${role_query-realms}', 'query-realms', 'demo', 'b6c158d8-d49c-4a26-aa0a-c4ead414c72f', NULL);

-- ----------------------------
-- Table structure for MIGRATION_MODEL
-- ----------------------------
DROP TABLE IF EXISTS `MIGRATION_MODEL`;
CREATE TABLE `MIGRATION_MODEL`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VERSION` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `UPDATE_TIME` bigint(20) NOT NULL DEFAULT 0,
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `IDX_UPDATE_TIME`(`UPDATE_TIME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of MIGRATION_MODEL
-- ----------------------------
INSERT INTO `MIGRATION_MODEL` VALUES ('58ul8', '11.0.3', 1604626604);

-- ----------------------------
-- Table structure for OFFLINE_CLIENT_SESSION
-- ----------------------------
DROP TABLE IF EXISTS `OFFLINE_CLIENT_SESSION`;
CREATE TABLE `OFFLINE_CLIENT_SESSION`  (
  `USER_SESSION_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `CLIENT_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `OFFLINE_FLAG` varchar(4) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `TIMESTAMP` int(11) DEFAULT NULL,
  `DATA` longtext CHARACTER SET latin1 COLLATE latin1_swedish_ci,
  `CLIENT_STORAGE_PROVIDER` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL DEFAULT 'local',
  `EXTERNAL_CLIENT_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL DEFAULT 'local',
  PRIMARY KEY (`USER_SESSION_ID`, `CLIENT_ID`, `CLIENT_STORAGE_PROVIDER`, `EXTERNAL_CLIENT_ID`, `OFFLINE_FLAG`) USING BTREE,
  INDEX `IDX_US_SESS_ID_ON_CL_SESS`(`USER_SESSION_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for OFFLINE_USER_SESSION
-- ----------------------------
DROP TABLE IF EXISTS `OFFLINE_USER_SESSION`;
CREATE TABLE `OFFLINE_USER_SESSION`  (
  `USER_SESSION_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `USER_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `CREATED_ON` int(11) NOT NULL,
  `OFFLINE_FLAG` varchar(4) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `DATA` longtext CHARACTER SET latin1 COLLATE latin1_swedish_ci,
  `LAST_SESSION_REFRESH` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`USER_SESSION_ID`, `OFFLINE_FLAG`) USING BTREE,
  INDEX `IDX_OFFLINE_USS_CREATEDON`(`CREATED_ON`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for POLICY_CONFIG
-- ----------------------------
DROP TABLE IF EXISTS `POLICY_CONFIG`;
CREATE TABLE `POLICY_CONFIG`  (
  `POLICY_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VALUE` longtext CHARACTER SET latin1 COLLATE latin1_swedish_ci,
  PRIMARY KEY (`POLICY_ID`, `NAME`) USING BTREE,
  CONSTRAINT `FKDC34197CF864C4E43` FOREIGN KEY (`POLICY_ID`) REFERENCES `RESOURCE_SERVER_POLICY` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of POLICY_CONFIG
-- ----------------------------
INSERT INTO `POLICY_CONFIG` VALUES ('2486a449-2904-4b57-ab60-ef3dc73bf49e', 'code', '// by default, grants any permission associated with this policy\n$evaluation.grant();\n');
INSERT INTO `POLICY_CONFIG` VALUES ('d4063bf0-5388-40c7-afa2-42ad44cf441e', 'defaultResourceType', 'urn:democlient:resources:default');

-- ----------------------------
-- Table structure for PROTOCOL_MAPPER
-- ----------------------------
DROP TABLE IF EXISTS `PROTOCOL_MAPPER`;
CREATE TABLE `PROTOCOL_MAPPER`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `PROTOCOL` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `PROTOCOL_MAPPER_NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `CLIENT_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `CLIENT_SCOPE_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `IDX_PROTOCOL_MAPPER_CLIENT`(`CLIENT_ID`) USING BTREE,
  INDEX `IDX_CLSCOPE_PROTMAP`(`CLIENT_SCOPE_ID`) USING BTREE,
  CONSTRAINT `FK_CLI_SCOPE_MAPPER` FOREIGN KEY (`CLIENT_SCOPE_ID`) REFERENCES `CLIENT_SCOPE` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_PCM_REALM` FOREIGN KEY (`CLIENT_ID`) REFERENCES `CLIENT` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of PROTOCOL_MAPPER
-- ----------------------------
INSERT INTO `PROTOCOL_MAPPER` VALUES ('01acd369-3df2-47dc-a1d2-3c37e7bde1f2', 'given name', 'openid-connect', 'oidc-usermodel-property-mapper', NULL, '63a96a62-31d2-4089-92a2-97e9f43c281f');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('05099817-ee4a-47c8-aea4-ed9a271e58bb', 'realm roles', 'openid-connect', 'oidc-usermodel-realm-role-mapper', NULL, '4c2b4fcb-5f1e-4c78-a341-fd27dfab38e0');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('07e9f1e3-0f29-42bb-9224-85af462699ae', 'nickname', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, '63a96a62-31d2-4089-92a2-97e9f43c281f');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('081c98f6-60cd-4abd-baac-97eaec2ca6e8', 'email verified', 'openid-connect', 'oidc-usermodel-property-mapper', NULL, '0e9473b7-de09-484d-82e0-d88097d14b5d');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('0d0e72f0-848c-460e-8f42-86d69fb97f8a', 'gender', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, 'f59745e4-deef-4a93-82d1-eccdd7b4eff1');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('15ad0636-070b-4d62-b18b-55efd17eae60', 'address', 'openid-connect', 'oidc-address-mapper', NULL, '81734f9f-7835-4af1-87f7-4625aa7ce31f');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('19e38f51-2abc-4df3-811c-f2df5b5d49d3', 'audience resolve', 'openid-connect', 'oidc-audience-resolve-mapper', NULL, 'b4d036d9-3470-4960-a3b0-5f6365b99c8c');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('1a07304b-e051-4787-8383-a17ccd3f0a7a', 'phone number verified', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, '7e69eefb-5f29-45cb-b249-3285b708f4e9');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('1b800984-67b9-4a8b-9262-4345f9d96718', 'groups', 'openid-connect', 'oidc-usermodel-realm-role-mapper', NULL, 'b092e195-d133-4604-af92-27c80735ad3a');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('24bc9b3f-5c83-470e-9136-a22082257066', 'updated at', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, '63a96a62-31d2-4089-92a2-97e9f43c281f');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('27eef850-61a3-46c8-8b12-1ae3747022dc', 'groups', 'openid-connect', 'oidc-usermodel-realm-role-mapper', NULL, '9d697e2e-581d-4e01-92b5-09baef8c98f4');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('29fe679f-3711-477f-8c8b-d8716ba3dffc', 'Client Host', 'openid-connect', 'oidc-usersessionmodel-note-mapper', '8cedc74f-6b51-42a1-ba12-e2b80c11d39a', NULL);
INSERT INTO `PROTOCOL_MAPPER` VALUES ('30d17320-4578-404b-81e7-e0f1789d5f6a', 'upn', 'openid-connect', 'oidc-usermodel-property-mapper', NULL, 'b092e195-d133-4604-af92-27c80735ad3a');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('30e63d50-85d4-4e10-9599-04311190ccb8', 'role list', 'saml', 'saml-role-list-mapper', NULL, '60240200-c0bd-4105-9f83-6dfb5d862eea');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('361c7b35-baf7-4435-957c-7ac0c727fd10', 'audience resolve', 'openid-connect', 'oidc-audience-resolve-mapper', '23979e0b-c766-42bc-aaa3-f320a171837a', NULL);
INSERT INTO `PROTOCOL_MAPPER` VALUES ('37a6f72b-5fcf-4acd-853e-7e64373a58d6', 'audience resolve', 'openid-connect', 'oidc-audience-resolve-mapper', '80e2ea2a-23ba-4fed-af92-cabc4d2ea4ed', NULL);
INSERT INTO `PROTOCOL_MAPPER` VALUES ('391d0eeb-e879-4159-97a9-18262438746a', 'locale', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, 'f59745e4-deef-4a93-82d1-eccdd7b4eff1');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('3fa78b49-debc-41cc-92df-14c642981156', 'phone number', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, '7e69eefb-5f29-45cb-b249-3285b708f4e9');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('40c91545-a5e7-4279-9732-21ed4f943ccf', 'profile', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, '63a96a62-31d2-4089-92a2-97e9f43c281f');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('47c0a9d5-16b1-4e71-9e4b-a03b5e03d785', 'phone number', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, '91791326-87e9-4bfd-a5b3-b6752ef1bfc6');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('4ee45d83-d9ea-436c-9a38-820efab82955', 'gender', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, '63a96a62-31d2-4089-92a2-97e9f43c281f');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('66b82c18-2fc8-4310-8af2-5187e82c08e8', 'family name', 'openid-connect', 'oidc-usermodel-property-mapper', NULL, 'f59745e4-deef-4a93-82d1-eccdd7b4eff1');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('6821a1c1-e8d5-4238-9365-d07b6a92a1f8', 'zoneinfo', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, '63a96a62-31d2-4089-92a2-97e9f43c281f');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('72541ba0-18d5-4caa-a2a0-79524d7d0a10', 'birthdate', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, 'f59745e4-deef-4a93-82d1-eccdd7b4eff1');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('733a49ed-93f7-4d0c-9ad0-64877aede096', 'role list', 'saml', 'saml-role-list-mapper', NULL, '85983e59-945c-4f81-b2b3-b2f91ce56db9');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('73b75be7-7f70-44c2-94a9-8356f82a116c', 'updated at', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, 'f59745e4-deef-4a93-82d1-eccdd7b4eff1');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('7d50fb11-0910-4677-a8a7-eeb7fb4f7f66', 'website', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, 'f59745e4-deef-4a93-82d1-eccdd7b4eff1');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('82a00f24-628a-4830-9db7-82f805928b97', 'phone number verified', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, '91791326-87e9-4bfd-a5b3-b6752ef1bfc6');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('84e1dca2-f765-40c9-b1ff-ded092eb0069', 'nickname', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, 'f59745e4-deef-4a93-82d1-eccdd7b4eff1');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('86cb6e72-2aff-4fac-b24c-aeeff73bdf5e', 'email verified', 'openid-connect', 'oidc-usermodel-property-mapper', NULL, '459e53e1-17bb-4dc9-bc04-9eea36319a60');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('87625511-a2aa-4b5f-a5eb-9763b811233b', 'username', 'openid-connect', 'oidc-usermodel-property-mapper', NULL, '63a96a62-31d2-4089-92a2-97e9f43c281f');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('8d1b57bb-fa06-4d43-92e4-1122491449eb', 'family name', 'openid-connect', 'oidc-usermodel-property-mapper', NULL, '63a96a62-31d2-4089-92a2-97e9f43c281f');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('8d8f23b2-feb0-4897-80f8-0864e74c185d', 'realm roles', 'openid-connect', 'oidc-usermodel-realm-role-mapper', NULL, 'b4d036d9-3470-4960-a3b0-5f6365b99c8c');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('8e706f49-1ad3-453b-b376-33afd3a98d30', 'client roles', 'openid-connect', 'oidc-usermodel-client-role-mapper', NULL, 'b4d036d9-3470-4960-a3b0-5f6365b99c8c');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('8f98568f-c2b4-47f0-a630-93c555f8b5bb', 'full name', 'openid-connect', 'oidc-full-name-mapper', NULL, '63a96a62-31d2-4089-92a2-97e9f43c281f');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('90d5cb16-c873-41be-813d-0ea3576b9c65', 'username', 'openid-connect', 'oidc-usermodel-property-mapper', NULL, 'f59745e4-deef-4a93-82d1-eccdd7b4eff1');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('916f4b79-9470-4134-997b-f33d12440354', 'given name', 'openid-connect', 'oidc-usermodel-property-mapper', NULL, 'f59745e4-deef-4a93-82d1-eccdd7b4eff1');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('91b03862-5349-4460-a33a-a5a321927aec', 'Client ID', 'openid-connect', 'oidc-usersessionmodel-note-mapper', '8cedc74f-6b51-42a1-ba12-e2b80c11d39a', NULL);
INSERT INTO `PROTOCOL_MAPPER` VALUES ('960ce637-fb62-4ea5-adf0-52e510fdad1d', 'client roles', 'openid-connect', 'oidc-usermodel-client-role-mapper', NULL, '4c2b4fcb-5f1e-4c78-a341-fd27dfab38e0');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('a9389de5-fe8e-4b13-9f6f-a83bddf6ee0d', 'middle name', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, 'f59745e4-deef-4a93-82d1-eccdd7b4eff1');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('aae4c427-56e6-4893-8f0a-73c2714a5c2d', 'full name', 'openid-connect', 'oidc-full-name-mapper', NULL, 'f59745e4-deef-4a93-82d1-eccdd7b4eff1');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('b0464b1a-37ba-4fdd-9bc2-31b20fdb5afa', 'allowed web origins', 'openid-connect', 'oidc-allowed-origins-mapper', NULL, '948be43b-bd24-4840-ac17-8b0477b17a77');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('b6ceefc5-6d74-421e-8bb8-7c21e11235d3', 'picture', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, '63a96a62-31d2-4089-92a2-97e9f43c281f');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('bde0cd07-21f9-4cba-9df9-c821a1233350', 'email', 'openid-connect', 'oidc-usermodel-property-mapper', NULL, '459e53e1-17bb-4dc9-bc04-9eea36319a60');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('c29c7ab7-6d61-48e9-8ed4-ffc49de36aea', 'website', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, '63a96a62-31d2-4089-92a2-97e9f43c281f');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('c5adc9d5-d987-4519-8a65-1a10dff3b21e', 'Client IP Address', 'openid-connect', 'oidc-usersessionmodel-note-mapper', '8cedc74f-6b51-42a1-ba12-e2b80c11d39a', NULL);
INSERT INTO `PROTOCOL_MAPPER` VALUES ('c8252c06-c5bc-49e0-a967-255c0d967f6c', 'zoneinfo', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, 'f59745e4-deef-4a93-82d1-eccdd7b4eff1');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('cd620f9e-3b91-4040-acd6-20bea5a6729c', 'allowed web origins', 'openid-connect', 'oidc-allowed-origins-mapper', NULL, 'f82637b5-3526-4226-a37f-682ca3153857');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('d0366be0-65d0-4718-adc5-680383842ed1', 'locale', 'openid-connect', 'oidc-usermodel-attribute-mapper', 'f2fba72e-4130-482a-9ac3-50674c17fe38', NULL);
INSERT INTO `PROTOCOL_MAPPER` VALUES ('d315dda0-9a45-4e6e-a5e0-916e728abf20', 'locale', 'openid-connect', 'oidc-usermodel-attribute-mapper', '605a5c15-5cfb-4453-b771-313bb7239afd', NULL);
INSERT INTO `PROTOCOL_MAPPER` VALUES ('d6895d5a-8974-4a2b-ae88-eea50aca1ddd', 'audience resolve', 'openid-connect', 'oidc-audience-resolve-mapper', NULL, '4c2b4fcb-5f1e-4c78-a341-fd27dfab38e0');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('dca220e9-c3a5-4156-91bd-22c7f3dadf9b', 'upn', 'openid-connect', 'oidc-usermodel-property-mapper', NULL, '9d697e2e-581d-4e01-92b5-09baef8c98f4');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('de3e87b5-5e45-4e89-909d-b57910327503', 'birthdate', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, '63a96a62-31d2-4089-92a2-97e9f43c281f');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('e507a587-82f0-46a1-a4fd-f8bb2dc1f2f8', 'address', 'openid-connect', 'oidc-address-mapper', NULL, 'ef20b41d-2b0a-4ae4-986f-84c02d5c0832');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('ef16033b-7e59-4e94-85a5-3b99dfa73b3b', 'email', 'openid-connect', 'oidc-usermodel-property-mapper', NULL, '0e9473b7-de09-484d-82e0-d88097d14b5d');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('f066cca6-19b2-4f1a-8d07-90b9a2c2fde7', 'middle name', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, '63a96a62-31d2-4089-92a2-97e9f43c281f');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('f4a846d3-8876-4644-84e7-9d086f9a9478', 'profile', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, 'f59745e4-deef-4a93-82d1-eccdd7b4eff1');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('f9b5055c-1e2b-41ce-b658-cca20f3c3adc', 'picture', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, 'f59745e4-deef-4a93-82d1-eccdd7b4eff1');
INSERT INTO `PROTOCOL_MAPPER` VALUES ('fe856f7c-0738-435e-ae54-8262d75473f3', 'locale', 'openid-connect', 'oidc-usermodel-attribute-mapper', NULL, '63a96a62-31d2-4089-92a2-97e9f43c281f');

-- ----------------------------
-- Table structure for PROTOCOL_MAPPER_CONFIG
-- ----------------------------
DROP TABLE IF EXISTS `PROTOCOL_MAPPER_CONFIG`;
CREATE TABLE `PROTOCOL_MAPPER_CONFIG`  (
  `PROTOCOL_MAPPER_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VALUE` longtext CHARACTER SET latin1 COLLATE latin1_swedish_ci,
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`PROTOCOL_MAPPER_ID`, `NAME`) USING BTREE,
  CONSTRAINT `FK_PMCONFIG` FOREIGN KEY (`PROTOCOL_MAPPER_ID`) REFERENCES `PROTOCOL_MAPPER` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of PROTOCOL_MAPPER_CONFIG
-- ----------------------------
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('01acd369-3df2-47dc-a1d2-3c37e7bde1f2', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('01acd369-3df2-47dc-a1d2-3c37e7bde1f2', 'given_name', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('01acd369-3df2-47dc-a1d2-3c37e7bde1f2', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('01acd369-3df2-47dc-a1d2-3c37e7bde1f2', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('01acd369-3df2-47dc-a1d2-3c37e7bde1f2', 'firstName', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('01acd369-3df2-47dc-a1d2-3c37e7bde1f2', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('05099817-ee4a-47c8-aea4-ed9a271e58bb', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('05099817-ee4a-47c8-aea4-ed9a271e58bb', 'realm_access.roles', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('05099817-ee4a-47c8-aea4-ed9a271e58bb', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('05099817-ee4a-47c8-aea4-ed9a271e58bb', 'true', 'multivalued');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('05099817-ee4a-47c8-aea4-ed9a271e58bb', 'foo', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('07e9f1e3-0f29-42bb-9224-85af462699ae', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('07e9f1e3-0f29-42bb-9224-85af462699ae', 'nickname', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('07e9f1e3-0f29-42bb-9224-85af462699ae', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('07e9f1e3-0f29-42bb-9224-85af462699ae', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('07e9f1e3-0f29-42bb-9224-85af462699ae', 'nickname', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('07e9f1e3-0f29-42bb-9224-85af462699ae', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('081c98f6-60cd-4abd-baac-97eaec2ca6e8', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('081c98f6-60cd-4abd-baac-97eaec2ca6e8', 'email_verified', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('081c98f6-60cd-4abd-baac-97eaec2ca6e8', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('081c98f6-60cd-4abd-baac-97eaec2ca6e8', 'boolean', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('081c98f6-60cd-4abd-baac-97eaec2ca6e8', 'emailVerified', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('081c98f6-60cd-4abd-baac-97eaec2ca6e8', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('0d0e72f0-848c-460e-8f42-86d69fb97f8a', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('0d0e72f0-848c-460e-8f42-86d69fb97f8a', 'gender', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('0d0e72f0-848c-460e-8f42-86d69fb97f8a', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('0d0e72f0-848c-460e-8f42-86d69fb97f8a', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('0d0e72f0-848c-460e-8f42-86d69fb97f8a', 'gender', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('0d0e72f0-848c-460e-8f42-86d69fb97f8a', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('15ad0636-070b-4d62-b18b-55efd17eae60', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('15ad0636-070b-4d62-b18b-55efd17eae60', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('15ad0636-070b-4d62-b18b-55efd17eae60', 'country', 'user.attribute.country');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('15ad0636-070b-4d62-b18b-55efd17eae60', 'formatted', 'user.attribute.formatted');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('15ad0636-070b-4d62-b18b-55efd17eae60', 'locality', 'user.attribute.locality');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('15ad0636-070b-4d62-b18b-55efd17eae60', 'postal_code', 'user.attribute.postal_code');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('15ad0636-070b-4d62-b18b-55efd17eae60', 'region', 'user.attribute.region');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('15ad0636-070b-4d62-b18b-55efd17eae60', 'street', 'user.attribute.street');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('15ad0636-070b-4d62-b18b-55efd17eae60', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('1a07304b-e051-4787-8383-a17ccd3f0a7a', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('1a07304b-e051-4787-8383-a17ccd3f0a7a', 'phone_number_verified', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('1a07304b-e051-4787-8383-a17ccd3f0a7a', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('1a07304b-e051-4787-8383-a17ccd3f0a7a', 'boolean', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('1a07304b-e051-4787-8383-a17ccd3f0a7a', 'phoneNumberVerified', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('1a07304b-e051-4787-8383-a17ccd3f0a7a', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('1b800984-67b9-4a8b-9262-4345f9d96718', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('1b800984-67b9-4a8b-9262-4345f9d96718', 'groups', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('1b800984-67b9-4a8b-9262-4345f9d96718', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('1b800984-67b9-4a8b-9262-4345f9d96718', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('1b800984-67b9-4a8b-9262-4345f9d96718', 'true', 'multivalued');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('1b800984-67b9-4a8b-9262-4345f9d96718', 'foo', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('24bc9b3f-5c83-470e-9136-a22082257066', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('24bc9b3f-5c83-470e-9136-a22082257066', 'updated_at', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('24bc9b3f-5c83-470e-9136-a22082257066', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('24bc9b3f-5c83-470e-9136-a22082257066', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('24bc9b3f-5c83-470e-9136-a22082257066', 'updatedAt', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('24bc9b3f-5c83-470e-9136-a22082257066', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('27eef850-61a3-46c8-8b12-1ae3747022dc', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('27eef850-61a3-46c8-8b12-1ae3747022dc', 'groups', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('27eef850-61a3-46c8-8b12-1ae3747022dc', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('27eef850-61a3-46c8-8b12-1ae3747022dc', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('27eef850-61a3-46c8-8b12-1ae3747022dc', 'true', 'multivalued');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('27eef850-61a3-46c8-8b12-1ae3747022dc', 'foo', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('29fe679f-3711-477f-8c8b-d8716ba3dffc', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('29fe679f-3711-477f-8c8b-d8716ba3dffc', 'clientHost', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('29fe679f-3711-477f-8c8b-d8716ba3dffc', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('29fe679f-3711-477f-8c8b-d8716ba3dffc', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('29fe679f-3711-477f-8c8b-d8716ba3dffc', 'clientHost', 'user.session.note');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('30d17320-4578-404b-81e7-e0f1789d5f6a', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('30d17320-4578-404b-81e7-e0f1789d5f6a', 'upn', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('30d17320-4578-404b-81e7-e0f1789d5f6a', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('30d17320-4578-404b-81e7-e0f1789d5f6a', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('30d17320-4578-404b-81e7-e0f1789d5f6a', 'username', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('30d17320-4578-404b-81e7-e0f1789d5f6a', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('30e63d50-85d4-4e10-9599-04311190ccb8', 'Role', 'attribute.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('30e63d50-85d4-4e10-9599-04311190ccb8', 'Basic', 'attribute.nameformat');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('30e63d50-85d4-4e10-9599-04311190ccb8', 'false', 'single');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('391d0eeb-e879-4159-97a9-18262438746a', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('391d0eeb-e879-4159-97a9-18262438746a', 'locale', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('391d0eeb-e879-4159-97a9-18262438746a', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('391d0eeb-e879-4159-97a9-18262438746a', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('391d0eeb-e879-4159-97a9-18262438746a', 'locale', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('391d0eeb-e879-4159-97a9-18262438746a', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('3fa78b49-debc-41cc-92df-14c642981156', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('3fa78b49-debc-41cc-92df-14c642981156', 'phone_number', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('3fa78b49-debc-41cc-92df-14c642981156', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('3fa78b49-debc-41cc-92df-14c642981156', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('3fa78b49-debc-41cc-92df-14c642981156', 'phoneNumber', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('3fa78b49-debc-41cc-92df-14c642981156', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('40c91545-a5e7-4279-9732-21ed4f943ccf', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('40c91545-a5e7-4279-9732-21ed4f943ccf', 'profile', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('40c91545-a5e7-4279-9732-21ed4f943ccf', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('40c91545-a5e7-4279-9732-21ed4f943ccf', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('40c91545-a5e7-4279-9732-21ed4f943ccf', 'profile', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('40c91545-a5e7-4279-9732-21ed4f943ccf', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('47c0a9d5-16b1-4e71-9e4b-a03b5e03d785', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('47c0a9d5-16b1-4e71-9e4b-a03b5e03d785', 'phone_number', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('47c0a9d5-16b1-4e71-9e4b-a03b5e03d785', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('47c0a9d5-16b1-4e71-9e4b-a03b5e03d785', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('47c0a9d5-16b1-4e71-9e4b-a03b5e03d785', 'phoneNumber', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('47c0a9d5-16b1-4e71-9e4b-a03b5e03d785', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('4ee45d83-d9ea-436c-9a38-820efab82955', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('4ee45d83-d9ea-436c-9a38-820efab82955', 'gender', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('4ee45d83-d9ea-436c-9a38-820efab82955', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('4ee45d83-d9ea-436c-9a38-820efab82955', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('4ee45d83-d9ea-436c-9a38-820efab82955', 'gender', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('4ee45d83-d9ea-436c-9a38-820efab82955', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('66b82c18-2fc8-4310-8af2-5187e82c08e8', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('66b82c18-2fc8-4310-8af2-5187e82c08e8', 'family_name', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('66b82c18-2fc8-4310-8af2-5187e82c08e8', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('66b82c18-2fc8-4310-8af2-5187e82c08e8', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('66b82c18-2fc8-4310-8af2-5187e82c08e8', 'lastName', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('66b82c18-2fc8-4310-8af2-5187e82c08e8', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('6821a1c1-e8d5-4238-9365-d07b6a92a1f8', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('6821a1c1-e8d5-4238-9365-d07b6a92a1f8', 'zoneinfo', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('6821a1c1-e8d5-4238-9365-d07b6a92a1f8', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('6821a1c1-e8d5-4238-9365-d07b6a92a1f8', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('6821a1c1-e8d5-4238-9365-d07b6a92a1f8', 'zoneinfo', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('6821a1c1-e8d5-4238-9365-d07b6a92a1f8', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('72541ba0-18d5-4caa-a2a0-79524d7d0a10', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('72541ba0-18d5-4caa-a2a0-79524d7d0a10', 'birthdate', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('72541ba0-18d5-4caa-a2a0-79524d7d0a10', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('72541ba0-18d5-4caa-a2a0-79524d7d0a10', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('72541ba0-18d5-4caa-a2a0-79524d7d0a10', 'birthdate', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('72541ba0-18d5-4caa-a2a0-79524d7d0a10', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('733a49ed-93f7-4d0c-9ad0-64877aede096', 'Role', 'attribute.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('733a49ed-93f7-4d0c-9ad0-64877aede096', 'Basic', 'attribute.nameformat');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('733a49ed-93f7-4d0c-9ad0-64877aede096', 'false', 'single');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('73b75be7-7f70-44c2-94a9-8356f82a116c', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('73b75be7-7f70-44c2-94a9-8356f82a116c', 'updated_at', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('73b75be7-7f70-44c2-94a9-8356f82a116c', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('73b75be7-7f70-44c2-94a9-8356f82a116c', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('73b75be7-7f70-44c2-94a9-8356f82a116c', 'updatedAt', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('73b75be7-7f70-44c2-94a9-8356f82a116c', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('7d50fb11-0910-4677-a8a7-eeb7fb4f7f66', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('7d50fb11-0910-4677-a8a7-eeb7fb4f7f66', 'website', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('7d50fb11-0910-4677-a8a7-eeb7fb4f7f66', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('7d50fb11-0910-4677-a8a7-eeb7fb4f7f66', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('7d50fb11-0910-4677-a8a7-eeb7fb4f7f66', 'website', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('7d50fb11-0910-4677-a8a7-eeb7fb4f7f66', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('82a00f24-628a-4830-9db7-82f805928b97', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('82a00f24-628a-4830-9db7-82f805928b97', 'phone_number_verified', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('82a00f24-628a-4830-9db7-82f805928b97', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('82a00f24-628a-4830-9db7-82f805928b97', 'boolean', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('82a00f24-628a-4830-9db7-82f805928b97', 'phoneNumberVerified', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('82a00f24-628a-4830-9db7-82f805928b97', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('84e1dca2-f765-40c9-b1ff-ded092eb0069', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('84e1dca2-f765-40c9-b1ff-ded092eb0069', 'nickname', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('84e1dca2-f765-40c9-b1ff-ded092eb0069', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('84e1dca2-f765-40c9-b1ff-ded092eb0069', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('84e1dca2-f765-40c9-b1ff-ded092eb0069', 'nickname', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('84e1dca2-f765-40c9-b1ff-ded092eb0069', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('86cb6e72-2aff-4fac-b24c-aeeff73bdf5e', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('86cb6e72-2aff-4fac-b24c-aeeff73bdf5e', 'email_verified', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('86cb6e72-2aff-4fac-b24c-aeeff73bdf5e', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('86cb6e72-2aff-4fac-b24c-aeeff73bdf5e', 'boolean', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('86cb6e72-2aff-4fac-b24c-aeeff73bdf5e', 'emailVerified', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('86cb6e72-2aff-4fac-b24c-aeeff73bdf5e', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('87625511-a2aa-4b5f-a5eb-9763b811233b', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('87625511-a2aa-4b5f-a5eb-9763b811233b', 'preferred_username', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('87625511-a2aa-4b5f-a5eb-9763b811233b', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('87625511-a2aa-4b5f-a5eb-9763b811233b', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('87625511-a2aa-4b5f-a5eb-9763b811233b', 'username', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('87625511-a2aa-4b5f-a5eb-9763b811233b', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('8d1b57bb-fa06-4d43-92e4-1122491449eb', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('8d1b57bb-fa06-4d43-92e4-1122491449eb', 'family_name', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('8d1b57bb-fa06-4d43-92e4-1122491449eb', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('8d1b57bb-fa06-4d43-92e4-1122491449eb', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('8d1b57bb-fa06-4d43-92e4-1122491449eb', 'lastName', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('8d1b57bb-fa06-4d43-92e4-1122491449eb', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('8d8f23b2-feb0-4897-80f8-0864e74c185d', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('8d8f23b2-feb0-4897-80f8-0864e74c185d', 'realm_access.roles', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('8d8f23b2-feb0-4897-80f8-0864e74c185d', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('8d8f23b2-feb0-4897-80f8-0864e74c185d', 'true', 'multivalued');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('8d8f23b2-feb0-4897-80f8-0864e74c185d', 'foo', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('8e706f49-1ad3-453b-b376-33afd3a98d30', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('8e706f49-1ad3-453b-b376-33afd3a98d30', 'resource_access.${client_id}.roles', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('8e706f49-1ad3-453b-b376-33afd3a98d30', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('8e706f49-1ad3-453b-b376-33afd3a98d30', 'true', 'multivalued');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('8e706f49-1ad3-453b-b376-33afd3a98d30', 'foo', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('8f98568f-c2b4-47f0-a630-93c555f8b5bb', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('8f98568f-c2b4-47f0-a630-93c555f8b5bb', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('8f98568f-c2b4-47f0-a630-93c555f8b5bb', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('90d5cb16-c873-41be-813d-0ea3576b9c65', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('90d5cb16-c873-41be-813d-0ea3576b9c65', 'preferred_username', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('90d5cb16-c873-41be-813d-0ea3576b9c65', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('90d5cb16-c873-41be-813d-0ea3576b9c65', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('90d5cb16-c873-41be-813d-0ea3576b9c65', 'username', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('90d5cb16-c873-41be-813d-0ea3576b9c65', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('916f4b79-9470-4134-997b-f33d12440354', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('916f4b79-9470-4134-997b-f33d12440354', 'given_name', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('916f4b79-9470-4134-997b-f33d12440354', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('916f4b79-9470-4134-997b-f33d12440354', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('916f4b79-9470-4134-997b-f33d12440354', 'firstName', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('916f4b79-9470-4134-997b-f33d12440354', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('91b03862-5349-4460-a33a-a5a321927aec', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('91b03862-5349-4460-a33a-a5a321927aec', 'clientId', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('91b03862-5349-4460-a33a-a5a321927aec', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('91b03862-5349-4460-a33a-a5a321927aec', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('91b03862-5349-4460-a33a-a5a321927aec', 'clientId', 'user.session.note');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('960ce637-fb62-4ea5-adf0-52e510fdad1d', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('960ce637-fb62-4ea5-adf0-52e510fdad1d', 'resource_access.${client_id}.roles', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('960ce637-fb62-4ea5-adf0-52e510fdad1d', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('960ce637-fb62-4ea5-adf0-52e510fdad1d', 'true', 'multivalued');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('960ce637-fb62-4ea5-adf0-52e510fdad1d', 'foo', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('a9389de5-fe8e-4b13-9f6f-a83bddf6ee0d', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('a9389de5-fe8e-4b13-9f6f-a83bddf6ee0d', 'middle_name', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('a9389de5-fe8e-4b13-9f6f-a83bddf6ee0d', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('a9389de5-fe8e-4b13-9f6f-a83bddf6ee0d', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('a9389de5-fe8e-4b13-9f6f-a83bddf6ee0d', 'middleName', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('a9389de5-fe8e-4b13-9f6f-a83bddf6ee0d', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('aae4c427-56e6-4893-8f0a-73c2714a5c2d', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('aae4c427-56e6-4893-8f0a-73c2714a5c2d', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('aae4c427-56e6-4893-8f0a-73c2714a5c2d', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('b6ceefc5-6d74-421e-8bb8-7c21e11235d3', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('b6ceefc5-6d74-421e-8bb8-7c21e11235d3', 'picture', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('b6ceefc5-6d74-421e-8bb8-7c21e11235d3', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('b6ceefc5-6d74-421e-8bb8-7c21e11235d3', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('b6ceefc5-6d74-421e-8bb8-7c21e11235d3', 'picture', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('b6ceefc5-6d74-421e-8bb8-7c21e11235d3', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('bde0cd07-21f9-4cba-9df9-c821a1233350', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('bde0cd07-21f9-4cba-9df9-c821a1233350', 'email', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('bde0cd07-21f9-4cba-9df9-c821a1233350', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('bde0cd07-21f9-4cba-9df9-c821a1233350', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('bde0cd07-21f9-4cba-9df9-c821a1233350', 'email', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('bde0cd07-21f9-4cba-9df9-c821a1233350', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('c29c7ab7-6d61-48e9-8ed4-ffc49de36aea', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('c29c7ab7-6d61-48e9-8ed4-ffc49de36aea', 'website', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('c29c7ab7-6d61-48e9-8ed4-ffc49de36aea', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('c29c7ab7-6d61-48e9-8ed4-ffc49de36aea', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('c29c7ab7-6d61-48e9-8ed4-ffc49de36aea', 'website', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('c29c7ab7-6d61-48e9-8ed4-ffc49de36aea', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('c5adc9d5-d987-4519-8a65-1a10dff3b21e', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('c5adc9d5-d987-4519-8a65-1a10dff3b21e', 'clientAddress', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('c5adc9d5-d987-4519-8a65-1a10dff3b21e', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('c5adc9d5-d987-4519-8a65-1a10dff3b21e', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('c5adc9d5-d987-4519-8a65-1a10dff3b21e', 'clientAddress', 'user.session.note');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('c8252c06-c5bc-49e0-a967-255c0d967f6c', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('c8252c06-c5bc-49e0-a967-255c0d967f6c', 'zoneinfo', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('c8252c06-c5bc-49e0-a967-255c0d967f6c', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('c8252c06-c5bc-49e0-a967-255c0d967f6c', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('c8252c06-c5bc-49e0-a967-255c0d967f6c', 'zoneinfo', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('c8252c06-c5bc-49e0-a967-255c0d967f6c', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('d0366be0-65d0-4718-adc5-680383842ed1', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('d0366be0-65d0-4718-adc5-680383842ed1', 'locale', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('d0366be0-65d0-4718-adc5-680383842ed1', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('d0366be0-65d0-4718-adc5-680383842ed1', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('d0366be0-65d0-4718-adc5-680383842ed1', 'locale', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('d0366be0-65d0-4718-adc5-680383842ed1', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('d315dda0-9a45-4e6e-a5e0-916e728abf20', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('d315dda0-9a45-4e6e-a5e0-916e728abf20', 'locale', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('d315dda0-9a45-4e6e-a5e0-916e728abf20', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('d315dda0-9a45-4e6e-a5e0-916e728abf20', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('d315dda0-9a45-4e6e-a5e0-916e728abf20', 'locale', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('d315dda0-9a45-4e6e-a5e0-916e728abf20', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('dca220e9-c3a5-4156-91bd-22c7f3dadf9b', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('dca220e9-c3a5-4156-91bd-22c7f3dadf9b', 'upn', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('dca220e9-c3a5-4156-91bd-22c7f3dadf9b', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('dca220e9-c3a5-4156-91bd-22c7f3dadf9b', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('dca220e9-c3a5-4156-91bd-22c7f3dadf9b', 'username', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('dca220e9-c3a5-4156-91bd-22c7f3dadf9b', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('de3e87b5-5e45-4e89-909d-b57910327503', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('de3e87b5-5e45-4e89-909d-b57910327503', 'birthdate', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('de3e87b5-5e45-4e89-909d-b57910327503', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('de3e87b5-5e45-4e89-909d-b57910327503', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('de3e87b5-5e45-4e89-909d-b57910327503', 'birthdate', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('de3e87b5-5e45-4e89-909d-b57910327503', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('e507a587-82f0-46a1-a4fd-f8bb2dc1f2f8', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('e507a587-82f0-46a1-a4fd-f8bb2dc1f2f8', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('e507a587-82f0-46a1-a4fd-f8bb2dc1f2f8', 'country', 'user.attribute.country');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('e507a587-82f0-46a1-a4fd-f8bb2dc1f2f8', 'formatted', 'user.attribute.formatted');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('e507a587-82f0-46a1-a4fd-f8bb2dc1f2f8', 'locality', 'user.attribute.locality');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('e507a587-82f0-46a1-a4fd-f8bb2dc1f2f8', 'postal_code', 'user.attribute.postal_code');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('e507a587-82f0-46a1-a4fd-f8bb2dc1f2f8', 'region', 'user.attribute.region');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('e507a587-82f0-46a1-a4fd-f8bb2dc1f2f8', 'street', 'user.attribute.street');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('e507a587-82f0-46a1-a4fd-f8bb2dc1f2f8', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('ef16033b-7e59-4e94-85a5-3b99dfa73b3b', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('ef16033b-7e59-4e94-85a5-3b99dfa73b3b', 'email', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('ef16033b-7e59-4e94-85a5-3b99dfa73b3b', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('ef16033b-7e59-4e94-85a5-3b99dfa73b3b', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('ef16033b-7e59-4e94-85a5-3b99dfa73b3b', 'email', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('ef16033b-7e59-4e94-85a5-3b99dfa73b3b', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('f066cca6-19b2-4f1a-8d07-90b9a2c2fde7', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('f066cca6-19b2-4f1a-8d07-90b9a2c2fde7', 'middle_name', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('f066cca6-19b2-4f1a-8d07-90b9a2c2fde7', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('f066cca6-19b2-4f1a-8d07-90b9a2c2fde7', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('f066cca6-19b2-4f1a-8d07-90b9a2c2fde7', 'middleName', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('f066cca6-19b2-4f1a-8d07-90b9a2c2fde7', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('f4a846d3-8876-4644-84e7-9d086f9a9478', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('f4a846d3-8876-4644-84e7-9d086f9a9478', 'profile', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('f4a846d3-8876-4644-84e7-9d086f9a9478', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('f4a846d3-8876-4644-84e7-9d086f9a9478', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('f4a846d3-8876-4644-84e7-9d086f9a9478', 'profile', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('f4a846d3-8876-4644-84e7-9d086f9a9478', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('f9b5055c-1e2b-41ce-b658-cca20f3c3adc', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('f9b5055c-1e2b-41ce-b658-cca20f3c3adc', 'picture', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('f9b5055c-1e2b-41ce-b658-cca20f3c3adc', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('f9b5055c-1e2b-41ce-b658-cca20f3c3adc', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('f9b5055c-1e2b-41ce-b658-cca20f3c3adc', 'picture', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('f9b5055c-1e2b-41ce-b658-cca20f3c3adc', 'true', 'userinfo.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('fe856f7c-0738-435e-ae54-8262d75473f3', 'true', 'access.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('fe856f7c-0738-435e-ae54-8262d75473f3', 'locale', 'claim.name');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('fe856f7c-0738-435e-ae54-8262d75473f3', 'true', 'id.token.claim');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('fe856f7c-0738-435e-ae54-8262d75473f3', 'String', 'jsonType.label');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('fe856f7c-0738-435e-ae54-8262d75473f3', 'locale', 'user.attribute');
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('fe856f7c-0738-435e-ae54-8262d75473f3', 'true', 'userinfo.token.claim');

-- ----------------------------
-- Table structure for REALM
-- ----------------------------
DROP TABLE IF EXISTS `REALM`;
CREATE TABLE `REALM`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `ACCESS_CODE_LIFESPAN` int(11) DEFAULT NULL,
  `USER_ACTION_LIFESPAN` int(11) DEFAULT NULL,
  `ACCESS_TOKEN_LIFESPAN` int(11) DEFAULT NULL,
  `ACCOUNT_THEME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `ADMIN_THEME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `EMAIL_THEME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `EVENTS_ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `EVENTS_EXPIRATION` bigint(20) DEFAULT NULL,
  `LOGIN_THEME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `NOT_BEFORE` int(11) DEFAULT NULL,
  `PASSWORD_POLICY` varchar(2550) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `REGISTRATION_ALLOWED` bit(1) NOT NULL DEFAULT b'0',
  `REMEMBER_ME` bit(1) NOT NULL DEFAULT b'0',
  `RESET_PASSWORD_ALLOWED` bit(1) NOT NULL DEFAULT b'0',
  `SOCIAL` bit(1) NOT NULL DEFAULT b'0',
  `SSL_REQUIRED` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `SSO_IDLE_TIMEOUT` int(11) DEFAULT NULL,
  `SSO_MAX_LIFESPAN` int(11) DEFAULT NULL,
  `UPDATE_PROFILE_ON_SOC_LOGIN` bit(1) NOT NULL DEFAULT b'0',
  `VERIFY_EMAIL` bit(1) NOT NULL DEFAULT b'0',
  `MASTER_ADMIN_CLIENT` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `LOGIN_LIFESPAN` int(11) DEFAULT NULL,
  `INTERNATIONALIZATION_ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `DEFAULT_LOCALE` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `REG_EMAIL_AS_USERNAME` bit(1) NOT NULL DEFAULT b'0',
  `ADMIN_EVENTS_ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `ADMIN_EVENTS_DETAILS_ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `EDIT_USERNAME_ALLOWED` bit(1) NOT NULL DEFAULT b'0',
  `OTP_POLICY_COUNTER` int(11) DEFAULT 0,
  `OTP_POLICY_WINDOW` int(11) DEFAULT 1,
  `OTP_POLICY_PERIOD` int(11) DEFAULT 30,
  `OTP_POLICY_DIGITS` int(11) DEFAULT 6,
  `OTP_POLICY_ALG` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT 'HmacSHA1',
  `OTP_POLICY_TYPE` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT 'totp',
  `BROWSER_FLOW` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `REGISTRATION_FLOW` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `DIRECT_GRANT_FLOW` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `RESET_CREDENTIALS_FLOW` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `CLIENT_AUTH_FLOW` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `OFFLINE_SESSION_IDLE_TIMEOUT` int(11) DEFAULT 0,
  `REVOKE_REFRESH_TOKEN` bit(1) NOT NULL DEFAULT b'0',
  `ACCESS_TOKEN_LIFE_IMPLICIT` int(11) DEFAULT 0,
  `LOGIN_WITH_EMAIL_ALLOWED` bit(1) NOT NULL DEFAULT b'1',
  `DUPLICATE_EMAILS_ALLOWED` bit(1) NOT NULL DEFAULT b'0',
  `DOCKER_AUTH_FLOW` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `REFRESH_TOKEN_MAX_REUSE` int(11) DEFAULT 0,
  `ALLOW_USER_MANAGED_ACCESS` bit(1) NOT NULL DEFAULT b'0',
  `SSO_MAX_LIFESPAN_REMEMBER_ME` int(11) NOT NULL,
  `SSO_IDLE_TIMEOUT_REMEMBER_ME` int(11) NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `UK_ORVSDMLA56612EAEFIQ6WL5OI`(`NAME`) USING BTREE,
  INDEX `IDX_REALM_MASTER_ADM_CLI`(`MASTER_ADMIN_CLIENT`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of REALM
-- ----------------------------
INSERT INTO `REALM` VALUES ('demo', 60, 300, 300, NULL, NULL, NULL, b'1', b'0', 0, NULL, 'demo', 0, NULL, b'0', b'0', b'0', b'0', 'EXTERNAL', 1800, 36000, b'0', b'0', '7515bb98-a373-4166-b03c-22aa4f14cea4', 1800, b'0', NULL, b'0', b'0', b'0', b'0', 0, 1, 30, 6, 'HmacSHA1', 'totp', 'ad5470c8-4e0c-4144-98b6-37e8089c6d1a', '3844c363-faa4-497e-8b9d-45fbc407e479', 'fefadc8e-45e8-4fb5-a00e-dfd976f05408', 'd8e22d97-5a49-4841-ab5e-58f48b7b3752', '45262779-1e0f-4bd2-907a-2c5c6203eb4c', 2592000, b'0', 900, b'1', b'0', '7432f960-4836-4bbe-b4be-01943a80a813', 0, b'0', 0, 0);
INSERT INTO `REALM` VALUES ('master', 60, 300, 60, NULL, NULL, NULL, b'1', b'0', 0, NULL, 'master', 0, NULL, b'0', b'0', b'0', b'0', 'EXTERNAL', 1800, 36000, b'0', b'0', 'b8e29510-4692-4ac8-8aba-fc6882c30fc7', 1800, b'0', NULL, b'0', b'0', b'0', b'0', 0, 1, 30, 6, 'HmacSHA1', 'totp', 'b5b8c015-7a28-44e9-9f44-ee412fc1ae94', '0aa31c40-d28e-4442-b442-f871fdff5498', '4c22b971-6ad7-4660-90de-81e672a27497', 'bf641639-e075-479d-9a93-505405935d98', '12b75397-babc-4d8a-9836-c4d9b373b53a', 2592000, b'0', 900, b'1', b'0', 'ae8f6231-743e-43a7-8c1f-38a73df4e2f0', 0, b'0', 0, 0);

-- ----------------------------
-- Table structure for REALM_ATTRIBUTE
-- ----------------------------
DROP TABLE IF EXISTS `REALM_ATTRIBUTE`;
CREATE TABLE `REALM_ATTRIBUTE`  (
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VALUE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`NAME`, `REALM_ID`) USING BTREE,
  INDEX `IDX_REALM_ATTR_REALM`(`REALM_ID`) USING BTREE,
  CONSTRAINT `FK_8SHXD6L3E9ATQUKACXGPFFPTW` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of REALM_ATTRIBUTE
-- ----------------------------
INSERT INTO `REALM_ATTRIBUTE` VALUES ('actionTokenGeneratedByAdminLifespan', '43200', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('actionTokenGeneratedByUserLifespan', '300', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('bruteForceProtected', 'false', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('bruteForceProtected', 'false', 'master');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('displayName', 'Keycloak', 'master');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('displayNameHtml', '<div class=\"kc-logo-text\"><span>Keycloak</span></div>', 'master');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('failureFactor', '30', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('failureFactor', '30', 'master');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('maxDeltaTimeSeconds', '43200', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('maxDeltaTimeSeconds', '43200', 'master');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('maxFailureWaitSeconds', '900', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('maxFailureWaitSeconds', '900', 'master');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('minimumQuickLoginWaitSeconds', '60', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('minimumQuickLoginWaitSeconds', '60', 'master');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('offlineSessionMaxLifespan', '5184000', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('offlineSessionMaxLifespan', '5184000', 'master');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('offlineSessionMaxLifespanEnabled', 'false', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('offlineSessionMaxLifespanEnabled', 'false', 'master');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('permanentLockout', 'false', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('permanentLockout', 'false', 'master');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('quickLoginCheckMilliSeconds', '1000', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('quickLoginCheckMilliSeconds', '1000', 'master');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('waitIncrementSeconds', '60', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('waitIncrementSeconds', '60', 'master');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('webAuthnPolicyAttestationConveyancePreference', 'not specified', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('webAuthnPolicyAttestationConveyancePreferencePasswordless', 'not specified', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('webAuthnPolicyAuthenticatorAttachment', 'not specified', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('webAuthnPolicyAuthenticatorAttachmentPasswordless', 'not specified', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('webAuthnPolicyAvoidSameAuthenticatorRegister', 'false', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('webAuthnPolicyAvoidSameAuthenticatorRegisterPasswordless', 'false', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('webAuthnPolicyCreateTimeout', '0', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('webAuthnPolicyCreateTimeoutPasswordless', '0', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('webAuthnPolicyRequireResidentKey', 'not specified', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('webAuthnPolicyRequireResidentKeyPasswordless', 'not specified', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('webAuthnPolicyRpEntityName', 'keycloak', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('webAuthnPolicyRpEntityNamePasswordless', 'keycloak', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('webAuthnPolicyRpId', '', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('webAuthnPolicyRpIdPasswordless', '', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('webAuthnPolicySignatureAlgorithms', 'ES256', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('webAuthnPolicySignatureAlgorithmsPasswordless', 'ES256', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('webAuthnPolicyUserVerificationRequirement', 'not specified', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('webAuthnPolicyUserVerificationRequirementPasswordless', 'not specified', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('_browser_header.contentSecurityPolicy', 'frame-src \'self\'; frame-ancestors \'self\'; object-src \'none\';', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('_browser_header.contentSecurityPolicy', 'frame-src \'self\'; frame-ancestors \'self\'; object-src \'none\';', 'master');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('_browser_header.contentSecurityPolicyReportOnly', '', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('_browser_header.contentSecurityPolicyReportOnly', '', 'master');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('_browser_header.strictTransportSecurity', 'max-age=31536000; includeSubDomains', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('_browser_header.strictTransportSecurity', 'max-age=31536000; includeSubDomains', 'master');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('_browser_header.xContentTypeOptions', 'nosniff', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('_browser_header.xContentTypeOptions', 'nosniff', 'master');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('_browser_header.xFrameOptions', 'SAMEORIGIN', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('_browser_header.xFrameOptions', 'SAMEORIGIN', 'master');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('_browser_header.xRobotsTag', 'none', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('_browser_header.xRobotsTag', 'none', 'master');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('_browser_header.xXSSProtection', '1; mode=block', 'demo');
INSERT INTO `REALM_ATTRIBUTE` VALUES ('_browser_header.xXSSProtection', '1; mode=block', 'master');

-- ----------------------------
-- Table structure for REALM_DEFAULT_GROUPS
-- ----------------------------
DROP TABLE IF EXISTS `REALM_DEFAULT_GROUPS`;
CREATE TABLE `REALM_DEFAULT_GROUPS`  (
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `GROUP_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`REALM_ID`, `GROUP_ID`) USING BTREE,
  UNIQUE INDEX `CON_GROUP_ID_DEF_GROUPS`(`GROUP_ID`) USING BTREE,
  INDEX `IDX_REALM_DEF_GRP_REALM`(`REALM_ID`) USING BTREE,
  CONSTRAINT `FK_DEF_GROUPS_GROUP` FOREIGN KEY (`GROUP_ID`) REFERENCES `KEYCLOAK_GROUP` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_DEF_GROUPS_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for REALM_DEFAULT_ROLES
-- ----------------------------
DROP TABLE IF EXISTS `REALM_DEFAULT_ROLES`;
CREATE TABLE `REALM_DEFAULT_ROLES`  (
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `ROLE_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`REALM_ID`, `ROLE_ID`) USING BTREE,
  UNIQUE INDEX `UK_H4WPD7W4HSOOLNI3H0SW7BTJE`(`ROLE_ID`) USING BTREE,
  INDEX `IDX_REALM_DEF_ROLES_REALM`(`REALM_ID`) USING BTREE,
  CONSTRAINT `FK_EVUDB1PPW84OXFAX2DRS03ICC` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_H4WPD7W4HSOOLNI3H0SW7BTJE` FOREIGN KEY (`ROLE_ID`) REFERENCES `KEYCLOAK_ROLE` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of REALM_DEFAULT_ROLES
-- ----------------------------
INSERT INTO `REALM_DEFAULT_ROLES` VALUES ('master', '07b566ca-e0e1-4032-98cb-5bbd0ffe0ae4');
INSERT INTO `REALM_DEFAULT_ROLES` VALUES ('demo', '90aa7861-6913-4f70-b72d-c0a5cbcef83f');
INSERT INTO `REALM_DEFAULT_ROLES` VALUES ('demo', '93e446e1-b50c-4261-9e26-a1b9e8989a77');
INSERT INTO `REALM_DEFAULT_ROLES` VALUES ('master', 'cfe52ad4-e562-469a-878e-6930aadac1a1');

-- ----------------------------
-- Table structure for REALM_ENABLED_EVENT_TYPES
-- ----------------------------
DROP TABLE IF EXISTS `REALM_ENABLED_EVENT_TYPES`;
CREATE TABLE `REALM_ENABLED_EVENT_TYPES`  (
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VALUE` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`REALM_ID`, `VALUE`) USING BTREE,
  INDEX `IDX_REALM_EVT_TYPES_REALM`(`REALM_ID`) USING BTREE,
  CONSTRAINT `FK_H846O4H0W8EPX5NWEDRF5Y69J` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for REALM_EVENTS_LISTENERS
-- ----------------------------
DROP TABLE IF EXISTS `REALM_EVENTS_LISTENERS`;
CREATE TABLE `REALM_EVENTS_LISTENERS`  (
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VALUE` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`REALM_ID`, `VALUE`) USING BTREE,
  INDEX `IDX_REALM_EVT_LIST_REALM`(`REALM_ID`) USING BTREE,
  CONSTRAINT `FK_H846O4H0W8EPX5NXEV9F5Y69J` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of REALM_EVENTS_LISTENERS
-- ----------------------------
INSERT INTO `REALM_EVENTS_LISTENERS` VALUES ('demo', 'jboss-logging');
INSERT INTO `REALM_EVENTS_LISTENERS` VALUES ('master', 'jboss-logging');

-- ----------------------------
-- Table structure for REALM_REQUIRED_CREDENTIAL
-- ----------------------------
DROP TABLE IF EXISTS `REALM_REQUIRED_CREDENTIAL`;
CREATE TABLE `REALM_REQUIRED_CREDENTIAL`  (
  `TYPE` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `FORM_LABEL` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `INPUT` bit(1) NOT NULL DEFAULT b'0',
  `SECRET` bit(1) NOT NULL DEFAULT b'0',
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`REALM_ID`, `TYPE`) USING BTREE,
  CONSTRAINT `FK_5HG65LYBEVAVKQFKI3KPONH9V` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of REALM_REQUIRED_CREDENTIAL
-- ----------------------------
INSERT INTO `REALM_REQUIRED_CREDENTIAL` VALUES ('password', 'password', b'1', b'1', 'demo');
INSERT INTO `REALM_REQUIRED_CREDENTIAL` VALUES ('password', 'password', b'1', b'1', 'master');

-- ----------------------------
-- Table structure for REALM_SMTP_CONFIG
-- ----------------------------
DROP TABLE IF EXISTS `REALM_SMTP_CONFIG`;
CREATE TABLE `REALM_SMTP_CONFIG`  (
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VALUE` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`REALM_ID`, `NAME`) USING BTREE,
  CONSTRAINT `FK_70EJ8XDXGXD0B9HH6180IRR0O` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for REALM_SUPPORTED_LOCALES
-- ----------------------------
DROP TABLE IF EXISTS `REALM_SUPPORTED_LOCALES`;
CREATE TABLE `REALM_SUPPORTED_LOCALES`  (
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VALUE` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`REALM_ID`, `VALUE`) USING BTREE,
  INDEX `IDX_REALM_SUPP_LOCAL_REALM`(`REALM_ID`) USING BTREE,
  CONSTRAINT `FK_SUPPORTED_LOCALES_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for REDIRECT_URIS
-- ----------------------------
DROP TABLE IF EXISTS `REDIRECT_URIS`;
CREATE TABLE `REDIRECT_URIS`  (
  `CLIENT_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VALUE` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`CLIENT_ID`, `VALUE`) USING BTREE,
  INDEX `IDX_REDIR_URI_CLIENT`(`CLIENT_ID`) USING BTREE,
  CONSTRAINT `FK_1BURS8PB4OUJ97H5WUPPAHV9F` FOREIGN KEY (`CLIENT_ID`) REFERENCES `CLIENT` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of REDIRECT_URIS
-- ----------------------------
INSERT INTO `REDIRECT_URIS` VALUES ('23979e0b-c766-42bc-aaa3-f320a171837a', '/realms/master/account/*');
INSERT INTO `REDIRECT_URIS` VALUES ('605a5c15-5cfb-4453-b771-313bb7239afd', '/admin/master/console/*');
INSERT INTO `REDIRECT_URIS` VALUES ('80e2ea2a-23ba-4fed-af92-cabc4d2ea4ed', '/realms/demo/account/*');
INSERT INTO `REDIRECT_URIS` VALUES ('8cedc74f-6b51-42a1-ba12-e2b80c11d39a', '/*');
INSERT INTO `REDIRECT_URIS` VALUES ('9f327fb4-6a74-4b8f-8a22-03c07dcc6475', '/realms/master/account/*');
INSERT INTO `REDIRECT_URIS` VALUES ('aa33da79-41ff-42be-9518-96b12bcd8055', '/realms/demo/account/*');
INSERT INTO `REDIRECT_URIS` VALUES ('f2fba72e-4130-482a-9ac3-50674c17fe38', '/admin/demo/console/*');

-- ----------------------------
-- Table structure for REQUIRED_ACTION_CONFIG
-- ----------------------------
DROP TABLE IF EXISTS `REQUIRED_ACTION_CONFIG`;
CREATE TABLE `REQUIRED_ACTION_CONFIG`  (
  `REQUIRED_ACTION_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VALUE` longtext CHARACTER SET latin1 COLLATE latin1_swedish_ci,
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`REQUIRED_ACTION_ID`, `NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for REQUIRED_ACTION_PROVIDER
-- ----------------------------
DROP TABLE IF EXISTS `REQUIRED_ACTION_PROVIDER`;
CREATE TABLE `REQUIRED_ACTION_PROVIDER`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `ALIAS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `DEFAULT_ACTION` bit(1) NOT NULL DEFAULT b'0',
  `PROVIDER_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `IDX_REQ_ACT_PROV_REALM`(`REALM_ID`) USING BTREE,
  CONSTRAINT `FK_REQ_ACT_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of REQUIRED_ACTION_PROVIDER
-- ----------------------------
INSERT INTO `REQUIRED_ACTION_PROVIDER` VALUES ('14b512d7-9dec-4c52-a6a5-85b434b984c0', 'update_user_locale', 'Update User Locale', 'master', b'1', b'0', 'update_user_locale', 1000);
INSERT INTO `REQUIRED_ACTION_PROVIDER` VALUES ('2009b385-3d68-4e15-bc8b-8ea59fdcecef', 'UPDATE_PROFILE', 'Update Profile', 'master', b'1', b'0', 'UPDATE_PROFILE', 40);
INSERT INTO `REQUIRED_ACTION_PROVIDER` VALUES ('322fde45-f718-4a7a-a6ab-db7da0a9e530', 'update_user_locale', 'Update User Locale', 'demo', b'1', b'0', 'update_user_locale', 1000);
INSERT INTO `REQUIRED_ACTION_PROVIDER` VALUES ('59e6375f-691f-45b6-80f4-5957d61185ee', 'VERIFY_EMAIL', 'Verify Email', 'master', b'1', b'0', 'VERIFY_EMAIL', 50);
INSERT INTO `REQUIRED_ACTION_PROVIDER` VALUES ('67474c2e-3f54-466e-9d57-244dec811f35', 'VERIFY_EMAIL', 'Verify Email', 'demo', b'1', b'0', 'VERIFY_EMAIL', 50);
INSERT INTO `REQUIRED_ACTION_PROVIDER` VALUES ('818f9f96-8682-413f-8daf-fb5ea1ec7bab', 'terms_and_conditions', 'Terms and Conditions', 'demo', b'0', b'0', 'terms_and_conditions', 20);
INSERT INTO `REQUIRED_ACTION_PROVIDER` VALUES ('8702b1f6-61fc-443a-8dfa-cf5bf321a239', 'UPDATE_PASSWORD', 'Update Password', 'demo', b'1', b'0', 'UPDATE_PASSWORD', 30);
INSERT INTO `REQUIRED_ACTION_PROVIDER` VALUES ('99b9cb20-bc8e-464f-a921-eca1965127aa', 'UPDATE_PROFILE', 'Update Profile', 'demo', b'1', b'0', 'UPDATE_PROFILE', 40);
INSERT INTO `REQUIRED_ACTION_PROVIDER` VALUES ('b06729da-87f5-4a0f-b0c4-3e2c5803c849', 'terms_and_conditions', 'Terms and Conditions', 'master', b'0', b'0', 'terms_and_conditions', 20);
INSERT INTO `REQUIRED_ACTION_PROVIDER` VALUES ('c667f457-fb54-47e0-bd01-758761df063b', 'CONFIGURE_TOTP', 'Configure OTP', 'demo', b'1', b'0', 'CONFIGURE_TOTP', 10);
INSERT INTO `REQUIRED_ACTION_PROVIDER` VALUES ('e6fe2355-520c-4f58-824b-8c484a119f47', 'CONFIGURE_TOTP', 'Configure OTP', 'master', b'1', b'0', 'CONFIGURE_TOTP', 10);
INSERT INTO `REQUIRED_ACTION_PROVIDER` VALUES ('fa7a0458-81e5-46f6-9c7f-6dd96b809d17', 'UPDATE_PASSWORD', 'Update Password', 'master', b'1', b'0', 'UPDATE_PASSWORD', 30);

-- ----------------------------
-- Table structure for RESOURCE_ATTRIBUTE
-- ----------------------------
DROP TABLE IF EXISTS `RESOURCE_ATTRIBUTE`;
CREATE TABLE `RESOURCE_ATTRIBUTE`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL DEFAULT 'sybase-needs-something-here',
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VALUE` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `RESOURCE_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `FK_5HRM2VLF9QL5FU022KQEPOVBR`(`RESOURCE_ID`) USING BTREE,
  CONSTRAINT `FK_5HRM2VLF9QL5FU022KQEPOVBR` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `RESOURCE_SERVER_RESOURCE` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for RESOURCE_POLICY
-- ----------------------------
DROP TABLE IF EXISTS `RESOURCE_POLICY`;
CREATE TABLE `RESOURCE_POLICY`  (
  `RESOURCE_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `POLICY_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`RESOURCE_ID`, `POLICY_ID`) USING BTREE,
  INDEX `IDX_RES_POLICY_POLICY`(`POLICY_ID`) USING BTREE,
  CONSTRAINT `FK_FRSRPOS53XCX4WNKOG82SSRFY` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `RESOURCE_SERVER_RESOURCE` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_FRSRPP213XCX4WNKOG82SSRFY` FOREIGN KEY (`POLICY_ID`) REFERENCES `RESOURCE_SERVER_POLICY` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for RESOURCE_SCOPE
-- ----------------------------
DROP TABLE IF EXISTS `RESOURCE_SCOPE`;
CREATE TABLE `RESOURCE_SCOPE`  (
  `RESOURCE_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `SCOPE_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`RESOURCE_ID`, `SCOPE_ID`) USING BTREE,
  INDEX `IDX_RES_SCOPE_SCOPE`(`SCOPE_ID`) USING BTREE,
  CONSTRAINT `FK_FRSRPOS13XCX4WNKOG82SSRFY` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `RESOURCE_SERVER_RESOURCE` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_FRSRPS213XCX4WNKOG82SSRFY` FOREIGN KEY (`SCOPE_ID`) REFERENCES `RESOURCE_SERVER_SCOPE` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for RESOURCE_SERVER
-- ----------------------------
DROP TABLE IF EXISTS `RESOURCE_SERVER`;
CREATE TABLE `RESOURCE_SERVER`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `ALLOW_RS_REMOTE_MGMT` bit(1) NOT NULL DEFAULT b'0',
  `POLICY_ENFORCE_MODE` varchar(15) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `DECISION_STRATEGY` tinyint(4) NOT NULL DEFAULT 1,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of RESOURCE_SERVER
-- ----------------------------
INSERT INTO `RESOURCE_SERVER` VALUES ('8cedc74f-6b51-42a1-ba12-e2b80c11d39a', b'1', '0', 1);

-- ----------------------------
-- Table structure for RESOURCE_SERVER_PERM_TICKET
-- ----------------------------
DROP TABLE IF EXISTS `RESOURCE_SERVER_PERM_TICKET`;
CREATE TABLE `RESOURCE_SERVER_PERM_TICKET`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `OWNER` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `REQUESTER` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `CREATED_TIMESTAMP` bigint(20) NOT NULL,
  `GRANTED_TIMESTAMP` bigint(20) DEFAULT NULL,
  `RESOURCE_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `SCOPE_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `RESOURCE_SERVER_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `POLICY_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `UK_FRSR6T700S9V50BU18WS5PMT`(`OWNER`, `REQUESTER`, `RESOURCE_SERVER_ID`, `RESOURCE_ID`, `SCOPE_ID`) USING BTREE,
  INDEX `FK_FRSRHO213XCX4WNKOG82SSPMT`(`RESOURCE_SERVER_ID`) USING BTREE,
  INDEX `FK_FRSRHO213XCX4WNKOG83SSPMT`(`RESOURCE_ID`) USING BTREE,
  INDEX `FK_FRSRHO213XCX4WNKOG84SSPMT`(`SCOPE_ID`) USING BTREE,
  INDEX `FK_FRSRPO2128CX4WNKOG82SSRFY`(`POLICY_ID`) USING BTREE,
  CONSTRAINT `FK_FRSRHO213XCX4WNKOG82SSPMT` FOREIGN KEY (`RESOURCE_SERVER_ID`) REFERENCES `RESOURCE_SERVER` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_FRSRHO213XCX4WNKOG83SSPMT` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `RESOURCE_SERVER_RESOURCE` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_FRSRHO213XCX4WNKOG84SSPMT` FOREIGN KEY (`SCOPE_ID`) REFERENCES `RESOURCE_SERVER_SCOPE` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_FRSRPO2128CX4WNKOG82SSRFY` FOREIGN KEY (`POLICY_ID`) REFERENCES `RESOURCE_SERVER_POLICY` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for RESOURCE_SERVER_POLICY
-- ----------------------------
DROP TABLE IF EXISTS `RESOURCE_SERVER_POLICY`;
CREATE TABLE `RESOURCE_SERVER_POLICY`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `TYPE` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `DECISION_STRATEGY` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `LOGIC` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `RESOURCE_SERVER_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `OWNER` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `UK_FRSRPT700S9V50BU18WS5HA6`(`NAME`, `RESOURCE_SERVER_ID`) USING BTREE,
  INDEX `IDX_RES_SERV_POL_RES_SERV`(`RESOURCE_SERVER_ID`) USING BTREE,
  CONSTRAINT `FK_FRSRPO213XCX4WNKOG82SSRFY` FOREIGN KEY (`RESOURCE_SERVER_ID`) REFERENCES `RESOURCE_SERVER` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of RESOURCE_SERVER_POLICY
-- ----------------------------
INSERT INTO `RESOURCE_SERVER_POLICY` VALUES ('2486a449-2904-4b57-ab60-ef3dc73bf49e', 'Default Policy', 'A policy that grants access only for users within this realm', 'js', '0', '0', '8cedc74f-6b51-42a1-ba12-e2b80c11d39a', NULL);
INSERT INTO `RESOURCE_SERVER_POLICY` VALUES ('d4063bf0-5388-40c7-afa2-42ad44cf441e', 'Default Permission', 'A permission that applies to the default resource type', 'resource', '1', '0', '8cedc74f-6b51-42a1-ba12-e2b80c11d39a', NULL);

-- ----------------------------
-- Table structure for RESOURCE_SERVER_RESOURCE
-- ----------------------------
DROP TABLE IF EXISTS `RESOURCE_SERVER_RESOURCE`;
CREATE TABLE `RESOURCE_SERVER_RESOURCE`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `TYPE` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `ICON_URI` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `OWNER` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `RESOURCE_SERVER_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `OWNER_MANAGED_ACCESS` bit(1) NOT NULL DEFAULT b'0',
  `DISPLAY_NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `UK_FRSR6T700S9V50BU18WS5HA6`(`NAME`, `OWNER`, `RESOURCE_SERVER_ID`) USING BTREE,
  INDEX `IDX_RES_SRV_RES_RES_SRV`(`RESOURCE_SERVER_ID`) USING BTREE,
  CONSTRAINT `FK_FRSRHO213XCX4WNKOG82SSRFY` FOREIGN KEY (`RESOURCE_SERVER_ID`) REFERENCES `RESOURCE_SERVER` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of RESOURCE_SERVER_RESOURCE
-- ----------------------------
INSERT INTO `RESOURCE_SERVER_RESOURCE` VALUES ('bdea7365-dda0-4b7e-93c8-90d709445eb5', 'Default Resource', 'urn:democlient:resources:default', NULL, '8cedc74f-6b51-42a1-ba12-e2b80c11d39a', '8cedc74f-6b51-42a1-ba12-e2b80c11d39a', b'0', NULL);

-- ----------------------------
-- Table structure for RESOURCE_SERVER_SCOPE
-- ----------------------------
DROP TABLE IF EXISTS `RESOURCE_SERVER_SCOPE`;
CREATE TABLE `RESOURCE_SERVER_SCOPE`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `ICON_URI` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `RESOURCE_SERVER_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `DISPLAY_NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `UK_FRSRST700S9V50BU18WS5HA6`(`NAME`, `RESOURCE_SERVER_ID`) USING BTREE,
  INDEX `IDX_RES_SRV_SCOPE_RES_SRV`(`RESOURCE_SERVER_ID`) USING BTREE,
  CONSTRAINT `FK_FRSRSO213XCX4WNKOG82SSRFY` FOREIGN KEY (`RESOURCE_SERVER_ID`) REFERENCES `RESOURCE_SERVER` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for RESOURCE_URIS
-- ----------------------------
DROP TABLE IF EXISTS `RESOURCE_URIS`;
CREATE TABLE `RESOURCE_URIS`  (
  `RESOURCE_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VALUE` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`RESOURCE_ID`, `VALUE`) USING BTREE,
  CONSTRAINT `FK_RESOURCE_SERVER_URIS` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `RESOURCE_SERVER_RESOURCE` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of RESOURCE_URIS
-- ----------------------------
INSERT INTO `RESOURCE_URIS` VALUES ('bdea7365-dda0-4b7e-93c8-90d709445eb5', '/*');

-- ----------------------------
-- Table structure for ROLE_ATTRIBUTE
-- ----------------------------
DROP TABLE IF EXISTS `ROLE_ATTRIBUTE`;
CREATE TABLE `ROLE_ATTRIBUTE`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `ROLE_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VALUE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `IDX_ROLE_ATTRIBUTE`(`ROLE_ID`) USING BTREE,
  CONSTRAINT `FK_ROLE_ATTRIBUTE_ID` FOREIGN KEY (`ROLE_ID`) REFERENCES `KEYCLOAK_ROLE` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for SCOPE_MAPPING
-- ----------------------------
DROP TABLE IF EXISTS `SCOPE_MAPPING`;
CREATE TABLE `SCOPE_MAPPING`  (
  `CLIENT_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `ROLE_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`CLIENT_ID`, `ROLE_ID`) USING BTREE,
  INDEX `IDX_SCOPE_MAPPING_ROLE`(`ROLE_ID`) USING BTREE,
  CONSTRAINT `FK_OUSE064PLMLR732LXJCN1Q5F1` FOREIGN KEY (`CLIENT_ID`) REFERENCES `CLIENT` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_P3RH9GRKU11KQFRS4FLTT7RNQ` FOREIGN KEY (`ROLE_ID`) REFERENCES `KEYCLOAK_ROLE` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of SCOPE_MAPPING
-- ----------------------------
INSERT INTO `SCOPE_MAPPING` VALUES ('23979e0b-c766-42bc-aaa3-f320a171837a', '09b53e53-331f-4fbd-8c4d-87c6c62da264');
INSERT INTO `SCOPE_MAPPING` VALUES ('80e2ea2a-23ba-4fed-af92-cabc4d2ea4ed', '63c27012-2e75-4b40-a03b-1538f37bca36');

-- ----------------------------
-- Table structure for SCOPE_POLICY
-- ----------------------------
DROP TABLE IF EXISTS `SCOPE_POLICY`;
CREATE TABLE `SCOPE_POLICY`  (
  `SCOPE_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `POLICY_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`SCOPE_ID`, `POLICY_ID`) USING BTREE,
  INDEX `IDX_SCOPE_POLICY_POLICY`(`POLICY_ID`) USING BTREE,
  CONSTRAINT `FK_FRSRASP13XCX4WNKOG82SSRFY` FOREIGN KEY (`POLICY_ID`) REFERENCES `RESOURCE_SERVER_POLICY` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_FRSRPASS3XCX4WNKOG82SSRFY` FOREIGN KEY (`SCOPE_ID`) REFERENCES `RESOURCE_SERVER_SCOPE` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for USERNAME_LOGIN_FAILURE
-- ----------------------------
DROP TABLE IF EXISTS `USERNAME_LOGIN_FAILURE`;
CREATE TABLE `USERNAME_LOGIN_FAILURE`  (
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `USERNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `FAILED_LOGIN_NOT_BEFORE` int(11) DEFAULT NULL,
  `LAST_FAILURE` bigint(20) DEFAULT NULL,
  `LAST_IP_FAILURE` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `NUM_FAILURES` int(11) DEFAULT NULL,
  PRIMARY KEY (`REALM_ID`, `USERNAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for USER_ATTRIBUTE
-- ----------------------------
DROP TABLE IF EXISTS `USER_ATTRIBUTE`;
CREATE TABLE `USER_ATTRIBUTE`  (
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VALUE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `USER_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL DEFAULT 'sybase-needs-something-here',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `IDX_USER_ATTRIBUTE`(`USER_ID`) USING BTREE,
  CONSTRAINT `FK_5HRM2VLF9QL5FU043KQEPOVBR` FOREIGN KEY (`USER_ID`) REFERENCES `USER_ENTITY` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for USER_CONSENT
-- ----------------------------
DROP TABLE IF EXISTS `USER_CONSENT`;
CREATE TABLE `USER_CONSENT`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `CLIENT_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `USER_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `CREATED_DATE` bigint(20) DEFAULT NULL,
  `LAST_UPDATED_DATE` bigint(20) DEFAULT NULL,
  `CLIENT_STORAGE_PROVIDER` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `EXTERNAL_CLIENT_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `UK_JKUWUVD56ONTGSUHOGM8UEWRT`(`CLIENT_ID`, `CLIENT_STORAGE_PROVIDER`, `EXTERNAL_CLIENT_ID`, `USER_ID`) USING BTREE,
  INDEX `IDX_USER_CONSENT`(`USER_ID`) USING BTREE,
  CONSTRAINT `FK_GRNTCSNT_USER` FOREIGN KEY (`USER_ID`) REFERENCES `USER_ENTITY` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for USER_CONSENT_CLIENT_SCOPE
-- ----------------------------
DROP TABLE IF EXISTS `USER_CONSENT_CLIENT_SCOPE`;
CREATE TABLE `USER_CONSENT_CLIENT_SCOPE`  (
  `USER_CONSENT_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `SCOPE_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`USER_CONSENT_ID`, `SCOPE_ID`) USING BTREE,
  INDEX `IDX_USCONSENT_CLSCOPE`(`USER_CONSENT_ID`) USING BTREE,
  CONSTRAINT `FK_GRNTCSNT_CLSC_USC` FOREIGN KEY (`USER_CONSENT_ID`) REFERENCES `USER_CONSENT` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for USER_ENTITY
-- ----------------------------
DROP TABLE IF EXISTS `USER_ENTITY`;
CREATE TABLE `USER_ENTITY`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `EMAIL` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `EMAIL_CONSTRAINT` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `EMAIL_VERIFIED` bit(1) NOT NULL DEFAULT b'0',
  `ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `FEDERATION_LINK` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `FIRST_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `LAST_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `REALM_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `USERNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `CREATED_TIMESTAMP` bigint(20) DEFAULT NULL,
  `SERVICE_ACCOUNT_CLIENT_LINK` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `NOT_BEFORE` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `UK_DYKN684SL8UP1CRFEI6ECKHD7`(`REALM_ID`, `EMAIL_CONSTRAINT`) USING BTREE,
  UNIQUE INDEX `UK_RU8TT6T700S9V50BU18WS5HA6`(`REALM_ID`, `USERNAME`) USING BTREE,
  INDEX `IDX_USER_EMAIL`(`EMAIL`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of USER_ENTITY
-- ----------------------------
INSERT INTO `USER_ENTITY` VALUES ('621ab63a-7b78-4b1c-9ef8-0d629f0d09fa', NULL, '67f392d3-36a9-4592-b1a8-5822ec3a77b7', b'0', b'1', NULL, NULL, NULL, 'demo', 'service-account-democlient', 1606463876313, '8cedc74f-6b51-42a1-ba12-e2b80c11d39a', 0);
INSERT INTO `USER_ENTITY` VALUES ('9854c0b7-3d91-4a7e-9e74-c2f7438081ae', NULL, '5d6bc4f0-8b57-458d-93d5-39f700e89661', b'0', b'1', NULL, NULL, NULL, 'demo', 'demouser', 1606724070842, NULL, 0);
INSERT INTO `USER_ENTITY` VALUES ('bf1fccf1-0a50-4f64-aa43-0f71b8a16c0c', NULL, '78ad4754-c505-451e-9b8d-41e8135bf02c', b'0', b'1', NULL, NULL, NULL, 'master', 'admin', 1604626608468, NULL, 0);

-- ----------------------------
-- Table structure for USER_FEDERATION_CONFIG
-- ----------------------------
DROP TABLE IF EXISTS `USER_FEDERATION_CONFIG`;
CREATE TABLE `USER_FEDERATION_CONFIG`  (
  `USER_FEDERATION_PROVIDER_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VALUE` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`USER_FEDERATION_PROVIDER_ID`, `NAME`) USING BTREE,
  CONSTRAINT `FK_T13HPU1J94R2EBPEKR39X5EU5` FOREIGN KEY (`USER_FEDERATION_PROVIDER_ID`) REFERENCES `USER_FEDERATION_PROVIDER` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for USER_FEDERATION_MAPPER
-- ----------------------------
DROP TABLE IF EXISTS `USER_FEDERATION_MAPPER`;
CREATE TABLE `USER_FEDERATION_MAPPER`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `FEDERATION_PROVIDER_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `FEDERATION_MAPPER_TYPE` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `IDX_USR_FED_MAP_FED_PRV`(`FEDERATION_PROVIDER_ID`) USING BTREE,
  INDEX `IDX_USR_FED_MAP_REALM`(`REALM_ID`) USING BTREE,
  CONSTRAINT `FK_FEDMAPPERPM_FEDPRV` FOREIGN KEY (`FEDERATION_PROVIDER_ID`) REFERENCES `USER_FEDERATION_PROVIDER` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_FEDMAPPERPM_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for USER_FEDERATION_MAPPER_CONFIG
-- ----------------------------
DROP TABLE IF EXISTS `USER_FEDERATION_MAPPER_CONFIG`;
CREATE TABLE `USER_FEDERATION_MAPPER_CONFIG`  (
  `USER_FEDERATION_MAPPER_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VALUE` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`USER_FEDERATION_MAPPER_ID`, `NAME`) USING BTREE,
  CONSTRAINT `FK_FEDMAPPER_CFG` FOREIGN KEY (`USER_FEDERATION_MAPPER_ID`) REFERENCES `USER_FEDERATION_MAPPER` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for USER_FEDERATION_PROVIDER
-- ----------------------------
DROP TABLE IF EXISTS `USER_FEDERATION_PROVIDER`;
CREATE TABLE `USER_FEDERATION_PROVIDER`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `CHANGED_SYNC_PERIOD` int(11) DEFAULT NULL,
  `DISPLAY_NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `FULL_SYNC_PERIOD` int(11) DEFAULT NULL,
  `LAST_SYNC` int(11) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `PROVIDER_NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `REALM_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `IDX_USR_FED_PRV_REALM`(`REALM_ID`) USING BTREE,
  CONSTRAINT `FK_1FJ32F6PTOLW2QY60CD8N01E8` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for USER_GROUP_MEMBERSHIP
-- ----------------------------
DROP TABLE IF EXISTS `USER_GROUP_MEMBERSHIP`;
CREATE TABLE `USER_GROUP_MEMBERSHIP`  (
  `GROUP_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `USER_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`GROUP_ID`, `USER_ID`) USING BTREE,
  INDEX `IDX_USER_GROUP_MAPPING`(`USER_ID`) USING BTREE,
  CONSTRAINT `FK_USER_GROUP_USER` FOREIGN KEY (`USER_ID`) REFERENCES `USER_ENTITY` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for USER_REQUIRED_ACTION
-- ----------------------------
DROP TABLE IF EXISTS `USER_REQUIRED_ACTION`;
CREATE TABLE `USER_REQUIRED_ACTION`  (
  `USER_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `REQUIRED_ACTION` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL DEFAULT ' ',
  PRIMARY KEY (`REQUIRED_ACTION`, `USER_ID`) USING BTREE,
  INDEX `IDX_USER_REQACTIONS`(`USER_ID`) USING BTREE,
  CONSTRAINT `FK_6QJ3W1JW9CVAFHE19BWSIUVMD` FOREIGN KEY (`USER_ID`) REFERENCES `USER_ENTITY` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for USER_ROLE_MAPPING
-- ----------------------------
DROP TABLE IF EXISTS `USER_ROLE_MAPPING`;
CREATE TABLE `USER_ROLE_MAPPING`  (
  `ROLE_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `USER_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`ROLE_ID`, `USER_ID`) USING BTREE,
  INDEX `IDX_USER_ROLE_MAPPING`(`USER_ID`) USING BTREE,
  CONSTRAINT `FK_C4FQV34P1MBYLLOXANG7B1Q3L` FOREIGN KEY (`USER_ID`) REFERENCES `USER_ENTITY` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of USER_ROLE_MAPPING
-- ----------------------------
INSERT INTO `USER_ROLE_MAPPING` VALUES ('255644dd-b96e-4ff9-a1d2-6a9d1be655ad', '621ab63a-7b78-4b1c-9ef8-0d629f0d09fa');
INSERT INTO `USER_ROLE_MAPPING` VALUES ('63c27012-2e75-4b40-a03b-1538f37bca36', '621ab63a-7b78-4b1c-9ef8-0d629f0d09fa');
INSERT INTO `USER_ROLE_MAPPING` VALUES ('900d9963-6141-47fb-9cf0-fb02d628a7ca', '621ab63a-7b78-4b1c-9ef8-0d629f0d09fa');
INSERT INTO `USER_ROLE_MAPPING` VALUES ('90aa7861-6913-4f70-b72d-c0a5cbcef83f', '621ab63a-7b78-4b1c-9ef8-0d629f0d09fa');
INSERT INTO `USER_ROLE_MAPPING` VALUES ('93e446e1-b50c-4261-9e26-a1b9e8989a77', '621ab63a-7b78-4b1c-9ef8-0d629f0d09fa');
INSERT INTO `USER_ROLE_MAPPING` VALUES ('63c27012-2e75-4b40-a03b-1538f37bca36', '9854c0b7-3d91-4a7e-9e74-c2f7438081ae');
INSERT INTO `USER_ROLE_MAPPING` VALUES ('900d9963-6141-47fb-9cf0-fb02d628a7ca', '9854c0b7-3d91-4a7e-9e74-c2f7438081ae');
INSERT INTO `USER_ROLE_MAPPING` VALUES ('90aa7861-6913-4f70-b72d-c0a5cbcef83f', '9854c0b7-3d91-4a7e-9e74-c2f7438081ae');
INSERT INTO `USER_ROLE_MAPPING` VALUES ('93e446e1-b50c-4261-9e26-a1b9e8989a77', '9854c0b7-3d91-4a7e-9e74-c2f7438081ae');
INSERT INTO `USER_ROLE_MAPPING` VALUES ('07b566ca-e0e1-4032-98cb-5bbd0ffe0ae4', 'bf1fccf1-0a50-4f64-aa43-0f71b8a16c0c');
INSERT INTO `USER_ROLE_MAPPING` VALUES ('09b53e53-331f-4fbd-8c4d-87c6c62da264', 'bf1fccf1-0a50-4f64-aa43-0f71b8a16c0c');
INSERT INTO `USER_ROLE_MAPPING` VALUES ('2ba43097-0c34-4cfa-a7ea-f291fb5fd016', 'bf1fccf1-0a50-4f64-aa43-0f71b8a16c0c');
INSERT INTO `USER_ROLE_MAPPING` VALUES ('cfe52ad4-e562-469a-878e-6930aadac1a1', 'bf1fccf1-0a50-4f64-aa43-0f71b8a16c0c');
INSERT INTO `USER_ROLE_MAPPING` VALUES ('d5eff4ff-3253-4520-bc85-cf0c0d321668', 'bf1fccf1-0a50-4f64-aa43-0f71b8a16c0c');

-- ----------------------------
-- Table structure for USER_SESSION
-- ----------------------------
DROP TABLE IF EXISTS `USER_SESSION`;
CREATE TABLE `USER_SESSION`  (
  `ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `AUTH_METHOD` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `IP_ADDRESS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `LAST_SESSION_REFRESH` int(11) DEFAULT NULL,
  `LOGIN_USERNAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `REALM_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `REMEMBER_ME` bit(1) NOT NULL DEFAULT b'0',
  `STARTED` int(11) DEFAULT NULL,
  `USER_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `USER_SESSION_STATE` int(11) DEFAULT NULL,
  `BROKER_SESSION_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `BROKER_USER_ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for USER_SESSION_NOTE
-- ----------------------------
DROP TABLE IF EXISTS `USER_SESSION_NOTE`;
CREATE TABLE `USER_SESSION_NOTE`  (
  `USER_SESSION` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VALUE` varchar(2048) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`USER_SESSION`, `NAME`) USING BTREE,
  CONSTRAINT `FK5EDFB00FF51D3472` FOREIGN KEY (`USER_SESSION`) REFERENCES `USER_SESSION` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for WEB_ORIGINS
-- ----------------------------
DROP TABLE IF EXISTS `WEB_ORIGINS`;
CREATE TABLE `WEB_ORIGINS`  (
  `CLIENT_ID` varchar(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VALUE` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`CLIENT_ID`, `VALUE`) USING BTREE,
  INDEX `IDX_WEB_ORIG_CLIENT`(`CLIENT_ID`) USING BTREE,
  CONSTRAINT `FK_LOJPHO213XCX4WNKOG82SSRFY` FOREIGN KEY (`CLIENT_ID`) REFERENCES `CLIENT` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of WEB_ORIGINS
-- ----------------------------
INSERT INTO `WEB_ORIGINS` VALUES ('605a5c15-5cfb-4453-b771-313bb7239afd', '+');
INSERT INTO `WEB_ORIGINS` VALUES ('f2fba72e-4130-482a-9ac3-50674c17fe38', '+');

SET FOREIGN_KEY_CHECKS = 1;
