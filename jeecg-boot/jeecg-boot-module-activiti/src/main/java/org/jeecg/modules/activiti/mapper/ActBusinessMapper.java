package org.jeecg.modules.activiti.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.jeecg.modules.activiti.entity.ActBusiness;
import org.jeecg.modules.activiti.entity.ProblemSheet;

import java.util.List;
import java.util.Map;

/**
 * @Description: 流程业务扩展表
 * @Author: pmc
 * @Date:   2020-03-30
 * @Version: V1.0
 */
public interface ActBusinessMapper extends BaseMapper<ActBusiness> {
    @Select("select * from ${tableName} where id = #{tableId}")
    Map<String,Object> getBusiData(@Param("tableId") String tableId, @Param("tableName") String tableName);

    @Insert("${sql}")
    int insertBusiData(@Param("sql") String sql);

    @Update("${sql}")
    int updateBusiData(@Param("sql") String sql);
    
    @Delete("${sql}")
    int deleteBusiData1(@Param("sql") String sql);

    @Delete("delete from ${tableName} where id = #{tableId}")
    int deleteBusiData(@Param("tableId") String tableId, @Param("tableName") String tableName);

    @Select("SELECT ahi.USER_ID_ FROM ACT_HI_IDENTITYLINK ahi\n" +
            "      WHERE TYPE_ = #{type} AND TASK_ID_ = #{taskId}\n" +
            "      ")  //LIMIT 1
    String findUserIdByTypeAndTaskId(@Param("type") String type, @Param("taskId") String taskId);

    @Insert("INSERT INTO ACT_HI_IDENTITYLINK (ID_, TYPE_, USER_ID_, TASK_ID_, PROC_INST_ID_)\n" +
            "      VALUES (#{id}, #{type}, #{userId}, #{taskId}, #{procInstId})")
    int insertHI_IDENTITYLINK(@Param("id") String id, @Param("type")String type, @Param("userId")String userId, @Param("taskId")String taskId, @Param("procInstId")String procInstId);

    @Select("SELECT ari.ID_ FROM ACT_RU_IDENTITYLINK ari\n" +
            "      WHERE TYPE_ = #{type} AND TASK_ID_ = #{taskId}")
    List<String> selectIRunIdentity(@Param("taskId")String taskId,@Param("type") String type);

    @Update("update ${tableName} set act_status = #{actStatus} where id = #{tableId}")
    int updateBusinessStatus(@Param("tableName")String tableName, @Param("tableId")String tableId, @Param("actStatus")String actStatus);

    @Select("select COM_ID from pjplat.BS_PROJECT_COMPANY where PRJ_ID=#{psPrjId} and COM_TYPE=#{type}")
	String findCompanyIdByPrjIdAndType(@Param("psPrjId")Long psPrjId, @Param("type") String type);
    
    @Insert("${sql}")
    void insertProblemSheetPhoto(@Param("sql") String sql);
    
    @Select("${sql}")
	String selectTableId(@Param("sql") String sql);
    
    @Select("select b.prj_name as prj_name, a.ps_location, c.name as ps_type, d.item_text as ps_level, a.ps_description, e.item_text as ps_advice,to_char(a.ps_time,'yyyy-mm-dd hh24:mi:ss') as ps_time,case when a.ps_end_time is not null then to_char(a.ps_end_time,'yyyy-mm-dd hh24:mi:ss')  else '' end as ps_end_time from pjplat.BS_PROBLEM_SHEET a \n" + 
    		"       left join pjplat.BS_PROJECT b on a.ps_prj_id = b.id\n" + 
    		"       left join htplat.SYS_CATEGORY c on a.ps_type = c.code\n" + 
    		"       left join (select item_value, item_text from htplat.SYS_DICT_ITEM where dict_id=(select id from htplat.SYS_DICT where dict_code='hazard_level' )) d on a.ps_level=d.item_value\n" + 
    		"       left join (select item_value, item_text from htplat.SYS_DICT_ITEM where dict_id=(select id from htplat.SYS_DICT where dict_code='deal_advice' )) e on a.ps_advice=e.item_value\n" + 
    		"       where a.id=#{tableId}")
    Map<String,Object> getProblemSheetData(@Param("tableId") String tableId);

    @Select("select PSP_PHOTO_PATH from pjplat.BS_PROBLEM_SHEET_PHOTO where PSP_PS_ID=#{tableId}")
	List<String> getProblemSheetPhotoData(@Param("tableId") String tableId);

    @Select("select b.depart_name as user_depart, a.user_prj, d.com_name as user_com, user_post from pjplat.bs_user a\n" + 
    		"       left join htplat.sys_depart b on a.user_depart= b.org_code \n" + 
    		"       left join pjplat.bs_company d on a.user_com = d.id\n" + 
    		"       where a.id=#{tableId}")
	Map<String, Object> getUserData(@Param("tableId") String tableId);

    @Select("select PRJ_NAME from pjplat.BS_PROJECT where id=#{tableId}")
	String getUserPrjData(@Param("tableId") String tableId);
    
    @Select("${sql}")
  	List<String> getSelectListData(@Param("sql") String sql);
    
    @Select("${sql}")
   	List<Map<String, Object>> getSelectListMap(@Param("sql") String sql);
}
