package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class RepairOrderVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String orderNo;

    private String deviceType;

    private String deviceLocation;

    private String faultType;

    private String faultDescription;

    private String faultImages;

    private Integer status;

    private Integer priority;

    private Long assigneeId;

    private String assigneeName;

    private Integer assignType;

    private LocalDateTime assignTime;

    private LocalDateTime acceptTime;

    private LocalDateTime completeTime;

    private Long creatorId;

    private String creatorName;

    private LocalDateTime createTime;

    private Long updaterId;

    private LocalDateTime updateTime;
}
