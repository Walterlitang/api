package com.app.controller.backstage;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.app.common.SystemConstant;
import com.app.model.AdminUserModel;
import com.app.model.AttachmentModel;
import com.app.service.AdminUserService;
import com.app.service.AttachmentService;
import com.app.util.COSClientUtil;
import com.app.util.CommonHelp;
import com.app.util.Result;
import com.app.vo.AttachmentVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

@Controller
@RestController
@RequestMapping("/admin")
public class AdminUploadController {
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private AdminUserService adminUserService;

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public Result<?> upload(HttpServletRequest request) throws IOException {
        String token = request.getHeader("token");
        if (token == null) {
            return Result.error("token不存在");
        }
        Object loginIdByToken = StpUtil.getLoginIdByToken(token);
        if (loginIdByToken == null) {
            Result result = new Result(401, "登录失效");
            return result;
        }
        String userId = loginIdByToken.toString();
        AdminUserModel user = adminUserService.getUserByUserId(Integer.parseInt(userId));
        if (user == null) {
            return Result.error("用户不存在");
        }
        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
        List<MultipartFile> multipart = ((MultipartHttpServletRequest) request).getFiles("multipart");
        Integer pid = Integer.valueOf(params.getParameter("pid"));
        File filePath;
        if (!multipart.isEmpty()) {
            //上传文件路径
            String path = "";
            path = SystemConstant.DEFAULT_UPLOAD_URL;
            //获得上传文件名
            String fileName = multipart.get(0).getOriginalFilename();
            assert fileName != null;
            int lastIndexOf = fileName.lastIndexOf(".");
            String date = getRandomString2(8);
            //获取文件的后缀名 .jpg
            String suffix = fileName.substring(lastIndexOf);
            String suffix2 = fileName.substring(lastIndexOf + 1);
            filePath = new File(path + File.separator + date + suffix);
            System.out.println(filePath);
            //如果文件目录不存在，创建目录
            if (!filePath.getParentFile().exists()) {
                filePath.getParentFile().mkdirs();
            }
            //将上传文件保存到一个目标文件中
            multipart.get(0).transferTo(filePath);
            //获取文件大小
            String fileLength = String.valueOf(filePath.length());
            String fileURL = "";
            File file1 = new File(filePath.getPath());
            fileURL = SystemConstant.URL + file1.getName();
            AttachmentModel model = new AttachmentModel();
            model.setName(filePath.getName());
            model.setAttDir(fileURL);
            model.setAttSize(fileLength);//文件大小
            model.setAttType(suffix2);//文件后缀名
            String[] suffixType1Arr = SystemConstant.DEFAULT_type1.split(",");
            for (int i = 0; i < suffixType1Arr.length; i++) {
                if (suffix2.contains(suffixType1Arr[i])) {
                    model.setFileType(1);
                }
            }
            String[] suffixType2Arr = SystemConstant.DEFAULT_type2.split(",");
            for (int i = 0; i < suffixType2Arr.length; i++) {
                if (suffix2.contains(suffixType2Arr[i])) {
                    model.setFileType(2);
                }
            }
            try {
                model.setPid(pid);
                model.setCreateTime(CommonHelp.getNowTimestamp());
                attachmentService.insert(model);
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            return Result.error("上传失败");
        }
        COSClient cosClient = COSClientUtil.getCosClient();
        // 设置存储桶名称和文件路径
        String bucketName = SystemConstant.bucketName;
        String key = "uploads/" + filePath.getName();
        File localFile = new File(filePath.getPath());
        try {
            // 上传文件到存储桶
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
            // 设置存储桶权限
            putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
            cosClient.putObject(putObjectRequest);
            File file1 = new File(filePath.getPath());
            String url = SystemConstant.URL + file1.getName();
            System.out.println("文件上传成功");
            if (file1.delete()) {
                System.out.println(file1.getName() + " 图片已被删除！");
            } else {
                System.out.println("删除图片操作失败！");
            }
            return Result.success(url);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭存储桶客户端
            cosClient.shutdown();
        }
        return Result.success("文件上传失败");
    }

    //可以指定字符串的某个位置是什么范围的值
    public static String getRandomString2(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(3);
            long result = 0;
            switch (number) {
                case 0:
                    result = Math.round(Math.random() * 25 + 65);
                    sb.append(String.valueOf((char) result));
                    break;
                case 1:
                    result = Math.round(Math.random() * 25 + 97);
                    sb.append(String.valueOf((char) result));
                    break;
                case 2:
                    sb.append(String.valueOf(new Random().nextInt(10)));
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * 获取文件列表
     */
    @GetMapping("/getAttachmentList")
    public Result<Object> getAttachmentList(
            HttpServletRequest request,
            Integer page, Integer limit, Integer pid, Integer fileType
    ) {
        Page<AttachmentModel> modelPage = attachmentService.getPageAttList(page, limit, pid, fileType);
        AttachmentVo vo = new AttachmentVo();
        vo.setList(modelPage.getRecords());
        vo.setCount((int) modelPage.getTotal());
        return Result.success(vo);
    }

    /**
     * 删除文件
     */
    @GetMapping("/delAttachment")
    public Result<Object> delAttachment(HttpServletRequest request, String ids) {
        //获取ID数组
        String[] str = ids.split(",");
        for (int i = 0; i < str.length; i++) {
            AttachmentModel model = attachmentService.getAttById(Integer.parseInt(str[i]));
            COSClient cosClient = COSClientUtil.getCosClient();
            // Bucket 的命名格式为 BucketName-APPID ，此处填写的存储桶名称必须为此格式
            String bucketName = SystemConstant.bucketName;
            // 指定被删除的文件在 COS 上的路径，即对象键。例如对象键为 folder/picture.jpg，则表示删除位于 folder 路径下的文件 picture.jpg
            String key = "/uploads/" + model.getName();
            cosClient.deleteObject(bucketName, key);
            attachmentService.delete(Integer.parseInt(str[i]));
        }
        return Result.success("操作成功");
    }

    /**
     * 文件移动
     */
    @PostMapping("/updateAttachment")
    public Result<Object> updateAttachment(
            HttpServletRequest request,
            @RequestBody JSONObject json
    ) {
        String ids = json.getString("ids");
        Integer pid = Integer.valueOf(json.getString("pid"));
        //获取ID数组
        String[] str = ids.split(",");
        for (int i = 0; i < str.length; i++) {
            AttachmentModel model = attachmentService.getAttById(Integer.valueOf(str[i]));
            model.setPid(pid);
            attachmentService.update(model);
        }
        return Result.success("操作成功");
    }
}
