CREATE TABLE `meeting_room` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(100) NOT NULL COMMENT '会议室名称',
  `location` varchar(200) DEFAULT NULL COMMENT '位置',
  `capacity` int NOT NULL DEFAULT 0 COMMENT '容量',
  `equipment` varchar(500) DEFAULT NULL COMMENT '设备（JSON格式）',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `creator_id` bigint DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater_id` bigint DEFAULT NULL COMMENT '更新者ID',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` tinyint DEFAULT 0 COMMENT '乐观锁版本号',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会议室表';

CREATE TABLE `meeting_booking` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `room_id` bigint NOT NULL COMMENT '会议室ID',
  `user_id` bigint NOT NULL COMMENT '预约用户ID',
  `title` varchar(200) NOT NULL COMMENT '会议主题',
  `description` text COMMENT '事由/描述',
  `attendees` int DEFAULT 0 COMMENT '参会人数',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态：0-待审批，1-已通过，2-已驳回，3-已取消',
  `creator_id` bigint DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater_id` bigint DEFAULT NULL COMMENT '更新者ID',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` tinyint DEFAULT 0 COMMENT '乐观锁版本号',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_room_id` (`room_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_time` (`start_time`, `end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会议室预约表';

CREATE TABLE `booking_approval` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `booking_id` bigint NOT NULL COMMENT '预约ID',
  `approver_id` bigint NOT NULL COMMENT '审批人ID',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '审批结果：0-待审批，1-通过，2-驳回',
  `comment` varchar(500) DEFAULT NULL COMMENT '审批意见',
  `approval_time` datetime DEFAULT NULL COMMENT '审批时间',
  `creator_id` bigint DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater_id` bigint DEFAULT NULL COMMENT '更新者ID',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` tinyint DEFAULT 0 COMMENT '乐观锁版本号',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_booking_id` (`booking_id`),
  KEY `idx_approver_id` (`approver_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='预约审批表';

CREATE TABLE `meeting_checkin` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `booking_id` bigint NOT NULL COMMENT '预约ID',
  `user_id` bigint NOT NULL COMMENT '签到用户ID',
  `checkin_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
  `creator_id` bigint DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater_id` bigint DEFAULT NULL COMMENT '更新者ID',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` tinyint DEFAULT 0 COMMENT '乐观锁版本号',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_booking_id` (`booking_id`),
  KEY `idx_user_id` (`user_id`),
  UNIQUE KEY `uk_booking_user` (`booking_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会议签到表';

CREATE TABLE `meeting_notification` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `booking_id` bigint NOT NULL COMMENT '预约ID',
  `user_id` bigint NOT NULL COMMENT '接收用户ID',
  `type` tinyint NOT NULL COMMENT '通知类型：1-审批结果通知，2-会议开始提醒',
  `content` varchar(500) DEFAULT NULL COMMENT '通知内容',
  `is_read` tinyint NOT NULL DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
  `read_time` datetime DEFAULT NULL COMMENT '阅读时间',
  `creator_id` bigint DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater_id` bigint DEFAULT NULL COMMENT '更新者ID',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` tinyint DEFAULT 0 COMMENT '乐观锁版本号',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_booking_id` (`booking_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_type` (`type`),
  KEY `idx_is_read` (`is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会议通知表';
