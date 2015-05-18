package de.tum.inappbillinghack;

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

        findAndHookMethod("com.android.vending.billing.IInAppBillingService.Stub", lpparam.classLoader,
                "getBuyIntent", int.class, String.class, String.class, String.class, String.class, new GetBuyIntentHook());
    }
}