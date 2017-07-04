package com.tpay.android.library.web;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.security.NoSuchAlgorithmException;

import static com.tpay.android.library.TpayUtils.md5;

public class TpayPayment implements Parcelable {

    public static final Creator<TpayPayment> CREATOR = new Creator<TpayPayment>() {
        @Override
        public TpayPayment createFromParcel(Parcel source) {
            return new TpayPayment(source);
        }

        @Override
        public TpayPayment[] newArray(int size) {
            return new TpayPayment[size];
        }
    };

    private final String mLink;

    private TpayPayment(Parcel in) {
        mLink = in.readString();
    }

    private TpayPayment(String link) {
        mLink = link;
    }

    public static TpayPayment newInstance(String paymentLink) {
        return new TpayPayment(paymentLink);
    }

    @NonNull
    public String getPaymentLink() {
        return mLink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mLink);
    }

    public static class Builder implements Parcelable {

        public static final Creator<Builder> CREATOR = new Creator<Builder>() {
            @Override
            public Builder createFromParcel(Parcel source) {
                return new Builder(source);
            }

            @Override
            public Builder[] newArray(int size) {
                return new Builder[size];
            }
        };
        private String mPaymentLink = null;
        private String mId = null;
        private String mAmount = null;
        private String mDescription = null;
        private String mCrc = null;
        private String mSecurityCode = null;
        private String mMd5Code = null;
        private String mOnline = null;
        private String mCanal = null;
        private String mGroup = null;
        private String mDirect = null;
        private String mResultUrl = null;
        private String mResultEmail = null;
        private String mSellerDescription = null;
        private String mAdditionalDescription = null;
        private String mReturnUrl = null;
        private String mReturnErrorUrl = null;
        private String mLanguage = null;
        private String mClientEmail = null;
        private String mClientName = null;
        private String mClientAddress = null;
        private String mClientCity = null;
        private String mClientCode = null;
        private String mClientCountry = null;
        private String mClientPhone = null;
        private String mAcceptTerms = null;

        /**
         * Default empty constructor.
         */
        public Builder() {

        }

        private Builder(Parcel in) {
            mPaymentLink = in.readString();
            mId = in.readString();
            mAmount = in.readString();
            mDescription = in.readString();
            mCrc = in.readString();
            mSecurityCode = in.readString();
            mMd5Code = in.readString();
            mOnline = in.readString();
            mCanal = in.readString();
            mGroup = in.readString();
            mDirect = in.readString();
            mResultUrl = in.readString();
            mResultEmail = in.readString();
            mSellerDescription = in.readString();
            mAdditionalDescription = in.readString();
            mReturnUrl = in.readString();
            mReturnErrorUrl = in.readString();
            mLanguage = in.readString();
            mClientEmail = in.readString();
            mClientName = in.readString();
            mClientAddress = in.readString();
            mClientCity = in.readString();
            mClientCode = in.readString();
            mClientCountry = in.readString();
            mClientPhone = in.readString();
            mAcceptTerms = in.readString();
        }

        public String getPaymentLink() {
            return mPaymentLink;
        }

        public Builder setPaymentLink(String paymentLink) {
            mPaymentLink = paymentLink;
            return this;
        }

        public String getId() {
            return mId;
        }

        /**
         * Liczbowy identyfikator odbiorcy płatności nadany podczas rejestracji.
         * <p>Parametr obowiązkowy!</p>
         *
         * @param id Identyfikator odbiorcy płatności.
         * @return Ten sam obiekt.
         */

        public Builder setId(String id) {
            mId = id;
            return this;
        }

        public String getAmount() {
            return mAmount;
        }

        public Builder setAmount(String amount) {
            mAmount = amount;
            return this;
        }

        public String getDescription() {
            return mDescription;
        }

        public Builder setDescription(String description) {
            mDescription = description;
            return this;
        }

        public String getCrc() {
            return mCrc;
        }

        public Builder setCrc(String crc) {
            mCrc = crc;
            return this;
        }

        public String getSecurityCode() {
            return mSecurityCode;
        }

        public Builder setSecurityCode(String securityCode) {
            mSecurityCode = securityCode;
            return this;
        }

        public Builder setMd5Code(String md5Code) {
            mMd5Code = md5Code;
            return this;
        }

        public String getOnline() {
            return mOnline;
        }

        public Builder setOnline(String online) {
            mOnline = online;
            return this;
        }

        public String getCanal() {
            return mCanal;
        }

        public Builder setCanal(String canal) {
            mCanal = canal;
            return this;
        }

        public String getGroup() {
            return mGroup;
        }

        public Builder setGroup(String group) {
            mGroup = group;
            return this;
        }

        public String getDirect() {
            return mDirect;
        }

        public Builder setDirect(String direct) {
            mDirect = direct;
            return this;
        }

        public String getResultUrl() {
            return mResultUrl;
        }

        public Builder setResultUrl(String resultUrl) {
            mResultUrl = resultUrl;
            return this;
        }

        public String getResultEmail() {
            return mResultEmail;
        }

        public Builder setResultEmail(String resultEmail) {
            mResultEmail = resultEmail;
            return this;
        }

        public String getSellerDescription() {
            return mSellerDescription;
        }

        public Builder setSellerDescription(String sellerDescription) {
            mSellerDescription = sellerDescription;
            return this;
        }

        public String getAdditionalDescription() {
            return mAdditionalDescription;
        }

        public Builder setAdditionalDescription(String additionalDescription) {
            mAdditionalDescription = additionalDescription;
            return this;
        }

        public String getReturnUrl() {
            return mReturnUrl;
        }

        public Builder setReturnUrl(String returnUrl) {
            mReturnUrl = returnUrl;
            return this;
        }

        public String getReturnErrorUrl() {
            return mReturnErrorUrl;
        }

        public Builder setReturnErrorUrl(String returnErrorUrl) {
            mReturnErrorUrl = returnErrorUrl;
            return this;
        }

        public String getLanguage() {
            return mLanguage;
        }

        public Builder setLanguage(String language) {
            mLanguage = language;
            return this;
        }

        public String getClientEmail() {
            return mClientEmail;
        }

        public Builder setClientEmail(String clientEmail) {
            mClientEmail = clientEmail;
            return this;
        }

        public String getClientName() {
            return mClientName;
        }

        public Builder setClientName(String clientName) {
            mClientName = clientName;
            return this;
        }

        public String getClientAddress() {
            return mClientAddress;
        }

        public Builder setClientAddress(String clientAddress) {
            mClientAddress = clientAddress;
            return this;
        }

        public String getClientCity() {
            return mClientCity;
        }

        public Builder setClientCity(String clientCity) {
            mClientCity = clientCity;
            return this;
        }

        public String getClientCode() {
            return mClientCode;
        }

        public Builder setClientCode(String clientCode) {
            mClientCode = clientCode;
            return this;
        }

        public String getClientCountry() {
            return mClientCountry;
        }

        public Builder setClientCountry(String clientCountry) {
            mClientCountry = clientCountry;
            return this;
        }

        public String getClientPhone() {
            return mClientPhone;
        }

        public Builder setClientPhone(String clientPhone) {
            mClientPhone = clientPhone;
            return this;
        }

        public String getAcceptTerms() {
            return mAcceptTerms;
        }

        public Builder setAcceptTerms(String acceptTerms) {
            mAcceptTerms = acceptTerms;
            return this;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mPaymentLink);
            dest.writeString(mId);
            dest.writeString(mAmount);
            dest.writeString(mDescription);
            dest.writeString(mCrc);
            dest.writeString(mSecurityCode);
            dest.writeString(mMd5Code);
            dest.writeString(mOnline);
            dest.writeString(mCanal);
            dest.writeString(mGroup);
            dest.writeString(mDirect);
            dest.writeString(mResultUrl);
            dest.writeString(mResultEmail);
            dest.writeString(mSellerDescription);
            dest.writeString(mAdditionalDescription);
            dest.writeString(mReturnUrl);
            dest.writeString(mReturnErrorUrl);
            dest.writeString(mLanguage);
            dest.writeString(mClientEmail);
            dest.writeString(mClientName);
            dest.writeString(mClientAddress);
            dest.writeString(mClientCity);
            dest.writeString(mClientCode);
            dest.writeString(mClientCountry);
            dest.writeString(mClientPhone);
            dest.writeString(mAcceptTerms);
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "mPaymentLink='" + mPaymentLink + '\'' +
                    ", mId='" + mId + '\'' +
                    ", mAmount='" + mAmount + '\'' +
                    ", mDescription='" + mDescription + '\'' +
                    ", mCrc='" + mCrc + '\'' +
                    ", mSecurityCode='" + mSecurityCode + '\'' +
                    ", mMd5Code='" + mMd5Code + '\'' +
                    ", mOnline='" + mOnline + '\'' +
                    ", mCanal='" + mCanal + '\'' +
                    ", mGroup='" + mGroup + '\'' +
                    ", mDirect='" + mDirect + '\'' +
                    ", mResultUrl='" + mResultUrl + '\'' +
                    ", mResultEmail='" + mResultEmail + '\'' +
                    ", mSellerDescription='" + mSellerDescription + '\'' +
                    ", mAdditionalDescription='" + mAdditionalDescription + '\'' +
                    ", mReturnUrl='" + mReturnUrl + '\'' +
                    ", mReturnErrorUrl='" + mReturnErrorUrl + '\'' +
                    ", mLanguage='" + mLanguage + '\'' +
                    ", mClientEmail='" + mClientEmail + '\'' +
                    ", mClientName='" + mClientName + '\'' +
                    ", mClientAddress='" + mClientAddress + '\'' +
                    ", mClientCity='" + mClientCity + '\'' +
                    ", mClientCode='" + mClientCode + '\'' +
                    ", mClientCountry='" + mClientCountry + '\'' +
                    ", mClientPhone='" + mClientPhone + '\'' +
                    ", mAcceptTerms='" + mAcceptTerms + '\'' +
                    '}';
        }

        public TpayPayment create() throws IllegalStateException {
            if (!TextUtils.isEmpty(mPaymentLink)) {
                return TpayPayment.newInstance(mPaymentLink);
            } else {
                if (TextUtils.isEmpty(mId))
                    throw new IllegalStateException("Set 'id' first.");
                if (TextUtils.isEmpty(mAmount))
                    throw new IllegalStateException("Set 'amount' first.");
                if (TextUtils.isEmpty(mDescription))
                    throw new IllegalStateException("Set 'description' first.");
                if (TextUtils.isEmpty(mCrc))
                    throw new IllegalStateException("Set 'crc' first.");
                if (TextUtils.isEmpty(mMd5Code) && TextUtils.isEmpty(mSecurityCode))
                    throw new IllegalStateException("Set 'md5Code' or 'securityCode' first.");

                if (mReturnErrorUrl == null) {
                    mReturnErrorUrl = "http://google.com/error";
                }

                if (mReturnUrl == null) {
                    mReturnUrl = "http://google.com/success";
                }

                final StringBuilder stringBuilder = new StringBuilder("https://secure.tpay.com/?id=");
                stringBuilder.append(mId);
                stringBuilder.append(String.format("&kwota=%s", mAmount));
                stringBuilder.append(String.format("&opis=%s", mDescription));
                stringBuilder.append(String.format("&crc=%s", mCrc));

                if (!TextUtils.isEmpty(mMd5Code)) {
                    stringBuilder.append(String.format("&md5sum=%s", mMd5Code));
                } else {
                    final String input = String.format("%s%s%s%s", mId, mAmount, mCrc, mSecurityCode);
                    try {
                        final String md5sum = md5(input);
                        stringBuilder.append(String.format("&md5sum=%s", md5sum));
                    } catch (NoSuchAlgorithmException e) {
                        throw new IllegalStateException("Error while calculating checksum", e);
                    }
                }

                if (!TextUtils.isEmpty(mOnline))
                    stringBuilder.append(String.format("&online=%s", mOnline));

                if (!TextUtils.isEmpty(mCanal))
                    stringBuilder.append(String.format("&kanal=%s", mCanal));

                if (!TextUtils.isEmpty(mGroup))
                    stringBuilder.append(String.format("&grupa=%s", mGroup));

                if (!TextUtils.isEmpty(mResultUrl))
                    stringBuilder.append(String.format("&wyn_url=%s", mResultUrl));

                if (!TextUtils.isEmpty(mResultEmail))
                    stringBuilder.append(String.format("&wyn_email=%s", mResultEmail));

                if (!TextUtils.isEmpty(mSellerDescription))
                    stringBuilder.append(String.format("&opis_sprzed=%s", mSellerDescription));

                if (!TextUtils.isEmpty(mAdditionalDescription))
                    stringBuilder.append(String.format("&opis_dodatkowy=%s", mAdditionalDescription));

                if (!TextUtils.isEmpty(mReturnUrl))
                    stringBuilder.append(String.format("&pow_url=%s", mReturnUrl));

                if (!TextUtils.isEmpty(mReturnErrorUrl))
                    stringBuilder.append(String.format("&pow_url_blad=%s", mReturnErrorUrl));

                if (!TextUtils.isEmpty(mLanguage))
                    stringBuilder.append(String.format("&jezyk=%s", mLanguage));

                if (!TextUtils.isEmpty(mClientEmail))
                    stringBuilder.append(String.format("&email=%s", mClientEmail));

                if (!TextUtils.isEmpty(mClientName))
                    stringBuilder.append(String.format("&nazwisko=%s", mClientName));

                if (!TextUtils.isEmpty(mClientAddress))
                    stringBuilder.append(String.format("&adres=%s", mClientAddress));

                if (!TextUtils.isEmpty(mClientCity))
                    stringBuilder.append(String.format("&miasto=%s", mClientCity));

                if (!TextUtils.isEmpty(mClientCode))
                    stringBuilder.append(String.format("&kod=%s", mClientCode));

                if (!TextUtils.isEmpty(mClientCountry))
                    stringBuilder.append(String.format("&kraj=%s", mClientCountry));

                if (!TextUtils.isEmpty(mClientPhone))
                    stringBuilder.append(String.format("&telefon=%s", mClientPhone));

                if (!TextUtils.isEmpty(mAcceptTerms))
                    stringBuilder.append(String.format("&akceptuje_regulamin=%s", mAcceptTerms));

                return TpayPayment.newInstance(stringBuilder.toString());
            }
        }
    }

}
