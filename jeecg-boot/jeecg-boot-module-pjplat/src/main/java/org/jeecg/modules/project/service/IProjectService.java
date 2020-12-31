package org.jeecg.modules.project.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.modules.company.entity.Company;
import org.jeecg.modules.project.entity.KeyValue;
import org.jeecg.modules.project.entity.Project;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.project.vo.ProjectPage;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 项目信息
 * @Author: jeecg-boot
 * @Date:   2020-10-15
 * @Version: V1.0
 */
public interface IProjectService extends IService<Project> {

	public List<Project> getProjectListByUsername(String username, Page<Project> page,String prjName,String prjType);
	public List<Company> getCompanyInfoById(Long mainId,String type, Integer pageNo, Integer pageSize);
	public boolean deleteCompanyInfoById(Long cId,Long prjId);

	/**
	 * 删除一对多
	 */
	public void delMain(Long id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);


    void addProjectCompanyBatch(JSONObject jsonObject);
	public List<Project> getProjectListByUsername2(String username) ;

    void createProject(JSONObject jsonObject);

    void deleteBatchProjectCompany(JSONObject jsonObject);
	List<KeyValue<String,String>> listProject();

	ModelAndView exportXls(HttpServletRequest request, Project object, Class<ProjectPage> clazz, String title,List<Project> pageList);
}
