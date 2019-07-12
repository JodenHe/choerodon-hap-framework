package io.choerodon.hap.modeling;

import io.choerodon.base.exception.BaseRuntimeException;

public class ModelingException extends BaseRuntimeException {
    public ModelingException(String descriptionKey, Object... parameters) {
        super("MODELING", descriptionKey, parameters);
    }
}
