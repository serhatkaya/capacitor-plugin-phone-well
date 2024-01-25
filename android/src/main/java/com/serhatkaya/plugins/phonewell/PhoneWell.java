package com.serhatkaya.plugins.phonewell;

import android.content.Context;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PhoneWell {

    private Context context;

    public PhoneWell(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return this.context;
    }

    public String start(String phone) {
        return phone;
    }

    interface CallStateChangeListener {
        void onCallStateChanged();
    }

    @Nullable
    private CallStateChangeListener callStateChangeListener;

    public void setCallStateChangeListener(@Nullable CallStateChangeListener listener) {
        this.callStateChangeListener = listener;
    }

    private static CallBroadcastReceiver receiver = null;

    public String echo(String value) {
        Log.i("Echo", value);
        return value;
    }

    public void enableDetectCall() {
        Log.i("enableDetectCall", "Called");
        receiver = new CallBroadcastReceiver();
        receiver.setCallStateChangeListener(callStateChangeListener);
    }

    public void disableDetectCall() {
        Log.i("disableDetectCall", "Called");
        receiver.abortBroadcast();
        receiver = null;
    }

    public PhoneState getCurrentPhoneState() {
        return receiver.getCurrentPhoneState();
    }

    public void startDetection(@NonNull AppCompatActivity activity) {
        IntentFilter filter = new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        activity.registerReceiver(receiver, filter);
    }
}
