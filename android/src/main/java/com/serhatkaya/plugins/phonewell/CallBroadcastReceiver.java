package com.serhatkaya.plugins.phonewell;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.core.app.ActivityCompat;

public class CallBroadcastReceiver extends BroadcastReceiver {

    PhoneWell.CallStateChangeListener callStateChangeListener = null;
    private final PhoneState currentPhoneState = new PhoneState();
    private int prevState = TelephonyManager.CALL_STATE_IDLE;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("CallBroadcastReceiver", intent.getAction());

        // Handle phone state changed
        if (intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            int callState = tm.getCallState();
            if (callState == this.prevState && callState != TelephonyManager.CALL_STATE_RINGING) {
                return;
            }

            String callerNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            if (callState == TelephonyManager.CALL_STATE_RINGING && callerNumber == null) {
                return;
            }
            checkPhoneState(callState, intent);
            callStateChangeListener.onCallStateChanged();
        }
    }

    private void checkPhoneState(int state, Intent intent) {
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                this.currentPhoneState.setCallActive(false);
                this.currentPhoneState.setCallState("IDLE");
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                this.currentPhoneState.setCallActive(true);
                this.currentPhoneState.setCallState("RINGING");
                String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                if (incomingNumber != null) {
                    this.currentPhoneState.setIncomingNumber(incomingNumber);
                }
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                if (prevState == TelephonyManager.CALL_STATE_RINGING) {
                    this.currentPhoneState.setCallActive(true);
                    this.currentPhoneState.setCallState("ON_CALL");
                } else {
                    this.currentPhoneState.setCallActive(false);
                    this.currentPhoneState.setCallState("ON_HOLD");
                }
                this.currentPhoneState.setOutgoingNumber(intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER));
                break;
        }
        this.prevState = state;
    }

    public void setCallStateChangeListener(PhoneWell.CallStateChangeListener callStateChangeListener) {
        this.callStateChangeListener = callStateChangeListener;
    }

    public PhoneState getCurrentPhoneState() {
        return this.currentPhoneState;
    }
}
