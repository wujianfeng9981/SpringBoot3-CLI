package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.bookingapproval.BookingApprovalAddRequest;
import com.rosy.main.domain.dto.bookingapproval.BookingApprovalQueryRequest;
import com.rosy.main.domain.entity.BookingApproval;
import com.rosy.main.domain.entity.MeetingBooking;
import com.rosy.main.domain.vo.BookingApprovalVO;
import com.rosy.main.mapper.BookingApprovalMapper;
import com.rosy.main.service.IBookingApprovalService;
import com.rosy.main.service.IMeetingBookingService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingApprovalServiceImpl extends ServiceImpl<BookingApprovalMapper, BookingApproval> implements IBookingApprovalService {

    @Resource
    @Lazy
    private IMeetingBookingService meetingBookingService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(BookingApprovalAddRequest request, Long approverId) {
        MeetingBooking booking = meetingBookingService.getById(request.getBookingId());
        if (booking == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "预约记录不存在");
        }

        if (booking.getStatus() != 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该预约已处理过，不能重复审批");
        }

        BookingApproval approval = new BookingApproval();
        approval.setBookingId(request.getBookingId());
        approval.setApproverId(approverId);
        approval.setStatus(request.getStatus());
        approval.setComment(request.getComment());
        approval.setApprovalTime(LocalDateTime.now());

        boolean approvalResult = this.save(approval);
        if (!approvalResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "审批失败");
        }

        booking.setStatus(request.getStatus());
        boolean bookingResult = meetingBookingService.updateById(booking);
        if (!bookingResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新预约状态失败");
        }

    }

    @Override
    public BookingApprovalVO getApprovalVO(BookingApproval approval) {
        return Optional.ofNullable(approval)
                .map(a -> {
                    BookingApprovalVO vo = BeanUtil.copyProperties(a, BookingApprovalVO.class);
                    MeetingBooking booking = meetingBookingService.getById(a.getBookingId());
                    if (booking != null) {
                        vo.setMeetingTitle(booking.getTitle());
                    }
                    vo.setStatusName(getStatusName(a.getStatus()));
                    return vo;
                })
                .orElse(null);
    }

    @Override
    public Page<BookingApprovalVO> getApprovalVOPage(Page<BookingApproval> page) {
        List<BookingApprovalVO> voList = page.getRecords().stream()
                .map(this::getApprovalVO)
                .collect(Collectors.toList());

        Page<BookingApprovalVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public LambdaQueryWrapper<BookingApproval> getQueryWrapper(BookingApprovalQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }

        LambdaQueryWrapper<BookingApproval> queryWrapper = new LambdaQueryWrapper<>();

        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getId(), BookingApproval::getId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getBookingId(), BookingApproval::getBookingId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getApproverId(), BookingApproval::getApproverId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getStatus(), BookingApproval::getStatus);

        QueryWrapperUtil.addSortCondition(queryWrapper,
                queryRequest.getSortField(),
                queryRequest.getSortOrder(),
                BookingApproval::getCreateTime);

        return queryWrapper;
    }

    private String getStatusName(Byte status) {
        if (status == null) {
            return "未知";
        }
        return switch (status) {
            case 0 -> "待审批";
            case 1 -> "通过";
            case 2 -> "驳回";
            default -> "未知";
        };
    }
}
