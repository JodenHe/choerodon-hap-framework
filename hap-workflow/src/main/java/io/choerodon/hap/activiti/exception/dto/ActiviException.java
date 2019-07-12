package io.choerodon.hap.activiti.exception.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@Table(name = "act_exception")
public class ActiviException {

    private static final Logger logger = LoggerFactory.getLogger(ActiviException.class);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String procId;

    @JsonIgnore
    private byte[] message;

    @Transient
    private String messStr;

    private Date duedate;

    @Transient
    private String procDefName;

    public ActiviException() {
    }

    public ActiviException(String procId, String messStr, Date duedate) {
        this.procId = procId;
        this.duedate = duedate;
        byte[] mesb = null;
        try {
            mesb = messStr.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }
        message = mesb;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setProcId(String procId) {
        this.procId = procId;
    }

    public String getProcId() {
        return procId;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    public Date getDuedate() {
        return duedate;
    }

    public void setDuedate(Date duedate) {
        this.duedate = duedate;
    }

    public String getProcDefName() {
        return procDefName;
    }

    public void setProcDefName(String procDefName) {
        this.procDefName = procDefName;
    }

    public String getMessStr() {
        if (message != null) {
            try {
                messStr = new String(message, "utf-8");
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return messStr;
    }

    public void setMessStr(String messStr) {
        this.messStr = messStr;
    }
}
