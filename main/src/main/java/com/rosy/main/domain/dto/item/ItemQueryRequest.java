package com.rosy.main.domain.dto.item;

import com.rosy.common.domain.entity.PageRequest;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
public class ItemQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     * 必须为正整数
     */
    @Positive(message = "ID 必须为正整数")
    private Long id;

    /**
     * 名称
     * 最大长度 100，可选
     */
    @Size(max = 100, message = "名称长度不能超过 100 个字符")
    private String name;

    /**
     * 简介/内容
     * 最大长度 500，可选
     */
    @Size(max = 500, message = "简介长度不能超过 500 个字符")
    private String description;

    /**
     * 类型
     * 值范围为 1-127，可选
     */
    @Min(value = 1, message = "类型值不能小于 1")
    @Max(value = 127, message = "类型值不能大于 127")
    private Byte type;

    /**
     * 状态
     * 只能为 0 或 1，可选
     */
    @Min(value = 0, message = "状态值只能为 0 或 1")
    @Max(value = 1, message = "状态值只能为 0 或 1")
    private Byte status;

    /**
     * 创建者ID，关联用户表
     * 必须为正整数，可选
     */
    @Positive(message = "创建者ID 必须为正整数")
    private Long creatorId;

    /**
     * 创建时间
     * 可选
     */
    private LocalDateTime createTime;

    /**
     * 更新者ID，关联用户表
     * 必须为正整数，可选
     */
    @Positive(message = "更新者ID 必须为正整数")
    private Long updaterId;

    /**
     * 更新时间
     * 可选
     */
    private LocalDateTime updateTime;

    /**
     * 排序顺序
     */
    @PositiveOrZero(message = "排序字段必须为非负整数")
    private String sortOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Long getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Long updaterId) {
        this.updaterId = updaterId;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
