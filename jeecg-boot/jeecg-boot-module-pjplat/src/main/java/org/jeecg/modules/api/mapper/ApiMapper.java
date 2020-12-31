package org.jeecg.modules.api.mapper;


import java.util.LinkedHashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ApiMapper {
	
	
	/**
	 * 
	 * 项目总数和总投资
	 * @author lei.yin
	 * @date 2020年11月16日 上午8:16:41
	 * @return
	 */
	LinkedHashMap<String,Object> selectProjectTotal();

	/**
	 * 
	 * 在建项目和在建项目总投资 
	 * @author lei.yin
	 * @date 2020年11月16日 上午8:18:37
	 * @return
	 */
	LinkedHashMap<String,Object> selectProjectUnderConstructionTotal();
	
	/**
	 * 
	 * 在建设项目完成投资 
	 * @author lei.yin
	 * @date 2020年11月16日 上午8:20:55
	 * @return
	 */
	LinkedHashMap<String,Object> selectProjectUnderConstructionMoney();
	
	/**
	 * 
	 * 本月新立项 
	 * @author lei.yin
	 * @date 2020年11月16日 上午8:21:56
	 * @return
	 */
	LinkedHashMap<String,Object> selectNewCurrentMonthProjectCount();
	
	/**
	 * 
	 * 项目类型分布 
	 * @author lei.yin
	 * @date 2020年11月19日 下午4:06:26
	 * @return
	 */
	List<LinkedHashMap<String, Object>> selectProjectTypeSpread();
	
	/**
	 * 
	 * 项目投资进度 
	 * @author lei.yin
	 * @date 2020年11月18日 下午2:16:21
	 * @return
	 */
	List<LinkedHashMap<String, Object>> selectProjectInvestmentWarningCensus();
	
	/**
	 * 
	 * 里程碑节点按照计划开始时间正序
	 * @author lei.yin
	 * @date 2020年11月11日 下午2:50:47
	 * @return
	 */
	List<LinkedHashMap<String, Object>> selectProjectMilestoneForward(@Param("projectID") Long projectID);
	
	/**
	 * 
	 * 里程碑节点按照计划结束时间倒序
	 * @author lei.yin
	 * @date 2020年11月11日 下午2:50:47
	 * @return
	 */
	List<LinkedHashMap<String, Object>> selectProjectMilestonebackward(@Param("projectID") Long projectID);
	
	
	
	/**
	 * 
	 * 项目问题统计
	 * @author lei.yin
	 * @date 2020年11月11日 下午2:50:47
	 * @return
	 */
	List<LinkedHashMap<String, Object>> selectProjectProblemCensus();
	
	/**
	 * 
	 * 可视化2项目统计发生问题的部位
	 * @author lei.yin
	 * @date 2020年11月11日 下午2:50:47
	 * @return
	 */
	List<LinkedHashMap<String, Object>> selectProjectProblemCensusGroupByLocation(@Param("projectID") Long projectID);
	
	
	/**
	 * 
	 * 可视化2项目根据不同类型问题统计
	 * @author lei.yin
	 * @date 2020年11月11日 下午2:50:47
	 * @return
	 */
	List<LinkedHashMap<String, Object>> selectProjectProblemCensusGroupByType(@Param("projectID") Long projectID);
	
	/**
	 * 
	 * 可视化2项目根据不同位置不同类型问题统计 
	 * @author lei.yin
	 * @date 2020年11月11日 下午2:50:47
	 * @return
	 */
	List<LinkedHashMap<String, Object>> selectProjectProblemCensusGroupByTypeAndLocation(@Param("projectID") Long projectID);
	
	
	/**
	 * 
	 * 项目简介
	 * @author lei.yin
	 * @date 2020年11月11日 下午2:50:47
	 * @return
	 */
	List<LinkedHashMap<String, Object>> selectProjectContent(@Param("id") Long id);
	
	/**
	 * 
	 * 可视化2项目里程碑排序
	 * @author lei.yin
	 * @date 2020年11月11日 下午2:50:47
	 * @return
	 */
	List<LinkedHashMap<String, Object>> selectProjectMilestoneByProject(@Param("projectID") Long projectID);
	
	
	/**
	 * 
	 * 可视化2项目投资
	 * @author lei.yin
	 * @date 2020年11月13日 下午2:50:47
	 * @return
	 */
	List<LinkedHashMap<String, Object>> selectProjectInvestment(@Param("projectID") Long projectID,@Param("year") String year);
	
	
//	/**
//	 * 
//	 * 实时天气 
//	 * @author lei.yin
//	 * @date 2020年8月10日 上午8:52:17
//	 * @param areaNum
//	 * @param cityName
//	 * @return
//	 */
//	LinkedHashMap<String,Object> getRealTimeWeather(@Param("areaNum") String areaNum , @Param("cityName") String cityName);

	/**
	 * 
	 * 安全帽详细信息 
	 * @author guodong,zhou
	 * @date 2020年12月08日
	 * @return
	 */
	List<LinkedHashMap<String, Object>> selectHelmetViewInfo(@Param("prj_id") String prj_id);


	/**
	 * 
	 * 监控详细信息 
	 * @author guodong,zhou
	 * @date 2020年12月08日
	 * @return
	 */
	List<LinkedHashMap<String, Object>> selectVideoViewInfo(@Param("prj_id") String prj_id);

}
