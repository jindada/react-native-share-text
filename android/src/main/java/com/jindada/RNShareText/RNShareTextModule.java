package com.jindada.RNShareText;

import android.content.Intent;
import android.app.Activity;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RNShareTextModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

  private static final int SHARE_QUEST_CODE = 1;

  private final ReactApplicationContext reactContext;

  private static final String ERROR_USER_CANCELLED = "USER_CANCELLED";

  private Promise promise;

  private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
      if (promise != null) {
        if (resultCode == Activity.RESULT_OK) {
          if (requestCode == SHARE_QUEST_CODE) {
            promise.resolve(null);
          }
        } else {
          if (requestCode == SHARE_QUEST_CODE){
            promise.reject(ERROR_USER_CANCELLED, "User cancelled");
          }
        }
      }
      promise = null;
    }
  };

  public RNShareTextModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    reactContext.addActivityEventListener(mActivityEventListener);
    reactContext.addLifecycleEventListener(this);
  }

  @Override
  public String getName() {
    return "RNWhatsAppShare";
  }

  @ReactMethod
  public void shareWhatsApp(ReadableMap customFields, Promise promise) {
    this.promise = promise;

    String title = customFields.getString("title");
    String url = customFields.getString("url");

    Intent intent = new Intent(reactContext);
    intent.setAction(Intent.ACTION_SEND);
    intent.putExtra(Intent.EXTRA_TEXT, "this is my text to send.");
    intent.setType("text/plain");
    intent.setPackage("com.whatsapp");

    try {
      getCurrentActivity().startActivityForResult(intent, SHARE_QUEST_CODE);
    } catch(Exception e) {
      promise.reject(ERROR_NOT_FOUND, "Not Found");
    }
  }

  @ReactMethod
  public void shareTwitter(ReadableMap customFields, Promise promise) {
    this.promise = promise;

    String title = customFields.getString("title");
    String url = customFields.getString("url");

    Intent intent = new Intent(reactContext);
    intent.setAction(Intent.ACTION_SEND);
    intent.putExtra(Intent.EXTRA_TEXT, "this is my text to send.");
    intent.setType("text/plain");
    intent.setPackage("com.twitter.android");

    try {
      getCurrentActivity().startActivityForResult(intent, SHARE_QUEST_CODE);
    } catch(Exception e) {
      promise.reject(ERROR_NOT_FOUND, "Not Found");
    }
  }

  @Override
  public void onHostDestroy() {
    // Do nothing
  }

  @Override
  public void onHostResume() {
    // Do nothing
  }

  @Override
  public void onHostPause() {
    // Do nothing
  }
}
