package inappbillinghack.tum.de.workshop;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by m.hesse on 14.05.2015.
 */
public class BuyIntent extends Activity {
    public static final String BUY_INTENT = "inappbillinghack.tum.de.workshop.buyhack";
    public static final String EXTRA_PACKAGENAME = "HACKPACKAGE";
    public static final String EXTRA_PRODUCT_ID = "hackedProduct";
    public static final String EXTRA_DEV_PAYLOAD = "devload";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("HACK", "I'm there");
    }
}