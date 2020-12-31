package org.jeecg.modules.api.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IApiService {

	public LinkedHashMap<String, Object> getAllProjectOverview();
	public List<LinkedHashMap<String, Object>> getAllProject();
	
	public List<LinkedHashMap<String, Object>> getProjectTypeSpread();
	
	public List<LinkedHashMap<String, Object>> getProjectInvestmentWarningCensus();
	public LinkedHashMap<String, Object> getProjectOverview(Long projectID);
	
	public LinkedHashMap<String, Object> getProjectProblemCensus();
	public LinkedHashMap<String, Object> getProjectProblemCensus2(Long projectID);
	
	public List<LinkedHashMap<String, Object>> getProjectMilestoneByProject(Long projectID);
	
	
	public LinkedHashMap<String, Object> getProjectInvestment(Long projectID,String year);

	
//	public LinkedHashMap<String, Object> getWeather(String areaNum, String cityName);
	
	public List<LinkedHashMap<String, Object>> getHelmetViewInfo(@Param("prj_id") String prj_id);
	
	public List<LinkedHashMap<String, Object>> getVideoViewInfo(@Param("prj_id") String prj_id);
	
	
}
