package com.rosy.main.modules.meeting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.modules.meeting.dto.BookingDTO;
import com.rosy.main.modules.meeting.dto.BookingRequest;
import com.rosy.main.modules.meeting.entity.Booking;
import com.rosy.main.modules.meeting.enums.BookingStatus;

import java.util.List;

/**
 * 预约服务接口
 * 定义预约相关的业务逻辑
 */
public interface IBookingService extends IService<Booking> {

    /**
     * 创建预约申请
     * @param request 预约申请信息
     * @return 创建的预约记录
     */
    BookingDTO createBooking(BookingRequest request);

    /**
     * 根据ID查询预约记录
     * @param id 预约ID
     * @return 预约记录
     */
    BookingDTO getBookingById(Long id);

    /**
     * 查询用户的所有预约记录
     * @param userId 用户ID
     * @return 预约记录列表
     */
    List<BookingDTO> getUserBookings(Long userId);

    /**
     * 查询会议室的预约记录
     * @param roomId 会议室ID
     * @param status 预约状态
     * @return 预约记录列表
     */
    List<BookingDTO> getRoomBookings(Long roomId, BookingStatus status);

    /**
     * 取消预约
     * @param id 预约ID
     * @param userId 用户ID（用于权限校验）
     */
    void cancelBooking(Long id, Long userId);

    /**
     * 审批预约
     * @param id 预约ID
     * @param approved 是否通过
     * @param comment 审批意见
     */
    void approveBooking(Long id, Boolean approved, String comment);

    /**
     * 查询用户的即将开始的会议
     * @param userId 用户ID
     * @return 即将开始的会议列表
     */
    List<BookingDTO> getUpcomingMeetings(Long userId);
}
