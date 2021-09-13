package com.sdk.devpaysdk;

import android.content.Context;

import com.sdk.devpaysdk.models.PaymentDetail;

import java.io.Serializable;
import java.util.HashMap;

public class DevpayClient {

    Context context;
    Config config;
    PaymentManager manager;

    DevPayClientCallback callback;

    public interface DevPayClientCallback {
        public void onCompletion(Boolean success, Error error);
    }

    public static class Config implements Serializable {
        String accountId, accessKey;
        public boolean sandbox = false;
        public boolean debug = false;

        public Config(String accountId,
                      String accessKey) {
            this.accountId = accountId;
            this.accessKey = accessKey;
        }
    }

    public DevpayClient(Context context, Config config) {
        this.config = config;
        this.context = context;
        RestClient restClient = paymentManagerRestClient();
        manager = new PaymentManager(config,restClient);
    }

    public void confirmPayment(PaymentDetail paymentDetail,
                               DevPayClientCallback callback) {

        this.callback = callback;
        confirmPayment(paymentDetail);
    }

    private void confirmPayment(PaymentDetail paymentDetail) {
        manager.confirmPayment(paymentDetail,
                new PaymentManager.Callback() {
                    @Override
                    public void onCompletion(Boolean status, Error error) {
                        DevpayClient.this.callback.onCompletion(status,
                                error);
                    }
                });
    }

    RestClient paymentManagerRestClient() {
        HashMap headers = new HashMap();
        headers.put("Content-Type", "application/json");

        String baseURL = "https://api.devpay.io";
        RestClient client = new RestClient(context, headers, baseURL);
        client.debug = config.debug;
        return client;
    }
}
