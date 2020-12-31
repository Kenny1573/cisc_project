package org.jeecg.modules.helmet.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.company.entity.Company;
import org.jeecg.modules.helmet.entity.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface IHelmetService extends IService<HelmetInfo> {
    public List<JSONObject> getLikeList(Integer pageStart, Integer pageSize,String deviceSerial) ;
    public List<JSONObject> getList(Integer pageStart, Integer pageSize );
    public JSONObject addHelmet(String deviceSerial,String validateCode);
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response, Class<HelmetExcelEntity> clazz);
    public SafetyHelmetCfg getConfigInfo(String deviceSerial);
    public Result<?> configHelmet(JSONObject safetyHelmetCfg);
    public Result<?> configHelmetInfo(String deviceSerial);
    public Result<?> addMyHelmet(String deviceSerial, String validateCode,String userId);


    Result<?> getHelmetInfo(String userId);

    Result<?> unlinkHelmet(String deviceSerial, String userId);
    Result<?> linkHelmet(String deviceSerial, String userId);

    Result<?> dismissHelmet(String deviceSerial, String userId);

    Result<?> wearHelmetStatistics(String prjId, Date date1, Integer startTime, Integer endTime);

    Result<?> errorStatistics(String prjId, String startDate, String endDate, Integer startTime, Integer endTime,String type) throws ParseException;
}
