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
import com.jsibbold.zoomage.ZoomageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityViewImageBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final ZoomageView iv;

  @NonNull
  public final ImageView ivDownload;

  private ActivityViewImageBinding(@NonNull RelativeLayout rootView, @NonNull ZoomageView iv,
      @NonNull ImageView ivDownload) {
    this.rootView = rootView;
    this.iv = iv;
    this.ivDownload = ivDownload;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityViewImageBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityViewImageBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_view_image, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityViewImageBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.iv;
      ZoomageView iv = ViewBindings.findChildViewById(rootView, id);
      if (iv == null) {
        break missingId;
      }

      id = R.id.ivDownload;
      ImageView ivDownload = ViewBindings.findChildViewById(rootView, id);
      if (ivDownload == null) {
        break missingId;
      }

      return new ActivityViewImageBinding((RelativeLayout) rootView, iv, ivDownload);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
