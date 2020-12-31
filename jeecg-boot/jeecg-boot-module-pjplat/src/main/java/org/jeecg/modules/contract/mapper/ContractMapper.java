package org.jeecg.modules.contract.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.contract.entity.Contract;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 合同信息
 * @Author: jeecg-boot
 * @Date:   2020-10-22
 * @Version: V1.0
 */
public interface ContractMapper extends BaseMapper<Contract> {
    @Select(value = "select PRJ_NAME from pjplat.BS_PROJECT where ID=#{conPrjId}")
    public String getCompanyName(Long conPrjId);

}
