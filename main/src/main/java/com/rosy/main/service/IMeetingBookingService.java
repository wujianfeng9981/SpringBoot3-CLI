package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.MeetingBookingAddRequest;
import com.rosy.main.domain.dto.MeetingBookingUpdateRequest;
import com.rosy.main.domain.entity.MeetingBooking;
import com.rosy.main.domain.vo.MeetingBookingVO;

import java.time.LocalDateTime;
import java.util.List;

public interface IMeetingBookingService extends IService<MeetingBooking> {

    Long createBooking(MeetingBookingAddRequest request);

    void updateBooking(MeetingBookingUpdateRequest request);

    void cancelBooking(Long id);

    MeetingBookingVO getBookingById(Long id);

    Page<MeetingBookingVO> listBookings(int current, int size, Long roomId, Long applicantId, Byte status);

    List<MeetingBooking> checkConflict(Long roomId, LocalDateTime startTime, LocalDateTime endTime, Long excludeBookingId);
}
