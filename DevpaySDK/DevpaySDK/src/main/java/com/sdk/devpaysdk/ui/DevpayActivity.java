package com.sdk.devpaysdk.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.sdk.devpaysdk.DevpayClient;
import com.sdk.devpaysdk.R;
import com.sdk.devpaysdk.models.BillingAddress;
import com.sdk.devpaysdk.models.Card;
import com.sdk.devpaysdk.models.PaymentDetail;

public class DevpayActivity extends AppCompatActivity {

    public static final String ConfigKey = "config";
    public static final String AmountKey = "amount";
    public static final String CustomPayActionTextKey = "CustomPayActionText";
    public static final String PaymentIntentResultKey = "PaymentIntentResultKey";

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Button payBtn = (Button) findViewById(R.id.devpay_pay_btn);

        // Set custom text
        String payActionText = (String) getIntent().getStringExtra(CustomPayActionTextKey);
        if (payActionText != null && !payActionText.isEmpty()) {
            payBtn.setText(payActionText);
        }

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePayment();
            }
        });

    }

    public void showLoadingDialog() {
        if (progress == null) {
            progress = new ProgressDialog(this);
            progress.setTitle("Loading ...");
        }
        progress.show();
    }

    public void dismissLoadingDialog() {

        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }

    void initiatePayment() {

        // Card details
        EditText cardNumberTf = (EditText) findViewById(R.id.card_number);
        EditText expiryTf = (EditText) findViewById(R.id.expiry);
        EditText cvvTf = (EditText) findViewById(R.id.cvv);

        // User details
        EditText nameTf = (EditText) findViewById(R.id.name);
        EditText streetTf = (EditText) findViewById(R.id.street);
        EditText cityTf = (EditText) findViewById(R.id.city);
        EditText zipTf = (EditText) findViewById(R.id.zip);
        EditText stateTf = (EditText) findViewById(R.id.state);
        EditText countryTf = (EditText) findViewById(R.id.country);

        if (cardNumberTf.getText().toString().length() <= 0 ||
                expiryTf.getText().toString().length() <= 0 ||
                cvvTf.getText().toString().length() <= 0 ||
                nameTf.getText().toString().length() <= 0 ||
                streetTf.getText().toString().length() <= 0 ||
                cityTf.getText().toString().length() <= 0 ||
                zipTf.getText().toString().length() <= 0 ||
                stateTf.getText().toString().length() <= 0 ||
                countryTf.getText().toString().length() <= 0) {
            showAlert("Please add all the details correctly");
            return;
        }

        Integer amount = (Integer) getIntent().getSerializableExtra(AmountKey);

        if (expiryTf.getText().toString().split("/").length < 1) {
            showAlert("Please add all the details correctly");
            return;
        }
        String expiryMoth = expiryTf.getText().toString().split("/")[0];
        String expiryYear = expiryTf.getText().toString().split("/")[1];
        String cvv = cvvTf.getText().toString();
        Card card = new Card(cardNumberTf.getText().toString(),
                expiryMoth, expiryYear, cvv);
        BillingAddress address = new BillingAddress(streetTf.getText().toString(),
                cityTf.getText().toString(),
                zipTf.getText().toString(),
                stateTf.getText().toString(),
                countryTf.getText().toString());

        PaymentDetail paymentDetail = new PaymentDetail("Jnix-Android",
                amount,
                card,
                address);

        DevpayClient.Config config = (DevpayClient.Config) getIntent().getSerializableExtra(ConfigKey);

        DevpayClient client = new DevpayClient(this, config);

        DevpayActivity.this.showLoadingDialog();

        client.confirmPayment(paymentDetail, new DevpayClient.DevPayClientCallback() {

            @Override
            public void onCompletion(Boolean status, Error error) {
                DevpayActivity.this.dismissLoadingDialog();
                if (error != null) {
                    showAlert(error.getLocalizedMessage());
                }

                if (status != null) {
                    Intent actIntent = new Intent();
                    actIntent.putExtra(PaymentIntentResultKey, status);
                    setResult(RESULT_OK, actIntent);
                    finish();
                }
            }
        });

    }

    private void showAlert(String msg) {

        new AlertDialog.Builder(this)
                .setTitle("Message")
                .setMessage(msg)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
