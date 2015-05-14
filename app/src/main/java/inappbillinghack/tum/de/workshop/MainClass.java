package inappbillinghack.tum.de.workshop;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.*;


public class MainClass implements IXposedHookLoadPackage {
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.android.systemui"))
            return;
        XposedBridge.log("Loaded app: " + lpparam.packageName);

        findAndHookMethod("com.android.systemui.statusbar.policy.Clock", lpparam.classLoader, "updateClock", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                TextView tv = (TextView) param.thisObject;
                tv.setText(new StringBuilder(tv.getText().toString()).reverse().toString());
            }
        });

        findAndHookMethod("com.android.vending.billing.IInAppBillingService$Stub$Proxy", lpparam.classLoader, "getBuyIntent", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("hook");
                Bundle bundle = new Bundle();
                bundle.putInt(IabHelper.RESPONSE_CODE, IabHelper.BILLING_RESPONSE_RESULT_OK);

                Context c = new BuyIntent();
                PendingIntent pendingIntent;
                Intent intent = new Intent();
                intent.setClass(c, BuyIntent.class);
                intent.setAction(BuyIntent.BUY_INTENT);
                intent.putExtra(BuyIntent.EXTRA_PACKAGENAME, (String) param.args[1]);
                intent.putExtra(BuyIntent.EXTRA_PRODUCT_ID, (String) param.args[2]);
                intent.putExtra(BuyIntent.EXTRA_DEV_PAYLOAD,(String) param.args[4]);
                pendingIntent = PendingIntent.getActivity(c, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                bundle.putParcelable(IabHelper.RESPONSE_BUY_INTENT, pendingIntent);
                param.setResult(bundle);
            }
        });
    }
}