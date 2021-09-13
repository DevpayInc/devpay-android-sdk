package com.sdk.devpaysdk;

import com.sdk.devpaysdk.models.PaymentDetail;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class PaymentManager {
    RestClient client;
    DevpayClient.Config config;

    public PaymentManager(DevpayClient.Config config, RestClient client) {
        this.client = client;
        this.config = config;
    }


    interface Callback {
        public void onCompletion(Boolean status, Error error);
    }


    interface PaymentIntentCallback {
        public void onCompletion(String token, Error error);
    }

    public void confirmPayment(PaymentDetail paymentDetail,
                               Callback callback) {
        createPaymentIntent(paymentDetail, new PaymentIntentCallback() {
            @Override
            public void onCompletion(String token, Error error) {
                if (error != null) {
                    callback.onCompletion(null, error);
                } else {
                    confirmPaymentIntent(token,
                            paymentDetail, callback);
                }
            }
        });
    }

    private void createPaymentIntent(PaymentDetail paymentDetail,
                                     PaymentIntentCallback callback) {

        JSONObject requestPayload = new JSONObject();

        try {

            // Card details
            JSONObject cardObject = new JSONObject();
            cardObject.put("Number", paymentDetail.getCard().cardNum);
            cardObject.put("ExpMonth", paymentDetail.getCard().expiryMonth);
            cardObject.put("ExpYear", paymentDetail.getCard().expiryYear);
            cardObject.put("Cvv", paymentDetail.getCard().cvv);

            // Charges
            JSONObject chargesObject = new JSONObject();

            chargesObject.put("amount", paymentDetail.getAmount());
            chargesObject.put("fee_amount", 0);
            chargesObject.put("description", paymentDetail.getDecription());
            chargesObject.put("account_id", config.accountId);
            chargesObject.put("secreate_key", config.accessKey);

            if (config.sandbox) {
                chargesObject.put("environment", "sandbox");
            }

            requestPayload.put("CardDetails", cardObject);
            requestPayload.put("ChargeDetails", chargesObject);

        } catch (JSONException e) {
            callback.onCompletion(null, new Error(e.getLocalizedMessage()));
            return;
        }

        client.post("/v1/charge/create_Intend", requestPayload, new HashMap(), new RestClient.RestClientCallback() {
            @Override
            public void completion(Error err, JSONObject response) {
                if (err != null) {
                    callback.onCompletion(null, err);
                } else {
                    try {
                        String token = response.getString("token");
                        if (token != null) {
                            callback.onCompletion(token, err);
                        } else {
                            callback.onCompletion(null, new Error("Failed to parse JSON " + response.toString()));
                        }
                    } catch (JSONException e) {
                        callback.onCompletion(null, new Error("Failed to parse JSON " + response.toString()));
                    }
                }
            }
        });
    }

    private void confirmPaymentIntent(String token,
                                     PaymentDetail paymentDetail,
                                     Callback callback) {

        JSONObject requestPayload = new JSONObject();

        try {
                // Card details
                JSONObject cardObject = new JSONObject();
                cardObject.put("Number", paymentDetail.getCard().cardNum);
                cardObject.put("ExpMonth", paymentDetail.getCard().expiryMonth);
                cardObject.put("ExpYear", paymentDetail.getCard().expiryYear);
                cardObject.put("Cvv", paymentDetail.getCard().cvv);
                cardObject.put("token", token);

                // Charges
                JSONObject chargesObject = new JSONObject();

                chargesObject.put("amount", paymentDetail.getAmount());
                chargesObject.put("fee_amount", 0);
                chargesObject.put("description", paymentDetail.getDecription());
                chargesObject.put("account_id", config.accountId);
                chargesObject.put("secreate_key", config.accessKey);

                if (config.sandbox) {
                    chargesObject.put("environment", "sandbox");
                }

                requestPayload.put("CardDetails", cardObject);
                requestPayload.put("ChargeDetails", chargesObject);
        } catch (JSONException e) {
            callback.onCompletion(null, new Error(e.getLocalizedMessage()));
            return;
        }

        HashMap headers = new HashMap();

        client.post("/v1/charge/confirm_charge",
                requestPayload,
                headers, new RestClient.RestClientCallback() {
                    @Override
                    public void completion(Error err, JSONObject response) {
                        if (err != null) {
                            callback.onCompletion(null, err);
                        } else {
                            Boolean status = false;
                            try {
                                status = response.getBoolean("status");
                                if (status != null) {
                                    callback.onCompletion(status, null);
                                } else {
                                    callback.onCompletion(null, new Error("Failed to parse JSON " + response.toString()));
                                }
                            } catch (JSONException e) {
                                callback.onCompletion(null, new Error("Failed to parse JSON " + response.toString()));
                            }
                        }
                    }
                });
    }
}
