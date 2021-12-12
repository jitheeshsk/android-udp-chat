package com.hiskysat.udpchat.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LifecycleService;

import com.hiskysat.udpchat.MainActivity;
import com.hiskysat.udpchat.R;
import com.hiskysat.udpchat.data.NotificationMessage;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChatService extends LifecycleService {

    @Inject
    ChatViewModel chatViewModel;

    private static final int ID_CHAT_NOTIFICATION = 1001;
    private static final int REQUEST_CODE = 100;

    private static final String ID_NOTIFICATION_CHANNEL = "CHAT_CHANNEL";
    private static final int ID_NOTIFICATION_CHANNEL_NAME = R.string.chat_service_channel_name;

    private static final String ID_MESSAGE_CHANNEL = "MESSAGE_CHANNEL";
    private static final int ID_MESSAGE_CHANNEL_NAME = R.string.message_channel_name;

    private static final String ACTION_START_SERVICE = ChatService.class.getName() + ".ACTION_START_SERVICE";
    private static final String ACTION_STOP_SERVICE = ChatService.class.getName() + "ACTION_STOP_SERVICE";

    private boolean isStarted;

    public static void startService(Context context) {
        Intent intent = new Intent(context, ChatService.class);
        intent.setAction(ACTION_START_SERVICE);
        context.startService(intent);
    }

    public static void stopService(Context context) {
        Intent intent = new Intent(context, ChatService.class);
        intent.setAction(ACTION_STOP_SERVICE);
        context.stopService(intent);
    }

    public ChatService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        chatViewModel.getAccount().observe(this, account -> updateNotification(getString(R.string.chat_service_setup_complete,
                account.getIpAddress(), String.valueOf(account.getPort()))));
        chatViewModel.getMessageReceived().observe(this, notificationMessageEvent -> {
            NotificationMessage message = notificationMessageEvent.getContentIfNotHandled();
            if (message != null) {
               Notification notification = getMessageNotification(message, ID_MESSAGE_CHANNEL);
               updateNotification(message.getChatId().intValue(), notification);
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            stopService();
            isStarted = false;
            return super.onStartCommand(null, flags, startId);
        }

        String action = intent.getAction();

        if (TextUtils.isEmpty(action)) {
            stopService();
            isStarted = false;
            return super.onStartCommand(null, flags, startId);
        }
        if ((action.equals(ACTION_START_SERVICE)) && !isStarted) {
            createNotificationChannelCompat(ID_MESSAGE_CHANNEL, getString(ID_MESSAGE_CHANNEL_NAME));
            String channelId = createNotificationChannelCompat(ID_NOTIFICATION_CHANNEL,
                    getString(ID_NOTIFICATION_CHANNEL_NAME));
            startForeground(ID_CHAT_NOTIFICATION, getServiceNotification(
                    getString(R.string.chat_service_setup_in_progress), channelId));
            chatViewModel.start();
            isStarted = true;
        } else if (action.equals(ACTION_STOP_SERVICE) && isStarted) {
            stopService();
            isStarted = false;
        }

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatViewModel != null) {
            chatViewModel.destroy();
        }
    }

    public void stopService() {
        stopForeground(true);
        stopSelf();
    }

    private void updateNotification(String content) {
        Notification notification = getServiceNotification(content, ID_NOTIFICATION_CHANNEL);
        NotificationManagerCompat.from(this)
                .notify(ID_CHAT_NOTIFICATION, notification);
    }

    private void updateNotification(int notificationId, Notification notification) {
        NotificationManagerCompat.from(this)
                .notify(notificationId, notification);
    }

    private Notification getMessageNotification(NotificationMessage message, String channelId) {
        return new NotificationCompat.Builder(this, channelId)
                .setContentTitle(getString(R.string.msg_new_message_received, message.getIpAddress()))
                .setContentText(message.getContent())
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(getMainActivityIntent())
                .build();
    }

    private Notification getServiceNotification(String content, String channelId) {

        return new NotificationCompat.Builder(this, channelId)
                .setContentTitle(getText(R.string.chat_service_title))
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOngoing(true)
                .setContentIntent(getMainActivityIntent())
                .build();
    }

    private PendingIntent getMainActivityIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(this, REQUEST_CODE, intent,
                getIntentFlag());
    }

    private int getIntentFlag() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT;
        } else {
            return PendingIntent.FLAG_UPDATE_CURRENT;
        }
    }

    private String createNotificationChannelCompat(String channelId, String channelName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = createNotificationChannel(channelId, channelName);
        } else {
            channelId = "";
        }
        return channelId;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(String channelId, String channelName) {
        NotificationChannel channel = new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_HIGH);
        channel.setLightColor(Color.BLUE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManagerCompat.from(this)
                .createNotificationChannel(channel);
        return channelId;
    }

}