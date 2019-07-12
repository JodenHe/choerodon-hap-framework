package io.choerodon.hap.activiti.controllers;

import io.choerodon.base.annotation.Permission;
import io.choerodon.base.enums.ResourceType;
import org.activiti.engine.ActivitiException;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

@RestController
@RequestMapping(value = {"/wfl", "/api/wfl"})
public class StencilsetRestResource {

    @Permission(type = ResourceType.SITE)
    @GetMapping(value = {"/editor/stencilset", "/service/editor/stencilset"}, produces = "application/javascript;charset=UTF-8")
    public String getStencilset(HttpServletRequest request) {
        String env = RequestContextUtils.getLocale(request).toString();
        InputStream stencilsetStream = null;
        if ("en_GB".equalsIgnoreCase(env) || "en_US".equalsIgnoreCase(env) || "en".equalsIgnoreCase(env)) {
            stencilsetStream = getClass().getClassLoader().getResourceAsStream("stencilset.json");
        } else {
            stencilsetStream = getClass().getClassLoader().getResourceAsStream("stencilset_" + env + ".json");
            if (stencilsetStream == null) {
                stencilsetStream = getClass().getClassLoader().getResourceAsStream("stencilset.json");
            }
        }
        try (InputStream ignore = stencilsetStream) {
            return IOUtils.toString(ignore, "utf-8");
        } catch (Exception e) {
            throw new ActivitiException("Error while loading stencil set", e);
        }
    }

    @Permission(type = ResourceType.SITE)
    @GetMapping(value = {"/editor/stencilset_new", "/service/editor/stencilset_new"}, produces = "application/javascript;charset=UTF-8")
    public String getStencilsetNew(HttpServletRequest request) {
        String env = RequestContextUtils.getLocale(request).toString();
        InputStream stencilsetStream = null;
        if (env.equalsIgnoreCase("en_GB") || env.equalsIgnoreCase("en_US")
                || env.equalsIgnoreCase("en")) {
            stencilsetStream = getClass().getClassLoader().getResourceAsStream("stencilset.json");
        } else {
            stencilsetStream = getClass().getClassLoader().getResourceAsStream("stencilset_new_" + env + ".json");
            if (stencilsetStream == null) {
                stencilsetStream = getClass().getClassLoader().getResourceAsStream("stencilset.json");
            }
        }
        try (InputStream ignore = stencilsetStream) {
            return IOUtils.toString(ignore, "utf-8");
        } catch (Exception e) {
            throw new ActivitiException("Error while loading stencil set", e);
        }
    }
}
