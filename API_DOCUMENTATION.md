# 会议室预约管理系统 API 文档

## 项目结构
```
SpringBoot3-CLI/
├── common/            # 公共模块
│   └── src/main/java/com/rosy/common/
│       ├── domain/entity/ApiResponse.java   # API响应封装
│       ├── enums/                      # 枚举类
│       └── exception/                  # 异常处理
├── framework/         # 框架配置模块
├── web/               # Web模块
└── main/              # 主业务模块
    └── src/main/java/com/rosy/main/
        ├── controller/        # 控制器
        ├── service/           # 服务接口
        ├── service/impl/      # 服务实现
        ├── mapper/            # 数据访问
        ├── domain/entity/     # 实体类
        ├── domain/dto/        # 数据传输对象
        └── domain/vo/         # 视图对象
```

## API 访问路径

### 1. 会议室管理接口

| 接口路径 | 方法 | 功能描述 | 请求体 (JSON) |
|---------|------|---------|--------------|
| `/api/meeting-room` | POST | 创建会议室 | `{"name": "会议室A", "location": "1F", "capacity": 10, "equipment": "投影仪", "status": 1}` |
| `/api/meeting-room` | PUT | 更新会议室 | `{"id": 1, "name": "会议室A", "location": "1F", "capacity": 12, "equipment": "投影仪+白板", "status": 1}` |
| `/api/meeting-room/{id}` | DELETE | 删除会议室 | N/A |
| `/api/meeting-room/{id}` | GET | 获取会议室详情 | N/A |
| `/api/meeting-room` | GET | 获取会议室列表 | 查询参数：name, location, minCapacity, equipment, status |
| `/api/meeting-room/page` | GET | 分页查询会议室 | 查询参数：name, location, minCapacity, equipment, status, pageNum, pageSize |

### 2. 预约管理接口

| 接口路径 | 方法 | 功能描述 | 请求体 (JSON) |
|---------|------|---------|--------------|
| `/api/booking` | POST | 创建预约 | `{"roomId": 1, "userId": 1, "userName": "张三", "startTime": "2024-01-01T10:00:00", "endTime": "2024-01-01T11:00:00", "subject": "项目讨论"}` |
| `/api/booking/cancel/{id}` | PUT | 取消预约 | 查询参数：userId=1 |
| `/api/booking/approve` | PUT | 通过预约 | `{"bookingId": 1, "approverId": 2, "approvalResult": 1, "comment": "同意"}` |
| `/api/booking/reject` | PUT | 驳回预约 | `{"bookingId": 1, "approverId": 2, "approvalResult": 2, "comment": "时间段冲突"}` |
| `/api/booking/{id}` | GET | 获取预约详情 | N/A |
| `/api/booking/list` | GET | 获取预约列表 | 查询参数：roomId, userId, status, startDate, endDate |
| `/api/booking/page` | GET | 分页查询预约 | 查询参数：roomId, userId, status, startDate, endDate, pageNum, pageSize |
| `/api/booking/my` | GET | 获取我的预约 | 查询参数：userId=1 |
| `/api/booking/pending` | GET | 获取待审批预约 | N/A |

### 3. 签到管理接口

| 接口路径 | 方法 | 功能描述 | 请求体 (JSON) |
|---------|------|---------|--------------|
| `/api/check-in` | POST | 执行签到 | 查询参数：bookingId=1, userId=1 |
| `/api/check-in/count/{bookingId}` | GET | 获取签到人数 | N/A |

### 4. 通知管理接口

| 接口路径 | 方法 | 功能描述 | 请求体 (JSON) |
|---------|------|---------|--------------|
| `/api/notification/my` | GET | 获取我的通知 | 查询参数：userId=1 |
| `/api/notification/unread/{userId}` | GET | 获取未读通知数 | N/A |
| `/api/notification/read/{id}` | PUT | 标记通知已读 | 查询参数：userId=1 |
| `/api/notification/read/all` | PUT | 标记全部已读 | 查询参数：userId=1 |

### 5. 统计管理接口

| 接口路径 | 方法 | 功能描述 | 请求体 (JSON) |
|---------|------|---------|--------------|
| `/api/statistics/generate` | POST | 生成统计数据 | N/A |
| `/api/statistics/date-range` | GET | 获取日期范围统计 | 查询参数：startDate=2024-01-01, endDate=2024-01-31 |
| `/api/statistics/room` | GET | 获取会议室统计 | 查询参数：roomId=1, startDate=2024-01-01, endDate=2024-01-31 |
| `/api/statistics/overview` | GET | 获取使用概况 | 查询参数：startDate=2024-01-01, endDate=2024-01-31 |

## 业务规则描述

### 1. 会议室管理规则
- 会议室名称必须唯一
- 容量必须大于0
- 状态为1表示可用，0表示不可用

### 2. 预约管理规则
- 必须选择可用的会议室
- 开始时间不能早于当前时间
- 最少预约30分钟，单次最多8小时
- 不能与该会议室其他待审批或已通过的预约冲突
- 必须填写会议事由
- 只有预约者本人可以取消预约
- 会议开始后不能取消预约
- 只有已通过的预约才能签到

### 3. 审批管理规则
- 只能审批待审批状态的预约
- 审批结果为1表示通过，2表示驳回
- 审批后会发送通知给预约者

### 4. 签到管理规则
- 只有已通过的预约才能签到
- 签到时间范围：会议开始前30分钟内
- 一个用户只能签到一次

### 5. 通知管理规则
- 审批结果通知：预约被通过或驳回时发送
- 会议提醒通知：会议开始前15分钟发送
- 通知支持标记已读和获取未读数量

### 6. 统计管理规则
- 每日自动生成前一天的使用统计
- 统计数据包括：预约次数、使用时长、签到人数
- 支持按日期范围和会议室查询统计数据

## 业务逻辑讲解

### 1. 预约创建流程
1. 接收预约请求，校验参数
2. 检查会议室是否存在且可用
3. 检查预约时间段是否符合规则
4. 检查是否与其他预约冲突
5. 保存预约记录，状态为待审批
6. 返回预约ID

### 2. 预约审批流程
1. 接收审批请求，校验参数
2. 检查预约是否存在且状态为待审批
3. 更新预约状态（通过/驳回）
4. 保存审批记录
5. 发送审批结果通知
6. 返回操作结果

### 3. 会议签到流程
1. 接收签到请求，校验参数
2. 检查预约是否存在且状态为已通过
3. 检查签到时间是否在允许范围内
4. 检查用户是否已签到
5. 保存签到记录
6. 更新统计数据

### 4. 通知发送流程
1. 触发通知事件（审批结果/会议提醒）
2. 构建通知内容
3. 保存通知记录
4. 标记为未读状态

### 5. 统计生成流程
1. 定时任务触发或手动调用
2. 查询前一天的已通过预约
3. 按会议室分组计算使用数据
4. 查询每个预约的签到人数
5. 保存统计记录

## 调用逻辑

### 典型预约场景
1. **预约会议室**：`POST /api/booking` → `BookingServiceImpl.createBooking()` → 校验并保存预约
2. **审批预约**：`PUT /api/booking/approve` → `BookingServiceImpl.approveBooking()` → 更新状态并通知
3. **执行签到**：`POST /api/check-in` → `CheckInServiceImpl.doCheckIn()` → 保存签到记录
4. **查看通知**：`GET /api/notification/my` → `NotificationServiceImpl.getMyNotifications()` → 获取用户通知
5. **查看统计**：`GET /api/statistics/date-range` → `RoomUsageStatisticsServiceImpl.getStatisticsByDateRange()` → 获取统计数据

### 依赖关系
- `Controller` → `Service` → `Mapper`
- `BookingService` 依赖 `MeetingRoomService`、`BookingApprovalService`、`NotificationService`
- `CheckInService` 依赖 `BookingService`
- `NotificationService` 依赖 `MeetingRoomService`
- `RoomUsageStatisticsService` 依赖 `BookingMapper`、`CheckInMapper`

## 错误码说明

| 错误码 | 描述 |
|-------|------|
| 40000 | 参数错误 |
| 40400 | 资源不存在 |
| 40300 | 无权限操作 |
| 50000 | 系统内部错误 |
