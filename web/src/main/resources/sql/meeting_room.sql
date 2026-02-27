-- 会议室预约管理系统数据库脚本 (H2兼容版本)

-- 会议室表
CREATE TABLE IF NOT EXISTS meeting_room (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(200) NOT NULL,
    capacity INT NOT NULL,
    equipment VARCHAR(500) DEFAULT NULL,
    status TINYINT NOT NULL DEFAULT 0,
    description VARCHAR(500) DEFAULT NULL,
    creator_id BIGINT DEFAULT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updater_id BIGINT DEFAULT NULL,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version TINYINT DEFAULT 0,
    is_deleted TINYINT DEFAULT 0,
    PRIMARY KEY (id)
);

-- 预约表
CREATE TABLE IF NOT EXISTS reservation (
    id BIGINT NOT NULL AUTO_INCREMENT,
    room_id BIGINT NOT NULL,
    applicant_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    purpose VARCHAR(500) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status TINYINT NOT NULL DEFAULT 0,
    reject_reason VARCHAR(500) DEFAULT NULL,
    attendee_count INT NOT NULL,
    creator_id BIGINT DEFAULT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updater_id BIGINT DEFAULT NULL,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version TINYINT DEFAULT 0,
    is_deleted TINYINT DEFAULT 0,
    PRIMARY KEY (id)
);

-- 签到表
CREATE TABLE IF NOT EXISTS sign_in (
    id BIGINT NOT NULL AUTO_INCREMENT,
    reservation_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    sign_in_status TINYINT NOT NULL DEFAULT 0,
    sign_in_time TIMESTAMP DEFAULT NULL,
    remark VARCHAR(200) DEFAULT NULL,
    creator_id BIGINT DEFAULT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updater_id BIGINT DEFAULT NULL,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version TINYINT DEFAULT 0,
    is_deleted TINYINT DEFAULT 0,
    PRIMARY KEY (id)
);

-- 通知表
CREATE TABLE IF NOT EXISTS notification (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    reservation_id BIGINT DEFAULT NULL,
    type TINYINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    content VARCHAR(500) NOT NULL,
    is_read TINYINT NOT NULL DEFAULT 0,
    send_time TIMESTAMP NOT NULL,
    creator_id BIGINT DEFAULT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updater_id BIGINT DEFAULT NULL,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version TINYINT DEFAULT 0,
    is_deleted TINYINT DEFAULT 0,
    PRIMARY KEY (id)
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_room_name ON meeting_room(name);
CREATE INDEX IF NOT EXISTS idx_room_status ON meeting_room(status);
CREATE INDEX IF NOT EXISTS idx_reservation_room ON reservation(room_id);
CREATE INDEX IF NOT EXISTS idx_reservation_applicant ON reservation(applicant_id);
CREATE INDEX IF NOT EXISTS idx_reservation_status ON reservation(status);
CREATE INDEX IF NOT EXISTS idx_signin_reservation ON sign_in(reservation_id);
CREATE INDEX IF NOT EXISTS idx_notification_user ON notification(user_id);

-- 插入测试数据
INSERT INTO meeting_room (name, location, capacity, equipment, status, description) VALUES
('会议室A', '1号楼101室', 10, '["投影仪","白板","音响"]', 0, '小型会议室，适合部门内部会议'),
('会议室B', '1号楼201室', 20, '["投影仪","白板","音响","视频会议设备"]', 0, '中型会议室，适合跨部门会议'),
('会议室C', '2号楼301室', 50, '["投影仪","白板","音响","视频会议设备","同声传译"]', 0, '大型会议室，适合公司级会议'),
('培训室', '3号楼102室', 30, '["投影仪","白板","电脑"]', 1, '培训专用，当前维护中'),
('VIP会议室', '行政楼501室', 8, '["投影仪","白板","音响","咖啡机"]', 0, 'VIP接待专用');
