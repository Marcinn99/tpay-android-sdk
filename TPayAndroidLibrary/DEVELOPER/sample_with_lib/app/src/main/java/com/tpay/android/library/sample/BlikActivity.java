package com.tpay.android.library.sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.tpay.android.library.blik.TpayBlikDefaultActivity;
import com.tpay.android.library.blik.TPayBlikResponse;
import com.tpay.android.library.blik.TpayBlikCallback;
import com.tpay.android.library.blik.TpayBlikTransaction;
import com.tpay.android.library.blik.TpayBlikTransactionBuilder;
import com.tpay.android.library.blik.TpayClient;

import okhttp3.ResponseBody;

public class BlikActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blik);

        setupToolbar();
        setupPayButton();

        // use own apiKey to run demo
        findViewById(R.id.open_default_view_unregistered).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TpayBlikTransaction transaction = getTpayBlikTransaction();
                Intent intent = TpayBlikDefaultActivity.createIntent(BlikActivity.this,
                        transaction,
                        "apiKey",
                        TpayBlikDefaultActivity.BlikViewType.UNREGISTERED_ALIAS);
                startActivityForResult(intent, TpayBlikDefaultActivity.BLIK_ACTIVITY_REQUEST);
            }
        });

        findViewById(R.id.open_default_view_registered).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TpayBlikTransaction transaction = getTpayBlikTransaction();
                Intent intent = TpayBlikDefaultActivity.createIntent(BlikActivity.this,
                        transaction,
                        "apiKey",
                        TpayBlikDefaultActivity.BlikViewType.REGISTERED_ALIAS);
                startActivityForResult(intent, TpayBlikDefaultActivity.BLIK_ACTIVITY_REQUEST);
            }
        });

        findViewById(R.id.open_default_view_only_blik).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TpayBlikTransaction transaction = getTpayBlikTransaction();
                Intent intent = TpayBlikDefaultActivity.createIntent(BlikActivity.this,
                        transaction,
                        "apiKey",
                        TpayBlikDefaultActivity.BlikViewType.ONLY_BLIK);
                startActivityForResult(intent, TpayBlikDefaultActivity.BLIK_ACTIVITY_REQUEST);
            }
        });

        hideKeyboard();
    }

    private TpayBlikTransaction getTpayBlikTransaction() {
        //use own api password, id and security code to run demo
        return new TpayBlikTransactionBuilder()
                                                    .setApiPassword("apiPassword")
                                                    .setId("00000")
                                                    .setAmount("0.01")
                                                    .setCrc("demo")
                                                    .setSecurityCode("securityCode")
                                                    .setDescription("Opis demonstracyjnej transakcji.")
                                                    .setClientEmail("demo@tpay.com")
                                                    .setClientName("Demo")
                                                    .addBlikAlias("DemoBlikAlias12345", null, null)
                                                    .build();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupPayButton() {
        findViewById(R.id.pay_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();

                EditText blikET = ((EditText)findViewById(R.id.blikNumber));
                String blikNumber = blikET.getText().toString();

                if (blikNumber.length() != 6) {
                    blikET.setError("Kod BLIK niepoprawny");

                    return;
                }

                TpayBlikTransaction transaction = setupTransactionWith(blikNumber);
                postTransactionAndSetupCallbacks(transaction);
            }
        });
    }

    private void postTransactionAndSetupCallbacks(TpayBlikTransaction transaction) {

        final ProgressDialog progress = makeProgressDialog();
        progress.show();

        // use own apiKey to run demo
        TpayClient.getInstance().postBlikTransaction("apiKey", transaction, new TpayBlikCallback<TPayBlikResponse>() {
            @Override
            public void onResponseSuccess(TPayBlikResponse response) {
                progress.hide();
                if (response.result == 1) {
                    Snackbar.make(findViewById(R.id.fab), "Płatność została rozpoczęta poprawnie. Potwierdź płatność w aplikacji banku.", Snackbar.LENGTH_SHORT).show();
                }  else if (response.result == 0 && "ERR63".equals(response.err)) {
                    Snackbar.make(findViewById(R.id.fab), "Niepoprawny kod BLIK. Płatność nie została wykonana.", Snackbar.LENGTH_SHORT).show();
                } else if (response.result == 0) {
                    String errorBody = response.err != null ? " " + response.err : "";
                    Snackbar.make(findViewById(R.id.fab), "Wystąpił błąd" + errorBody + ". Płatność nie została wykonana.", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onResponseError(ResponseBody errorBody) {
                progress.hide();
                Snackbar.make(findViewById(R.id.fab), "Płatność nie została wykonana.", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onResponseFailure(Throwable t) {
                progress.hide();
                Snackbar.make(findViewById(R.id.fab), "Płatność nie została wykonana.", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private TpayBlikTransaction setupTransactionWith(String blikNumber) {
        //use own api password, id and security code to run demo
        return new TpayBlikTransactionBuilder()
                            .setApiPassword("apiPassword")
                            .setId("00000")
                            .setAmount("0.01")
                            .setCrc("demo")
                            .setSecurityCode("securityCode")
                            .setDescription("Opis demonstracyjnej transakcji.")
                            .setClientEmail("demo@tpay.com")
                            .setClientName("Demo")
                            .setBlikCode(blikNumber)
                            .build();
    }

    private ProgressDialog makeProgressDialog() {
        ProgressDialog progress =  new ProgressDialog(this);
        progress.setMessage("Realizacja płatności w toku...");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.setProgressNumberFormat(null);
        progress.setProgressPercentFormat(null);
        progress.setCanceledOnTouchOutside(false);

        return progress;
    }

    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TpayBlikDefaultActivity.BLIK_ACTIVITY_REQUEST) {
            switch (resultCode) {
                case TpayBlikDefaultActivity.BLIK_RESULT_SUCCESS:
                    hanleSuccessResponse(data);
                    break;

                case TpayBlikDefaultActivity.BLIK_RESULT_ERROR:
                    handleErrorResponse(data);
                    break;

                case TpayBlikDefaultActivity.BLIK_RESULT_FAILURE:
                    handleFailureResponse(data);
                    break;
            }
        }
    }

    private void handleFailureResponse(Intent data) {
        Throwable t = null;
        try {
            t = (Throwable) data.getSerializableExtra(TpayBlikDefaultActivity.BLIK_FAILURE_RESPONSE);
        } catch (Exception e) {
            e.printStackTrace();
            showAlertWithMessage("Wystąpił błąd.");
        }
        if (t != null && t.getLocalizedMessage() != null) {
            showAlertWithMessage(t.getLocalizedMessage());
        } else {
            showAlertWithMessage("Wystąpił błąd.");
        }
    }

    private void handleErrorResponse(Intent data) {
        String s = null;
        if (data.hasExtra(TpayBlikDefaultActivity.BLIK_ERROR_RESPONSE)) {
            s = data.getStringExtra(TpayBlikDefaultActivity.BLIK_ERROR_RESPONSE);
        }

        if (s != null) {
            showAlertWithMessage(s);
        } else {
            showAlertWithMessage("Wystąpił błąd.");
        }
    }

    private void hanleSuccessResponse(Intent data) {
        TPayBlikResponse response = null;
        try {
          response = (TPayBlikResponse) data.getSerializableExtra(TpayBlikDefaultActivity.BLIK_SUCCESS_RESPONSE);
        } catch (Exception e) {
            e.printStackTrace();
            showAlertWithMessage("Wystąpił błąd.");
        }

        if (response != null && response.result == 1) {
            showAlertWithMessage("Potwierdź płatność BLIK w aplikacji banku.");
        } else if (response != null && response.err != null) {
            showAlertWithMessage("Błąd: " + response.err);
        } else {
            showAlertWithMessage("Wystąpił błąd.");
        }
    }

    private void showAlertWithMessage(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Płatność BLIK")
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("OK", null)
                .create()
                .show();
    }
}
