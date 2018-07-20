package com.example.mypc.counterapp.PushNotification;

public class Constants {

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final String PUSH_NOTIFICATIONLOGOUT = "pushNotification_logout";
    public static final String PUSH_NOTIFICATION_DATA = "pushNotification_file";
    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";
    public static final String SHARED_NOTIFY = "notification_data";
}
