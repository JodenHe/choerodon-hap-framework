package io.choerodon.hap.activiti.listeners;

import io.choerodon.hap.account.dto.User;
import io.choerodon.hap.account.service.IUserService;
import io.choerodon.hap.activiti.core.IActivitiConstants;
import io.choerodon.hap.core.AppContextInitListener;
import io.choerodon.hap.system.dto.SysPreferences;
import io.choerodon.hap.system.service.ISysPreferencesService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @author Qixiangyu
 * @since 2017/5/9.
 */
@Component(value = "autoDeliverListener")
public class AutoDelegateListener implements TaskListener, IActivitiConstants, AppContextInitListener {

    private static final String PREFERENCE_AUTO_DELIVER = "autoDelegate";

    private static final String PREFERENCE_START_DELIVER = "deliverStartDate";

    private static final String PREFERENCE_END_DELIVER = "deliverEndDate";
    @Autowired
    private ISysPreferencesService preferencesService;

    @Autowired
    private IUserService userService;

    private TaskService taskService;


    @Override
    public void notify(DelegateTask delegateTask) {
        TaskEntity task = (TaskEntity) delegateTask;
        String taskId = task.getId();
        String delegateName = autoDelegate(task);
        if (StringUtils.isNotEmpty(delegateName)) {
            Authentication.setAuthenticatedUserId(null);
            taskService.addComment(taskId, task.getProcessInstanceId(), COMMENT_ACTION, AUTO_DELEGATE);
            taskService.addComment(taskId, task.getProcessInstanceId(), PROP_COMMENT, task.getAssignee() + " 转交给 " + delegateName);
            taskService.setAssignee(taskId, delegateName);
        }

    }

    private String autoDelegate(TaskEntity task) {
        if (StringUtils.isEmpty(task.getAssignee())) {
            return null;
        }
        List<User> users = userService.selectUserNameByEmployeeCode(task.getAssignee());
        if (CollectionUtils.isEmpty(users)) {
            return null;
        }
        User user = users.get(0);
        SysPreferences preferences = preferencesService.selectUserPreference(PREFERENCE_AUTO_DELIVER, user.getUserId());
        if (preferences == null) {
            return null;
        }
        if (StringUtils.isNotEmpty(preferences.getPreferencesValue())) {
            //自动转交人是自己
            if (task.getAssignee().equalsIgnoreCase(preferences.getPreferencesValue())) {
                return null;
            }
            SysPreferences startDate = preferencesService.selectUserPreference(PREFERENCE_START_DELIVER, user.getUserId());
            SysPreferences endDate = preferencesService.selectUserPreference(PREFERENCE_END_DELIVER, user.getUserId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                //未设置开始日期，不自动转交
                if (startDate == null || StringUtils.isEmpty(startDate.getPreferencesValue())) {
                    return null;
                }
                Date start = sdf.parse(startDate.getPreferencesValue());
                if (start.getTime() > System.currentTimeMillis()) {
                    return null;
                }
                if (endDate != null && StringUtils.isNotEmpty(endDate.getPreferencesValue())) {
                    Date end = sdf.parse(endDate.getPreferencesValue());
                    if (end.getTime() < System.currentTimeMillis()) {
                        return null;
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
        return preferences.getPreferencesValue();
    }

    @Override
    public void contextInitialized(ApplicationContext applicationContext) {
        taskService = applicationContext.getBean(TaskService.class);
    }
}
