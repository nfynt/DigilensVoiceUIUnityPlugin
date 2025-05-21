package com.nfynt.digilensvoiceuiunityplugin;

/**
 * Created by NFYNT on 19/05/2025.
 * Copyright (c) 2025 . All rights reserved.
 */

import com.unity3d.player.UnityPlayer;
import com.digilens.digios_voiceui_api.VoiceUI_Interface;
import com.digilens.digios_voiceui_api.utils.VoiceUI_Model;
import com.digilens.digios_voiceui_api.utils.VoiceUICallback;
import com.digilens.digios_voiceui_api.utils.VoiceUI_Listener;
import static com.digilens.digios_voiceui_api.utils.Constants.Voice_Command_CONFIG_TYPE_FEEDBACK_ONLY;
import static com.digilens.digios_voiceui_api.utils.Constants.Voice_Command_CONFIG_TYPE_FEEDBACK_WITH_NUMBER_ONLY;

import android.util.Log;

import java.util.ArrayList;

public final class SpeechRecognizer {

    public static final String LOG_TAG="NFYNT_SpeechRecognizer";

    public static final int CONFIG_TYPE=Voice_Command_CONFIG_TYPE_FEEDBACK_ONLY;
//    CONFIG_TYPE = Voice_Command_CONFIG_TYPE_FEEDBACK_ONLY;
//    CONFIG_TYPE = Voice_Command_CONFIG_TYPE_FEEDBACK_WITH_NUMBER_ONLY;
    public static final String LANGUAGE_CODE = "en"; //ENGLISH - "en" //Español - "es"
    public static VoiceUI_Model voiceUI_model=null;
    private static VoiceUI_Interface voiceUIInterface=null;

    public static int TestConnection(int val){
        Log.d(LOG_TAG,"Updating received val of: "+val);
        return 2*val;
    }

    private static void CreateTestListener(ArrayList<String> voiceCmds, String gameObjectName, String methodName) {
        try {
            for(String cmd:voiceCmds) {
                VoiceUI_Listener voiceUI_Listener_1 = new VoiceUI_Listener(cmd, CONFIG_TYPE) {
                    // For Voice_Command_CONFIG_TYPE_FEEDBACK_ONLY config type
                    @Override
                    public void onReceive() {
                        Log.d(LOG_TAG, "Listener detected voice command : " + this.voice_command);
                        UnityBridge.CallUnityMethod(gameObjectName,methodName,this.voice_command);
                    }
                    // For Voice_Command_CONFIG_TYPE_FEEDBACK_WITH_NUMBER_ONLY config type
                    @Override
                    public void onReceive(int value) {
                        Log.d(LOG_TAG, "Listener detected voice command with number: " + this.voice_command);
                        UnityBridge.CallUnityMethod(gameObjectName,methodName,this.voice_command+value);
                    }
                };
                voiceUI_model.addVoiceUI_Listener(voiceUI_Listener_1);
                UnityBridge.CallUnityLog("Added voice listener for cmd: "+cmd);
            }
//        VoiceUI_Listener voiceUI_Listener_2 = new VoiceUI_Listener(new VoiceUICallback() {
//            @Override    public void onReceive(String voice_command) {
//                Log.d(LOG_TAG,"Listener detected voice command : " + voice_command);
//            }
//
//            @Override    public void onReceive(String voice_command, int value) {
//                Log.d(LOG_TAG,"Detected voice command : "+voice_command);
//                Log.d(LOG_TAG,"Listener detected number value : "+value);
//            }
//        },VOICE_COMMAND_TO_BE_REGISTERED,CONFIG_TYPE));
        } catch(Exception e){
            UnityBridge.CallUnityLog("Failed to voice recognition listener! Exception: "+e.getMessage(),2);
            return;
        }
    }

    public static void StartVoiceRecognition(ArrayList<String> voiceCmds, String gameObjectName, String callbackMethod) {
        try {
            voiceUI_model = new VoiceUI_Model(LANGUAGE_CODE);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to construct VoiceUI_model! Error: " + e.getMessage());
            return;
        }
        CreateTestListener(voiceCmds,gameObjectName,callbackMethod);
        voiceUIInterface = new VoiceUI_Interface();
        voiceUIInterface.add_model(voiceUI_model);
        try {
            voiceUIInterface.start(UnityPlayer.currentActivity);
            UnityBridge.CallUnityLog( "Successfully started voice recognition");
        } catch (Exception e) {
            UnityBridge.CallUnityLog( "Failed to start voice recognition interface! Exception: " + e.getMessage(),2);
        }
    }

    public static void StopVoiceRecognition(){
        try {
            voiceUIInterface.stop();
            voiceUIInterface=null;
            voiceUI_model=null;
            UnityBridge.CallUnityLog("Successfully stopped voice recognition");
        } catch (Exception exception) {
            Log.d(LOG_TAG,exception.getMessage());
            exception.printStackTrace();
        }
    }
}



/*
 __  _ _____   ____  _ _____  
|  \| | __\ `v' /  \| |_   _| 
| | ' | _| `. .'| | ' | | |   
|_|\__|_|   !_! |_|\__| |_|
 

*/