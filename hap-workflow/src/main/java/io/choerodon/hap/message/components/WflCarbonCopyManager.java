package io.choerodon.hap.message.components;


import io.choerodon.hap.activiti.core.IActivitiConstants;
import io.choerodon.hap.activiti.dto.HiIdentitylink;
import io.choerodon.hap.activiti.listeners.TaskCreateNotificationListener;
import io.choerodon.hap.activiti.mapper.HiIdentitylinkMapper;
import io.choerodon.base.util.BaseConstants;
import io.choerodon.message.IMessageConsumer;
import io.choerodon.message.annotation.QueueMonitor;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.db.DbSqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiangyu.qi@hand-china.com
 * @since 2017/9/27.
 */
@Component
@QueueMonitor(queue = IActivitiConstants.CHANNEL_CARBON_COPY)
public class WflCarbonCopyManager implements IMessageConsumer<Map> {

    @Autowired
    private TaskCreateNotificationListener taskCreateNotificationListener;

    @Autowired
    private DbSqlSessionFactory dbSqlSessionFactory;

    @Autowired
    private HiIdentitylinkMapper hiIdentitylinkMapper;

    @Autowired
    private TaskService taskService;

    @Override
    @SuppressWarnings("unchecked")
    public void onMessage(Map message, String pattern) {
        String processInstanceId = message.get(IActivitiConstants.MSG_PAEM_PROCESSINSTANCEID).toString();
        List<String> users = (List<String>) message.get(IActivitiConstants.MSG_PARM_USERS);
        Map<String, String> params = new HashMap<>();
        users.forEach(t -> {
            //查询是否已经抄送，抄送过的流程，修改读取状态
            HiIdentitylink identitylink = new HiIdentitylink();
            identitylink.setProcInstId_(processInstanceId);
            identitylink.setUserId_(t);
            identitylink.setType_(IActivitiConstants.ACTION_CARBON_COPY);
            int size = hiIdentitylinkMapper.select(identitylink).size();
            if (size > 0) {
                identitylink.setReadFlag_(BaseConstants.NO);
                hiIdentitylinkMapper.updateReadFlag(identitylink);
            } else {
                String id = dbSqlSessionFactory.getIdGenerator().getNextId();
                params.put("id", id);
                params.put("userId", t);
                params.put("type", IActivitiConstants.ACTION_CARBON_COPY);
                params.put("processInstanceId", processInstanceId);
                hiIdentitylinkMapper.insertCarbonCopy(params);
            }
            taskCreateNotificationListener.sendMessageForCC(t);
            params.clear();
        });
    }
}
