package com.tpay.android.library.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tpay.android.library.web.TpayActivity;
import com.tpay.android.library.web.TpayPayment;

public class WebActivity extends AppCompatActivity {

    private TpayPayment.Builder paymentBuilder = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TpayActivity.TPAY_PAYMENT_REQUEST:
                if (resultCode == RESULT_OK) {
                    Snackbar.make(findViewById(R.id.fab), "Płatność została wykonana poprawnie", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(findViewById(R.id.fab), "Płatność nie została wykonana", Snackbar.LENGTH_SHORT).show();
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        setupToolbar();
        setupPayment(savedInstanceState);
        setupPayButton();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupPayment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            paymentBuilder = new TpayPayment.Builder()
                    .setId("1010")
                    .setAmount("1010")
                    .setCrc("demo")
                    .setSecurityCode("demo")
                    .setDescription("Opis demonstracyjnej transakcji.")
                    .setClientEmail("demo@tpay.com")
                    .setClientName("Demo");

        } else {
            paymentBuilder = savedInstanceState.getParcelable(TpayActivity.EXTRA_TPAY_PAYMENT);
        }
    }

    private void setupPayButton() {
        findViewById(R.id.pay_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent payIntent = new Intent(WebActivity.this, TpayActivity.class);
                final TpayPayment tpayPayment = paymentBuilder.create();
                payIntent.putExtra(TpayActivity.EXTRA_TPAY_PAYMENT, tpayPayment);
                startActivityForResult(payIntent, TpayActivity.TPAY_PAYMENT_REQUEST);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(TpayActivity.EXTRA_TPAY_PAYMENT, paymentBuilder);
    }
}
