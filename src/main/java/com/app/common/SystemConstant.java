package com.app.common;

import java.io.File;

public class SystemConstant {

    public static final String DEFAULT_UPLOAD_URL = System.getProperty("user.dir") + File.separator + "file" + File.separator;

    public static final String URL = "https://suzhou-polite-1306862033.cos.ap-chengdu.myqcloud.com/uploads/";

    public static final String secretKey = "73rQvKOKGCyYt5ADA8xjWAh3GI7mIdli";

    public static final String secretId = "AKIDiIdz89zfARXSFxb2I2avBF47DC2pSuAD";
    public static final String regionName = "ap-chengdu";
    public static final String bucketName = "suzhou-polite-1306862033";

    public static final String DEFAULT_type1 = "xbm,tif,pjp,svgz,jpg,jpeg,ico,tiff,gif,svg,jfif,webp,png,bmp,pjpeg,avif";//图片扩展名
    public static final String DEFAULT_type2 = "wmv,asf,asx,wmv,asf,asx,mp4,3gp,mov,m4v,avi,dat,mkv,flv,vob";//视频扩展名
    public static final String DEFAULT_type3 = "Doc,doc,docx,Docx,xls,xlsx,Xls,Xlsx,pdf,zip,arc,rel";//文档扩展名
    public static final String DEFAULT_type4 = "zip,rar";//压缩包扩展名

    public static final String DEFAULT_PASSWORD = "Info2025!@";
}
