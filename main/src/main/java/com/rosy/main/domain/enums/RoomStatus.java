package com.rosy.main.domain.enums;

import lombok.Getter;

@Getter
public enum RoomStatus {
    AVAILABLE((byte) 0, "可用"),
    MAINTENANCE((byte) 1, "维护中"),
    DISABLED((byte) 2, "已停用");

    private final Byte code;
    private final String desc;

    RoomStatus(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static RoomStatus getByCode(Byte code) {
        for (RoomStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
