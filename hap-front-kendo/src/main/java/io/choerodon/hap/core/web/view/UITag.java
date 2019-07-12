package io.choerodon.hap.core.web.view;

import io.choerodon.hap.core.web.view.ui.ViewTag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author njq.niu@hand-china.com
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UITag {
    
    String name() default "";

    String nameSpace() default ViewTag.DEFAULT_NAME_SPACE;
}
