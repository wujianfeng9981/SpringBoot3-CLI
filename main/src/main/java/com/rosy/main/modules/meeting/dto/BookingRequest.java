package com.rosy.main.modules.meeting.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 预约申请请求对象
 * 用于接收前端提交的预约申请
 */
@Data
public class BookingRequest {

    /**
     * 会议室ID，必填
     */
    @NotNull(message = "会议室ID不能为空")
    private Long roomId;

    /**
     * 用户ID，必填
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 用户名称，必填
     */
    @NotBlank(message = "用户名不能为空")
    private String userName;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 开始时间，必填，必须是未来时间
     */
    @NotNull(message = "开始时间不能为空")
    @Future(message = "开始时间必须是未来时间")
    private LocalDateTime startTime;

    /**
     * 结束时间，必填，必须是未来时间
     */
    @NotNull(message = "结束时间不能为空")
    @Future(message = "结束时间必须是未来时间")
    private LocalDateTime endTime;

    /**
     * 会议主题，必填
     */
    @NotBlank(message = "会议主题不能为空")
    private String subject;

    /**
     * 会议描述
     */
    private String description;

    /**
     * 参会人数
     */
    private Integer attendeesCount;
}
