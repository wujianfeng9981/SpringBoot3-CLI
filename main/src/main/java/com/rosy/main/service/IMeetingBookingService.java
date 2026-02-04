package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.meetingbooking.MeetingBookingAddRequest;
import com.rosy.main.domain.dto.meetingbooking.MeetingBookingQueryRequest;
import com.rosy.main.domain.dto.meetingbooking.MeetingBookingUpdateRequest;
import com.rosy.main.domain.entity.MeetingBooking;
import com.rosy.main.domain.vo.MeetingBookingVO;

public interface IMeetingBookingService extends IService<MeetingBooking> {

    void addBooking(MeetingBookingAddRequest addRequest);

    void updateBooking(MeetingBookingUpdateRequest updateRequest);

    MeetingBookingVO getBookingVO(MeetingBooking booking);

    Page<MeetingBookingVO> getBookingVOPage(Page<MeetingBooking> page);

    LambdaQueryWrapper<MeetingBooking> getQueryWrapper(MeetingBookingQueryRequest queryRequest);

    boolean checkConflict(Long roomId, java.time.LocalDateTime startTime, java.time.LocalDateTime endTime, Long excludeId);
}
