package com.rosy.main.domain.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

@Data
public class MeetingRoomUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "ID不能为空")
    private Long id;

    @NotBlank(message = "会议室名称不能为空")
    private String name;

    @NotBlank(message = "位置不能为空")
    private String location;

    @NotNull(message = "容量不能为空")
    private Integer capacity;

    private String equipment;

    private String description;

    private Byte status;
}
