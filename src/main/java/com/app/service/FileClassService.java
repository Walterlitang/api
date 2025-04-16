package com.app.service;

import com.app.model.FileClassModel;

import java.util.List;

public interface FileClassService {

    List<FileClassModel> getModelList();

    List<FileClassModel> getModelListByPid(int pid);

}
