package com.rosy.main.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MeetingRoomVO {

    private Long id;

    private String name;

    private String location;

    private Integer capacity;

    private String equipment;

    private Byte status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
