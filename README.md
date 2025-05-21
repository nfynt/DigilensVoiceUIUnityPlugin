# DigilensVoiceUIUnityPlugin

A Unity plugin to use [Digilens VoiceUI](https://developer.digilens.com/hc/en-us/articles/19926227188251-Add-Voice-Commands-to-Android-apps) library from unity android application.

### Installing in Unity

To install in Unity, place the `digilensvoiceuiunityplugin.aar` and `digios_voiceui_api_1_1_5.aar` under your Unity project's `Assets/Plugins/Android` directory.

Then invoke the relevant Start/Stop voice recognition APIs to use the speech recognition service and recieve callback in Unity.

```
private List<string> voiceCmds = new List<string> { "Hello World", "Move Left", "Move Right" };

public void ToggleVoiceRecognition(bool activate)
{
#if UNITY_ANDROID && !UNITY_EDITOR
	if (activate)
	{
		AndroidJavaClass voiceRecognizer = new AndroidJavaClass("com.nfynt.digilensvoiceuiunityplugin.SpeechRecognizer");
		AndroidJavaObject cmdList = new AndroidJavaObject("java.util.ArrayList");
		foreach (var cmd in voiceCmds)
		{
			cmdList.Call<bool>("add", new AndroidJavaObject("java.lang.String",vcmds.Value));
		}
		voiceRecognizer.CallStatic("StartVoiceRecognition", cmdList, gameObject.name, nameof(VoiceCommandListenerCallback));
        Debug.Log("Voice Recognition started");
	}
	else
	{
		AndroidJavaClass voiceRecognizer = new AndroidJavaClass("com.nfynt.digilensvoiceuiunityplugin.SpeechRecognizer");
		voiceRecognizer.CallStatic("StopVoiceRecognition");
		Debug.Log("Voice recognition stopped");
	}
	IsSpeechRecognitionActive = activate;
#else
	Debug.Log("Voice Recognition is not supported on this platform!");
#endif
}

public void VoiceCommandListenerCallback(string message)
{
	foreach (var cmd in voiceCmds)
	{
		if (cmd.Value.Equals(message,System.StringComparison.OrdinalIgnoreCase))
		{
			mVoiceCmdRecognized?.Invoke(message);
			Debug.Log("Recognized voice cmd: " + message);
			return;
		}
	}
	Debug.LogError("Unhandled voice command callback! Message: "+message);
}
```

And some addiional utility to intercept plugin log messages in Unity.

```
public void RegisterAndroidPluginCallback()
{
	AndroidJavaClass unityBridge = new AndroidJavaClass("com.nfynt.digilensvoiceuiunityplugin.UnityBridge");
	unityBridge.CallStatic("RegisterUnityLogCallback", gameObject.name, nameof(AndroidMessageCallback));
}

public void AndroidMessageCallback(string message)
{
	int type = 0;
	if (message.Contains(","))
	{
		if (int.TryParse(message.Substring(0, message.IndexOf(",")), out int t))
		{
			type = t;
		}
		message=message.Substring(message.IndexOf(",")+1);
	}
	if(type==0) Debug.Log(message);
	else if(type==1) Debug.LogWarning(message);
	else Debug.LogError(message);
}
```


#### Authors
Shubham