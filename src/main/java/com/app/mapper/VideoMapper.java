package com.app.mapper;

import com.app.model.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 视频管理表 Mapper 接口
 * </p>
 *
 * @author yoominic
 * @since 2025-02-07
 */
public interface VideoMapper extends BaseMapper<Video> {

    IPage<Video> adminPage(Page<Video> videoPage, @Param("title") String title);

    IPage<Video> webList(Page<Video> videoPage, @Param("title") String title);
}
