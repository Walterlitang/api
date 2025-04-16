package com.app.controller.backstage;

import com.app.model.FileClassModel;
import com.app.service.FileClassService;
import com.app.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Controller
@RequestMapping("/admin")
public class FileController {
    @Autowired
    private FileClassService fileClassService;


    /**
     * 文件
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/getFileTypeList")
    public Result<Object> getTextList(Integer pid) {
        List<FileClassModel> modelList = fileClassService.getModelList();
        return Result.success(findChildren(modelList, pid));
    }

    private List<FileClassModel> findChildren(List<FileClassModel> modelList, Integer pid) {
        List<FileClassModel> fileClassModelList = new ArrayList<>();
        for (int i = 0; i < modelList.size(); i++) {
            if (modelList.get(i).getPid() == pid) {
                FileClassModel model = modelList.get(i);
                List<FileClassModel> modelList1 = findChildren(modelList, model.getId());
                for (int j = 0; j < modelList1.size(); j++) {
                    if (modelList1 != null && modelList1.size() > 0) {
                        model.setChild(modelList1);
                    }
                }
                fileClassModelList.add(model);
            }
        }
        return fileClassModelList;
    }

}
