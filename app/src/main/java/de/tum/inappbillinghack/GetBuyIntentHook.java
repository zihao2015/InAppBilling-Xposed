package de.tum.inappbillinghack;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

public class GetBuyIntentHook extends XC_MethodHook {
    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        XposedBridge.log("hook");

        Bundle bundle = new Bundle();
        bundle.putInt(IabHelper.RESPONSE_CODE, IabHelper.BILLING_RESPONSE_RESULT_OK);

        Context c = new BuyIntent();

        // create our own intent
        Intent intent = new Intent();
        intent.setClass(c, BuyIntent.class);
        intent.setAction(BuyIntent.BUY_INTENT);
        intent.putExtra(BuyIntent.EXTRA_PACKAGENAME, (String) param.args[1]);
        intent.putExtra(BuyIntent.EXTRA_PRODUCT_ID, (String) param.args[2]);
        intent.putExtra(BuyIntent.EXTRA_DEV_PAYLOAD,(String) param.args[4]);

        bundle.putParcelable(IabHelper.RESPONSE_BUY_INTENT, PendingIntent.getActivity(c, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        // replace original intent with our intent
        param.setResult(bundle);
    }
}
