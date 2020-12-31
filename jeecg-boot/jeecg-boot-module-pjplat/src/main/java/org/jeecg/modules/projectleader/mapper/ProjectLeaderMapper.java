package org.jeecg.modules.projectleader.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.project.entity.SysUser;
import org.jeecg.modules.projectleader.entity.ProjectLeader;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: BS_PROJECT_LEADER
 * @Author: jeecg-boot
 * @Date:   2020-10-19
 * @Version: V1.0
 */
public interface ProjectLeaderMapper extends BaseMapper<ProjectLeader> {
   @Select(value = " select realname ,phone from htplat.sys_user where id in (select PRJL_LEADER from pjplat.BS_PROJECT_LEADER where PRJL_PRJ_ID=#{prjId} and PRJL_DEPARTMENT =#{departmentId})")
   public List<SysUser> getProjectRealNameById(String prjId, String departmentId);

   @Select(value = " select username from htplat.sys_user where id in (select PRJL_LEADER from pjplat.BS_PROJECT_LEADER where PRJL_PRJ_ID=#{prjId} and PRJL_DEPARTMENT =#{departmentId})")
   public List<String> getProjectUserNameById(String prjId,String departmentId);

   public String getDepartmentIdByDepartmentName(String departmentName,String bussiness);

}
