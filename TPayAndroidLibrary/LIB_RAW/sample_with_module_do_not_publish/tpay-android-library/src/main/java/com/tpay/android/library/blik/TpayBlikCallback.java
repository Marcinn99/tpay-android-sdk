package com.tpay.android.library.blik;

import okhttp3.ResponseBody;

public interface TpayBlikCallback<T> {
    public void onResponseSuccess(T response);
    public void onResponseError(ResponseBody errorBody);
    public void onResponseFailure(Throwable t);
}
