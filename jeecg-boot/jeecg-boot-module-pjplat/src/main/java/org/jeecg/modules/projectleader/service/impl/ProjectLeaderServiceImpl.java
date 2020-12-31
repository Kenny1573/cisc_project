package org.jeecg.modules.projectleader.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.project.entity.SysUser;
import org.jeecg.modules.project.mapper.SysUserProjectMapper;
import org.jeecg.modules.projectleader.entity.ProjectLeader;
import org.jeecg.modules.projectleader.mapper.ProjectLeaderMapper;
import org.jeecg.modules.projectleader.service.IProjectLeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @Description: BS_PROJECT_LEADER
 * @Author: jeecg-boot
 * @Date: 2020-10-19
 * @Version: V1.0
 */
@Service
public class ProjectLeaderServiceImpl extends ServiceImpl<ProjectLeaderMapper, ProjectLeader> implements IProjectLeaderService {

    @Autowired
    ProjectLeaderMapper projectLeaderMapper;

    @Autowired
    SysUserProjectMapper sysUserMapper;
    //33025代表 前期业主代表
    //33026代表 实施业主代表
    @Override
    public JSONObject getProjectRealNameById(String prjId) {
        Map departmentMap = setMapstr();

        JSONObject jsonObject = new JSONObject();
        departmentMap.forEach((key, value) -> {
            if (value != null && !value.equals(""))
                jsonObject.put((String) key, projectLeaderMapper.getProjectRealNameById(prjId, (String) value).stream().map(sysUser -> {
                    if(sysUser.getPhone()==null||sysUser.getPhone().equals("")){
                        sysUser.setPhone("");
                    }
                    return sysUser.toRealNamePhoneString();
                }).collect(Collectors.toList()));
            else
                jsonObject.put((String) key, projectLeaderMapper.getProjectRealNameById(prjId, ""));
        });
        return jsonObject;
    }



    //33025代表 前期业主代表
    //33026代表 实施业主代表
    @Override
    public JSONObject getProjectUserNameById(String prjId) {
        Map departmentMap = setMapstr();
        JSONObject jsonObject = new JSONObject();
        departmentMap.forEach((key, value) -> {
            if (value != null && !value.equals(""))
                jsonObject.put((String) key, projectLeaderMapper.getProjectUserNameById(prjId, (String) value));
            else
                jsonObject.put((String) key, projectLeaderMapper.getProjectUserNameById(prjId, ""));
        });
        return jsonObject;
    }

    @Override
    public boolean addProjectLeader(JSONObject jsonObject) {
        Map departmentMap = setMapstr();
        departmentMap.forEach((key,value)->{
            List<String> users= Arrays.asList(jsonObject.getString((String) key).split(","));
            Consumer<String> consumer=user -> projectLeaderMapper.insert(new ProjectLeader(jsonObject.getString("prjId"), (String) value,user));
            if (users.size()>0)
            users.forEach((x) ->{
                List<String> userIds= sysUserMapper.getUserIdByName(x);
                if (userIds.size()>0)
                userIds.forEach(consumer);
            });
        });
        return true;

    }

    @Override
    public boolean updateProjectLeader(JSONObject jsonObject) {

        Map departmentMap = setMapstr();
        departmentMap.forEach((key, value) -> {
            if (value != null && !value.equals("")) {
                List<String> usernames = projectLeaderMapper.getProjectUserNameById(jsonObject.getString("prjId"), (String) value);
                List<String> users = Arrays.asList(jsonObject.getString((String) key).split(","));
                Predicate<String> saveP = x->!usernames.contains(x);
                List<String> saveUser=users.stream().filter(saveP).collect(Collectors.toList());
                Consumer<String> consumer=user -> projectLeaderMapper.insert(new ProjectLeader(jsonObject.getString("prjId"), (String) value,user));
                saveUser.forEach((x) ->{
                    List<String> userIds= sysUserMapper.getUserIdByName(x);
                    userIds.forEach(consumer);
                });
                Predicate<String> deleteP = x->!users.contains(x);
                List<String> deleteUser = usernames.stream().filter(deleteP).collect(Collectors.toList());
                Consumer<String> deleteConsumer = x->projectLeaderMapper.delete(new QueryWrapper<ProjectLeader>().eq("PRJL_LEADER",x));
                deleteUser.forEach((x)->{
                    List<String> userIds= sysUserMapper.getUserIdByName(x);
                    userIds.forEach(deleteConsumer);
                });
            }
        });
        return true;
    }

    @Override
    public String getDepartmentIdByDepartmentName(String departmentName, String bussiness) {
        return projectLeaderMapper.getDepartmentIdByDepartmentName(departmentName,bussiness);
    }

    public Map setMapstr(){
        String bussiness = projectLeaderMapper.getDepartmentIdByDepartmentName("无锡市城市重点建设项目管理中心", "");
        Map departmentMap = new HashMap();
        departmentMap.put("preOwner", "33025");
        departmentMap.put("midOwner", "33026");
        departmentMap.put("other", "33027");
        String office = projectLeaderMapper.getDepartmentIdByDepartmentName("总工程师办公室", bussiness);
        departmentMap.put("office", office);
        String plan = projectLeaderMapper.getDepartmentIdByDepartmentName("计划部", bussiness);
        departmentMap.put("plan", plan);
        String bridge = projectLeaderMapper.getDepartmentIdByDepartmentName("道桥工程部", bussiness);
        departmentMap.put("bridge", bridge);
        String build = projectLeaderMapper.getDepartmentIdByDepartmentName("建筑工程部", bussiness);
        departmentMap.put("build", build);
        String quality = projectLeaderMapper.getDepartmentIdByDepartmentName("质量安全监督部", bussiness);
        departmentMap.put("quality", quality);
        String finance = projectLeaderMapper.getDepartmentIdByDepartmentName("财务审计部", bussiness);
        departmentMap.put("finance", finance);
        return departmentMap;
    }


}
