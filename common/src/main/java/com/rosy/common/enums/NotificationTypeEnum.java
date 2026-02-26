package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum NotificationTypeEnum {

    APPROVAL_RESULT(0, "审批结果通知"),
    MEETING_REMINDER(1, "会议开始前提醒"),
    BOOKING_CANCELLED(2, "预约取消通知");

    private final int code;
    private final String text;

    NotificationTypeEnum(int code, String text) {
        this.code = code;
        this.text = text;
    }
}
