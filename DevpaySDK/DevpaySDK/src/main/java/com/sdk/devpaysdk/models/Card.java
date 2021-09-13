package com.sdk.devpaysdk.models;

public class Card {

    public String cardNum;
    public String cvv;
    public String expiryMonth;
    public String expiryYear;

    public Card(String cardNum,
                String expiryMonth,
                String expiryYear,
                String cvv) {

        this.cardNum = cardNum;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.cvv = cvv;
    }

}
