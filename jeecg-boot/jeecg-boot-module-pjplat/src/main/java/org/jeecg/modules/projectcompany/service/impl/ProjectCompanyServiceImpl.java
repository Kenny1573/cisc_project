package org.jeecg.modules.projectcompany.service.impl;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.projectcompany.entity.ProjectCompany;
import org.jeecg.modules.projectcompany.mapper.ProjectCompanyMapper;
import org.jeecg.modules.projectcompany.service.IProjectCompanyService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: BS_PROJECT_COMPANY
 * @Author: jeecg-boot
 * @Date:   2020-10-15
 * @Version: V1.0
 */
@Service
@DS(value = "pjplat")
public class ProjectCompanyServiceImpl extends ServiceImpl<ProjectCompanyMapper, ProjectCompany> implements IProjectCompanyService {

}
