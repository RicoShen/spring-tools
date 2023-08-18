package com.ricoandilet.springtools.json;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.ricoandilet.springtools.exception.BusinessException;
import com.ricoandilet.springtools.exception.BusinessException.BaseErrorCode;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author rico
 */
@Slf4j
public class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String JSON_STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    static {
        //对象的所有字段全部列入
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        //忽略空Bean转json的错误
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        //所有的日期格式都统一为以下的样式
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat(JSON_STANDARD_FORMAT));

        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        OBJECT_MAPPER.registerModule(createTimeModule());
    }

    /**
     *
     */
    private static SimpleModule createTimeModule() {
        //return new JavaTimeModule();
        var timeModule = new SimpleModule();
        timeModule.addDeserializer(Instant.class, InstantDeserializer.INSTANT);
        timeModule.addDeserializer(LocalDate.class, LocalDateDeserializer.INSTANCE);
        timeModule.addDeserializer(LocalTime.class, LocalTimeDeserializer.INSTANCE);
        timeModule.addDeserializer(LocalDateTime.class, LocalDateTimeDeserializer.INSTANCE);

        //ltang: To be compatible with @JsonSetter
        timeModule.addSerializer(Instant.class, new ToStringSerializer());
        timeModule.addSerializer(LocalDate.class, new ToStringSerializer());
        timeModule.addSerializer(LocalTime.class, new ToStringSerializer());
        timeModule.addSerializer(LocalDateTime.class, new ToStringSerializer());

        return timeModule;
    }

    private static ObjectMapper createMapperDefault() {
        return new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .registerModule(createTimeModule())
                //ltang: Forgiving on unknown fields when deserializing
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    public static String stringify(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String str) {
            return str;
        }
        String json = null;
        try {
            json = OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JsonNode toJsonNode(@NonNull Object value) {
        return OBJECT_MAPPER.convertValue(value, JsonNode.class);
    }

    public static ObjectNode toObjectNode(@NonNull Object value) {
        return OBJECT_MAPPER.convertValue(value, ObjectNode.class);
    }

    public static <T> T fromJsonNode(@NonNull JsonNode value, Class<T> classOfT) {
        return OBJECT_MAPPER.convertValue(value, classOfT);
    }

    public static Object string2Obj(@NonNull String jsonText) {
        return string2Obj(jsonText, Object.class);
    }

    public static <T> T string2Obj(String jsonText, Class<T> classOfT) {
        if (jsonText == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(jsonText, classOfT);
        } catch (JsonProcessingException e) {
            throw new BusinessException(BaseErrorCode.SERIALIZATION_FAILURE, e, jsonText);
        }
    }

    public static <T> T parseObject(String jsonText, com.alibaba.fastjson.TypeReference<T> typeReference) {
        try {
            return JSON.parseObject(jsonText, typeReference);
        } catch (Exception e) {
            throw new BusinessException(BaseErrorCode.SERIALIZATION_FAILURE, e, jsonText);
        }
    }

    public static <T> T parseObject(String jsonText, Class<T> clazz) {
        try {
            return JSON.parseObject(jsonText, clazz);
        } catch (Exception e) {
            throw new BusinessException(BaseErrorCode.SERIALIZATION_FAILURE, e, jsonText);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T string2Obj(String str, TypeReference<T> typeReference) {
        if (StringUtils.hasLength(str) || typeReference == null) {
            return null;
        }
        try {
            return (T) ((typeReference.getType().equals(String.class)) ? str : OBJECT_MAPPER.readValue(str,
                    typeReference));
        } catch (IOException e) {
            throw new BusinessException(BaseErrorCode.SERIALIZATION_FAILURE, e, str);
        }
    }

    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        return OBJECT_MAPPER.convertValue(fromValue, toValueType);
    }

    public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
        return OBJECT_MAPPER.convertValue(fromValue, toValueTypeRef);
    }

    public static Object file2Obj(@NonNull String filename) throws IOException {
        return file2Obj(filename, Object.class, OBJECT_MAPPER);
    }

    public static <T> T file2Obj(@NonNull String filename, Class<T> classOfT) throws IOException {
        return file2Obj(filename, classOfT, OBJECT_MAPPER);
    }

    public static <T> T file2Obj(@NonNull String filename, TypeReference<T> valueTypeRef) throws IOException {
        return file2Obj(filename, valueTypeRef, OBJECT_MAPPER);
    }

    public static <T> T file2Obj(@NonNull String filename
            , Class<T> classOfT
            , ObjectMapper mapper) throws IOException {
        if (mapper == null)
            mapper = OBJECT_MAPPER;
        try (Reader reader = Files.newBufferedReader(Paths.get(filename))) {
            return mapper.readValue(reader, classOfT);
        }
    }

    public static <T> T file2Obj(@NonNull String filename
            , TypeReference<T> valueTypeRef
            , ObjectMapper mapper) throws IOException {
        if (mapper == null)
            mapper = OBJECT_MAPPER;
        try (Reader reader = Files.newBufferedReader(Paths.get(filename))) {
            return mapper.readValue(reader, valueTypeRef);
        }
    }

    public static <T> T deepClone(@NonNull T origin) throws JsonProcessingException {
        return deepClone(origin, OBJECT_MAPPER);
    }

    public static <T> T deepClone(@NonNull T origin, ObjectMapper mapper) throws JsonProcessingException {
        if (mapper == null)
            mapper = OBJECT_MAPPER;

        @SuppressWarnings("unchecked")
        T deepCopy = (T) mapper
                .readValue(mapper.writeValueAsString(origin), origin.getClass());
        return deepCopy;
    }

    /**
     * NOTES: ltang - Merging Array will always insert new values. If you want to update collection,
     * better merging Map rather than Array.
     * Merge other into target and return merged node. B/c in place,
     * the target and merged is the same reference.
     *
     * @param target
     * @param other
     * @return
     * @throws IOException
     */
    public static ObjectNode mergeNodeInPlace(@NonNull final ObjectNode target
            , @NonNull final ObjectNode other) throws IOException {
        ObjectReader updater = OBJECT_MAPPER.readerForUpdating(target);
        var merged = (ObjectNode) updater.readValue(other);
        return merged;
    }

    public static void injectFieldIfMissing(@NonNull ObjectNode head
            , @NonNull String fieldName
            , @NonNull String fieldValue) {
        JsonNode node = head.get(fieldName);
        if (node == null) {
            head.put(fieldName, fieldValue);
        }
    }

    public static String yamlDashPathToJsonCamelCase(String yamlPath) {
        if (yamlPath == null)
            return null;

        StringBuilder jsonPath
                = new StringBuilder(yamlPath.replace(".", "/"));

        int index = 0;
        while (index >= 0) {
            index = jsonPath.indexOf("-");
            if (index >= 0) {
                jsonPath.deleteCharAt(index);
                if (index < jsonPath.length()) {
                    char camelCase = Character.toUpperCase(jsonPath.charAt(index));
                    jsonPath.deleteCharAt(index);
                    jsonPath.insert(index, camelCase);
                }
            }
        }

        if (jsonPath.charAt(0) != '/')
            jsonPath = jsonPath.insert(0, '/');

        return jsonPath.toString();
    }



}
