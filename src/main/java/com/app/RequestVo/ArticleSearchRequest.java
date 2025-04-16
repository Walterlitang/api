package com.app.RequestVo;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 文章 搜索Request
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ArticleSearchRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer page = 1;

    private Integer limit = 5;

    private Integer focusPage = 1;

    private Integer focusLimit = 4;

    private Integer currentPoliticsPage = 1;

    private Integer currentPoliticsLimit = 6;

    private Integer NewDevelopmentsPage = 1;

    private Integer NewDevelopmentsLimit = 10;

}
