package com.rosy.main.domain.enums;

import lombok.Getter;

@Getter
public enum SignInStatus {
    NOT_SIGNED((byte) 0, "未签到"),
    SIGNED((byte) 1, "已签到"),
    LATE((byte) 2, "迟到"),
    ABSENT((byte) 3, "缺席");

    private final Byte code;
    private final String desc;

    SignInStatus(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static SignInStatus getByCode(Byte code) {
        for (SignInStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
