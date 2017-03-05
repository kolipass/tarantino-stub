package mobi.tarantino.stub.auto.gcm.gcmHandlerStrategy;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import mobi.tarantino.stub.auto.R;
import mobi.tarantino.stub.auto.analytics.AnalyticReporter;
import mobi.tarantino.stub.auto.feature.dashboard.DashBoardActivity;

import static mobi.tarantino.stub.auto.Consts.Notification.CATEGORY_NEW_FINE;
import static mobi.tarantino.stub.auto.Consts.Notification.ID_FINE;

public class FineGcmHandler extends AbstractGcmHandlerStrategy {

    public FineGcmHandler(Context context) {
        super(context);
    }

    @Override
    protected void createNotification(@NonNull Bundle bundle) {
        String message = context.getString(R.string.new_fine);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_notification_tray)
                .setContentTitle(context.getString(R.string.mobi_auto))
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Intent intent = new Intent(context, DashBoardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction(CATEGORY_NEW_FINE);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, ID_FINE, intent, PendingIntent
                        .FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManagerCompat.from(context).notify(ID_FINE, notificationBuilder.build());
    }

    @Override
    protected void processData(Bundle bundle) {
        analyticReporter.showNotificationEvent(AnalyticReporter.SCREEN_UNKNOWN, CATEGORY_NEW_FINE);
        preferencesManager.setNewFinesCount(preferencesManager.getNewFinesCount() + 1);
    }
}
