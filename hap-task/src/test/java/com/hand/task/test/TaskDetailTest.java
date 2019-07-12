package io.choerodon.task.test;

import io.choerodon.hap.task.service.impl.TaskDetailServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class TaskDetailTest {

    @Autowired
    TaskDetailServiceImpl service;
    @Test
    public void test() {
        List<String> list = new ArrayList<>();

        service.queryChildrenByPrimaryKey(list);
    }
}
