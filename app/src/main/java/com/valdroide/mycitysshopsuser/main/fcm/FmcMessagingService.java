package com.valdroide.mycitysshopsuser.main.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.shop.DrawWinner;
import com.valdroide.mycitysshopsuser.main.splash.ui.SplashActivity;
import com.valdroide.mycitysshopsuser.utils.Utils;

public class FmcMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            String id_city = remoteMessage.getData().get("id_city");
            String id = remoteMessage.getData().get("id");
            int id_city_int = Integer.parseInt(id_city);

            if (id_city_int == Utils.getIdCity(getApplicationContext())) {

                String title = "", message = "", url_shop = "", id_draw = "";
                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                if (id.equalsIgnoreCase("0")) {//isDraw
                    title = remoteMessage.getData().get("name");
                    message = remoteMessage.getData().get("message");

                    if (title != null && !title.isEmpty() && message != null && !message.isEmpty()) {

                        intent.putExtra("draw", true);
                        intent.putExtra("name", title);
                        intent.putExtra("messasge", message);

                        fillNotification(intent, title, message);
                    }
                } else if (id.equalsIgnoreCase("2")) {
                    title = remoteMessage.getData().get("name");
                    message = remoteMessage.getData().get("message");
                    id_draw = remoteMessage.getData().get("id_draw");
                    if (title != null && !title.isEmpty() && message != null &&
                            !message.isEmpty() && id_draw != null && !id_draw.isEmpty()) {

                        DrawWinner drawWinner = new DrawWinner(id_city_int, Integer.parseInt(id_draw));
                        drawWinner.save();

                        intent.putExtra("draw", true);
                        intent.putExtra("name", title);
                        intent.putExtra("messasge", message);

                        fillNotification(intent, title, message);
                    }
                } else {
                    title = remoteMessage.getData().get("title");
                    message = remoteMessage.getData().get("body");
                    url_shop = remoteMessage.getData().get("url_shop");

                    if (title != null && !title.isEmpty() && message != null && !message.isEmpty() && url_shop != null && !url_shop.isEmpty()) {
                        intent.putExtra("notification", true);
                        intent.putExtra("title", title);
                        intent.putExtra("messasge", message);
                        intent.putExtra("url_shop", url_shop);

                        fillNotification(intent, title, message);
                    }
                }
            } else {
                if (id.equalsIgnoreCase("2")) {
                    String id_draw = remoteMessage.getData().get("id_draw");
                    if (id_draw != null && !id_draw.isEmpty()) {
                        DrawWinner drawWinner = new DrawWinner(id_city_int, Integer.parseInt(id_draw));
                        drawWinner.save();
                    }
                }
            }
        } finally {
        }
    }

    private void fillNotification(Intent intent, String title, String message) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] pattern = {500, 500, 500, 500, 500};

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_icon_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setLights(Color.BLUE, 1, 1)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}
