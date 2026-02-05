package com.rosy.main.domain.dto.meetingroom;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class MeetingRoomUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @NotNull(message = "ID不能为空")
    private Long id;

    /**
     * 会议室名称
     */
    @Size(max = 100, message = "会议室名称长度不能超过 100 个字符")
    private String name;

    /**
     * 位置
     */
    @Size(max = 200, message = "位置长度不能超过 200 个字符")
    private String location;

    /**
     * 容量（人数）
     */
    @Min(value = 1, message = "容量至少为 1")
    @Max(value = 500, message = "容量不能超过 500")
    private Integer capacity;

    /**
     * 设备（投影仪/白板/音响等，JSON格式）
     */
    @Size(max = 500, message = "设备信息长度不能超过 500 个字符")
    private String equipment;

    /**
     * 状态：0-禁用，1-可用
     */
    private Byte status;

    /**
     * 描述
     */
    @Size(max = 500, message = "描述长度不能超过 500 个字符")
    private String description;
}
