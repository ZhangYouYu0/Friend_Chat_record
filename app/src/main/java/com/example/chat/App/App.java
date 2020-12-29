package com.example.chat.App;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.chat.AddFriendActivity;
import com.example.chat.MainActivity;
import com.example.chat.R;
import com.example.chat.SendActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;

import java.util.Iterator;
import java.util.List;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
// 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(true);
// 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true);
//初始化
        EMClient.getInstance().init(this, options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);


        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
// 如果APP启用了远程的service，此application:onCreate会被调用2次
// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null ||!processAppName.equalsIgnoreCase(this.getPackageName())) {
            Log.e("TAG", "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        initChat();

        EMClient.getInstance().addConnectionListener(new MyConnectionListener());


    }

    private void initChat() {
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactInvited(String username, String reason) {
                //收到好友邀请
                Log.e("TAG", "收到好友请求: ");
             new Thread(new Runnable() {
                 @Override
                 public void run() {
                     NotificationManager systemService1 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                     if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                         NotificationChannel notificationChannel = new NotificationChannel("", "", NotificationManager.IMPORTANCE_DEFAULT);
                         systemService1.createNotificationChannel(notificationChannel);
                     }

                     Intent intent = new Intent(getApplicationContext(), SendActivity.class);
                     intent.putExtra("name",username);
                     intent.putExtra("reason",reason);
                     PendingIntent activities = PendingIntent.getActivities(getApplicationContext(),99, new Intent[]{intent},PendingIntent.FLAG_UPDATE_CURRENT);

                     Notification notification = new NotificationCompat
                             .Builder(getApplicationContext(), "1")
                             .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark_focused)
                             .setContentTitle("这是标题")
                             .setContentText("我是内容，我是mode")
                             .setWhen(System.currentTimeMillis())
                             .setContentIntent(activities)
                             .build();

                     systemService1.notify(100,notification);
                 }
             }).start();

            }

            @Override
            public void onFriendRequestAccepted(String username) {
                Log.e("TAG", "好友请求通过: ");
            }

            @Override
            public void onFriendRequestDeclined(String username) {
                Log.e("TAG", "好友请求不同意: ");

            }

            @Override
            public void onContactDeleted(String username) {
                //被删除时回调此方法
                Log.e("TAG", "好友被删除: ");
            }


            @Override
            public void onContactAdded(String username) {
                //增加了联系人时回调此方法
                Log.e("TAG", "增加一位好友: "+username);
            }
        });


    }


    private String getAppName(int pid) {
            String processName = null;
            ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
            List l = am.getRunningAppProcesses();
            Iterator i = l.iterator();
            PackageManager pm = this.getPackageManager();
            while (i.hasNext()) {
                ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
                try {
                    if (info.pid == pid) {
                        processName = info.processName;
                        return processName;
                    }
                } catch (Exception e) {
                    // Log.d("Process", "Error>> :"+ e.toString());
                }
            }
            return null;
        }




    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }
        @Override
        public void onDisconnected(int error) {
            EMLog.d("global listener", "onDisconnect" + error);
            if (error == EMError.USER_REMOVED) {
                onUserException("Constant.ACCOUNT_REMOVED");
            } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                onUserException("Constant.ACCOUNT_CONFLICT");
            } else if (error == EMError.SERVER_SERVICE_RESTRICTED) {
                onUserException("Constant.ACCOUNT_FORBIDDEN");
            } else if (error == EMError.USER_KICKED_BY_CHANGE_PASSWORD) {
                onUserException("Constant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD");
            } else if (error == EMError.USER_KICKED_BY_OTHER_DEVICE) {
                onUserException("Constant.ACCOUNT_KICKED_BY_OTHER_DEVICE");
            }
        }
    }

    protected void onUserException(String exception){
        EMLog.e("TAG", "onUserException: " + exception);
    }



}
