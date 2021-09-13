package com.sdk.devpaysdk.models;

import org.json.JSONObject;

import java.util.HashMap;

public class PaymentDetail {


    Integer amount;

    public Integer getAmount() {
        return amount;
    }


    public String getName() {
        return name;
    }
    public String getDecription() {
        return decription;
    }

    public BillingAddress getBillingAddress() {
        return billingAddress;
    }

    public Card getCard() {
        return card;
    }

    public HashMap<String, String> getMetaData() {
        return metaData;
    }

    String decription;
    String name;
    BillingAddress billingAddress;
    Card card;

    public HashMap<String, String> metaData = new HashMap<>();

    public PaymentDetail(String name,
                         Integer amount,
                         Card card,
                         BillingAddress billingAddress) {
        this.amount = amount;
        this.name = name;
        this.billingAddress = billingAddress;
        this.card = card;
    }

}
