package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.reservation.ReservationQueryRequest;
import com.rosy.main.domain.entity.Reservation;
import com.rosy.main.domain.vo.ReservationVO;

import java.time.LocalDateTime;

public interface IReservationService extends IService<Reservation> {

    ReservationVO getReservationVO(Reservation reservation);

    LambdaQueryWrapper<Reservation> getQueryWrapper(ReservationQueryRequest queryRequest);

    boolean hasConflict(Long roomId, LocalDateTime startTime, LocalDateTime endTime);

    boolean hasConflictExcludeId(Long roomId, LocalDateTime startTime, LocalDateTime endTime, Long excludeId);
}
