package org.jeecg.modules.yearplandetail.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="投资统计汇总", description="投资统计汇总")
public class TotalInvestment {
    InvestmentStatistics total;
    List<InvestmentStatistics> list = new ArrayList<>();

    public TotalInvestment() {
    }

    public TotalInvestment(InvestmentStatistics total, List<InvestmentStatistics> list) {
        this.total = total;
        this.list = list;
    }

    public InvestmentStatistics getTotal() {
        return total;
    }



    public List<InvestmentStatistics> getList() {
        return list;
    }

    public void setList(List<InvestmentStatistics> list) {

        this.list = list;
    }
}
