package cn.way.lbs.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

public final class ToastUtils {
    private static Toast sToast;
    private static Handler sHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (sToast != null) {
                sToast.cancel();
            }

            String message = (String) msg.obj;
            if (TextUtils.isEmpty(message)) {
                return;
            }

            sToast = Toast.makeText(Utils.getApplication(), message, msg.arg2);
            sToast.show();
        }
    };

    public static void toast(String message, int duration) {
        sHandler.sendMessage(sHandler.obtainMessage(0, 0, duration, message));
    }

    public static void toast(String message) {
        toast(message, Toast.LENGTH_SHORT);
    }
}
