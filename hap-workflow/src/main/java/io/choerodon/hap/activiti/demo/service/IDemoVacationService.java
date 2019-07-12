package io.choerodon.hap.activiti.demo.service;

import io.choerodon.hap.activiti.demo.dto.DemoVacation;
import io.choerodon.hap.core.ProxySelf;
import io.choerodon.mybatis.service.IBaseService;
import io.choerodon.web.core.IRequest;

import java.util.List;

public interface IDemoVacationService extends IBaseService<DemoVacation>, ProxySelf<IDemoVacationService> {

    void createVacationInstance(IRequest iRequest, DemoVacation demoVacation);

    List<DemoVacation> selectVacationHistory(IRequest iRequest);

}