package com.app.controller;

import cn.hutool.core.util.IdUtil;
import com.app.common.SystemConstant;
import com.app.enums.StatusCodeEnum;
import com.app.util.COSClientUtil;
import com.app.util.Result;
import com.app.util.TokenUtils;
import com.qcloud.cos.COSClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@Controller
@RestController
@Api(tags = "图片上传删除接口")
public class UploadController {

    /**
     * 上传文件
     */
    @ApiOperation("上传图片")
    @PostMapping("/upload")
    public Result<?> upload(HttpServletRequest request,
                            @RequestParam("file") MultipartFile file) throws IOException {
        String token = request.getHeader("token");
        if (token == null) {
            return Result.error(StatusCodeEnum.STATUS401.getMsg());
        }
        String mobile = TokenUtils.nothingness(token);

        String type = file.getOriginalFilename();
        if (type != null && !type.endsWith("jpg") && !type.endsWith("png") && !type.endsWith("jpeg") && !type.endsWith("gif") && !type.endsWith("bmp") && !type.endsWith("mp4")) {
            return Result.error("文件类型错误，请上正确的文件!");
        }

        File filePath;
        if (!file.isEmpty()) {
            //上传文件路径
            String path = "";
            path = SystemConstant.DEFAULT_UPLOAD_URL;
            //获得上传文件名
            String fileName = file.getOriginalFilename();
            assert fileName != null;
            int lastIndexOf = fileName.lastIndexOf(".");
            String date = IdUtil.fastUUID();
            //获取文件的后缀名 .jpg
            String suffix = fileName.substring(lastIndexOf);
            filePath = new File(path + date + suffix);
            System.out.println(filePath);
            //如果文件目录不存在，创建目录
            if (!filePath.getParentFile().exists()) {
                filePath.getParentFile().mkdirs();
            }
            //将上传文件保存到一个目标文件中
            file.transferTo(filePath);
            return Result.success("/file/" + date + suffix);
        } else {
            return Result.error("上传失败");
        }
    }


    // 手机号正则表达式
    public boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^(13|14|15|17|18)[0-9]{9}$";
        return phoneNumber != null && phoneNumber.matches(regex);
    }

    public boolean isValidAmount(String amount) {
        // 金额的正则表达式，允许整数部分和小数部分（小数点后最多两位）
        // 注意：这个正则表达式假设金额字符串中不包含货币符号，且金额不会以小数点开头
        String regex = "^\\d+(\\.\\d{1,2})?$";

        return amount != null && amount.matches(regex);
    }


    //删除对象
    @ApiOperation(value = "删除图片")
    @GetMapping("/deleteObject")
    public Result<Object> deleteObject(String filename) {
        //获取COSClient对象
        COSClient cosClient = COSClientUtil.getCosClient();
        // Bucket 的命名格式为 BucketName-APPID ，此处填写的存储桶名称必须为此格式
        String bucketName = SystemConstant.bucketName;
        // 指定被删除的文件在 COS 上的路径，即对象键。例如对象键为 folder/picture.jpg，则表示删除位于 folder 路径下的文件 picture.jpg
        String key = "/uploads/FFuFQ6QV.png";
        cosClient.deleteObject(bucketName, key);
        return Result.success("删除成功");
    }

}
