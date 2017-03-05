package mobi.tarantino.stub.auto.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.telephony.SmsMessage;

import java.util.ArrayList;
import java.util.List;

/**

 */

public class SmsInterceptor extends BroadcastReceiver {

    private SmsCodeListener listener;
    @NonNull
    private List<String> senderNameList = new ArrayList<>();

    public SmsInterceptor() {
        super();
        senderNameList.add("mobi_wallet".toUpperCase());
    }

    public void onReceive(Context context, @NonNull Intent intent) {
        Bundle myBundle = intent.getExtras();
        SmsMessage[] messages;

        if (myBundle != null && myBundle.containsKey("pdus")) {
            Object[] pdus = (Object[]) myBundle.get("pdus");
            if (pdus != null) {
                messages = new SmsMessage[pdus.length];

                for (int i = 0; i < messages.length; i++) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String format = myBundle.getString("format");
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                    } else {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
                    String from = messages[i].getOriginatingAddress();
                    if (senderNameList.contains(from.toUpperCase())) {
                        parseMobiSms(messages[i].getMessageBody());
                    }
                }
            }
        }
    }

    private void parseMobiSms(@NonNull String messageBody) {
        String code = messageBody.replaceAll("\\D", "");
        if (isNumber(code)) {
            smsCodeIntercept(code);
        }
    }

    private boolean isNumber(String codePart) {
        try {
            return Integer.MAX_VALUE >= Integer.parseInt(codePart);
        } catch (Exception ignore) {
        }
        return false;
    }

    public void smsCodeIntercept(String smsCode) {
        listener.onSmsCodeIntercept(smsCode);
    }

    public void setSmsCodeListener(SmsCodeListener listener) {
        this.listener = listener;
    }

    public interface SmsCodeListener {
        void onSmsCodeIntercept(String smsCode);
    }
}
