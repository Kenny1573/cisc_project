package org.jeecg.modules.contract.service.impl;

import org.jeecg.modules.contract.entity.Contract;
import org.jeecg.modules.contract.mapper.ContractMapper;
import org.jeecg.modules.contract.service.IContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 合同信息
 * @Author: jeecg-boot
 * @Date:   2020-10-22
 * @Version: V1.0
 */
@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements IContractService {

    @Autowired
    ContractMapper contractMapper;
    @Override
    public String getCompanyName(Long conPrjId) {
        return contractMapper.getCompanyName(conPrjId);
    }
}
