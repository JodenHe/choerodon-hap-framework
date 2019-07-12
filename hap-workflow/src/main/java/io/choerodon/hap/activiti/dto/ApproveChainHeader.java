package io.choerodon.hap.activiti.dto;

import io.choerodon.mybatis.entity.BaseDTO;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Table(name = "wfl_approve_chain_header")
public class ApproveChainHeader extends BaseDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approveChainId;

    @NotEmpty
    private String processKey;

    @NotEmpty
    private String usertaskId;

    private String enableFlag;

    @Transient
    private List<ApproveChainLine> lines;

    public void setApproveChainId(Long approveChainId) {
        this.approveChainId = approveChainId;
    }

    public Long getApproveChainId() {
        return approveChainId;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

    public String getProcessKey() {
        return processKey;
    }

    public void setUsertaskId(String usertaskId) {
        this.usertaskId = usertaskId;
    }

    public String getUsertaskId() {
        return usertaskId;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public List<ApproveChainLine> getLines() {
        return lines;
    }

    public void setLines(List<ApproveChainLine> lines) {
        this.lines = lines;
    }
}
