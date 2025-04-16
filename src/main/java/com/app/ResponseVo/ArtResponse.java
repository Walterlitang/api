package com.app.ResponseVo;

import com.app.model.Article;
import com.app.model.SysCategory;
import lombok.Data;

import java.util.List;

@Data
public class ArtResponse {

    /**
     * 院线
     */
    private List<SysCategory> theaterList;

    /**
     * 影视
     */
    private List<SysCategory> movieList;

    /**
     * 音乐
     */
    private List<SysCategory> otherList;


}
