package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.constant.CommonConstant;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.SqlUtils;
import com.rosy.common.utils.StringUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.meetingbooking.MeetingBookingAddRequest;
import com.rosy.main.domain.dto.meetingbooking.MeetingBookingQueryRequest;
import com.rosy.main.domain.dto.meetingbooking.MeetingBookingUpdateRequest;
import com.rosy.main.domain.entity.MeetingBooking;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.vo.MeetingBookingVO;
import com.rosy.main.mapper.MeetingBookingMapper;
import com.rosy.main.mapper.MeetingRoomMapper;
import com.rosy.main.service.IMeetingBookingService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 会议预约表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Service
public class MeetingBookingServiceImpl extends ServiceImpl<MeetingBookingMapper, MeetingBooking> implements IMeetingBookingService {

    @Resource
    private MeetingRoomMapper meetingRoomMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addBooking(MeetingBookingAddRequest addRequest) {
        // 校验会议室是否存在
        MeetingRoom room = meetingRoomMapper.selectById(addRequest.getRoomId());
        ThrowUtils.throwIf(room == null, ErrorCode.NOT_FOUND_ERROR, "会议室不存在");
        ThrowUtils.throwIf(room.getStatus() != 1, ErrorCode.PARAMS_ERROR, "会议室不可用");

        // 校验时间
        ThrowUtils.throwIf(addRequest.getStartTime().isAfter(addRequest.getEndTime()),
                ErrorCode.PARAMS_ERROR, "开始时间不能晚于结束时间");
        ThrowUtils.throwIf(addRequest.getStartTime().isBefore(LocalDateTime.now()),
                ErrorCode.PARAMS_ERROR, "开始时间不能是过去时间");

        // 校验容量
        if (addRequest.getAttendees() != null && addRequest.getAttendees() > room.getCapacity()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参会人数超过会议室容量");
        }

        // 检查时间冲突
        boolean hasConflict = checkConflict(addRequest.getRoomId(),
                addRequest.getStartTime(), addRequest.getEndTime(), null);
        ThrowUtils.throwIf(hasConflict, ErrorCode.PARAMS_ERROR, "该时间段已被预约");

        MeetingBooking booking = new MeetingBooking();
        BeanUtil.copyProperties(addRequest, booking);
        // 设置预约人（暂时使用固定值，实际应从登录用户获取）
        booking.setUserId(1L);
        // 默认待审批状态
        booking.setStatus((byte) 0);

        boolean result = this.save(booking);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return booking.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBooking(MeetingBookingUpdateRequest updateRequest) {
        MeetingBooking oldBooking = this.getById(updateRequest.getId());
        ThrowUtils.throwIf(oldBooking == null, ErrorCode.NOT_FOUND_ERROR);

        // 只有待审批状态可以修改
        ThrowUtils.throwIf(oldBooking.getStatus() != 0, ErrorCode.OPERATION_ERROR, "只有待审批的预约可以修改");

        MeetingBooking booking = new MeetingBooking();
        BeanUtil.copyProperties(updateRequest, booking);

        // 如果修改了时间，需要重新校验冲突
        if (updateRequest.getStartTime() != null && updateRequest.getEndTime() != null) {
            ThrowUtils.throwIf(updateRequest.getStartTime().isAfter(updateRequest.getEndTime()),
                    ErrorCode.PARAMS_ERROR, "开始时间不能晚于结束时间");

            Long roomId = updateRequest.getRoomId() != null ? updateRequest.getRoomId() : oldBooking.getRoomId();
            boolean hasConflict = checkConflict(roomId, updateRequest.getStartTime(),
                    updateRequest.getEndTime(), updateRequest.getId());
            ThrowUtils.throwIf(hasConflict, ErrorCode.PARAMS_ERROR, "该时间段已被预约");
        }

        boolean result = this.updateById(booking);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelBooking(Long id) {
        MeetingBooking booking = this.getById(id);
        ThrowUtils.throwIf(booking == null, ErrorCode.NOT_FOUND_ERROR);

        // 只有待审批或已通过状态可以取消
        ThrowUtils.throwIf(booking.getStatus() != 0 && booking.getStatus() != 1,
                ErrorCode.OPERATION_ERROR, "当前状态无法取消预约");

        booking.setStatus((byte) 3); // 已取消
        return this.updateById(booking);
    }

    @Override
    public boolean checkConflict(Long roomId, LocalDateTime startTime, LocalDateTime endTime, Long excludeId) {
        int count = baseMapper.checkTimeConflict(roomId, startTime, endTime, excludeId);
        return count > 0;
    }

    @Override
    public MeetingBookingVO getBookingVO(MeetingBooking booking) {
        if (booking == null) {
            return null;
        }
        MeetingBookingVO bookingVO = new MeetingBookingVO();
        BeanUtil.copyProperties(booking, bookingVO);

        // 查询会议室名称
        MeetingRoom room = meetingRoomMapper.selectById(booking.getRoomId());
        if (room != null) {
            bookingVO.setRoomName(room.getName());
        }

        return bookingVO;
    }

    @Override
    public Page<MeetingBookingVO> getBookingVOPage(Page<MeetingBooking> page) {
        List<MeetingBooking> records = page.getRecords();
        Page<MeetingBookingVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<MeetingBookingVO> voList = records.stream()
                .map(this::getBookingVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public LambdaQueryWrapper<MeetingBooking> getQueryWrapper(MeetingBookingQueryRequest queryRequest) {
        LambdaQueryWrapper<MeetingBooking> queryWrapper = new LambdaQueryWrapper<>();
        if (queryRequest == null) {
            return queryWrapper;
        }

        Long id = queryRequest.getId();
        Long roomId = queryRequest.getRoomId();
        Long userId = queryRequest.getUserId();
        String title = queryRequest.getTitle();
        Byte status = queryRequest.getStatus();
        LocalDateTime startTimeBegin = queryRequest.getStartTimeBegin();
        LocalDateTime startTimeEnd = queryRequest.getStartTimeEnd();
        String sortField = queryRequest.getSortField();
        String sortOrder = queryRequest.getSortOrder();

        queryWrapper.eq(id != null, MeetingBooking::getId, id);
        queryWrapper.eq(roomId != null, MeetingBooking::getRoomId, roomId);
        queryWrapper.eq(userId != null, MeetingBooking::getUserId, userId);
        queryWrapper.like(StringUtils.isNotBlank(title), MeetingBooking::getTitle, title);
        queryWrapper.eq(status != null, MeetingBooking::getStatus, status);
        queryWrapper.ge(startTimeBegin != null, MeetingBooking::getStartTime, startTimeBegin);
        queryWrapper.le(startTimeEnd != null, MeetingBooking::getStartTime, startTimeEnd);

        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                CommonConstant.SORT_ORDER_ASC.equals(sortOrder),
                MeetingBooking::getCreateTime);
        return queryWrapper;
    }
}
