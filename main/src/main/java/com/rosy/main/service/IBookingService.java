package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.booking.BookingAddRequest;
import com.rosy.main.domain.dto.booking.BookingApprovalRequest;
import com.rosy.main.domain.dto.booking.BookingQueryRequest;
import com.rosy.main.domain.entity.Booking;
import com.rosy.main.domain.vo.BookingVO;

import java.util.List;

public interface IBookingService extends IService<Booking> {

    Long addBooking(BookingAddRequest request);

    Boolean cancelBooking(Long id, Long userId);

    Boolean approveBooking(BookingApprovalRequest request);

    Boolean rejectBooking(BookingApprovalRequest request);

    BookingVO getBookingById(Long id);

    List<BookingVO> getBookingList(BookingQueryRequest request);

    Page<BookingVO> getBookingPage(BookingQueryRequest request, Integer pageNum, Integer pageSize);

    QueryWrapper<Booking> getQueryWrapper(BookingQueryRequest request);

    BookingVO toBookingVO(Booking booking);
}
