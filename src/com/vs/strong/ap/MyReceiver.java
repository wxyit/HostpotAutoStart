package com.vs.strong.ap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;
import android.os.SystemProperties;
import android.os.Handler;
public class MyReceiver extends BroadcastReceiver {
    private String psward="0908822268";
    private OnStartTetheringCallback mStartTetheringCallback;
    private Handler mHandler = new Handler();  
    private String ssname="Hiyoung";
    private ConnectivityManager mCm;
    private static final String wifi_type="/sys/module/gxbb_pm/parameters/wifi_type";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            String getMac=SystemProperties.get("ro.mac","00:00:00:00:12:34");
            String temp=  getMac.replaceAll(":","");
            ssname=ssname+temp.substring(temp.length()-6);
            File file=new File(wifi_type);
            if (file.exists()){
                mCm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                boolean  isOK=  createWifiHotspot(context);
                if (isOK){
                  mStartTetheringCallback = new OnStartTetheringCallback(context);
                  mCm.startTethering(0, true, mStartTetheringCallback, mHandler);
                }
            }

        }
    }

    private boolean  createWifiHotspot(Context context) {
        boolean flag=false;
        WifiManager wifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            //如果wifi处于打开状态，则关闭wifi,
            wifiManager.setWifiEnabled(false);
        }
        WifiConfiguration config = new WifiConfiguration();
        config.SSID = ssname;
        config.preSharedKey = psward;
        config.hiddenSSID = true;
        config.allowedAuthAlgorithms
                .set(WifiConfiguration.AuthAlgorithm.OPEN);//开放系统认证
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA2_PSK);
        config.allowedPairwiseCiphers
                .set(WifiConfiguration.PairwiseCipher.TKIP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        config.allowedPairwiseCiphers
                .set(WifiConfiguration.PairwiseCipher.CCMP);
        config.status = WifiConfiguration.Status.ENABLED;

        //通过反射调用设置热点
        try {
            Method method = wifiManager.getClass().getMethod(
                    "setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            boolean enable = (Boolean) method.invoke(wifiManager, config, true);
            if (enable) {
                flag=true;
                Log.d("xxcsq","热点已开启 SSID:" + ssname + " password:"+psward);
            } else {
                Log.d("xxcsq","创建热点失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("xxcsq","创建热点失败");
        }
        return flag ;
    }
 private static final class OnStartTetheringCallback extends 
       ConnectivityManager.OnStartTetheringCallback {
	
     	public OnStartTetheringCallback(Context context) {
            
        }
		   
          @Override
         public void onTetheringStarted() {
         }
          @Override
         public void onTetheringFailed() {
         }
}

}
