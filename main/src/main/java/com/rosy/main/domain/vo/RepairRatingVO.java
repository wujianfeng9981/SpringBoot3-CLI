package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class RepairRatingVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long orderId;

    private String orderNo;

    private Integer rating;

    private String content;

    private Long creatorId;

    private String creatorName;

    private LocalDateTime createTime;

    private Long updaterId;

    private LocalDateTime updateTime;
}
