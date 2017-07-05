package com.tpay.android.library.blik;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TpayBlikInfo {

    @SerializedName("json")
    public String json = "1";

    @SerializedName("api_password")
    public String apiPassword = null;

    @SerializedName("code")
    public String blikCode;

    @SerializedName("title")
    public String transactionTitle;

    public ArrayList<HashMap<String, String>> alias;

    public Map<String, Object> toMap() {

        Field[] fields = getClass().getFields();
        Map<String, Object> map = new HashMap<>();

        for(Field f : fields) {
            try {
                if (f.isAnnotationPresent(SerializedName.class) && f.get(TpayBlikInfo.this) != null)
                    map.put(f.getAnnotation(SerializedName.class).value(), f.get(TpayBlikInfo.this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (alias != null) {
            int counter = 0;
            for (HashMap<String, String> aliasMap : alias) {
                String value = aliasMap.get("value");
                String appKey = aliasMap.get("key");
                String label = aliasMap.get("label");
                String type = aliasMap.get("type");

                if (value != null) {
                    map.put("alias[" + counter + "][value]", value);
                }

                if (appKey != null) {
                    map.put("alias[" + counter + "][key]", appKey);
                }

                if (label != null) {
                    map.put("alias[" + counter + "][label]", label);
                }

                if (type != null) {
                    map.put("alias[" + counter + "][type]", type);
                }

                counter++;
            }

        }

        return map;
    }

}
