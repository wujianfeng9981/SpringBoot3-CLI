package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.reservation.ReservationQueryRequest;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.entity.Reservation;
import com.rosy.main.domain.enums.ReservationStatus;
import com.rosy.main.domain.vo.ReservationVO;
import com.rosy.main.mapper.ReservationMapper;
import com.rosy.main.service.IMeetingRoomService;
import com.rosy.main.service.IReservationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ReservationServiceImpl extends ServiceImpl<ReservationMapper, Reservation> implements IReservationService {

    @Resource
    private IMeetingRoomService meetingRoomService;

    @Override
    public ReservationVO getReservationVO(Reservation reservation) {
        return Optional.ofNullable(reservation)
                .map(r -> {
                    ReservationVO vo = BeanUtil.copyProperties(r, ReservationVO.class);
                    MeetingRoom room = meetingRoomService.getById(r.getRoomId());
                    if (room != null) {
                        vo.setRoomName(room.getName());
                        vo.setRoomLocation(room.getLocation());
                    }
                    ReservationStatus status = ReservationStatus.getByCode(r.getStatus());
                    if (status != null) {
                        vo.setStatusDesc(status.getDesc());
                    }
                    return vo;
                })
                .orElse(null);
    }

    @Override
    public LambdaQueryWrapper<Reservation> getQueryWrapper(ReservationQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        LambdaQueryWrapper<Reservation> queryWrapper = new LambdaQueryWrapper<>();

        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getId(), Reservation::getId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getRoomId(), Reservation::getRoomId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getApplicantId(), Reservation::getApplicantId);
        QueryWrapperUtil.addLikeCondition(queryWrapper, queryRequest.getTitle(), Reservation::getTitle);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getStatus(), Reservation::getStatus);

        if (queryRequest.getStartTimeStart() != null) {
            queryWrapper.ge(Reservation::getStartTime, queryRequest.getStartTimeStart());
        }
        if (queryRequest.getStartTimeEnd() != null) {
            queryWrapper.le(Reservation::getStartTime, queryRequest.getStartTimeEnd());
        }

        QueryWrapperUtil.addSortCondition(queryWrapper,
                queryRequest.getSortField(),
                queryRequest.getSortOrder(),
                Reservation::getId);

        return queryWrapper;
    }

    @Override
    public boolean hasConflict(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        return baseMapper.countConflictReservations(roomId, startTime, endTime) > 0;
    }

    @Override
    public boolean hasConflictExcludeId(Long roomId, LocalDateTime startTime, LocalDateTime endTime, Long excludeId) {
        return baseMapper.countConflictReservationsExcludeId(roomId, startTime, endTime, excludeId) > 0;
    }
}
