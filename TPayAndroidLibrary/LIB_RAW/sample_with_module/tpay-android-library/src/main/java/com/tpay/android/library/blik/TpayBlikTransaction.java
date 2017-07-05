package com.tpay.android.library.blik;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TpayBlikTransaction implements Serializable {

    @SerializedName("api_password")
    public String apiPassword = null;

    @SerializedName("id")
    public String id = null;

    @SerializedName("kwota")
    public String amount = null;

    @SerializedName("opis")
    public String description = null;

    @SerializedName("crc")
    public String crc = null;

    @SerializedName("md5sum")
    public String md5Code = null;

    @SerializedName("online")
    public String online = null;

    @SerializedName("kanal")
    public String canal = null;

    @SerializedName("grupa")
    public String group = null;

    @SerializedName("wyn_url")
    public String resultUrl = null;

    @SerializedName("wyn_email")
    public String resultEmail = null;

    @SerializedName("opis_sprzed")
    public String sellerDescription = null;

    @SerializedName("opis_dodatkowy")
    public String additionalDescription = null;

    @SerializedName("pow_url")
    public String returnUrl = null;

    @SerializedName("pow_url_blad")
    public String returnErrorUrl = null;

    @SerializedName("jezyk")
    public String language = null;

    @SerializedName("email")
    public String clientEmail = null;

    @SerializedName("nazwisko")
    public String clientName = null;

    @SerializedName("adres")
    public String clientAddress = null;

    @SerializedName("miasto")
    public String clientCity = null;

    @SerializedName("kod")
    public String clientCode = null;

    @SerializedName("kraj")
    public String clientCountry = null;

    @SerializedName("telefon")
    public String clientPhone = null;

    @SerializedName("akceptuje_regulamin")
    public String acceptTerms = null;

    @SerializedName("json")
    public String json = null;

    public String code = null;

    public String securityCode = null;

    public ArrayList<HashMap<String, String>> alias = null;

    public Map<String, Object> toMap() {

        Field[] fields = getClass().getFields();
        Map<String, Object> map = new HashMap<>();

        for(Field f : fields) {
            try {
                if (f.isAnnotationPresent(SerializedName.class) && f.get(TpayBlikTransaction.this) != null)
                    map.put(f.getAnnotation(SerializedName.class).value(), f.get(TpayBlikTransaction.this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return map;
    }


}
