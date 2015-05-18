package de.tum.inappbillinghack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Random;

/**
 * Created by m.hesse on 14.05.2015.
 */
public class BuyIntent extends Activity {
    public static final String BUY_INTENT = "de.tum.inappbillinghack.buyintent";
    public static final String EXTRA_PACKAGENAME = "hackedPackage";
    public static final String EXTRA_PRODUCT_ID = "hackedProduct";
    public static final String EXTRA_DEV_PAYLOAD = "hackedDevload";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("INAPPBILLINGHACK", "Hacked Buy Intent executed");


        Intent result = new Intent();
        result.putExtra(IabHelper.RESPONSE_CODE, IabHelper.BILLING_RESPONSE_RESULT_OK);
        result.putExtra(IabHelper.INAPP_DATA_SIGNATURE, "won't validate");

        Random r = new Random();
        String data = "{"
                + "\"autoRenewing\": true,"
                + "\"orderId\": \"" + r.nextLong() + "\","
                + "\"packageName\": \"" + getIntent().getStringExtra(EXTRA_PACKAGENAME) + "\","
                + "\"productId\": \"" + getIntent().getStringExtra(EXTRA_PRODUCT_ID) + "\","
                + "\"purchaseTime\": " + System.currentTimeMillis() + ","
                + "\"purchaseState\": 0,"
                + "\"developerPayload\": " + getIntent().getStringExtra(EXTRA_DEV_PAYLOAD) + ","
                + "\"purchaseToken\": \"" + r.nextLong() + "\"" +
                "}";

        result.putExtra(IabHelper.RESPONSE_INAPP_PURCHASE_DATA, data);
        setResult(RESULT_OK, result);

        // add delay here
        finish();
    }
}