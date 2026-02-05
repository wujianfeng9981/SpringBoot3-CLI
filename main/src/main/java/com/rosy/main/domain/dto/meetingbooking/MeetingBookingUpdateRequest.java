package com.rosy.main.domain.dto.meetingbooking;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class MeetingBookingUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @NotNull(message = "ID不能为空")
    private Long id;

    /**
     * 会议室ID
     */
    private Long roomId;

    /**
     * 会议主题
     */
    @Size(max = 200, message = "会议主题长度不能超过 200 个字符")
    private String title;

    /**
     * 会议事由/描述
     */
    @Size(max = 1000, message = "会议描述长度不能超过 1000 个字符")
    private String description;

    /**
     * 开始时间
     */
    @Future(message = "开始时间必须是未来时间")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @Future(message = "结束时间必须是未来时间")
    private LocalDateTime endTime;

    /**
     * 参会人数
     */
    @Min(value = 1, message = "参会人数至少为 1")
    @Max(value = 500, message = "参会人数不能超过 500")
    private Integer attendees;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过 500 个字符")
    private String remark;

    /**
     * 乐观锁版本号
     */
    @NotNull(message = "版本号不能为空")
    private Integer version;
}
