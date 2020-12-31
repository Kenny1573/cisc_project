package org.jeecg.modules.projectleader.service;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.projectleader.entity.ProjectLeader;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: BS_PROJECT_LEADER
 * @Author: jeecg-boot
 * @Date:   2020-10-19
 * @Version: V1.0
 */
public interface IProjectLeaderService extends IService<ProjectLeader> {

     JSONObject getProjectRealNameById(String prjId);
     JSONObject getProjectUserNameById(String prjId);
     boolean addProjectLeader(JSONObject jsonObject);
    boolean updateProjectLeader(JSONObject jsonObject);
    public String getDepartmentIdByDepartmentName(String departmentName,String bussiness);

}
