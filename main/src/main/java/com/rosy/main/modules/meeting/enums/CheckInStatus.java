package com.rosy.main.modules.meeting.enums;

/**
 * 签到状态枚举
 * 用于标识会议签到情况
 */
public enum CheckInStatus {
    /** 未签到 */
    NOT_CHECKED_IN("未签到"),
    /** 已签到 */
    CHECKED_IN("已签到"),
    /** 未出席 */
    NO_SHOW("未出席");

    private final String description;

    CheckInStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
