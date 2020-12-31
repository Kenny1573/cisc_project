package org.jeecg.modules.company.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.company.entity.Company;
import org.jeecg.modules.company.mapper.CompanyMapper;
import org.jeecg.modules.company.service.ICompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 单位信息
 * @Author: jeecg-boot
 * @Date:   2020-10-21
 * @Version: V1.0
 */
@Service
@DS("pjplat")
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements ICompanyService {
    @Autowired
    CompanyMapper companyMapper;

    @Override
    public boolean deleteById(String id) {
        return companyMapper.deleteById(id);
    }
}
