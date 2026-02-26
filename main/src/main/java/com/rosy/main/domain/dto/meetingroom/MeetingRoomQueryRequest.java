package com.rosy.main.domain.dto.meetingroom;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MeetingRoomQueryRequest extends PageRequest {

    private String name;

    private String location;

    private Integer minCapacity;

    private String equipment;

    private Byte status;
}
