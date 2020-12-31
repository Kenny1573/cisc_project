package org.jeecg.modules.yearplandetail.service;

import lombok.Data;
import org.jeecg.modules.yearplandetail.entity.TotalInvestment;

public interface InvestmentStatisticsService {
    TotalInvestment getList(Long yearPlanId);

}
