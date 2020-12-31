package org.jeecg.modules.contract.service;

import org.jeecg.modules.contract.entity.Contract;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 合同信息
 * @Author: jeecg-boot
 * @Date:   2020-10-22
 * @Version: V1.0
 */
public interface IContractService extends IService<Contract> {
    public String getCompanyName(Long conPrjId);
}
