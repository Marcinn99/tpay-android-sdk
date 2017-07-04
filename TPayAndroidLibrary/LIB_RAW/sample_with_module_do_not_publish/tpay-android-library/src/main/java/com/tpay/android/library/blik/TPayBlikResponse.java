package com.tpay.android.library.blik;

import java.io.Serializable;
import java.util.ArrayList;

public class TPayBlikResponse implements Serializable {

    public class TpayAvailableApp {
        public String applicationName;
        public String applicationCode;
    }

    public int result;
    public String err;
    public ArrayList<TpayAvailableApp> availableUserApps;
}
