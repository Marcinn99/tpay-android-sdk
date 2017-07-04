package com.tpay.android.library.blik;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tpay.android.library.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;

import static com.tpay.android.library.blik.TpayBlikDefaultActivity.BlikViewType.*;

public class TpayBlikDefaultActivity extends AppCompatActivity implements TpayBlikCallback<TPayBlikResponse> {

    public static final int BLIK_ACTIVITY_REQUEST = 10000;

    public static final String BLIK_SUCCESS_RESPONSE = "BlikSuccessResponse";
    public static final String BLIK_ERROR_RESPONSE = "BlikErrorResponse";
    public static final String BLIK_FAILURE_RESPONSE = "BlikFailureResponse";

    public static final int BLIK_RESULT_SUCCESS = 10001;
    public static final int BLIK_RESULT_ERROR = 10002;
    public static final int BLIK_RESULT_FAILURE = 10003;

    public static final String BLIK_TRANSACTION = "BlikTransaction";
    public static final String KEY = "Key";
    public static final String BLIK_VIEW_TYPE = "BlikViewType";

    private static final String BLIK_CODE_KEY = "BlikCodeKey";
    private static final String PROGRESS_DIALOG_VISIBLE_KEY = "ProgressDialogVisibleKey";

    public static final int BLIK_CODE_LENGTH = 6;
    public static final String AVAILABLE_APPS_ERROR = "ERR82";

    private TpayBlikTransaction transaction = null;
    private String key = null;
    private BlikViewType viewType;
    private ArrayList<TPayBlikResponse.TpayAvailableApp> availableUserApps;

    private ProgressDialog progressDialog;

    private TextView transactionTitle;

    private View blikView;
    private EditText blikCodeET;
    private TextView blikAmountToPayTV;
    private CheckBox registerAliasCheckbox;
    private CheckBox useBlikCheckbox;
    private View payButton;
    private RadioGroup nonUniqueAliasRadioGroup;

    private boolean shouldShowProgressDialog = false;

    private ArrayList<RadioButton> radioButtons = new ArrayList<>();

    public enum BlikViewType {
        REGISTERED_ALIAS, UNREGISTERED_ALIAS, NON_UNIQUE_ALIAS, ONLY_BLIK
    }

    public static Intent createIntent(Activity activity,
                                      TpayBlikTransaction transaction,
                                      String key,
                                      BlikViewType viewType) {
        Intent intent = new Intent(activity, TpayBlikDefaultActivity.class);
        intent.putExtra(BLIK_TRANSACTION, transaction);
        intent.putExtra(KEY, key);
        intent.putExtra(BLIK_VIEW_TYPE, viewType);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blik_default);

        obtainParameters();
        setupViews();

        showAppropriateViews();

        setupBlikCodeET();
        setupPayButton();
        setupAmountToPay();
        setupTransactionTitle();
        setupUseBlikCheckbox();

        if (savedInstanceState != null) {
            hideKeyboard();
        }
    }

    private void obtainParameters() {
        if (getIntent().hasExtra(BLIK_TRANSACTION)) {
            transaction = (TpayBlikTransaction) getIntent().getSerializableExtra(BLIK_TRANSACTION);
        } else {
            throw new RuntimeException("To use BlikDefaultActivity it is necessary to pass TpayBlikTransaction object via extra serializable.");
        }

        if (getIntent().hasExtra(KEY)) {
            key = getIntent().getStringExtra(KEY);
        } else {
            throw new RuntimeException("To use BlikDefaultActivity it is necessary to pass key via extra string.");
        }

        if (getIntent().hasExtra(BLIK_VIEW_TYPE)) {
            viewType = (BlikViewType) getIntent().getSerializableExtra(BLIK_VIEW_TYPE);
        } else {
            throw new RuntimeException("To use BlikDefaultActivity it is necessary to pass blik view type via extra serializable.");
        }
    }

    private void setupViews() {
        transactionTitle = (TextView) findViewById(R.id.transactionTitle);

        blikView = findViewById(R.id.blikCodeView);
        blikCodeET = (EditText) findViewById(R.id.blikCodeET);
        blikAmountToPayTV = (TextView) findViewById(R.id.transactionAmountToPay);

        nonUniqueAliasRadioGroup = (RadioGroup) findViewById(R.id.nonUniqueAliasRadioGroup);

        payButton = findViewById(R.id.payButton);

        registerAliasCheckbox = (CheckBox) findViewById(R.id.registerAliasCheckbox);
        useBlikCheckbox = (CheckBox) findViewById(R.id.useBlikCheckbox);

        progressDialog = makeProgressDialog();
    }

    private void showAppropriateViews() {

        resetNonUniqueAliasRadioGroup();

        switch (viewType) {
            case ONLY_BLIK:
                blikView.setVisibility(View.VISIBLE);
                nonUniqueAliasRadioGroup.setVisibility(View.GONE);
                registerAliasCheckbox.setVisibility(View.GONE);
                useBlikCheckbox.setVisibility(View.GONE);
                break;

            case NON_UNIQUE_ALIAS:
                blikView.setVisibility(View.GONE);
                nonUniqueAliasRadioGroup.setVisibility(View.VISIBLE);
                registerAliasCheckbox.setVisibility(View.GONE);
                useBlikCheckbox.setVisibility(View.GONE);
                break;

            case UNREGISTERED_ALIAS:
                blikView.setVisibility(View.VISIBLE);
                nonUniqueAliasRadioGroup.setVisibility(View.GONE);
                registerAliasCheckbox.setVisibility(View.VISIBLE);
                useBlikCheckbox.setVisibility(View.GONE);
                break;

            case REGISTERED_ALIAS:
                blikView.setVisibility(View.GONE);
                nonUniqueAliasRadioGroup.setVisibility(View.GONE);
                registerAliasCheckbox.setVisibility(View.GONE);
                useBlikCheckbox.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setupBlikCodeET() {
        blikCodeET.setCursorVisible(false);
        blikCodeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (blikView.getVisibility() == View.VISIBLE) {
                    if (s.length() >= BLIK_CODE_LENGTH) {
                        payButton.setEnabled(true);
                    } else {
                        payButton.setEnabled(false);
                    }
                } else {
                    payButton.setEnabled(true);
                }
            }
        });
    }

    private void setupPayButton() {
        if (blikView.getVisibility() == View.VISIBLE) {
            payButton.setEnabled(false);
        } else {
            payButton.setEnabled(true);
        }
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();

                String blikCode = blikCodeET.getText().toString();

                if (blikView.getVisibility() == View.VISIBLE && !blikCode.isEmpty()) {

                    progressDialog.show();
                    shouldShowProgressDialog = true;

                    blikCodeET.clearFocus();

                    transaction.code = blikCode;

                    if (registerAliasCheckbox.getVisibility() == View.GONE
                            || !registerAliasCheckbox.isChecked()) {
                        transaction.alias = null;
                    }

                    TpayClient.getInstance().postBlikTransaction(key, transaction, TpayBlikDefaultActivity.this);
                } else {
                    progressDialog.show();
                    shouldShowProgressDialog = true;

                    int radioButtonID = nonUniqueAliasRadioGroup.getCheckedRadioButtonId();
                    if (radioButtonID != -1) {
                        View radioButton = nonUniqueAliasRadioGroup.findViewById(radioButtonID);
                        int idx = nonUniqueAliasRadioGroup.indexOfChild(radioButton) - 1;

                        if (idx < availableUserApps.size()) {
                            TPayBlikResponse.TpayAvailableApp availableApp = availableUserApps.get(idx);

                            if (transaction.alias.size() > 0) {
                                transaction.alias.get(0).put("key", availableApp.applicationCode);
                            }
                        }
                    }

                    TpayClient.getInstance().postBlikTransaction(key, transaction, TpayBlikDefaultActivity.this);
                }
            }
        });
    }

    private void setupAmountToPay() {
        if (transaction != null && transaction.amount != null) {
            blikAmountToPayTV.setText(String.format("%s PLN", transaction.amount));
        }
    }

    private void setupTransactionTitle() {
        if (transaction != null && transaction.description != null) {
            transactionTitle.setText(transaction.description);
        }
    }

    private void setupUseBlikCheckbox() {
        useBlikCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    blikView.setVisibility(View.VISIBLE);
                    showKeyboard();
                } else {
                    hideKeyboard();
                    blikView.setVisibility(View.GONE);
                    payButton.setEnabled(true);
                }
            }
        });
    }

    private void resetNonUniqueAliasRadioGroup() {
        for (RadioButton radioButton : radioButtons) {
            nonUniqueAliasRadioGroup.removeView(radioButton);
        }
        radioButtons.clear();
    }

    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showKeyboard() {
        blikCodeET.requestFocus();
        InputMethodManager inputMethodManager =
                (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                blikCodeET.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);

        if (blikCodeET.getText().toString().isEmpty()) {
            payButton.setEnabled(false);
        }
    }

    @Override
    public void onResponseSuccess(TPayBlikResponse response) {

        progressDialog.dismiss();
        shouldShowProgressDialog = false;

        if (AVAILABLE_APPS_ERROR.equals(response.err) && response.availableUserApps != null) {
            supportMultipleAvailableApps(response);
        } else {
            Intent data = new Intent();
            data.putExtra(BLIK_SUCCESS_RESPONSE, response);

            setResult(BLIK_RESULT_SUCCESS, data);
            finish();
        }
    }

    private void supportMultipleAvailableApps(TPayBlikResponse response) {
        viewType = BlikViewType.NON_UNIQUE_ALIAS;
        availableUserApps = response.availableUserApps;

        showAppropriateViews();

        radioButtons.clear();
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (TPayBlikResponse.TpayAvailableApp availableApp : availableUserApps) {
            RadioButton radioButton = new RadioButton(TpayBlikDefaultActivity.this);
            radioButton.setText(availableApp.applicationName);
            nonUniqueAliasRadioGroup.addView(radioButton, params);
            radioButtons.add(radioButton);
        }

        RadioButton radioButton = new RadioButton(TpayBlikDefaultActivity.this);
        radioButton.setText(R.string.tpay_want_to_enter_blik_code);
        nonUniqueAliasRadioGroup.addView(radioButton, params);
        radioButtons.add(radioButton);

        payButton.setEnabled(false);

        nonUniqueAliasRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                payButton.setEnabled(true);
                View radioButton = nonUniqueAliasRadioGroup.findViewById(checkedId);
                int idx = nonUniqueAliasRadioGroup.indexOfChild(radioButton);
                //0 item is header

                if (idx == radioButtons.size()) {
                    blikView.setVisibility(View.VISIBLE);
                    showKeyboard();
                } else {
                    blikView.setVisibility(View.GONE);
                    hideKeyboard();
                }
            }
        });
    }

    @Override
    public void onResponseError(ResponseBody errorBody) {

        progressDialog.dismiss();
        shouldShowProgressDialog = false;

        Intent data = new Intent();
        try {
            data.putExtra(BLIK_ERROR_RESPONSE, errorBody.string());
        } catch (IOException e) {
            e.printStackTrace();
        }

        setResult(BLIK_RESULT_ERROR, data);
        finish();
    }

    @Override
    public void onResponseFailure(Throwable t) {

        progressDialog.dismiss();
        shouldShowProgressDialog = false;

        Intent data = new Intent();
        data.putExtra(BLIK_FAILURE_RESPONSE, t);

        setResult(BLIK_RESULT_FAILURE, data);
        finish();
    }

    private ProgressDialog makeProgressDialog() {
        ProgressDialog progress =  new ProgressDialog(this);
        progress.setMessage(getString(R.string.tpay_payment_in_progress));
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.setProgressNumberFormat(null);
        progress.setProgressPercentFormat(null);
        progress.setCanceledOnTouchOutside(false);

        return progress;
    }

    @Override
    public void onSaveInstanceState(Bundle out) {
        out.putBoolean(PROGRESS_DIALOG_VISIBLE_KEY, shouldShowProgressDialog);
        out.putString(BLIK_CODE_KEY, blikCodeET.getText().toString());
        super.onSaveInstanceState(out);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        blikCodeET.setText(savedInstanceState.getString(BLIK_CODE_KEY));
        TpayClient.getInstance().setApiCallback(this);
        hideKeyboard();

        shouldShowProgressDialog = savedInstanceState.getBoolean(PROGRESS_DIALOG_VISIBLE_KEY);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (shouldShowProgressDialog) {
            progressDialog.show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        progressDialog.dismiss();
    }
}
