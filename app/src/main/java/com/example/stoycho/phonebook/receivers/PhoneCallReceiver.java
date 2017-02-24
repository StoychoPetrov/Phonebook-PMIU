package com.example.stoycho.phonebook.receivers;

import android.content.Context;

import java.util.Date;

/**
 * Created by stoycho.petrov on 24/02/2017.
 */

public class PhoneCallReceiver extends CallingReceiver {
    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start) {

    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start) {

    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {

    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {

    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {

    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {

    }
}
