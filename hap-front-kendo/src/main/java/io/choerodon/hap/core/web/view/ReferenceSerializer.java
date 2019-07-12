package io.choerodon.hap.core.web.view;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * 
 * @author njq.niu@hand-china.com
 *
 */
public class ReferenceSerializer extends JsonSerializer<ReferenceType> {

    @Override
    public void serialize(ReferenceType type, JsonGenerator jgen, SerializerProvider prov) throws IOException, JsonProcessingException {
        if (type.reference != null)
            jgen.writeRawValue(type.reference);
    }
}
