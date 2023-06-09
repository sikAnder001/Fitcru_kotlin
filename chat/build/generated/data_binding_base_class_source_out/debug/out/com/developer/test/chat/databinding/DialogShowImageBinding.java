// Generated by view binder compiler. Do not edit!
package com.developer.test.chat.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.developer.test.chat.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class DialogShowImageBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final ImageView ivDismiss;

  @NonNull
  public final ImageView ivProfilePic;

  private DialogShowImageBinding(@NonNull RelativeLayout rootView, @NonNull ImageView ivDismiss,
      @NonNull ImageView ivProfilePic) {
    this.rootView = rootView;
    this.ivDismiss = ivDismiss;
    this.ivProfilePic = ivProfilePic;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static DialogShowImageBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static DialogShowImageBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.dialog_show_image, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static DialogShowImageBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.ivDismiss;
      ImageView ivDismiss = ViewBindings.findChildViewById(rootView, id);
      if (ivDismiss == null) {
        break missingId;
      }

      id = R.id.ivProfilePic;
      ImageView ivProfilePic = ViewBindings.findChildViewById(rootView, id);
      if (ivProfilePic == null) {
        break missingId;
      }

      return new DialogShowImageBinding((RelativeLayout) rootView, ivDismiss, ivProfilePic);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
