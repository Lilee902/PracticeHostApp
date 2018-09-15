package com.lilee.hookservice.amshook;

import android.content.pm.ServiceInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lilee.hookservice.HookApp;

public class MockHClass  implements Handler.Callback{

    private static final String TAG = "liTag";
    Handler mBase;

    public MockHClass(Handler base) {
        mBase = base;
    }

    @Override
    public boolean handleMessage(Message msg) {

        Log.d(TAG, "handleMessage : " + String.valueOf(msg.what));
        switch (msg.what) {

            // ActivityThread里面 "CREATE_SERVICE" 这个字段的值是114
            // 本来使用反射的方式获取最好, 这里为了简便直接使用硬编码
            case 114:
                handleCreateService(msg);
                break;
        }

        mBase.handleMessage(msg);
        return true;
    }

    private void handleCreateService(Message msg) {
        // 这里简单起见,直接取出插件Servie

        Object obj = msg.obj;
        ServiceInfo serviceInfo = (ServiceInfo) RefInvoke.getFieldObject(obj, "info");

        String realServiceName = null;

        for (String key : HookApp.pluginServices.keySet()) {
            String value = HookApp.pluginServices.get(key);
            if(value.equals(serviceInfo.name)) {
                realServiceName = key;
                break;
            }
        }

        serviceInfo.name = realServiceName;
    }
}
