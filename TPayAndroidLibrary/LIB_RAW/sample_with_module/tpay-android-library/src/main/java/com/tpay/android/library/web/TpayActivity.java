package com.tpay.android.library.web;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tpay.android.library.R;

public class TpayActivity extends AppCompatActivity {

    public static final int TPAY_BACK_PRESSED_RESULT = 121;
    public static final int TPAY_SESSION_CLOSED_RESULT = 120;
    public static final int TPAY_PAYMENT_REQUEST = 119;
    public static final String TPAY_MAIN_PAGE_URL = "https://tpay.com/";
    public static final String TPAY_PAYMENT_SUCCESS_URL = "TPAY_PAYMENT_SUCCESS_URL";
    public static final String TPAY_PAYMENT_ERROR_URL = "TPAY_PAYMENT_ERROR_URL";
    public static final String EXTRA_TPAY_PAYMENT = "EXTRA_TPAY_PAYMENT";

    private String successUrl = null;
    private String errorUrl = null;

    private View progressBar = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tpay);

        progressBar = findViewById(R.id.tPayProgressBar);

        setupToolbar();
        configureWebView(savedInstanceState);
    }

    private void setupToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.tPayToolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.tpay_toolbar_title));
        setSupportActionBar(toolbar);
    }

    private void configureWebView(Bundle savedInstanceState) {
        final WebView webView = (WebView) findViewById(R.id.tPayWebView);
        final WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new TpayWebViewClient());
        webView.setWebChromeClient(new TpayWebChromeClient());
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webView.addJavascriptInterface(new TpayJavaScriptInterface(), "Tpay");

        if (savedInstanceState == null) {
            successUrl = getIntent().getStringExtra(TPAY_PAYMENT_SUCCESS_URL);
            if (successUrl == null) {
                successUrl = "http://google.com/success";
            }

            errorUrl = getIntent().getStringExtra(TPAY_PAYMENT_ERROR_URL);
            if (errorUrl == null) {
                errorUrl = "http://google.com/error";
            }

            final TpayPayment tpayPayment = getIntent().getParcelableExtra(EXTRA_TPAY_PAYMENT);
            webView.loadUrl(tpayPayment.getPaymentLink());
        }
    }

    public class TpayJavaScriptInterface {

        @JavascriptInterface
        void onPaymentSuccess() {
            setResult(Activity.RESULT_OK);
            finish();
        }

        @JavascriptInterface
        void onPaymentFail() {
            finish();
        }
    }

    private class TpayWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

                if (url != null && url.toLowerCase().startsWith(successUrl.toLowerCase())){
                    setResult(Activity.RESULT_OK);
                    finish();
                } else if (url != null && url.toLowerCase().startsWith(errorUrl.toLowerCase())) {
                    finish();
                }
        }

        @Override
        public void onLoadResource(WebView view, String url) {

            if (url != null && url.toLowerCase().equals(TPAY_MAIN_PAGE_URL.toLowerCase())) {
                finish();
            } else if (url != null && url.contains("error.php")) {
                setResult(TPAY_SESSION_CLOSED_RESULT);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        setResult(TPAY_BACK_PRESSED_RESULT);
        super.onBackPressed();
    }

    private class TpayWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);

            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }
}