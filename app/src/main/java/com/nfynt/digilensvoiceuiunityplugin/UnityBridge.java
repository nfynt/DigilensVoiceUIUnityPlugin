package com.nfynt.digilensvoiceuiunityplugin;

import android.app.Activity;
import android.util.Log;

import com.unity3d.player.UnityPlayer;

/**
 * Created by NFYNT on 20/05/2025.
 * Copyright (c) 2025 . All rights reserved.
 */
public final class UnityBridge {
    public static String LOG_TAG = "NFYNT_UnityBridge";

    private static String mUnityLogGameObjectName = null;
    private static String mUnityLogMethodName = null;

    public static void CallUnityMethod(String gameObjectName, String methodName, String message) {
        final Activity unityAct = UnityPlayer.currentActivity;
        if (unityAct == null) {
            CallUnityLog( "Unity activity is null!",2);
            return;
        }
        unityAct.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Log.d(LOG_TAG, "Sending message to Unity: " + message);
                UnityPlayer.UnitySendMessage(gameObjectName, methodName, message);
            }
        });
    }

    public static void RegisterUnityLogCallback(String gameObjectName, String methodName) {
        mUnityLogGameObjectName = gameObjectName;
        mUnityLogMethodName = methodName;
        CallUnityLog("Successfully registered unity log callback");
    }

    public static void CallUnityLog(String message) {
        CallUnityLog(message, 0);
    }

    public static void CallUnityLog(String message, int type) {
        if (mUnityLogMethodName == null || mUnityLogGameObjectName == null) {
            switch (type) {
                case 0:
                    Log.d(LOG_TAG, "Unity log callback not registered!\n" + message);
                    break;
                case 1:
                    Log.w(LOG_TAG, "Unity log callback not registered!\n" + message);
                    break;
                default:
                    Log.e(LOG_TAG, "Unity log callback not registered!\n" + message);
                    break;
            }
            return;
        }
        final Activity unityAct = UnityPlayer.currentActivity;
        if (unityAct == null) {
            Log.e(LOG_TAG, "Unity activity is null!");
            return;
        }
        unityAct.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UnityPlayer.UnitySendMessage(mUnityLogGameObjectName, mUnityLogMethodName, type+","+message);
            }
        });
    }
}
