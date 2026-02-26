package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.BookingApproval;
import com.rosy.main.mapper.BookingApprovalMapper;
import com.rosy.main.service.IBookingApprovalService;
import org.springframework.stereotype.Service;

@Service
public class BookingApprovalServiceImpl extends ServiceImpl<BookingApprovalMapper, BookingApproval> implements IBookingApprovalService {
}
