package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.meetingnotification.MeetingNotificationQueryRequest;
import com.rosy.main.domain.entity.MeetingNotification;
import com.rosy.main.domain.vo.MeetingNotificationVO;

/**
 * <p>
 * 会议通知表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
public interface IMeetingNotificationService extends IService<MeetingNotification> {

    /**
     * 发送审批结果通知
     *
     * @param bookingId 预约ID
     * @param userId    接收人ID
     * @param status    审批状态
     * @param comment   审批意见
     * @return 是否成功
     */
    boolean sendApprovalNotification(Long bookingId, Long userId, Byte status, String comment);

    /**
     * 发送会议开始提醒
     *
     * @param bookingId 预约ID
     * @param userId    接收人ID
     * @return 是否成功
     */
    boolean sendMeetingReminder(Long bookingId, Long userId);

    /**
     * 标记通知为已读
     *
     * @param id 通知ID
     * @return 是否成功
     */
    boolean markAsRead(Long id);

    /**
     * 获取通知VO
     *
     * @param meetingNotification 通知实体
     * @return 通知VO
     */
    MeetingNotificationVO getNotificationVO(MeetingNotification meetingNotification);

    /**
     * 获取通知VO分页
     *
     * @param page 分页对象
     * @return VO分页对象
     */
    Page<MeetingNotificationVO> getNotificationVOPage(Page<MeetingNotification> page);

    /**
     * 获取查询条件包装器
     *
     * @param queryRequest 查询请求
     * @return 查询条件包装器
     */
    LambdaQueryWrapper<MeetingNotification> getQueryWrapper(MeetingNotificationQueryRequest queryRequest);
}
