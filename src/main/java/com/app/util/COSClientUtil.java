package com.app.util;

import com.app.common.SystemConstant;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.region.Region;

public class COSClientUtil {

    public static COSClient getCosClient() {
        // 设置访问密钥
        String secretId = SystemConstant.secretId;
        String secretKey = SystemConstant.secretKey;
        BasicCOSCredentials credentials = new BasicCOSCredentials(secretId, secretKey);

        // 设置存储桶地域
        String regionName = SystemConstant.regionName;
        Region region = new Region(regionName);

        // 创建存储桶客户端
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端
        return new COSClient(credentials, clientConfig);
    }
}
