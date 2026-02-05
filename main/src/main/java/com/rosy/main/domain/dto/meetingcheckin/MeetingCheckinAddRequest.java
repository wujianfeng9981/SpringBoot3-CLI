package com.rosy.main.domain.dto.meetingcheckin;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class MeetingCheckinAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 预约ID
     */
    @NotNull(message = "预约ID不能为空")
    private Long bookingId;

    /**
     * 签到地点
     */
    @Size(max = 200, message = "签到地点长度不能超过 200 个字符")
    private String location;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过 500 个字符")
    private String remark;
}
