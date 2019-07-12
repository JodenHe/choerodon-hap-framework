package io.choerodon.hap.core.web.view.ui;

import io.choerodon.hap.core.web.view.ScreenBuilder;
import io.choerodon.hap.core.web.view.UITag;
import io.choerodon.hap.core.web.view.ViewContext;
import io.choerodon.hap.core.web.view.XMap;

import java.util.Iterator;
import java.util.List;

/**
 * Screen标签.
 * 
 * @author njq.niu@hand-china.com
 * 
 */
@UITag
public class Screen extends ViewTag {

    public static final String PROPERTITY_TITLE = "title";

    private static final String DEFAULT_TEMPLATE = "Screen.ftl";

    private static final String PROPERTITY_TEMPLATE = "template";

    public String execute(XMap view, ViewContext context) throws Exception {
    	
    	
        StringBuilder sb = new StringBuilder();
        List<XMap> children = view.getChildren();
        if (children != null) {
            Iterator<XMap> it = children.iterator();
            while (it.hasNext()) {
                XMap child = it.next();
                sb.append(ScreenBuilder.build(child, context));
            }
        }
        context.put("title", view.getString(PROPERTITY_TITLE, ""));
        context.put("content", sb.toString());
        return super.build(context, getTemplate(view));
    }

    private String getTemplate(XMap view) {
        String tpl = view.getString(PROPERTITY_TEMPLATE);
        return tpl == null ? DEFAULT_TEMPLATE : tpl;
    }

}
