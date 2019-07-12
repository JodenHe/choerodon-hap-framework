package io.choerodon.hap.activiti.custom.process;

import org.activiti.rest.service.api.history.HistoricProcessInstanceQueryRequest;

/**
 * @author njq.niu@hand-china.com
 */
public class CustomHistoricProcessInstanceQueryRequest extends HistoricProcessInstanceQueryRequest {

    private String processDefinitionNameLike;
    private String startUserName;
    private Boolean suspended;
    private String carbonCopyUser;
    private String readFlag;
    private String startedBy;
    private String involved;
    private String carbonCopy;

    public String getProcessDefinitionNameLike() {
        return processDefinitionNameLike;
    }

    public void setProcessDefinitionNameLike(String processDefinitionNameLike) {
        this.processDefinitionNameLike = processDefinitionNameLike;
    }

    public String getStartUserName() {
        return startUserName;
    }

    public void setStartUserName(String startUserName) {
        this.startUserName = startUserName;
    }

    public Boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(Boolean suspended) {
        this.suspended = suspended;
    }

    public String getCarbonCopyUser() {
        return carbonCopyUser;
    }

    public void setCarbonCopyUser(String carbonCopyUser) {
        this.carbonCopyUser = carbonCopyUser;
    }

    public String getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(String readFlag) {
        this.readFlag = readFlag;
    }

    @Override
    public String getStartedBy() {
        return startedBy;
    }

    @Override
    public void setStartedBy(String startedBy) {
        this.startedBy = startedBy;
    }

    public String getInvolved() {
        return involved;
    }

    public void setInvolved(String involved) {
        this.involved = involved;
    }

    public String getCarbonCopy() {
        return carbonCopy;
    }

    public void setCarbonCopy(String carbonCopy) {
        this.carbonCopy = carbonCopy;
    }
}
