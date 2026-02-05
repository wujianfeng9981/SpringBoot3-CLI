package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class MeetingRoomVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String location;

    private Integer capacity;

    private String equipment;

    private Byte status;

    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
