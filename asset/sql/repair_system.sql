-- 设备报修系统数据库表结构

-- 报修工单表
CREATE TABLE IF NOT EXISTS `repair_order` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '工单ID',
    `order_no` VARCHAR(32) NOT NULL COMMENT '工单编号',
    `user_id` BIGINT NOT NULL COMMENT '报修用户ID',
    `device_type` TINYINT NOT NULL COMMENT '设备类型：1-电脑，2-打印机，3-网络设备，4-其他',
    `location` VARCHAR(255) NOT NULL COMMENT '故障位置',
    `fault_type` TINYINT NOT NULL COMMENT '故障类型：1-硬件故障，2-软件故障，3-网络故障，4-其他',
    `description` TEXT NOT NULL COMMENT '故障描述',
    `images` VARCHAR(1000) DEFAULT NULL COMMENT '故障照片URL，多个用逗号分隔',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '工单状态：0-待处理，1-已分配，2-维修中，3-已完成，4-已取消',
    `priority` TINYINT NOT NULL DEFAULT 1 COMMENT '优先级：1-低，2-中，3-高，4-紧急',
    `assign_type` TINYINT NOT NULL DEFAULT 0 COMMENT '分配方式：0-自动分配，1-手动分配',
    `repairman_id` BIGINT DEFAULT NULL COMMENT '维修人员ID',
    `assigned_time` DATETIME DEFAULT NULL COMMENT '分配时间',
    `completed_time` DATETIME DEFAULT NULL COMMENT '完成时间',
    `creator_id` BIGINT DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id` BIGINT DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version` TINYINT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_repairman_id` (`repairman_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报修工单表';

-- 维修记录表
CREATE TABLE IF NOT EXISTS `repair_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `order_id` BIGINT NOT NULL COMMENT '关联工单ID',
    `repairman_id` BIGINT NOT NULL COMMENT '维修人员ID',
    `action_type` TINYINT NOT NULL COMMENT '操作类型：1-接单，2-开始维修，3-维修完成，4-转单，5-备注',
    `action_desc` VARCHAR(500) NOT NULL COMMENT '操作描述',
    `images` VARCHAR(1000) DEFAULT NULL COMMENT '维修过程照片URL',
    `cost` DECIMAL(10,2) DEFAULT 0.00 COMMENT '维修费用',
    `materials` VARCHAR(500) DEFAULT NULL COMMENT '使用材料',
    `notify_user` TINYINT NOT NULL DEFAULT 0 COMMENT '是否通知用户：0-否，1-是',
    `notify_content` VARCHAR(500) DEFAULT NULL COMMENT '通知内容',
    `creator_id` BIGINT DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id` BIGINT DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version` TINYINT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_repairman_id` (`repairman_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='维修记录表';

-- 维修评价表
CREATE TABLE IF NOT EXISTS `repair_rating` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评价ID',
    `order_id` BIGINT NOT NULL COMMENT '关联工单ID',
    `user_id` BIGINT NOT NULL COMMENT '评价用户ID',
    `rating` TINYINT NOT NULL COMMENT '评分：1-5星',
    `content` VARCHAR(500) DEFAULT NULL COMMENT '评价内容',
    `tags` VARCHAR(200) DEFAULT NULL COMMENT '评价标签，多个用逗号分隔',
    `is_anonymous` TINYINT NOT NULL DEFAULT 0 COMMENT '是否匿名：0-否，1-是',
    `reply` VARCHAR(500) DEFAULT NULL COMMENT '商家回复',
    `reply_time` DATETIME DEFAULT NULL COMMENT '回复时间',
    `creator_id` BIGINT DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id` BIGINT DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version` TINYINT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_id` (`order_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_rating` (`rating`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='维修评价表';
