package com.rosy.main.domain.dto.room;

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

    @Size(max = 200, message = "会议室位置长度不能超过200个字符")
    private String location;

    @Min(value = 1, message = "最小容量不能小于1")
    private Integer minCapacity;

    @Min(value = 0, message = "状态值只能为0、1或2")
    @Max(value = 2, message = "状态值只能为0、1或2")
    private Byte status;
}
