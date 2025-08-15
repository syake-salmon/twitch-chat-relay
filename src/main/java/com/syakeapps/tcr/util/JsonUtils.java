package com.syakeapps.tcr.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    private JsonUtils() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * 指定したキー（ドット区切りで深い階層も指定可）に対応する値を返すユーティリティメソッド
     * 例: key="user.name" の場合、{"user":{"name":"foo"}} から "foo" を返す
     */
    public static String getValueByKey(String json, String key) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(json);
            String[] keys = key.split("\\.");

            for (String k : keys) {
                if (node == null) return null;
                node = node.get(k);
            }
            
            return node != null ? node.asText() : null;
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }
}
