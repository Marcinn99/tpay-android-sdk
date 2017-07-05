package com.tpay.android.library.blik;

import android.text.TextUtils;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.tpay.android.library.TpayUtils.md5;

public class TpayBlikTransactionBuilder {

    private TpayBlikTransaction transaction = null;

    public TpayBlikTransactionBuilder() {
        transaction = new TpayBlikTransaction();
    }

    public TpayBlikTransactionBuilder setApiPassword(String apiPassword) {
        transaction.apiPassword = apiPassword;

        return this;
    }

    public TpayBlikTransactionBuilder setId(String id) {
        transaction.id = id;

        return this;
    }

    public TpayBlikTransactionBuilder setAmount(String amount) {
        transaction.amount = amount;

        return this;
    }

    public TpayBlikTransactionBuilder setDescription(String description) {
        transaction.description = description;

        return this;
    }

    public TpayBlikTransactionBuilder setCrc(String crc) {
        transaction.crc = crc;

        return this;
    }

    public TpayBlikTransactionBuilder setSecurityCode(String securityCode) {
        transaction.securityCode = securityCode;

        return this;
    }

    public TpayBlikTransactionBuilder setMd5Code(String md5code) {
        transaction.md5Code = md5code;

        return this;
    }

    public TpayBlikTransactionBuilder setOnline(String online) {
        transaction.online = online;

        return this;
    }

    public TpayBlikTransactionBuilder setGroup(String group) {
        transaction.group = group;

        return this;
    }

    public TpayBlikTransactionBuilder setResultUrl(String resultUrl) {
        transaction.resultUrl = resultUrl;

        return this;
    }

    public TpayBlikTransactionBuilder setResultEmail(String resultEmail) {
        transaction.resultEmail = resultEmail;

        return this;
    }

    public TpayBlikTransactionBuilder setSellerDescription(String sellerDescription) {
        transaction.sellerDescription = sellerDescription;

        return this;
    }

    public TpayBlikTransactionBuilder setAdditionalDescription(String additionalDescription) {
        transaction.additionalDescription = additionalDescription;

        return this;
    }

    public TpayBlikTransactionBuilder setReturnUrl(String returnUrl) {
        transaction.returnUrl = returnUrl;

        return this;
    }

    public TpayBlikTransactionBuilder setReturnErrorUrl(String returnErrorUrl) {
        transaction.returnErrorUrl = returnErrorUrl;

        return this;
    }

    public TpayBlikTransactionBuilder setLanguage(String language) {
        transaction.language= language;

        return this;
    }

    public TpayBlikTransactionBuilder setClientEmail(String clientEmail) {
        transaction.clientEmail = clientEmail;

        return this;
    }

    public TpayBlikTransactionBuilder setClientName(String clientName) {
        transaction.clientName = clientName;

        return this;
    }

    public TpayBlikTransactionBuilder setClientAddress(String clientAddress) {
        transaction.clientAddress = clientAddress;

        return this;
    }

    public TpayBlikTransactionBuilder setClientCity(String clientCity) {
        transaction.clientCity = clientCity;

        return this;
    }

    public TpayBlikTransactionBuilder setClientCode(String clientCode) {
        transaction.clientCode = clientCode;

        return this;
    }

    public TpayBlikTransactionBuilder setClientCountry(String clientCountry) {
        transaction.clientCountry = clientCountry;

        return this;
    }

    public TpayBlikTransactionBuilder setClientPhone(String clientPhone) {
        transaction.clientPhone = clientPhone;

        return this;
    }

    public TpayBlikTransactionBuilder setAcceptTerms(String acceptTerms) {
        transaction.acceptTerms = acceptTerms;

        return this;
    }

    public TpayBlikTransactionBuilder setBlikCode(String blikCode) {
        transaction.code = blikCode;

        return this;
    }

    public TpayBlikTransactionBuilder addBlikAlias(String alias, String label, String key) {
        if (transaction.alias == null) {
            transaction.alias = new ArrayList<>();
        }

        if (alias != null) {
            HashMap<String, String> aliasMap = new HashMap<>();
            aliasMap.put("type", "UID");
            aliasMap.put("value", alias);

            if (label != null) {
                aliasMap.put("label", label);
            }

            if (key != null) {
                aliasMap.put("key", key);
            }

            transaction.alias.add(aliasMap);
        } else
            throw new IllegalStateException("Alias cannot be null.");

        return this;
    }

    public TpayBlikTransaction build() {
        transaction.json = "1";
        transaction.canal = "64";

        if (TextUtils.isEmpty(transaction.md5Code)) {
            if (transaction.securityCode != null) {
                final String input = String.format("%s%s%s%s", transaction.id, transaction.amount, transaction.crc, transaction.securityCode);
                try {
                    transaction.md5Code = md5(input);
                } catch (NoSuchAlgorithmException e) {
                    throw new IllegalStateException("Error while calculating checksum", e);
                }
            } else
                throw new IllegalStateException("Set 'md5Code' or 'securityCode' first.");
        }

        return transaction;
    }
}
