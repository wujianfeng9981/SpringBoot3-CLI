package com.rosy.main.modules.meeting.enums;

/**
 * 预约状态枚举
 * 用于标识会议室预约的当前状态
 */
public enum BookingStatus {
    /** 待审批 */
    PENDING("待审批"),
    /** 已通过 */
    APPROVED("已通过"),
    /** 已驳回 */
    REJECTED("已驳回"),
    /** 已取消 */
    CANCELLED("已取消"),
    /** 已完成 */
    COMPLETED("已完成");

    private final String description;

    BookingStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
