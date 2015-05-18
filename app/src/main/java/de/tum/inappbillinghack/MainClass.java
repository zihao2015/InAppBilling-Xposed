package de.tum.inappbillinghack;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Parcel;
import android.widget.TextView;

import com.android.vending.billing.IInAppBillingService;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Method;

import static de.robv.android.xposed.XposedHelpers.*;


public class MainClass implements IXposedHookLoadPackage {
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("com.android.systemui")) {
            findAndHookMethod("com.android.systemui.statusbar.policy.Clock", lpparam.classLoader, "updateClock", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    TextView tv = (TextView) param.thisObject;
                    //tv.setText(new StringBuilder(tv.getText().toString()).reverse().toString());
                    tv.setRotation(180);
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
        /*XposedBridge.log("Loaded app: " + lpparam.packageName);



        findAndHookMethod("com.android.vending.billing.IInAppBillingService.Stub", lpparam.classLoader,
                "onTransact", int.class, Parcel.class, Parcel.class, int.class, new GetBuyIntentHook());

        /*Class<?> stub = IInAppBillingService.class.getDeclaredClasses()[0].getDeclaredClasses()[0];
        Method[] m = stub.getDeclaredMethods();
        XposedBridge.log("methods:");
        for (Method me:m) {
            XposedBridge.log("method: " + me.getName());
            if (me.getName().equalsIgnoreCase("getBuyIntent")) {
                XposedBridge.log("hook here..");
                XposedBridge.hookMethod(me, new GetBuyIntentHook());
            }
        }*/
    }
}