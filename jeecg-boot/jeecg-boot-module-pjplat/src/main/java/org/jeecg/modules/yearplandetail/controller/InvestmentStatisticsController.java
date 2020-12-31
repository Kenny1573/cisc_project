package org.jeecg.modules.yearplandetail.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.yearplandetail.service.InvestmentStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@Api(tags="投资统计")
@RestController
@RequestMapping("/investmentStatistics")
@Slf4j
public class InvestmentStatisticsController {
    @Autowired
    InvestmentStatisticsService investmentStatisticsService;

    @AutoLog(value = "获取xx年度xx项目的投资情况表")
    @ApiOperation(value="获取xx年度xx项目的投资情况表", notes="获取xx年度xx项目的投资情况表")
    @GetMapping(value = "/getMonthPlanList")
    public Result<?> getList(@PathParam(value = "yearId")Long yearId){


        return Result.ok(investmentStatisticsService.getList(yearId));

    }

}
