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
    // public static String getValueByKey(String json, String key) {
    // try {
    // ObjectMapper mapper = new ObjectMapper();
    // JsonNode node = mapper.readTree(json);
    // String[] keys = key.split("\\.");

    // for (String k : keys) {
    // if (node == null) return null;
    // node = node.get(k);
    // }

    // return node != null ? node.asText() : null;
    // } catch (Exception e) {
    // return null;
    // }
    // }
    public static String getValueByKey(String json, String keyPath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(json);
            String[] keys = keyPath.split("\\.");

            for (String k : keys) {
                if (node == null)
                    return null;

                // 配列インデックス付きキー (例: choices[0])
                if (k.contains("[") && k.endsWith("]")) {
                    String fieldName = k.substring(0, k.indexOf("["));
                    int index = Integer.parseInt(k.substring(k.indexOf("[") + 1, k.indexOf("]")));

                    node = node.get(fieldName);
                    if (node != null && node.isArray()) {
                        node = node.get(index);
                    } else {
                        return null;
                    }
                } else {
                    // 通常のオブジェクトキー
                    node = node.get(k);
                }
            }

            // 最終的なノードをテキスト化
            return node != null && !node.isNull() ? node.asText() : null;
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
