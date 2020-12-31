package org.jeecg.modules.monthreport.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.monthreport.entity.MonthReport;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.monthreport.vo.MonthReportExport;
import org.jeecg.modules.project.entity.Project;
import org.jeecg.modules.project.vo.ProjectPage;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 进度月报
 * @Author: jeecg-boot
 * @Date:   2020-10-30
 * @Version: V1.0
 */
public interface IMonthReportService extends IService<MonthReport> {
    IPage listMonthReport(Page<MonthReport> page);
    Page listMonthReportByPrjId(Long prjId,Page<JSONObject> page);
    Result<?> addMonthReport(JSONObject jsonObject);
    //boolean updateMonthReport(MonthReport monthReport);
    JSONObject getLastMonthData(Long prjId, String year, String month);
    public ModelAndView exportXls(HttpServletRequest request, Class<MonthReportExport> clazz, String title) ;

    Project getProjectByMrYpdId(Long mrYpdId);

}
