/*
 * Copyright Hand China Co.,Ltd.
 */

package io.choerodon.hap.activiti.demo.components;

import io.choerodon.hap.activiti.custom.IActivitiBean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author shengyang.zhou@hand-china.com
 */
@Component
public class OrgStructure implements IActivitiBean {
    public String getDirector(String starter) {
        return "Jessen";
    }

    public String getDeptLeader(String starter) {
        return "Tony";
    }

    public List<String> getPers() {
        return Arrays.asList("ADMIN", "Tony");
    }
}
