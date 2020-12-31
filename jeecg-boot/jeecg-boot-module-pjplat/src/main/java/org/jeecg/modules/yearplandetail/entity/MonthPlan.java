package org.jeecg.modules.yearplandetail.entity;

import com.baomidou.mybatisplus.annotation.KeySequence;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="pjplat.bs_yearplan_detail对象", description="月度计划")
public class MonthPlan {
    String year;
    List<YearPlanDetail> list;

    public MonthPlan(String ypYear, List<YearPlanDetail> monthPlanList) {
        this.year=ypYear;
        this.list=monthPlanList;
    }
}
