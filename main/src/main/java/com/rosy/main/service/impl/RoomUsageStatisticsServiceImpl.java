package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.Booking;
import com.rosy.main.domain.entity.CheckIn;
import com.rosy.main.domain.entity.RoomUsageStatistics;
import com.rosy.main.domain.vo.RoomUsageStatisticsVO;
import com.rosy.main.mapper.BookingMapper;
import com.rosy.main.mapper.CheckInMapper;
import com.rosy.main.mapper.RoomUsageStatisticsMapper;
import com.rosy.main.service.IRoomUsageStatisticsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoomUsageStatisticsServiceImpl extends ServiceImpl<RoomUsageStatisticsMapper, RoomUsageStatistics> implements IRoomUsageStatisticsService {

    private final BookingMapper bookingMapper;
    private final CheckInMapper checkInMapper;

    public RoomUsageStatisticsServiceImpl(BookingMapper bookingMapper, CheckInMapper checkInMapper) {
        this.bookingMapper = bookingMapper;
        this.checkInMapper = checkInMapper;
    }

    @Override
    @Transactional
    public void generateStatistics() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDateTime startOfDay = yesterday.atStartOfDay();
        LocalDateTime endOfDay = yesterday.atTime(23, 59, 59);

        QueryWrapper<Booking> bookingWrapper = new QueryWrapper<>();
        bookingWrapper.ge("start_time", startOfDay);
        bookingWrapper.lt("start_time", endOfDay);
        bookingWrapper.eq("status", 1); // 已通过
        List<Booking> bookings = bookingMapper.selectList(bookingWrapper);

        Map<Long, List<Booking>> bookingsByRoom = bookings.stream()
                .collect(Collectors.groupingBy(Booking::getRoomId));

        for (Map.Entry<Long, List<Booking>> entry : bookingsByRoom.entrySet()) {
            Long roomId = entry.getKey();
            List<Booking> roomBookings = entry.getValue();

            int bookingCount = roomBookings.size();
            int usageMinutes = 0;

            for (Booking booking : roomBookings) {
                long minutes = booking.getStartTime().until(booking.getEndTime(), java.time.temporal.ChronoUnit.MINUTES);
                usageMinutes += minutes;
            }

            QueryWrapper<CheckIn> checkInWrapper = new QueryWrapper<>();
            checkInWrapper.in("booking_id", roomBookings.stream().map(Booking::getId).collect(Collectors.toList()));
            int checkInCount = checkInMapper.selectCount(checkInWrapper);

            RoomUsageStatistics statistics = new RoomUsageStatistics();
            statistics.setRoomId(roomId);
            statistics.setDate(yesterday);
            statistics.setBookingCount(bookingCount);
            statistics.setUsageMinutes(usageMinutes);
            statistics.setCheckInCount(checkInCount);
            statistics.setCreateTime(LocalDateTime.now());

            this.save(statistics);
        }
    }

    @Override
    public List<RoomUsageStatisticsVO> getStatisticsByDateRange(LocalDate startDate, LocalDate endDate) {
        QueryWrapper<RoomUsageStatistics> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("date", startDate);
        queryWrapper.le("date", endDate);
        queryWrapper.orderByAsc("date");

        List<RoomUsageStatistics> statisticsList = this.list(queryWrapper);
        return convertToVOList(statisticsList);
    }

    @Override
    public List<RoomUsageStatisticsVO> getStatisticsByRoomId(Long roomId, LocalDate startDate, LocalDate endDate) {
        QueryWrapper<RoomUsageStatistics> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_id", roomId);
        queryWrapper.ge("date", startDate);
        queryWrapper.le("date", endDate);
        queryWrapper.orderByAsc("date");

        List<RoomUsageStatistics> statisticsList = this.list(queryWrapper);
        return convertToVOList(statisticsList);
    }

    @Override
    public List<RoomUsageStatisticsVO> getUsageOverview(LocalDate startDate, LocalDate endDate) {
        QueryWrapper<RoomUsageStatistics> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("date", startDate);
        queryWrapper.le("date", endDate);

        List<RoomUsageStatistics> statisticsList = this.list(queryWrapper);
        return convertToVOList(statisticsList);
    }

    private List<RoomUsageStatisticsVO> convertToVOList(List<RoomUsageStatistics> statisticsList) {
        List<RoomUsageStatisticsVO> voList = new ArrayList<>();
        for (RoomUsageStatistics statistics : statisticsList) {
            RoomUsageStatisticsVO vo = new RoomUsageStatisticsVO();
            vo.setId(statistics.getId());
            vo.setRoomId(statistics.getRoomId());
            vo.setDate(statistics.getDate());
            vo.setBookingCount(statistics.getBookingCount());
            vo.setUsageMinutes(statistics.getUsageMinutes());
            vo.setCheckInCount(statistics.getCheckInCount());
            vo.setCreateTime(statistics.getCreateTime());
            voList.add(vo);
        }
        return voList;
    }
}
