package com.example.kunal.socialapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.api.PaymentMethod;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.interfaces.HttpResponseCallback;
import com.braintreepayments.api.internal.HttpClient;
import com.braintreepayments.api.models.PaymentMethodNonce;

import java.util.HashMap;
import java.util.Map;

public class UpgradeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1234;
    final String API_GET_TOKEN = "http://172.16.24.55/Braintree/main.php";
    final String API_CHECK_OUT = "http://172.16.24.55/Braintree/checkout.php";

    String token,amount;
    HashMap<String,String> paramsHash;

    EditText amount_edt;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        amount_edt = findViewById(R.id.amount);
        button = findViewById(R.id.buttonUpgrade);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitPayment();
            }
        });
        new getToken().execute();

    }

    private void submitPayment() {
        DropInRequest dropInRequest = new DropInRequest().clientToken(token);
        startActivityForResult(dropInRequest.getIntent(this),REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){

                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                String strNonce = nonce.getNonce();

                if(!amount_edt.getText().toString().isEmpty()){
                    amount = amount_edt.getText().toString();
                    paramsHash = new HashMap<>();
                    paramsHash.put("amount",amount);
                    paramsHash.put("nonce",strNonce);

                    sendPayments();
                }
                else{
                    Toast.makeText(this, "Enter Valid Amount", Toast.LENGTH_SHORT).show();
                }
            }
            else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            }else{
                Exception exception = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendPayments() {
        RequestQueue requestQueue = Volley.newRequestQueue(UpgradeActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_CHECK_OUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.toString().contains("Successful")){
                            Toast.makeText(UpgradeActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(UpgradeActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if(paramsHash == null){
                    return null;
                }
                Map<String,String> params = new HashMap<>();
                for(String key:paramsHash.keySet()){
                    params.put(key,paramsHash.get(key));
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private class getToken extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpClient client = new HttpClient();
            client.get(API_GET_TOKEN, new HttpResponseCallback() {
                @Override
                public void success(final String responseBody) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            token = responseBody;
                        }
                    });
                }

                @Override
                public void failure(Exception exception) {
                    Toast.makeText(UpgradeActivity.this, exception.toString(), Toast.LENGTH_SHORT).show();

                }
            });
            return null;
        }
    }
}
