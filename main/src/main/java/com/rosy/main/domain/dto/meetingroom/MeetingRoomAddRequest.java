package com.rosy.main.domain.dto.meetingroom;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class MeetingRoomAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "会议室名称不能为空")
    @Size(max = 100, message = "会议室名称长度不能超过100个字符")
    private String name;

    @Size(max = 200, message = "位置长度不能超过200个字符")
    private String location;

    @NotNull(message = "容量不能为空")
    @Min(value = 1, message = "容量至少为1")
    @Max(value = 1000, message = "容量不能超过1000")
    private Integer capacity;

    @Size(max = 500, message = "设备信息长度不能超过500个字符")
    private String equipment;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态只能为0或1")
    @Max(value = 1, message = "状态只能为0或1")
    private Byte status;
}
