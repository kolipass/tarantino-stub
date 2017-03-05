package mobi.tarantino.stub.auto.gcm.gcmHandlerStrategy;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import mobi.tarantino.stub.auto.Consts;
import mobi.tarantino.stub.auto.R;
import mobi.tarantino.stub.auto.analytics.AnalyticReporter;
import mobi.tarantino.stub.auto.feature.dashboard.DashBoardActivity;
import mobi.tarantino.stub.auto.model.database.dbo.ArticleDBO;

import static mobi.tarantino.stub.auto.Consts.Notification.CATEGORY_NEW_LAW;
import static mobi.tarantino.stub.auto.Consts.Notification.ID_LAW;
import static mobi.tarantino.stub.auto.Consts.Notification.KEY_BODY;

public class LawGcmHandler extends AbstractGcmHandlerStrategy {

    public LawGcmHandler(Context context) {
        super(context);
    }

    @Override
    protected void createNotification(Bundle bundle) {
        String body = bundle.getString(KEY_BODY);
        String message = context.getString(R.string.new_law);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_notification_tray)
                .setContentTitle(context.getString(R.string.mobi_auto))
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Intent intent = new Intent(context, DashBoardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setAction(CATEGORY_NEW_LAW);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, ID_LAW, intent, PendingIntent
                        .FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManagerCompat.from(context).notify(ID_LAW, notificationBuilder.build());
    }

    @Override
    protected void processData(Bundle bundle) {
        analyticReporter.showNotificationEvent(AnalyticReporter.SCREEN_UNKNOWN, CATEGORY_NEW_LAW);
        String title = bundle.getString(Consts.Notification.KEY_TITLE);
        String preview = bundle.getString(Consts.Notification.KEY_PREVIEW);
        String url = bundle.getString(Consts.Notification.KEY_URL);
        int id = Integer.parseInt(bundle.getString(Consts.Notification.KEY_ID));
        ArticleDBO articleDBO = new ArticleDBO(id, url, title, preview, ArticleDBO.TYPE_LAW);
        model.saveLaw(articleDBO);
    }
}
