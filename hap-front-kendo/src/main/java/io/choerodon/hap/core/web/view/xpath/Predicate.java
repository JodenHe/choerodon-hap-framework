package io.choerodon.hap.core.web.view.xpath;

import io.choerodon.hap.core.web.view.XMap;

public interface Predicate {
    public boolean validate(XMap node);
}
