# Devpay Android SDK
Android SDK for Devpay Payment Gateway Get your API Keys at https://devpay.io, works with Java and Kotlin based applications

## Integration
Add the following in the root build.gradle.

```gradle
allprojects {
  repositories {
     ...
     maven { url 'https://jitpack.io' }
  }
}
```

Add implementation entry `DevpaySDK` in your app’s build.gradle with the following dependency.

```gradle
dependencies {
  implementation 'com.github.DevpayInc:DevpaySDK:1.0.0'
}
```

## Make payment
### Using built-in UI
#### Showing the payment UI
Devpay SDK provided handy UI to get payment inputs, please refer below code.
```java
DevpayClient.Config config = new DevpayClient.Config("ACC_ID",
        "SHAREABLE_KEY",
        "ACCESS_KEY");
config.debug = true;
config.sandbox = true;

Intent intent = new Intent(MainActivity.this, DevpayActivity.class);
intent.putExtra(DevpayActivity.ConfigKey, config);
intent.putExtra(DevpayActivity.AmountKey, Integer.parseInt(amountTf.getText().toString()));
intent.putExtra(DevpayActivity.CurrencyKey, PaymentDetail.Currency.USD);

startActivityForResult(intent, requestCode);


```

#### Getting the result
Use onActivityResult callback to know about the payment confirmation
```java
 public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.requestCode) {
            if (resultCode == RESULT_OK) {
                // Get the details about the payment intent
                Boolean status = (Boolean) data.getSerializableExtra(DevpayActivity.PaymentIntentResultKey);
            }
        }
    }
```

> Amount & Currency are mandatory inputs, please make sure its provided correctly

#### Set Custom Pay title
By default inbuilt UI shows pay button text as 'PAY', you can change the text as required. Below snippets gives an example to do so. 
```java
intent.putExtra(DevpayActivity.CustomPayActionTextKey, "PAY <AMOUNT> $");
```


### Using Devpay APIs with your own UI
In-case you want to use own UI to get payment details from the user, you are free to do so. Use below APIs to create payment details form your custom UI & confirm the payment.
```java
Card card = new Card("XXXXYYYYXXXXYYYY",
                "MM", 
                "YYYY", 
                "123");
BillingAddress address = new BillingAddress("street",
        "city",
        "zipTf",
        "state",
        "country");

PaymentDetail paymentDetail = new PaymentDetail("name",
        <amount>,
        card,
        address);

DevpayClient.Config config = ...;

DevpayClient client = new DevpayClient(this, config);
client.confirmPayment(paymentDetail, new DevpayClient.DevPayClientCallback() {

    @Override
    public void onCompletion(Boolean status, Error error) {

        if (error != null) {
            // Read the error
        }

        if (status != null) {
            // Payment successful
        }
    }
});
```
