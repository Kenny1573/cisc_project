package org.jeecg.modules.activiti.service.Impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import me.zhyd.oauth.utils.UuidUtils;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.jeecg.common.util.CommonUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.activiti.entity.ActBusiness;
import org.jeecg.modules.activiti.entity.ProblemSheet;
import org.jeecg.modules.activiti.mapper.ActBusinessMapper;
import org.jeecg.modules.activiti.service.IActBusinessService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @Description: 流程业务扩展表
 * @Author: pmc
 * @Date:   2020-03-30
 * @Version: V1.0
 */
@Service
public class ActBusinessServiceImpl extends ServiceImpl<ActBusinessMapper, ActBusiness> implements IActBusinessService {
	
    public List<ActBusiness> findByProcDefId(String id) {
       return this.list(new LambdaQueryWrapper<ActBusiness>().eq(ActBusiness::getProcDefId,id));
    }
    /**保存业务表单数据到数据库表
     * <br>该方法相对通用，复杂业务单独定制，套路类似
     * @param tableId 业务表中的数据id
     * */
    public void saveApplyForm(String tableId, HttpServletRequest request) {
        String tableName = request.getParameter("tableName");
        String filedNames = request.getParameter("filedNames");
        Map<String, Object> busiData = this.baseMapper.getBusiData(tableId, tableName);
        String[] fileds = filedNames.split(",");
        if (MapUtil.isEmpty(busiData)){ //没有，新增逻辑
            StringBuilder filedsB = new StringBuilder("id");
            StringBuilder filedsVB = new StringBuilder("'"+tableId+"'");
            for (String filed : fileds) {
                filedsB.append(","+filed);
                filedsVB.append(",'"+request.getParameter(filed)+"'");
            }
            this.baseMapper.insertBusiData(String.format("INSERT INTO %s (%s) VALUES (%s)",tableName,filedsB.toString().replace("file", "files"),filedsVB.toString()));
        }else { //有，修改
            StringBuilder setSql = new StringBuilder();
            for (String filed : fileds) {
                String parameter = request.getParameter(filed);
                if (parameter==null){
                    setSql.append(String.format("%s = null,",filed));
                }else {
                    setSql.append(String.format("%s = '%s',",filed, parameter));
                }
            }
            String substring = setSql.substring(0, setSql.length()-1);//去掉最后一个,号
            this.baseMapper.updateBusiData(String.format("update %s set %s where id = '%s'",tableName,substring,tableId));
        }

    }
    

    public Map<String, Object> getApplyForm(String tableId, String tableName) {
        return this.baseMapper.getBusiData(tableId, tableName);
    }

    public void deleteBusiness(String tableName, String tableId) {
        this.baseMapper.deleteBusiData(tableId,tableName);
    }
    /**
     *通过类型和任务id查找用户id
     * */
    public String findUserIdByTypeAndTaskId(String type, String taskId) {
        return baseMapper.findUserIdByTypeAndTaskId(type, taskId);
    }

    public void insertHI_IDENTITYLINK(String id, String type, String userId, String taskId, String procInstId) {
        this.baseMapper.insertHI_IDENTITYLINK(id, type, userId, taskId, procInstId);
    }

    public List<String> selectIRunIdentity(String taskId, String type) {
       return baseMapper.selectIRunIdentity(taskId,type);
    }
/**修改业务表的流程字段*/
    public void updateBusinessStatus(String tableName, String tableId, String actStatus) {
        try {
            baseMapper.updateBusinessStatus(tableName,tableId,actStatus);
        } catch (Exception e) {
             // 业务表需要有 act_status字段，没有会报错，不管他
            e.printStackTrace();
        }
    }
	public String getPsUnit(Long psPrjId, String type) {
		 return baseMapper.findCompanyIdByPrjIdAndType(psPrjId, type);
	}
	
	public String  saveApplyForms(HttpServletRequest request, String processType) {
		String tableId = "";
    	if (processType.contains("problem")) {
    		/*String sql = "select pjplat.SEQ_BS_PROBLEM_SHEET.nextval from dual";
    		tableId = this.baseMapper.selectTableId(sql);
    		sql = String.format("INSERT INTO pjplat.BS_PROBLEM_SHEET(id, PS_PRJ_ID, PS_CONSTRUCTION_UNIT, PS_SUPERVISING_UNIT, PS_LOCATION, PS_TYPE, PS_LEVEL, PS_DESCRIPTION, PS_ADVICE, PS_END_TIME, CREATE_BY) "
    				+ "VALUES (%s,%s,%s,%s,'%s','%s','%s','%s','%s',to_date('%s','yyyy-MM-dd hh24:mi:ss'),'%s')",tableId,problemSheet.getPsPrjId(), problemSheet.getPsConstructionUnit(),problemSheet.getPsSupervisingUnit(),problemSheet.getPsLocation(),
    				problemSheet.getPsType(), problemSheet.getPsLevel(), problemSheet.getPsDescription(), problemSheet.getPsAdvice(), problemSheet.getPsEndTime(), problemSheet.getCreateBy());
            this.baseMapper.insertProblemSheet(sql);*/
    		String sql = "select pjplat.SEQ_BS_PROBLEM_SHEET.nextval from dual";
    		tableId = this.baseMapper.selectTableId(sql);
    		String tableName = request.getParameter("tableName");
            String filedNames = request.getParameter("psFieldNames");
            Map<String, Object> busiData = this.baseMapper.getBusiData(tableId, tableName);
            String[] fileds = filedNames.replaceAll(" ", "").split(",");
            
            if (MapUtil.isEmpty(busiData)){ //没有，新增逻辑
                StringBuilder filedsB = new StringBuilder("id");
                StringBuilder filedsVB = new StringBuilder("'"+tableId+"'");
                for (String filed : fileds) {
                    filedsB.append(", "+filed);
                    //System.out.println(filed+":"+request.getParameter(filed));
                    if (StrUtil.isNotBlank(request.getParameter(filed) ) ) {
                    	if (filed.contains("TIME")) {
                    		filedsVB.append(",to_date('"+request.getParameter(filed).replace("null", "")+"','yyyy-mm-dd hh24:mi:ss')");
                        }
                        else 
                        	filedsVB.append(",'"+request.getParameter(filed).replace("null", "")+"'");
                    } else {
                    	filedsVB.append(",''");
                    }
                    	
                }
                //整改单数据插入
               // String sql1 = "insert into pjplat.BS_PROBLEM_SHEET (id,PS_PRJ_ID,PS_LOCATION,PS_TIME,PS_TYPE, PS_LEVEL,PS_DESCRIPTION, PS_ADVICE, PS_END_TIME)"
                //		+ "values()";
                System.out.println("-------------------");
               // System.out.println(String.format("INSERT INTO %s (%s) VALUES (%s)",tableName,filedsB.toString(),filedsVB.toString()));
                this.baseMapper.insertBusiData(String.format("INSERT INTO %s (%s) VALUES (%s)",tableName,filedsB.toString(),filedsVB.toString()));
            }else { //有，修改
                StringBuilder setSql = new StringBuilder();
                for (String filed : fileds) {
                    String parameter = request.getParameter(filed);
                    if (parameter==null){
                        setSql.append(String.format("%s = null,",filed));
                    }else {
                        setSql.append(String.format("%s = '%s',",filed, parameter));
                    }
                }
                String substring = setSql.substring(0, setSql.length()-1);//去掉最后一个,号
                this.baseMapper.updateBusiData(String.format("update %s set %s where id = '%s'",tableName,substring,tableId));
            }
    	} else if(processType.contains("user")) {
    		String sql = "select pjplat.SEQ_BS_USER.nextval from dual";
    		tableId = this.baseMapper.selectTableId(sql);
    		String tableName = request.getParameter("tableName");
            String filedNames = request.getParameter("userFieldNames");
            String[] fileds = filedNames.split(",");
            StringBuilder filedsB = new StringBuilder("id");
            StringBuilder filedsVB = new StringBuilder("'"+tableId+"'");
            for (String filed : fileds) {
                filedsB.append(","+filed);
                filedsVB.append(",'"+request.getParameter(filed)+"'");
            }
            //整改单数据插入
            this.baseMapper.insertBusiData(String.format("INSERT INTO %s (%s) VALUES (%s)",tableName,filedsB.toString(),filedsVB.toString()));
    	}
		return tableId;
    }
	public void saveApplyFormsPhoto(String tableId, String filePath) {
		String sql = "select pjplat.SEQ_BS_PROBLEM_SHEET_PHOTO.nextval from dual";
		String photoTableId = this.baseMapper.selectTableId(sql);
		sql = String.format("INSERT INTO pjplat.BS_PROBLEM_SHEET_PHOTO(id, PSP_PS_ID, PSP_PHOTO_PATH) "
				+ "VALUES (%s,%s,'%s')",photoTableId,tableId, filePath);
        this.baseMapper.insertProblemSheetPhoto(sql);
	}
	
	
	public Map<String,Object> getProblemSheetData(String tableId) {
        return this.baseMapper.getProblemSheetData(tableId);
	}
	public List<String> getProblemSheetPhotoData(String tableId) {
		return this.baseMapper.getProblemSheetPhotoData(tableId);
	}
	public Map<String, Object> getUserData(String tableId) {
		return this.baseMapper.getUserData(tableId);
	}
	public String getUserPrjData(String tableId) {
		return this.baseMapper.getUserPrjData(tableId);
	}
	public String selectProblemSheetId(String procInstId) {
		String sql = "select table_id from htplat.ACT_Z_BUSINESS where proc_inst_id="+procInstId+" and table_name='pjplat.BS_PROBLEM_SHEET'";
		return this.baseMapper.selectTableId(sql);
	}
	public void saveProblemZgPhoto(String tableId, String taskId, String filePath) {
		String sql = "select pjplat.SEQ_BS_PROBLEM_SHEET_RECORD.nextval from dual";
		String photoTableId = this.baseMapper.selectTableId(sql);
		sql = String.format("INSERT INTO pjplat.BS_PROBLEM_SHEET_RECORD(id, PSR_PS_ID, PSR_ACT_TASK_ID, PSR_PS_PHOTO) "
				+ "VALUES (%s,%s,'%s','%s')",photoTableId, tableId, taskId, filePath);
        this.baseMapper.insertProblemSheetPhoto(sql);
	}
	public void saveApplyFormsSender(String tableId, String processInstanceId, String senders) {
		String sql = "select pjplat.SEQ_BS_PROBLEM_SHEET_SENDER.nextval from dual";
		String photoTableId = this.baseMapper.selectTableId(sql);
		sql = String.format("INSERT INTO pjplat.BS_PROBLEM_SHEET_SENDER(id, PSS_PS_ID, PROC_INST_ID, SENDERS) "
				+ "VALUES (%s,%s,'%s','%s')",photoTableId, tableId, processInstanceId, senders);
        this.baseMapper.insertProblemSheetPhoto(sql);
	}
	
	public void syncUserDepartAndRole(String procInstId) {
		String sql = "select a.username, a.user_depart, a.user_prj, a.user_com,a.user_post, b.id as userid, c.id as departid from pjplat.bs_user a \n" + 
				"       left join htplat.sys_user b on a.username = b.username\n" + 
				"       left join htplat.sys_depart c on a.user_depart = c.org_code\n" + 
				"        where a.id=( select table_id from htplat.ACT_Z_BUSINESS where proc_inst_id="+procInstId+" and table_name='pjplat.BS_USER')";
		List<Map<String, Object>> userList =  this.baseMapper.getSelectListMap(sql);
		if (userList != null && userList.size() > 0) {
			String userName = userList.get(0).get("USERNAME")+"";
			String userDepart = userList.get(0).get("USER_DEPART")+"";
			String userPrj = userList.get(0).get("USER_PRJ")+"";
			String userCom = userList.get(0).get("USER_COM")+"";
			String userPost = userList.get(0).get("USER_POST")+"";
			String userId = userList.get(0).get("USERID")+"";
			String departId = userList.get(0).get("DEPARTID")+"";
			
			//更新sys_user表，同步org_code和post字段和com_id字段，其中com_id为新增字段
			sql = "update htplat.sys_user set org_code='"+userDepart+"', post='"+userPost+"', com_id='"+userCom+"'where username = '"+userName+"'";
			this.baseMapper.updateBusiData(sql);
			//向sys_user_depart表中插入记录  先删除已有的记录
			sql = "delete from htplat.sys_user_depart where user_id='"+userId+"'";
			this.baseMapper.deleteBusiData1(sql);
			String userDepaerId = UUID.randomUUID().toString().replace("-", "");
			sql = "insert into htplat.sys_user_depart values('"+userDepaerId+"', '"+userId+"', '"+departId+"')";
			this.baseMapper.insertBusiData(sql);
			//向sys_user_role
			//向bs_user_project表中插入记录
			if (StrUtil.isNotBlank(userPrj)) {
				String[] userPrjs = userPrj.split(",");
				if (userPrjs != null && userPrjs.length > 0) {
					for (int i = 0; i < userPrjs.length; i++) {
						sql = "insert into pjplat.BS_USER_PROJECT values(pjplat.SEQ_BS_USER_PROJECT.nextval, '"+userName+"', '"+userPrjs[i]+"')";
						this.baseMapper.insertBusiData(sql);
					}
				}
			}
		}
	}
	
	public Set<String> queryProcInstIds(String userName, String startTime, String endTime, String psType, String prjId) {
		
		String condition = "";
		String condition1 = "";
		if (StrUtil.isNotBlank(startTime)) {
			condition += " and b.create_time >= to_date('"+startTime+"','yyyy/mm/dd hh24:mi:ss')";
			condition1 += " and b.create_time >= to_date('"+startTime+"','yyyy/mm/dd hh24:mi:ss')";
		}
		if (StrUtil.isNotBlank(endTime)) {
			condition += " and b.create_time <= to_date('"+endTime+"','yyyy/mm/dd hh24:mi:ss')";
			condition1 += " and b.create_time <= to_date('"+endTime+"','yyyy/mm/dd hh24:mi:ss')";
		}
		if (StrUtil.isNotBlank(psType)) {
			condition += " and c.ps_type like '%"+psType+"%'";
			if ( "user".contains(psType))
				condition1 += "";
		}
		String sql = "";
		List<String> procInstIdList ;
		if (StrUtil.isBlank(prjId)) {//首页流程查询
			sql = "select distinct to_char(a.proc_inst_id_) as proc_inst_id from ACT_HI_IDENTITYLINK a \n" + 
					" left join ACT_Z_BUSINESS b on a.proc_inst_id_ = b.proc_inst_id(+) \n" + 
					" left join pjplat.bs_problem_sheet c on b.table_id = to_char(c.id) \n" + 
					"  where a.user_id_='"+userName+"' and  b.table_name='pjplat.BS_PROBLEM_SHEET' and a.proc_inst_id_ is not null and b.table_id is not null and b.proc_inst_id is not null "+condition
					+" union  select distinct proc_inst_id as proc_inst_id from pjplat.BS_PROBLEM_SHEET_SENDER  where senders='"+userName+	"'";
			procInstIdList = baseMapper.getSelectListData(sql);
			if (StrUtil.isBlank(psType) || "user".contains(psType)) {
				sql = "select distinct a.proc_inst_id_ from ACT_HI_IDENTITYLINK a \n" + 
						" left join ACT_Z_BUSINESS b on a.proc_inst_id_ = b.proc_inst_id(+) \n" + 
						" left join pjplat.bs_user c on b.table_id = to_char(c.id) \n" + 
						"  where a.user_id_='"+userName+"' and b.table_name='pjplat.BS_USER' and a.proc_inst_id_ is not null and b.table_id is not null and b.proc_inst_id is not null "+condition1;
				List<String> list1 = baseMapper.getSelectListData(sql);
				procInstIdList.addAll(list1);
			}
			
		} else {//问题整改单查询
			sql = "select distinct b.proc_inst_id from ACT_Z_BUSINESS b\n" + 
					"     left join pjplat.bs_problem_sheet c on b.table_id =  to_char(c.id)\n" + 
					"     where  b.table_name='pjplat.BS_PROBLEM_SHEET' and b.table_id is not null and b.proc_inst_id is not null and c.ps_prj_id='"+prjId+"' and b.table_name='pjplat.BS_PROBLEM_SHEET' "
					+ condition;
			procInstIdList = baseMapper.getSelectListData(sql);
		}
		
		Set<String> procInstIds= new HashSet<String>(procInstIdList);
		/*if (procInstIdList != null && procInstIdList.size() > 0) {
			for (int i = 0; i < procInstIdList.size(); i++) {
				String procInstId = procInstIdList.get(i);
				if (StrUtil.isNotBlank(procInstId))
					procInstIds.add(procInstId);
			}
		}*/
		return procInstIds;
	}
	
	public List<String> queryProcInstIdList(String userName, String startTime, String endTime, String psType) {
		String condition = "";
		String condition1 = "";
		String sql = "";
		if (StrUtil.isNotBlank(startTime)) {
			condition += " and b.create_time >= to_date('"+startTime+"','yyyy/mm/dd hh24:mi:ss')";
			condition1 += " and b.create_time >= to_date('"+startTime+"','yyyy/mm/dd hh24:mi:ss')";
		}
		if (StrUtil.isNotBlank(endTime)) {
			condition += " and b.create_time <= to_date('"+endTime+"','yyyy/mm/dd hh24:mi:ss')";
			condition1 += " and b.create_time <= to_date('"+endTime+"','yyyy/mm/dd hh24:mi:ss')";
		}
		if (StrUtil.isNotBlank(psType)) {
			condition += " and c.ps_type like '%"+psType+"%'";
			if ( "user".contains(psType))
				condition1 += "";
		}
		
		sql = "select distinct a.proc_inst_id_ from ACT_HI_IDENTITYLINK a \n" + 
				" left join ACT_Z_BUSINESS b on a.proc_inst_id_ = b.proc_inst_id(+) \n" + 
				" left join pjplat.bs_problem_sheet c on b.table_id = to_char(c.id) \n" + 
				"  where a.user_id_='"+userName+"' and b.table_name='pjplat.BS_PROBLEM_SHEET'  and a.proc_inst_id_ is not null and b.table_id is not null and b.proc_inst_id is not null "+condition;
		List<String> procInstIdList = baseMapper.getSelectListData(sql);
		if (StrUtil.isBlank(psType) || "user".contains(psType)) {
			sql = "select distinct a.proc_inst_id_ from ACT_HI_IDENTITYLINK a \n" + 
					" left join ACT_Z_BUSINESS b on a.proc_inst_id_ = b.proc_inst_id(+) \n" + 
					" left join pjplat.bs_user c on b.table_id = to_char(c.id) \n" + 
					"  where a.user_id_='"+userName+"' and b.table_name='pjplat.BS_USER' and a.proc_inst_id_ is not null and b.table_id is not null and b.proc_inst_id is not null "+condition1;
			List<String> list1 = baseMapper.getSelectListData(sql);
			procInstIdList.addAll(list1);
		}
		return procInstIdList;
	}
	

}
