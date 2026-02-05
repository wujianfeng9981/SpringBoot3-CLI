package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.meetingbooking.MeetingBookingAddRequest;
import com.rosy.main.domain.dto.meetingbooking.MeetingBookingQueryRequest;
import com.rosy.main.domain.dto.meetingbooking.MeetingBookingUpdateRequest;
import com.rosy.main.domain.entity.MeetingBooking;
import com.rosy.main.domain.vo.MeetingBookingVO;

import java.time.LocalDateTime;

/**
 * <p>
 * 会议预约表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
public interface IMeetingBookingService extends IService<MeetingBooking> {

    /**
     * 创建预约
     *
     * @param addRequest 创建请求
     * @return 预约ID
     */
    Long addBooking(MeetingBookingAddRequest addRequest);

    /**
     * 更新预约
     *
     * @param updateRequest 更新请求
     * @return 是否成功
     */
    boolean updateBooking(MeetingBookingUpdateRequest updateRequest);

    /**
     * 取消预约
     *
     * @param id 预约ID
     * @return 是否成功
     */
    boolean cancelBooking(Long id);

    /**
     * 检查时间段冲突
     *
     * @param roomId    会议室ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param excludeId 排除的预约ID
     * @return 是否冲突
     */
    boolean checkConflict(Long roomId, LocalDateTime startTime, LocalDateTime endTime, Long excludeId);

    /**
     * 获取预约VO
     *
     * @param meetingBooking 预约实体
     * @return 预约VO
     */
    MeetingBookingVO getBookingVO(MeetingBooking meetingBooking);

    /**
     * 获取预约VO分页
     *
     * @param page 分页对象
     * @return VO分页对象
     */
    Page<MeetingBookingVO> getBookingVOPage(Page<MeetingBooking> page);

    /**
     * 获取查询条件包装器
     *
     * @param queryRequest 查询请求
     * @return 查询条件包装器
     */
    LambdaQueryWrapper<MeetingBooking> getQueryWrapper(MeetingBookingQueryRequest queryRequest);
}
