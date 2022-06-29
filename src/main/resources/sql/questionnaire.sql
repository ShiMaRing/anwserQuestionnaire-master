/*
 Navicat Premium Data Transfer

 Source Server         : my
 Source Server Type    : MySQL
 Source Server Version : 50718
 Source Host           : localhost
 Source Database       : questionnaire

 Target Server Type    : MySQL
 Target Server Version : 50718
 File Encoding         : utf-8

 Date: 09/27/2020 22:47:21 PM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `project_info`
-- ----------------------------
DROP TABLE IF EXISTS `project_info`;
CREATE TABLE `project_info`
(
    `id`               varchar(50) NOT NULL COMMENT '项目表主键',
    `user_id`          varchar(50)  DEFAULT NULL COMMENT '用户id（没有用）',
    `project_name`     varchar(100) DEFAULT NULL COMMENT '项目名称',
    `project_content`  text COMMENT '项目说明',
    `created_by`       char(32)     DEFAULT NULL COMMENT '创建人',
    `creation_date`    datetime     DEFAULT NULL COMMENT '创建时间',
    `last_updated_by`  char(32)     DEFAULT NULL COMMENT '最后修改人',
    `last_update_date` datetime     DEFAULT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPACT;

-- ----------------------------
--  Records of `project_info`
-- ----------------------------
BEGIN;
INSERT INTO `project_info`
VALUES ('283dcf241cf245aea824dc10bbb3d680', '8ceeee2995f3459ba1955f85245dc7a5', '第一个项目', '第一个项目描述 问问', 'admin',
        '2020-09-23 20:27:42', 'admin', '2020-09-23 20:27:59'),
       ('4cd6ccb65c894eafaa70b12330f8c2f8', '8ceeee2995f3459ba1955f85245dc7a5', '第一个项目', '第一个项目 的方法v', 'admin',
        '2020-09-27 16:23:20', 'admin', '2020-09-27 16:23:20');
COMMIT;

-- ----------------------------
--  Table structure for `questionnaire_info`
-- ----------------------------
DROP TABLE IF EXISTS `questionnaire_info`;
CREATE TABLE `questionnaire_info`
(
    `id`                   varchar(50) NOT NULL COMMENT '问卷表主键',
    `project_id`           varchar(50)  DEFAULT NULL COMMENT '关联项目id',
    `question_name`        varchar(100) DEFAULT NULL COMMENT '问卷调查名称',
    `question_content`     text COMMENT '问卷调查说明',
    `question_end_content` text COMMENT '答卷结束语',
    `question_stop`        varchar(20)  DEFAULT NULL COMMENT '问卷是否结束？',

    `data_id`              varchar(50)  DEFAULT NULL COMMENT '问卷类型，在校生：2；毕业生：3；教师：4；用人单位：5',
    `answer_total`         varchar(50)  DEFAULT NULL COMMENT '答卷总数?',

    `release_time`         datetime     DEFAULT NULL COMMENT '发布时间',
    `start_time`           datetime     DEFAULT NULL COMMENT '开始时间',
    `end_time`             datetime     DEFAULT NULL COMMENT '结束时间',

    `created_by`           char(32)     DEFAULT NULL COMMENT '创建人',
    `creation_date`        datetime     DEFAULT NULL COMMENT '创建时间',
    `last_updated_by`      char(32)     DEFAULT NULL COMMENT '最后修改人',
    `last_update_date`     datetime     DEFAULT NULL COMMENT '最后修改时间',

    `question_title`       varchar(100) DEFAULT NULL COMMENT '问题标题？',
    `question`             text COMMENT '问题内容？',
    `context`              text COMMENT '短信/邮件/qq发送内容',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPACT;

-- ----------------------------
--  Records of `questionnaire_info`
-- ----------------------------
BEGIN;
INSERT INTO `questionnaire_info`
VALUES ('ec3ac69b-c2bf-4416-b2d1-c8c31364c163', 'a5b8c7ce37d1473aa63467bb92327741', '测试问卷', '测试用123', '感谢您答题', '5', '3',
        '10',
        '2022-06-12 17:27:01', '2022-06-13 17:27:11', '2022-06-14 17:27:18', 'wr', '2022-06-12 17:27:26', 'wr',
        '2022-06-12 17:27:32', '1', '1', '问卷链接：123，欢迎来填写');
COMMIT;
-- ----------------------------
--  Table structure for `user_info`
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`
(
    `id`               varchar(50) NOT NULL COMMENT '用户表主键',
    `username`         varchar(10) DEFAULT NULL COMMENT '用户名',
    `password`         varchar(10) DEFAULT NULL COMMENT '密码',
    `start_time`       datetime    DEFAULT NULL COMMENT '开始时间',
    `stop_time`        datetime    DEFAULT NULL COMMENT '截止时间（时间戳）',
    `status`           varchar(2)  DEFAULT NULL COMMENT '是否启用（1启用，0不启用）',
    `created_by`       char(32)    DEFAULT NULL COMMENT '创建人',
    `creation_date`    datetime    DEFAULT NULL COMMENT '创建时间',
    `last_updated_by`  char(32)    DEFAULT NULL COMMENT '最后修改人',
    `last_update_date` datetime    DEFAULT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPACT;

-- ----------------------------
--  Records of `user_info`
-- ----------------------------
BEGIN;
INSERT INTO `user_info`
VALUES ('8ceeee2995f3459ba1955f85245dc7a5', 'admin', '1', '2018-12-04 21:40:05', '2021-09-27 21:40:00', '1', 'admin',
        '2018-10-22 09:12:40', 'admin', '2018-12-04 21:40:13');
COMMIT;

-- ----------------------------
--  Procedure structure for `demo_in_parameter`
-- ----------------------------
DROP PROCEDURE IF EXISTS `demo_in_parameter`;
delimiter ;;
CREATE
    DEFINER = `edu`@`%` PROCEDURE `demo_in_parameter`(IN p_in int)
BEGIN
    SELECT p_in;
    SET p_in = 2;
    SELECT p_in;
END
;;
delimiter ;

-- ----------------------------
--  Procedure structure for `sp_name`
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_name`;
delimiter ;;
CREATE
    DEFINER = `edu`@`%` PROCEDURE `sp_name`(in id int)
begin
    SELECT id;
    set id = 2;
    SELECT id;
end
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
