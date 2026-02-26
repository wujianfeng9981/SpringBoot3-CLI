-- 会议室预约管理系统SQL建表语句

-- 创建会议室表
CREATE TABLE IF NOT EXISTS `meeting_room` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` VARCHAR(255) NOT NULL COMMENT '会议室名称',
  `location` VARCHAR(255) NOT NULL COMMENT '位置',
  `capacity` INT NOT NULL COMMENT '容量',
  `equipment` TEXT COMMENT '设备',
  `is_deleted` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除状态',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`),
  KEY `idx_location` (`location`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会议室表';

-- 创建预约表
CREATE TABLE IF NOT EXISTS `booking` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `room_id` BIGINT UNSIGNED NOT NULL COMMENT '会议室ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `user_name` VARCHAR(255) NOT NULL COMMENT '用户名',
  `start_time` DATETIME NOT NULL COMMENT '开始时间',
  `end_time` DATETIME NOT NULL COMMENT '结束时间',
  `subject` VARCHAR(255) NOT NULL COMMENT '事由',
  `status` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '状态：0-待审批 1-已通过 2-已驳回 3-已取消 4-已完成',
  `is_deleted` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除状态',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_room_id` (`room_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_time_range` (`start_time`, `end_time`),
  CONSTRAINT `fk_booking_room` FOREIGN KEY (`room_id`) REFERENCES `meeting_room` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='预约表';

-- 创建审批记录表
CREATE TABLE IF NOT EXISTS `booking_approval` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `booking_id` BIGINT UNSIGNED NOT NULL COMMENT '预约ID',
  `approver_id` BIGINT UNSIGNED NOT NULL COMMENT '审批人ID',
  `approver_name` VARCHAR(255) NOT NULL COMMENT '审批人姓名',
  `approval_result` TINYINT UNSIGNED NOT NULL COMMENT '审批结果：1-通过 2-驳回',
  `comment` TEXT COMMENT '审批意见',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_booking` (`booking_id`),
  CONSTRAINT `fk_approval_booking` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='审批记录表';

-- 创建签到表
CREATE TABLE IF NOT EXISTS `check_in` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `booking_id` BIGINT UNSIGNED NOT NULL COMMENT '预约ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `user_name` VARCHAR(255) NOT NULL COMMENT '用户名',
  `check_in_time` DATETIME NOT NULL COMMENT '签到时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_booking_user` (`booking_id`, `user_id`),
  KEY `idx_booking_id` (`booking_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_check_in_booking` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='签到表';

-- 创建通知表
CREATE TABLE IF NOT EXISTS `notification` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `type` TINYINT UNSIGNED NOT NULL COMMENT '通知类型：0-审批结果 1-会议提醒 2-预约取消',
  `title` VARCHAR(255) NOT NULL COMMENT '通知标题',
  `content` TEXT NOT NULL COMMENT '通知内容',
  `is_read` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已读',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_is_read` (`is_read`),
  KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知表';

-- 创建会议室使用统计表
CREATE TABLE IF NOT EXISTS `room_usage_statistics` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `room_id` BIGINT UNSIGNED NOT NULL COMMENT '会议室ID',
  `date` DATE NOT NULL COMMENT '统计日期',
  `booking_count` INT NOT NULL DEFAULT 0 COMMENT '预约次数',
  `usage_minutes` INT NOT NULL DEFAULT 0 COMMENT '使用分钟数',
  `check_in_count` INT NOT NULL DEFAULT 0 COMMENT '签到人数',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_room_date` (`room_id`, `date`),
  KEY `idx_room_id` (`room_id`),
  KEY `idx_date` (`date`),
  CONSTRAINT `fk_statistics_room` FOREIGN KEY (`room_id`) REFERENCES `meeting_room` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会议室使用统计表';

-- 创建索引以优化预约冲突检测
CREATE INDEX idx_booking_conflict ON booking(room_id, start_time, end_time, status, is_deleted);

-- 插入初始数据
INSERT INTO `meeting_room` (`name`, `location`, `capacity`, `equipment`) VALUES
('会议室A', '1楼', 20, '投影仪、白板、视频会议系统'),
('会议室B', '2楼', 10, '投影仪、白板'),
('会议室C', '3楼', 30, '投影仪、白板、视频会议系统、音响设备'),
('会议室D', '1楼', 8, '白板');
