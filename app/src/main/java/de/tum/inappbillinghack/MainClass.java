package de.tum.inappbillinghack;

import android.os.Bundle;
import android.os.Parcel;
import android.widget.TextView;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import org.apache.commons.lang3.ClassUtils;

import static de.robv.android.xposed.XposedHelpers.*;


public class MainClass implements IXposedHookLoadPackage {
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("com.android.systemui")) {
            findAndHookMethod("com.android.systemui.statusbar.policy.Clock", lpparam.classLoader, "updateClock", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    TextView tv = (TextView) param.thisObject;
                    //tv.setText(new StringBuilder(tv.getText().toString()).reverse().toString());
                    tv.setRotation(((System.currentTimeMillis()/100000)%8)*45);
                }
            });
        } else {
            try {
                ClassUtils.getClass(lpparam.classLoader, "com.android.vending.billing.IInAppBillingService.Stub");
                XposedBridge.log("hooking in package " + lpparam.packageName);

                findAndHookMethod("com.android.vending.billing.IInAppBillingService.Stub", lpparam.classLoader,
                        "onTransact", int.class, Parcel.class, Parcel.class, int.class, new GetBuyIntentHook());

            } catch(ClassNotFoundException e) {}
        }
    }
}