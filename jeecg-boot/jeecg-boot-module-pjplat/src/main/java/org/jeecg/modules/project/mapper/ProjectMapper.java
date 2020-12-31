package org.jeecg.modules.project.mapper;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.jeecg.modules.company.entity.Company;
import org.jeecg.modules.project.entity.KeyValue;
import org.jeecg.modules.project.entity.Project;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description: 项目信息
 * @Author: jeecg-boot
 * @Date:   2020-10-15
 * @Version: V1.0
 */
public interface ProjectMapper extends BaseMapper<Project> {

    @Select(value = "select USER_PRJ from pjplat.BS_USER where USERNAME=#{username}")
    public String  getProjectListByUsername(String username);



    public Project  getProject(String id,String prjName,String prjType);
    public List<Company> getCompanyInfoById(Long mainId,String type, Integer start, Integer end);

    public boolean deleteCompanyInfoById(Long cId,Long prjId);

    @Update(value = "update pjplat.bs_project set del_flag=1 where id=#{id}")
    void delById(Long id);

    List<KeyValue<String,String>> listProject();
}
