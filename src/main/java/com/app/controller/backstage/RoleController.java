package com.app.controller.backstage;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.app.model.AdminUserModel;
import com.app.model.PermissionModel;
import com.app.model.RoleModel;
import com.app.service.AdminUserService;
import com.app.service.PermissionService;
import com.app.service.RoleService;
import com.app.util.CommonHelp;
import com.app.util.Result;
import com.app.vo.RoleVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@RestController
@Controller
@RequestMapping("/admin/role")
public class RoleController {
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @GetMapping("/getRoleTree")//权限管理树
    public Result<Object> getRoleTree(
            HttpServletRequest request) {
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
        AdminUserModel user = adminUserService.getUserByUserId(Integer.valueOf(userId));
        if (user == null) {
            return Result.error("用户不存在");
        }
        List<PermissionModel> permissionModels = permissionService.selectTree();//全查
        if (permissionModels == null) {
            return Result.error("暂无数据");
        }

        List<Integer> arr1 = new ArrayList<>();
        for (int i = 0; i < permissionModels.size(); i++) {
            arr1.add(permissionModels.get(i).getId());
        }
        Integer ArrId = 1;
        boolean result1 = arr1.contains(ArrId);
        List<Integer> pidArr1 = new ArrayList<>();
        if (!result1) {
            for (int i = 0; i < permissionModels.size(); i++) {

                boolean PidResult = arr1.contains(permissionModels.get(i).getPid());
                if (!PidResult) {
                    pidArr1.add(permissionModels.get(i).getPid());
                }
            }
            pidArr1.add(ArrId);
            arr1.addAll(pidArr1);
            LinkedHashSet<Integer> hashSet = new LinkedHashSet<>(arr1);
            ArrayList<Integer> listWithoutDuplicates = new ArrayList<>(hashSet);
            List<PermissionModel> permissionModels1 = new ArrayList<>();
            for (int i = 0; i < listWithoutDuplicates.size(); i++) {
                PermissionModel permissionModel = permissionService.selectById(listWithoutDuplicates.get(i));
                permissionModels1.add(permissionModel);
            }
            return Result.success(findChildren(permissionModels1, 0));
        }
        return Result.success(findChildren(permissionModels, 0));
    }

    private List<PermissionModel> findChildren(List<PermissionModel> modelList, Integer pid) {
        List<PermissionModel> personModelList = new ArrayList<>();
        for (int i = 0; i < modelList.size(); i++) {
            if (modelList.get(i).getPid().equals(pid)) {
                PermissionModel model = modelList.get(i);
                List<PermissionModel> modelList1 = findChildren(modelList, model.getId());
                for (int j = 0; j < modelList1.size(); j++) {
                    model.setChildList(modelList1);
                }
                personModelList.add(model);
            }
        }
        return personModelList;
    }

    private List<PermissionModel> findChildren2(List<PermissionModel> modelList, Integer pid) {
        List<PermissionModel> personModelList = new ArrayList<>();
        for (int i = 0; i < modelList.size(); i++) {
            if (modelList.get(i).getPid() == pid) {
                PermissionModel model = modelList.get(i);
                List<PermissionModel> modelList1 = findChildren(modelList, model.getId());
                for (int j = 0; j < modelList1.size(); j++) {
                    if (modelList1 != null && modelList1.size() > 0) {
                        model.setChildList(modelList1);
                    } else {
                        model.setChildList(new ArrayList<>());
                    }
                }
                personModelList.add(model);
            }
        }
        return personModelList;
    }

    @GetMapping("/getRoleList")//角色管理分页查询
    public Result<Object> getRoleList(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            @RequestParam(required = false) String roleName,
            @RequestParam(required = false) Integer status,
            HttpServletRequest request) {
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
        AdminUserModel user = adminUserService.getUserByUserId(Integer.valueOf(userId));
        if (user == null) {
            return Result.error("用户不存在");
        }
        Page<RoleModel> roleModelPage = roleService.getPageRoleList(page, limit, roleName, status);

        for (int i = 0; i < roleModelPage.getRecords().size(); i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (roleModelPage.getRecords().get(i).getCreateTime() != null) {
                String crtTime = sdf.format(roleModelPage.getRecords().get(i).getCreateTime());
                roleModelPage.getRecords().get(i).setCreateTimeText(crtTime);
            }
            if (roleModelPage.getRecords().get(i).getUpdateTime() != null) {
                String upTime = sdf.format(roleModelPage.getRecords().get(i).getUpdateTime());
                roleModelPage.getRecords().get(i).setUpdateTimeText(upTime);
            }
        }
        if (roleModelPage == null) {
            return Result.error("暂无数据");
        }
        int count = (int) roleModelPage.getTotal();
        List<RoleModel> roleModelList;
        roleModelList = roleModelPage.getRecords();
        RoleVo vo = new RoleVo();
        vo.setCount(count);
        vo.setList(roleModelList);
        return Result.success(vo);
    }

    @PostMapping("/insertRole")//    新增/修改角色管理
    public Result<Object> insertRole(
            @RequestBody JSONObject json,
            HttpServletRequest request) {
        Integer id = Integer.valueOf(json.get("id").toString());
        String roleName = json.get("roleName").toString();
        String rules = json.get("rules").toString();
        Integer status = Integer.valueOf(json.get("status").toString());
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
        AdminUserModel user = adminUserService.getUserByUserId(Integer.valueOf(userId));
        if (user == null) {
            return Result.error("用户不存在");
        }
        RoleModel roleModel = new RoleModel();

        List<Integer> idList = new ArrayList<>();
        String ruleText = "";
        //获取ID数组
        if (rules == null || rules.length() <= 0) {
            roleModel.setRules(null);
        } else {
            String str[] = rules.split(",");

            for (int i = 0; i < str.length; i++) {
                idList.add(Integer.valueOf(str[i]));
            }
            List<PermissionModel> permissionModels = permissionService.getModelByIds(idList);
            for (int i = 0; i < permissionModels.size(); i++) {
                ruleText += permissionModels.get(i).getPerms() + ";";
            }
        }
        roleModel.setRules(ruleText);
        roleModel.setStatus(status);
        try {
            if (id != null && id > 0) {
                roleModel.setId(id);
                roleModel.setRoleName(roleName);
                roleModel.setUpdateTime(CommonHelp.getNowTimestamp());
                roleService.update(roleModel);
            } else {
                RoleModel model = roleService.getRoleByName(roleName);
                if (model != null) {
                    return Result.error("角色名称重复");
                }
                roleModel.setRoleName(roleName);
                roleModel.setCreateTime(CommonHelp.getNowTimestamp());
                roleService.insert(roleModel);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return Result.success(null);
    }

    @GetMapping("/updateStatus")//修改状态
    public Result<Object> updateStatus(
            @RequestParam("id") Integer id,
            @RequestParam("status") Integer status,
            HttpServletRequest request) {
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
        AdminUserModel user = adminUserService.getUserByUserId(Integer.valueOf(userId));
        if (user == null) {
            return Result.error("用户不存在");
        }
        RoleModel roleModel = new RoleModel();
        roleModel.setId(id);
        roleModel.setStatus(status);
        roleService.update(roleModel);
        return Result.success(null);
    }

    @GetMapping("/deleteById")//删除角色
    public Result<Object> deleteById(
            @RequestParam("id") Integer id,
            HttpServletRequest request) {
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
        AdminUserModel user = adminUserService.getUserByUserId(Integer.valueOf(userId));
        if (user == null) {
            return Result.error("用户不存在");
        }
        RoleModel roleModel = roleService.getRoleByRoleId(id);
        if (roleModel == null) {
            return Result.error("该角色不存在");
        } else {
            roleService.delete(id);
            return Result.success(null);
        }
    }

    @GetMapping("/getRoleById")//根据ID查询
    public Result<Object> getRoleById(
            @RequestParam("id") Integer id,
            HttpServletRequest request) {
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
        AdminUserModel user = adminUserService.getUserByUserId(Integer.valueOf(userId));
        if (user == null) {
            return Result.error("用户不存在");
        }
        RoleModel roleModel = roleService.getRoleByRoleId(id);
        List<PermissionModel> permissionModels = permissionService.selectTree();//全查
        for (int i = 0; i < permissionModels.size(); i++) {
            if (roleModel.getRules().contains(permissionModels.get(i).getPerms())) {
                permissionModels.get(i).setChecked(true);
            } else {
                permissionModels.get(i).setChecked(false);
            }
        }

        roleModel.setMenuList(findChildren(permissionModels, 0));
        return Result.success(roleModel);
    }

    @GetMapping("/getUserPower")//根据ID查询
    public Result<Object> getUserPower(
            HttpServletRequest request) {
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
        AdminUserModel user = adminUserService.getUserByUserId(Integer.valueOf(userId));
        if (user == null) {
            return Result.error("用户不存在");
        }
        RoleModel roleModel = roleService.getRoleByRoleId(user.getRoles());
        List<PermissionModel> permissionModels = permissionService.getListByNotMenuType("A");//查询按钮权限除外
        List<PermissionModel> modelList = new ArrayList<>();
        for (int i = 0; i < permissionModels.size(); i++) {
            if (roleModel.getRules().contains(permissionModels.get(i).getPerms())) {
                modelList.add(permissionModels.get(i));
            }
        }
        return Result.success(findChildren2(modelList, 0));
    }

}
