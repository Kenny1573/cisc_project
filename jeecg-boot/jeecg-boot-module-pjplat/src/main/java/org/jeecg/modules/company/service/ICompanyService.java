package org.jeecg.modules.company.service;

import org.jeecg.modules.company.entity.Company;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 单位信息
 * @Author: jeecg-boot
 * @Date:   2020-10-21
 * @Version: V1.0
 */
public interface ICompanyService extends IService<Company> {
    boolean deleteById(String id);
}
