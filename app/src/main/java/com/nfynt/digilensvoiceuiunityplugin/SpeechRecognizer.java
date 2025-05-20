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

public class SpeechRecognizer {

    public static String LOG_TAG="[NFYNT::SpeechRecognizer]";

    public static int CONFIG_TYPE=Voice_Command_CONFIG_TYPE_FEEDBACK_ONLY;
//    CONFIG_TYPE = Voice_Command_CONFIG_TYPE_FEEDBACK_ONLY;
//    CONFIG_TYPE = Voice_Command_CONFIG_TYPE_FEEDBACK_WITH_NUMBER_ONLY;
    public static String LANGUAGE_CODE = "en"; //ENGLISH - "en" //Español - "es"
    public static VoiceUI_Model voiceUI_model=null;
    private static VoiceUI_Interface voiceUIInterface=null;

    public static int TestConnection(int val){
        Log.d(LOG_TAG,"Updating received val of: "+val);
        return 2*val;
    }

    public static void CreateTestListener() {
        String voiceCmd="Hello World";
        try {
            VoiceUI_Listener voiceUI_Listener_1 = new VoiceUI_Listener(voiceCmd, CONFIG_TYPE) {
                // For Voice_Command_CONFIG_TYPE_FEEDBACK_ONLY config type
                @Override
                public void onReceive() {
                    Log.d(LOG_TAG, "Listener detected voice command : " + this.voice_command);
                    //TODO: Do your task
                }

                // For Voice_Command_CONFIG_TYPE_FEEDBACK_WITH_NUMBER_ONLY config type
                @Override
                public void onReceive(int value) {
                    //TODO: Do your task
                    Log.d(LOG_TAG, "Listener dectected voice command with number: " + this.voice_command);
                }
            };
            voiceUI_model.addVoiceUI_Listener(voiceUI_Listener_1);
            Log.d(LOG_TAG,"Added voice listener type 1");
//        VoiceUI_Listener voiceUI_Listener_2 = new VoiceUI_Listener(new VoiceUICallback() {
//            @Override    public void onReceive(String voice_command) {
//                Log.d(LOG_TAG,"Listener detected voice command : " + voice_command);
//                //TODO: Do your task
//            }
//
//            @Override    public void onReceive(String voice_command, int value) {
//                Log.d(LOG_TAG,"Detected voice command : "+voice_command);
//                Log.d(LOG_TAG,"Listener detected number value : "+value);
//                //TODO: Do your task
//            }
//        },VOICE_COMMAND_TO_BE_REGISTERED,CONFIG_TYPE));
        } catch(Exception e){
            Log.e(LOG_TAG,"Failed to voice recognition listener! Exception: "+e.getMessage());
            return;
        }
    }

    public static void StartVoiceRecognition() {
        Log.d(LOG_TAG,"Attempting to start voice recognition");
        try {
            voiceUI_model = new VoiceUI_Model(LANGUAGE_CODE);
        } catch (Exception e) {
            Log.e(LOG_TAG,"Failed to construct VoiceUI_model! Error: "+e.getMessage());
            return;
        }
            CreateTestListener();
            voiceUIInterface = new VoiceUI_Interface();
            voiceUIInterface.add_model(voiceUI_model);
        try {
            voiceUIInterface.start(UnityPlayer.currentActivity);
            Log.d(LOG_TAG, "Successfully started voice recognition");
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to start voice recognition interface! Exception: " + e.getMessage());
        }
    }

    public static void StopVoiceRecognition(){
        try {
            voiceUIInterface.stop();
            Log.d(LOG_TAG,"Successfully stopped voice recognition");
        } catch (Exception exception) {
            Log.d(LOG_TAG,exception.getMessage());
            exception.printStackTrace();
        }
    }
}
