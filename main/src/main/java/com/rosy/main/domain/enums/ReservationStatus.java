package com.rosy.main.domain.enums;

import lombok.Getter;

@Getter
public enum ReservationStatus {
    PENDING((byte) 0, "待审批"),
    APPROVED((byte) 1, "已通过"),
    REJECTED((byte) 2, "已驳回"),
    CANCELLED((byte) 3, "已取消"),
    COMPLETED((byte) 4, "已完成");

    private final Byte code;
    private final String desc;

    ReservationStatus(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ReservationStatus getByCode(Byte code) {
        for (ReservationStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
