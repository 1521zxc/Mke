package com.example.epay.getui;

/**
 * Created by liujin on 2018/4/22.
 */

public class CustomerApp {
        //extends AbsBaseApp {
//
//    private static CustomerApp _instance;
//    private AppStatusTracker appStatusTracker;
//
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        _instance = this;
//        appStatusTracker = new AppStatusTracker(this);
//    }
//
//    public synchronized void sendMessage(String msg) {
//        messageCount++;
//        if (System.currentTimeMillis() - msgTime < 5000) {//推送间隔小于5s，只连续响声三次
//            sendCount++;
//        }
//        if (msgCount < 3) {
//            msgTime = System.currentTimeMillis();
//            msgCount++;
//        } else {
//            if (noNotifyTime == 0) {
//                noNotifyTime = System.currentTimeMillis();
//            }
//        }
//        if (System.currentTimeMillis() - noNotifyTime > 20 * 1000 && noNotifyTime != 0) {
//            msgCount = 0;
//            noNotifyTime = 0;
//            sendCount = 0;
//        }
//        MessageContent messageContent = new Gson().fromJson(msg, MessageContent.class);
//        messageContent.setAppContent(msg);
//        if (messageContent.getMessageType() == 2) {
//            NewsBean newsBean = NewsUtils.getNewsBean(messageContent);
//            if (newsBean != null) {
//                long id = CustomerApp.getInstance().getDaoSession().getNewsBeanDao().insertOrReplace(newsBean);//保存消息到数据库（自己集成greendao）
//                newsBean.set_id(id);
//                Intent notifyIntent;
//                if (AppStatusManager.getInstance().getAppStatus() == AppStatusConstant.STATUS_FORCE_KILLED) {
//                    // no application live
//                    notifyIntent = new Intent(this, SplashActivity.class);
//                    sendNotification(newsBean.getNotifyContent(), NewsUtils.getNewsNotifyDraw(newsBean, -1), notifyIntent, newsBean);
//                } else if (appStatusTracker.isForGround()) {
//                    // app is live
//                    if (!isNewsMeOpen) {
//                        if (newsBean.isMe()) {
//                            startHomeActivity(newsBean);
//                        } else {
//                            if (!isNewsOtherOpen) {
//                                startMessageActivity(newsBean);
//                            }
//                        }
//                    }
//                } else {
//                    startHomeActivity(newsBean);
//                }
//                // send news bean to view
//                sendNewsBeanToView(newsBean);
//            }
//        }
//    }
//
//
//    /**
//     * 发送新消息
//     *
//     * @param newsBean 新消息
//     */
//    private void sendNewsBeanToView(NewsBean newsBean) {
//        if (getCurrentUser() != null) {
//            if (newsBean.isAlarm()) {
//                SPHelper.write(this, ConstantStr.USER_INFO, getCurrentUser().getUserId() + ConstantStr.NEWS_ALARM_STATE, true);
//            } else if (newsBean.isWork()) {
//                SPHelper.write(this, ConstantStr.USER_INFO, getCurrentUser().getUserId() + ConstantStr.NEWS_WORK_STATE, true);
//            } else if (newsBean.isEnterprise()) {
//                SPHelper.write(this, ConstantStr.USER_INFO, getCurrentUser().getUserId() + ConstantStr.NEWS_NOTIFY_STATE, true);
//            }
//        }
//        if (newsBean.isMe()) {
//            Intent intent = new Intent();
//            intent.setAction(BroadcastAction.MESSAGE_UN_READ_COUNT);
//            sendBroadcast(intent);
//        }
//        if (newsBean.isEnterprise() || newsBean.isAlarm() || newsBean.isWork()) {
//            Intent intent = new Intent();
//            intent.setAction(BroadcastAction.MESSAGE_UN_READ_STATE);
//            sendBroadcast(intent);
//        }
//        Intent intentBean = new Intent();
//        intentBean.setAction(BroadcastAction.NEWS_MESSAGE);
//        intentBean.putExtra(ConstantStr.KEY_BUNDLE_OBJECT, newsBean);
//        sendBroadcast(intentBean);
//    }
//
//    public void sendNotification(String message, @DrawableRes int drawRes, @Nullable Intent intent, NewsBean newsBean) {
//        int notifyId;
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        if (notificationManager == null) {
//            return;
//        }
//        if (newsBean.getNotifyId() == 1) {
//            notifyId = 1;
//        } else {
//            notifyId = messageCount;
//        }
//        Notification.Builder builder;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationChannel channel = notificationManager.getNotificationChannel(ConstantStr.NOTIFICATION_CHANNEL_ID);
//            if (channel == null) {
//                channel = new NotificationChannel(ConstantStr.NOTIFICATION_CHANNEL_ID
//                        , ConstantStr.NOTIFICATION_CHANNEL_NAME
//                        , NotificationManager.IMPORTANCE_HIGH);
//                channel.setDescription(ConstantStr.NOTIFICATION_CHANNEL_DESCRIPTION);
//                channel.enableLights(true);
//                channel.enableVibration(true);
//                channel.setShowBadge(true);
//                channel.setLightColor(Color.GREEN);
//                notificationManager.createNotificationChannel(channel);
//            }
//            builder = new Notification.Builder(this, ConstantStr.NOTIFICATION_CHANNEL_ID);
//        } else {
//            builder = new Notification.Builder(this);
//            builder.setPriority(Notification.PRIORITY_HIGH);
//        }
//        PendingIntent pendingIntent;
//        if (intent == null) {
//            intent = new Intent(this, SplashActivity.class);
//        }
//        if (sendCount < 3) {
//            builder.setDefaults(Notification.DEFAULT_ALL);//设置默认的声音和震动
//        }
//        pendingIntent = PendingIntent.getActivity(this, messageCount, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setSmallIcon(R.drawable.evpro_notif)
//                .setAutoCancel(true)
//                .setWhen(System.currentTimeMillis())
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), drawRes))
//                .setContentTitle(getString(R.string.app_name))
//                .setContentText(message)
//                .setContentIntent(pendingIntent);
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            builder.setColor(getResources().getColor(R.color.colorPrimary));
//        }
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
//            builder.setGroupSummary(true);
//            builder.setGroup(ConstantStr.NOTIFY_GROUP);
//        }
//        Notification notification = builder.build();
//        notificationManager.notify(notifyId, notification);
//    }
//
//    public void cleanNotify(int notifyId) {
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        if (notificationManager != null) {
//            notificationManager.cancel(notifyId);
//        }
//    }
}
