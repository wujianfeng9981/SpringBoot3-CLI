package com.rosy.main.domain.dto.meetingroom;

import com.rosy.common.domain.entity.PageRequest;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class MeetingRoomQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Positive(message = "ID必须为正整数")
    private Long id;

    @Size(max = 100, message = "会议室名称长度不能超过100个字符")
    private String name;

    @Size(max = 200, message = "位置长度不能超过200个字符")
    private String location;

    @Min(value = 1, message = "容量至少为1")
    private Integer minCapacity;

    @Size(max = 500, message = "设备信息长度不能超过500个字符")
    private String equipment;

    @Min(value = 0, message = "状态只能为0或1")
    @Max(value = 1, message = "状态只能为0或1")
    private Byte status;
}
