-- H2内存数据库初始化脚本
-- 注意：H2不支持USE语句，直接创建表即可

DROP TABLE IF EXISTS `item`;
CREATE TABLE IF NOT EXISTS `item`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `name`        VARCHAR(100)     DEFAULT NULL COMMENT '名称，最大长度 100，可选',
    `description` VARCHAR(500)     DEFAULT NULL COMMENT '简介/内容，最大长度 500，可选',
    `type`        TINYINT UNSIGNED DEFAULT NULL COMMENT '类型，值范围为 1-127，可选',
    `status`      TINYINT UNSIGNED DEFAULT NULL COMMENT '状态，0 或 1，可选',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID，关联用户表，必须为正整数，可选',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间，可选',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID，关联用户表，必须为正整数，可选',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间，可选',
    `sort_order`  INT              DEFAULT NULL COMMENT '排序字段，最大长度 50',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP逻辑删除字段，0 或 1'
) COMMENT 'item' COLLATE = utf8mb4_unicode_ci;

-- 会议室预约系统表结构
-- 会议室表
DROP TABLE IF EXISTS `meeting_rooms`;
CREATE TABLE IF NOT EXISTS `meeting_rooms`
(   
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '会议室ID' PRIMARY KEY,
    `name`        VARCHAR(100)     NOT NULL COMMENT '会议室名称，唯一',
    `location`    VARCHAR(200)     NOT NULL COMMENT '会议室位置',
    `capacity`    INT              NOT NULL COMMENT '会议室容量',
    `equipment`   VARCHAR(500)     DEFAULT NULL COMMENT '设备清单',
    `is_active`   TINYINT UNSIGNED DEFAULT 1 COMMENT '是否启用，0-停用，1-启用',
    `created_at`  DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updated_at`  DATETIME         DEFAULT NULL COMMENT '更新时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除标志，0-未删除，1-已删除',
    UNIQUE KEY `uk_name` (`name`)
) COMMENT '会议室表' COLLATE = utf8mb4_unicode_ci;

-- 预约记录表
DROP TABLE IF EXISTS `bookings`;
CREATE TABLE IF NOT EXISTS `bookings`
(
    `id`               BIGINT UNSIGNED AUTO_INCREMENT COMMENT '预约ID' PRIMARY KEY,
    `room_id`          BIGINT UNSIGNED  NOT NULL COMMENT '会议室ID',
    `user_id`          BIGINT UNSIGNED  NOT NULL COMMENT '预约用户ID',
    `user_name`        VARCHAR(100)     NOT NULL COMMENT '预约用户名称',
    `user_email`       VARCHAR(100)     DEFAULT NULL COMMENT '预约用户邮箱',
    `start_time`       DATETIME         NOT NULL COMMENT '会议开始时间',
    `end_time`         DATETIME         NOT NULL COMMENT '会议结束时间',
    `subject`          VARCHAR(200)     NOT NULL COMMENT '会议主题',
    `description`      VARCHAR(1000)    DEFAULT NULL COMMENT '会议描述',
    `attendees_count`  INT              DEFAULT NULL COMMENT '参会人数',
    `status`           VARCHAR(20)      DEFAULT 'PENDING' COMMENT '预约状态：PENDING-待审批，APPROVED-已通过，REJECTED-已驳回，CANCELLED-已取消，COMPLETED-已完成',
    `rejection_reason` VARCHAR(500)     DEFAULT NULL COMMENT '驳回原因',
    `created_at`       DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updated_at`       DATETIME         DEFAULT NULL COMMENT '更新时间',
    `is_deleted`       TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除标志',
    INDEX `idx_room_id` (`room_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_start_time` (`start_time`)
) COMMENT '预约记录表' COLLATE = utf8mb4_unicode_ci;

