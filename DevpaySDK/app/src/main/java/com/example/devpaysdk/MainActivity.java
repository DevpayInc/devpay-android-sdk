package com.example.devpaysdk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.sdk.devpaysdk.DevpayClient;
import com.sdk.devpaysdk.ui.DevpayActivity;


public class MainActivity extends AppCompatActivity {

    static int requestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText amountTf = (EditText) findViewById(R.id.cost_input);
        Button payBtn = (Button) findViewById(R.id.pay_btn);

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DevpayClient.Config config = new DevpayClient.Config("ACC_ID",
                        "ACCESS_KEY");
                config.debug = true;
                config.sandbox = true;

                Intent intent = new Intent(MainActivity.this, DevpayActivity.class);
                intent.putExtra(DevpayActivity.ConfigKey, config);
                intent.putExtra(DevpayActivity.AmountKey, Integer.parseInt(amountTf.getText().toString()));

                startActivityForResult(intent, requestCode);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.requestCode) {
            if (resultCode == RESULT_OK) {
                // Get the details about the payment intent
                Boolean status = (Boolean) data.getSerializableExtra(DevpayActivity.PaymentIntentResultKey);
            }
        }
    }

}