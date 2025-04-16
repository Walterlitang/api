package com.app.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 动态评论与回复表
 * </p>
 *
 * @author yoominic
 * @since 2025-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lm_comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论/回复ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属动态ID
     */
    private Integer postId;

    /**
     * 评论者ID
     */
    private Integer userId;

    /**
     * 内容
     */
    private String content;

    /**
     * 根评论ID（NULL表示顶级评论）
     */
    private Integer rootCommentId;

    /**
     * 被回复的评论ID（NULL表示直接回复动态）
     */
    private Integer toCommentId;

    /**
     * 被回复的用户ID
     */
    private Integer toUserId;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 评论时间
     */
    private LocalDateTime createdAt;

    /**
     * 逻辑删除标记
     */
    private Integer isDeleted;


}
