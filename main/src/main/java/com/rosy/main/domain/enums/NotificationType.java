package com.rosy.main.domain.enums;

import lombok.Getter;

@Getter
public enum NotificationType {
    APPROVAL_RESULT((byte) 0, "审批结果通知"),
    MEETING_REMINDER((byte) 1, "会议提醒"),
    CANCEL_NOTICE((byte) 2, "取消通知");

    private final Byte code;
    private final String desc;

    NotificationType(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static NotificationType getByCode(Byte code) {
        for (NotificationType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
