package lv.ctco.zephyr.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ObjectTransformer {
    private static final ObjectMapper mapper = getObjectMapper();

    public static void setPropertyNamingStrategy(PropertyNamingStrategy strategy) {
        mapper.setPropertyNamingStrategy(strategy);
    }

    private static ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

    public static String serialize(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            //TODO use logging
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T deserialize(String response, Class<T> clazz) {
        JavaType typeRef = mapper.getTypeFactory().constructType(clazz);
        try {
            return mapper.readValue(response, typeRef);
        } catch (IOException e) {
            //TODO use logging
            e.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> deserializeList(String response, Class<T> clazz) {
        JavaType typeRef = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
        try {
            return mapper.readValue(response, typeRef);
        } catch (IOException e) {
            //TODO use logging
            e.printStackTrace();
            return new ArrayList<T>();
        }
    }
}