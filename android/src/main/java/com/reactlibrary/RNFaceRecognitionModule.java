package com.reactlibrary;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

public class RNFaceRecognitionModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;
  private static final String REACT_CLASS = "RCTFaceRecognition";
  private boolean faceIsDetected = true;

  public RNFaceRecognitionModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  private static final float EYE_CLOSED_THRESHOLD = 0.30f;
  private int state = 0;

  @Override
  public String getName() {
    return "RNFaceRecognition";
  }

  @ReactMethod
  public void detect(String encondedImage, Promise promise) {
    byte[] base64Image = Base64.decode(encondedImage, Base64.DEFAULT);
    Bitmap bitmap = BitmapFactory.decodeByteArray(base64Image, 0, base64Image.length);

    FaceDetector detector = new FaceDetector.Builder(this.reactContext)
        .setTrackingEnabled(false)
        .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
        .build();
    Frame frame = new Frame.Builder().setBitmap(bitmap).build();

    SparseArray<Face> faces = detector.detect(frame);
    WritableMap faceMap = Arguments.createMap();

    if(faces.size() == 1){
      for (int i = 0; i < faces.size(); i++) {
        Face thisFace = faces.valueAt(i);
        int eyeState = this.getStateEye(thisFace);

        faceMap.putInt("x", (int) thisFace.getPosition().x);
        faceMap.putInt("y", (int) thisFace.getPosition().y);
        faceMap.putInt("width", (int) thisFace.getWidth());
        faceMap.putInt("height", (int) thisFace.getHeight());
        faceMap.putDouble("leftEyeOpen", thisFace.getIsLeftEyeOpenProbability());
        faceMap.putDouble("rightEyeOpen", thisFace.getIsRightEyeOpenProbability());
        faceMap.putDouble("smiling", thisFace.getIsSmilingProbability());
        faceMap.putInt("eyeStatus", eyeState);

        detector.release();
        promise.resolve(faceMap);
      }
    } else {
      detector.release();
      promise.reject("FACE_NOT_FOUND");
    }
  }

  private int getStateEye(Face face) {
    float leftEye  = face.getIsLeftEyeOpenProbability();
    float rightEye = face.getIsRightEyeOpenProbability();

    if ((leftEye == Face.UNCOMPUTED_PROBABILITY) || (rightEye == Face.UNCOMPUTED_PROBABILITY)) {
      return 3;
    }

    float eyesValue = Math.min(leftEye, rightEye);

    switch (state) {
      case 0:
        if (eyesValue > EYE_CLOSED_THRESHOLD) {
          state = 1;
        }
        break;

      case 1:
        if (eyesValue < EYE_CLOSED_THRESHOLD) {
          state = 2;
        }
        break;

      case 2:
        if (eyesValue > EYE_CLOSED_THRESHOLD) {
          state = 0;
        }
        break;

      default:
        break;
    }

    return state;
  }

}
