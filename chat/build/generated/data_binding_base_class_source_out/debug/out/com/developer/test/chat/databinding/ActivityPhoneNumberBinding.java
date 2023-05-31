// Generated by view binder compiler. Do not edit!
package com.developer.test.chat.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.developer.test.chat.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityPhoneNumberBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView btnContinue;

  @NonNull
  public final EditText etPhoneNumber;

  @NonNull
  public final ImageView imageView;

  @NonNull
  public final TextView tvVerifyNumberDesc;

  @NonNull
  public final TextView tvVerifyNumberTitle;

  private ActivityPhoneNumberBinding(@NonNull LinearLayout rootView, @NonNull TextView btnContinue,
      @NonNull EditText etPhoneNumber, @NonNull ImageView imageView,
      @NonNull TextView tvVerifyNumberDesc, @NonNull TextView tvVerifyNumberTitle) {
    this.rootView = rootView;
    this.btnContinue = btnContinue;
    this.etPhoneNumber = etPhoneNumber;
    this.imageView = imageView;
    this.tvVerifyNumberDesc = tvVerifyNumberDesc;
    this.tvVerifyNumberTitle = tvVerifyNumberTitle;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityPhoneNumberBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityPhoneNumberBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_phone_number, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityPhoneNumberBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnContinue;
      TextView btnContinue = ViewBindings.findChildViewById(rootView, id);
      if (btnContinue == null) {
        break missingId;
      }

      id = R.id.etPhoneNumber;
      EditText etPhoneNumber = ViewBindings.findChildViewById(rootView, id);
      if (etPhoneNumber == null) {
        break missingId;
      }

      id = R.id.imageView;
      ImageView imageView = ViewBindings.findChildViewById(rootView, id);
      if (imageView == null) {
        break missingId;
      }

      id = R.id.tvVerifyNumberDesc;
      TextView tvVerifyNumberDesc = ViewBindings.findChildViewById(rootView, id);
      if (tvVerifyNumberDesc == null) {
        break missingId;
      }

      id = R.id.tvVerifyNumberTitle;
      TextView tvVerifyNumberTitle = ViewBindings.findChildViewById(rootView, id);
      if (tvVerifyNumberTitle == null) {
        break missingId;
      }

      return new ActivityPhoneNumberBinding((LinearLayout) rootView, btnContinue, etPhoneNumber,
          imageView, tvVerifyNumberDesc, tvVerifyNumberTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}