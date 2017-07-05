package com.tpay.android.library.blik;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TpayClient {

    private static TpayClient tpayClient = null;

    public static TpayClient getInstance() {
        if (tpayClient == null) {
            tpayClient = new TpayClient();
        }

        return tpayClient;
    }

    private TpayApiInterface tpayApiInterface;
    private TpayBlikCallback<TPayBlikResponse> tpayBlikCallback;

    private TpayClient() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TpayApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        tpayApiInterface = retrofit.create(TpayApiInterface.class);
    }

    public void setApiCallback(TpayBlikCallback<TPayBlikResponse> callback) {
        this.tpayBlikCallback = callback;
    }

    public void postBlikTransaction(final String key, final TpayBlikTransaction transaction, final TpayBlikCallback<TPayBlikResponse> callback) {
        //first step: call create api method
        this.tpayBlikCallback = callback;
        callCreateRequest(key, transaction, tpayBlikCallback);
    }

    private void callCreateRequest(final String key, final TpayBlikTransaction transaction, final TpayBlikCallback<TPayBlikResponse> callback) {
        Call<TPayCreateResponse> call = tpayApiInterface.postCreateRequest(key, transaction.toMap());
        call.enqueue(new Callback<TPayCreateResponse>() {
            @Override
            public void onResponse(Call<TPayCreateResponse> call, Response<TPayCreateResponse> response) {
                if (response.isSuccessful()) {
                    TpayBlikInfo tpayBlikInfo = fillTpayBlikInfo(response, transaction);
                    //second step: when transaction is created, call blik api method
                    callBlikRequest(key, tpayBlikInfo, callback);
                } else {
                    callback.onResponseError(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<TPayCreateResponse> call, Throwable t) {
                callback.onResponseFailure(t);
            }
        });
    }

    @NonNull
    private TpayBlikInfo fillTpayBlikInfo(Response<TPayCreateResponse> response, TpayBlikTransaction transaction) {
        TpayBlikInfo tpayBlikInfo = new TpayBlikInfo();
        tpayBlikInfo.transactionTitle = response.body().title;
        tpayBlikInfo.alias = transaction.alias;
        tpayBlikInfo.blikCode = transaction.code;
        tpayBlikInfo.apiPassword = transaction.apiPassword;
        return tpayBlikInfo;
    }

    private void callBlikRequest(final String key, final TpayBlikInfo tpayBlikInfo, final TpayBlikCallback<TPayBlikResponse> callback) {

        Call<TPayBlikResponse> call = tpayApiInterface.postBlikRequest(key, tpayBlikInfo.toMap());
        call.enqueue(new Callback<TPayBlikResponse>() {
            @Override
            public void onResponse(Call<TPayBlikResponse> call, Response<TPayBlikResponse> response) {
                if (response.isSuccessful()) {
                    tpayBlikInfo.blikCode = null;
                    callback.onResponseSuccess(response.body());
                } else {
                    callback.onResponseError(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<TPayBlikResponse> call, Throwable t) {
                callback.onResponseFailure(t);
            }
        });
    }

}
