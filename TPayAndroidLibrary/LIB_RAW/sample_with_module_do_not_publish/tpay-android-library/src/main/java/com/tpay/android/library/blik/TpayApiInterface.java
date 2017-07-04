package com.tpay.android.library.blik;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface TpayApiInterface {

    String BASE_URL = "https://secure.tpay.com/api/gw/";

    @FormUrlEncoded
    @POST("{key}/transaction/create")
    Call<TPayCreateResponse> postCreateRequest(@Path("key") String key, @FieldMap Map<String, Object> blikTransactionAsMap);

    @FormUrlEncoded
    @POST("{key}/transaction/blik")
    Call<TPayBlikResponse> postBlikRequest(@Path("key") String key,
                                           @FieldMap Map<String, Object> blikInfoAsMap);
}
