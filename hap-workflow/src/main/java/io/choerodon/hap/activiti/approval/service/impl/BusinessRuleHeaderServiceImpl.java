package io.choerodon.hap.activiti.approval.service.impl;

/**
 * @author xiangyu.qi@hand-china.com
 */

import io.choerodon.hap.activiti.approval.dto.BusinessRuleHeader;
import io.choerodon.hap.activiti.approval.dto.BusinessRuleLine;
import io.choerodon.hap.activiti.approval.mapper.BusinessRuleHeaderMapper;
import io.choerodon.hap.activiti.approval.mapper.BusinessRuleLineMapper;
import io.choerodon.hap.activiti.approval.service.IBusinessRuleHeaderService;
import io.choerodon.hap.system.dto.DTOStatus;
import io.choerodon.web.core.IRequest;
import io.choerodon.base.annotation.Dataset;
import io.choerodon.dataset.exception.DatasetException;
import io.choerodon.dataset.service.IDatasetService;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.choerodon.mybatis.common.query.Comparison;
import io.choerodon.mybatis.common.query.WhereField;
import io.choerodon.mybatis.entity.Criteria;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
@Dataset("BusinessRuleHeader")
public class BusinessRuleHeaderServiceImpl extends BaseServiceImpl<BusinessRuleHeader> implements IBusinessRuleHeaderService, IDatasetService<BusinessRuleHeader> {

    @Autowired
    private BusinessRuleHeaderMapper headerMapper;

    @Autowired
    private BusinessRuleLineMapper lineMapper;


    /**
     * 批量操作头行数据.
     *
     * @param header 头行数据
     */
    private void processLines(BusinessRuleHeader header) {
        if (header.getLines() != null) {
            for (BusinessRuleLine line : header.getLines()) {
                if (line.getBusinessRuleId() == null) {
                    line.setBusinessRuleId(header.getBusinessRuleId()); // 设置头ID跟行ID一致
                    lineMapper.insertSelective(line);
                } else if (line.getBusinessRuleId() != null) {
                    int updateCount = lineMapper.updateByPrimaryKeySelective(line);
                    checkOvn(updateCount, header);
                }
            }
        }
    }

    @Override
    public List<BusinessRuleHeader> batchUpdate(List<BusinessRuleHeader> headers) {
        for (BusinessRuleHeader header : headers) {
            if (header.getBusinessRuleId() == null) {
                self().createRule(header);
            } else if (header.getBusinessRuleId() != null) {
                self().updateRule(header);
            }
        }
        return headers;
    }

    @Override
    public boolean batchDelete(IRequest request, List<BusinessRuleHeader> headers) {
        // 删除头行
        for (BusinessRuleHeader header : headers) {
            self().deleteByPrimaryKey(header);
        }
        return true;
    }

    @Override
    public int deleteByPrimaryKey(BusinessRuleHeader header) {
        // 删除行
        BusinessRuleLine line = new BusinessRuleLine();
        line.setBusinessRuleId(header.getBusinessRuleId());
        int count = lineMapper.delete(line);
        checkOvn(count, line);
        // 删除头
        int updateCount = headerMapper.deleteByPrimaryKey(header);
        checkOvn(updateCount, header);
        return updateCount;
    }

    @Override
    public List<BusinessRuleHeader> selectAll(IRequest request, BusinessRuleHeader header) {
        return headerMapper.select(header);
    }

    @Override
    public BusinessRuleHeader createRule(BusinessRuleHeader header) {
        // 插入头
        headerMapper.insertSelective(header);
        // 判断如果行不为空，则迭代循环插入
        if (header.getLines() != null) {
            processLines(header);
        }

        return null;
    }

    @Override
    public BusinessRuleHeader updateRule(BusinessRuleHeader header) {
        //headerMapper.updateByPrimaryKeySelective(header);
        int count = headerMapper.updateByPrimaryKey(header);
        checkOvn(count, header);
        // 判断如果行不为空，则迭代循环插入
        if (header.getLines() != null) {
            processLines(header);
        }
        return header;
    }

    @Override
    public List<?> queries(Map<String, Object> body, int page, int pageSize, String sortname, boolean isDesc) {
        try {
            BusinessRuleHeader candidateRule = new BusinessRuleHeader();
            BeanUtils.populate(candidateRule, body);
            Criteria criteria = new Criteria(candidateRule);
            criteria.where(BusinessRuleHeader.FIELD_CODE, new WhereField(BusinessRuleHeader.FIELD_DESCRIPTION, Comparison.LIKE));
            return super.selectOptions(candidateRule, criteria, page, pageSize);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new DatasetException("dataset.error.BusinessRuleHeader", e);
        }
    }

    @Override
    public List<BusinessRuleHeader> mutations(List<BusinessRuleHeader> objs) {
        for (BusinessRuleHeader ruleHeader : objs) {
            switch (ruleHeader.get__status()) {
                case DTOStatus.ADD:
                    headerMapper.insertSelective(ruleHeader);
                    processLines(ruleHeader);
                    break;
                case DTOStatus.UPDATE:
                    int count = headerMapper.updateByPrimaryKey(ruleHeader);
                    checkOvn(count, ruleHeader);
                    processLines(ruleHeader);
                    break;
                case DTOStatus.DELETE:
                    self().deleteByPrimaryKey(ruleHeader);
                    break;
                default:
                    break;
            }
        }
        return objs;
    }

}