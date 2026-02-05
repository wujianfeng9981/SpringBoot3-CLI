package com.rosy.main.domain.dto.meetingroom;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
public class MeetingRoomQueryRequest extends PageRequest {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 会议室名称（模糊查询）
     */
    private String name;

    /**
     * 位置（模糊查询）
     */
    private String location;

    /**
     * 最小容量
     */
    private Integer minCapacity;

    /**
     * 最大容量
     */
    private Integer maxCapacity;

    /**
     * 状态：0-禁用，1-可用
     */
    private Byte status;
}
