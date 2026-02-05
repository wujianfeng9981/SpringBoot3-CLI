CREATE DATABASE IF NOT EXISTS springboot_cli DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE springboot_cli;

DROP TABLE IF EXISTS meeting_notification;
DROP TABLE IF EXISTS meeting_checkin;
DROP TABLE IF EXISTS booking_approval;
DROP TABLE IF EXISTS meeting_booking;
DROP TABLE IF EXISTS meeting_room;

CREATE TABLE meeting_room (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    name VARCHAR(100) NOT NULL COMMENT '会议室名称',
    location VARCHAR(200) NOT NULL COMMENT '位置',
    capacity INT NOT NULL COMMENT '容量（人数）',
    equipment TEXT COMMENT '设备（投影仪/白板/音响等，JSON格式）',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-可用',
    description TEXT COMMENT '描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除',
    INDEX idx_status (status),
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会议室表';

CREATE TABLE meeting_booking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    room_id BIGINT NOT NULL COMMENT '会议室ID',
    applicant_id BIGINT NOT NULL COMMENT '申请人ID',
    applicant_name VARCHAR(100) COMMENT '申请人姓名',
    title VARCHAR(200) NOT NULL COMMENT '会议主题',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME NOT NULL COMMENT '结束时间',
    reason TEXT COMMENT '事由',
    attendees INT COMMENT '参会人数',
    status TINYINT DEFAULT 0 COMMENT '状态：0-待审批，1-已通过，2-已驳回，3-已取消，4-已完成',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除',
    INDEX idx_room_id (room_id),
    INDEX idx_applicant_id (applicant_id),
    INDEX idx_status (status),
    INDEX idx_time (start_time, end_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会议室预约表';

CREATE TABLE booking_approval (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    booking_id BIGINT NOT NULL COMMENT '预约ID',
    approver_id BIGINT NOT NULL COMMENT '审批人ID',
    approver_name VARCHAR(100) COMMENT '审批人姓名',
    action TINYINT NOT NULL COMMENT '操作：1-通过，2-驳回',
    comment TEXT COMMENT '审批意见',
    approval_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '审批时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除',
    INDEX idx_booking_id (booking_id),
    INDEX idx_approver_id (approver_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='预约审批表';

CREATE TABLE meeting_checkin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    booking_id BIGINT NOT NULL COMMENT '预约ID',
    user_id BIGINT NOT NULL COMMENT '签到人ID',
    user_name VARCHAR(100) COMMENT '签到人姓名',
    checkin_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除',
    INDEX idx_booking_id (booking_id),
    INDEX idx_user_id (user_id),
    UNIQUE KEY uk_booking_user (booking_id, user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会议签到表';

CREATE TABLE meeting_notification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '接收人ID',
    booking_id BIGINT COMMENT '预约ID',
    type TINYINT NOT NULL COMMENT '通知类型：1-审批结果通知，2-会议开始前通知，3-预约变更通知',
    title VARCHAR(200) COMMENT '标题',
    content TEXT COMMENT '通知内容',
    is_read TINYINT DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
    read_time DATETIME COMMENT '读取时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除',
    INDEX idx_user_id (user_id),
    INDEX idx_booking_id (booking_id),
    INDEX idx_is_read (is_read),
    INDEX idx_type (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会议通知表';
