package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum BookingStatusEnum {

    PENDING(0, "待审批"),
    APPROVED(1, "已通过"),
    REJECTED(2, "已驳回"),
    CANCELLED(3, "已取消"),
    COMPLETED(4, "已完成");

    private final int code;
    private final String text;

    BookingStatusEnum(int code, String text) {
        this.code = code;
        this.text = text;
    }
}
