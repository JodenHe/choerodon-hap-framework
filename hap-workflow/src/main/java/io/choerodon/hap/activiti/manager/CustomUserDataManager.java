package io.choerodon.hap.activiti.manager;

import io.choerodon.hap.activiti.util.ActivitiUtils;
import io.choerodon.hap.hr.dto.Employee;
import io.choerodon.hap.hr.dto.Position;
import io.choerodon.hap.hr.mapper.EmployeeMapper;
import io.choerodon.hap.hr.mapper.PositionMapper;
import io.choerodon.message.IMessageConsumer;
import io.choerodon.message.annotation.TopicMonitor;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.data.impl.MybatisUserDataManager;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shengyang.zhou@hand-china.com
 */
@TopicMonitor(channel = "employee.change")
@Component(value = "customUserDataManager")
public class CustomUserDataManager extends MybatisUserDataManager
        implements IMessageConsumer<Employee>, InitializingBean {

    @Autowired
    private PositionMapper positionMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    public Map<String, UserEntity> userCache = new HashMap<>();

    @Autowired
    private SpringProcessEngineConfiguration pec;

    public CustomUserDataManager() {
        super(null);
    }

    @Override
    public List<Group> findGroupsByUser(String userId) {
        List<Position> positions = positionMapper.getPositionByEmployeeCode(userId);
        List<Group> gs = new ArrayList<>();
        for (Position position : positions) {
            gs.add(ActivitiUtils.toActivitiGroup(position));
        }
        return gs;
    }

    /**
     * 这个方法使用非常频繁，做缓存支持
     *
     * @param entityId
     * @return
     */
    @Override
    public UserEntity findById(String entityId) {
        UserEntity userEntity = userCache.get(entityId);
        if (userEntity == null) {
            Employee employee = employeeMapper.queryByCode(entityId);
            userEntity = ActivitiUtils.toActivitiUser(employee);
            userCache.put(entityId, userEntity);
        }
        return userEntity;
    }

    @Override
    public void onMessage(Employee message, String pattern) {
        userCache.remove(message.getEmployeeCode());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.processEngineConfiguration = pec;
    }
}
