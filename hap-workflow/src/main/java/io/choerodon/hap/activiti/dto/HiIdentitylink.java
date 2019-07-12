package io.choerodon.hap.activiti.dto;

/**
 * Auto Generated By Hap Code Generator
 **/

import io.choerodon.mybatis.common.query.Where;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "act_hi_identitylink")
public class HiIdentitylink {

    public static final String FIELD_ID_ = "id_";
    public static final String FIELD_GROUP_ID_ = "groupId_";
    public static final String FIELD_TYPE_ = "type_";
    public static final String FIELD_USER_ID_ = "userId_";
    public static final String FIELD_TASK_ID_ = "taskId_";
    public static final String FIELD_PROC_INST_ID_ = "procInstId_";
    public static final String FIELD_READ_FLAG_ = "readFlag_";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    private String id_;

    @Length(max = 255)
    private String groupId_;

    @Length(max = 255)
    private String type_;

    @Length(max = 255)
    @Where
    private String userId_;

    @Length(max = 64)
    @Where
    private String taskId_;

    @Length(max = 64)
    private String procInstId_;

    @Length(max = 1)
    private String readFlag_;


    public void setId_(String id_) {
        this.id_ = id_;
    }

    public String getId_() {
        return id_;
    }

    public void setGroupId_(String groupId_) {
        this.groupId_ = groupId_;
    }

    public String getGroupId_() {
        return groupId_;
    }

    public void setType_(String type_) {
        this.type_ = type_;
    }

    public String getType_() {
        return type_;
    }

    public void setUserId_(String userId_) {
        this.userId_ = userId_;
    }

    public String getUserId_() {
        return userId_;
    }

    public void setTaskId_(String taskId_) {
        this.taskId_ = taskId_;
    }

    public String getTaskId_() {
        return taskId_;
    }

    public void setProcInstId_(String procInstId_) {
        this.procInstId_ = procInstId_;
    }

    public String getProcInstId_() {
        return procInstId_;
    }

    public void setReadFlag_(String readFlag_) {
        this.readFlag_ = readFlag_;
    }

    public String getReadFlag_() {
        return readFlag_;
    }

}
