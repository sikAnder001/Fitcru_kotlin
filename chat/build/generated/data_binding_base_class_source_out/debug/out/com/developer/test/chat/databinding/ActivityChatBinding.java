// Generated by view binder compiler. Do not edit!
package com.developer.test.chat.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.developer.test.chat.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityChatBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final EditText etMessageBox;

  @NonNull
  public final ImageView ivAttachment;

  @NonNull
  public final ImageView ivBack;

  @NonNull
  public final ImageView ivCamera;

  @NonNull
  public final ImageView ivProfilePic;

  @NonNull
  public final RelativeLayout rlMessageBox;

  @NonNull
  public final RecyclerView rvChats;

  @NonNull
  public final Toolbar toolbar;

  @NonNull
  public final TextView tvName;

  @NonNull
  public final TextView tvStatus;

  private ActivityChatBinding(@NonNull ConstraintLayout rootView, @NonNull EditText etMessageBox,
      @NonNull ImageView ivAttachment, @NonNull ImageView ivBack, @NonNull ImageView ivCamera,
      @NonNull ImageView ivProfilePic, @NonNull RelativeLayout rlMessageBox,
      @NonNull RecyclerView rvChats, @NonNull Toolbar toolbar, @NonNull TextView tvName,
      @NonNull TextView tvStatus) {
    this.rootView = rootView;
    this.etMessageBox = etMessageBox;
    this.ivAttachment = ivAttachment;
    this.ivBack = ivBack;
    this.ivCamera = ivCamera;
    this.ivProfilePic = ivProfilePic;
    this.rlMessageBox = rlMessageBox;
    this.rvChats = rvChats;
    this.toolbar = toolbar;
    this.tvName = tvName;
    this.tvStatus = tvStatus;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityChatBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityChatBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_chat, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityChatBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.etMessageBox;
      EditText etMessageBox = ViewBindings.findChildViewById(rootView, id);
      if (etMessageBox == null) {
        break missingId;
      }

      id = R.id.ivAttachment;
      ImageView ivAttachment = ViewBindings.findChildViewById(rootView, id);
      if (ivAttachment == null) {
        break missingId;
      }

      id = R.id.ivBack;
      ImageView ivBack = ViewBindings.findChildViewById(rootView, id);
      if (ivBack == null) {
        break missingId;
      }

      id = R.id.ivCamera;
      ImageView ivCamera = ViewBindings.findChildViewById(rootView, id);
      if (ivCamera == null) {
        break missingId;
      }

      id = R.id.ivProfilePic;
      ImageView ivProfilePic = ViewBindings.findChildViewById(rootView, id);
      if (ivProfilePic == null) {
        break missingId;
      }

      id = R.id.rlMessageBox;
      RelativeLayout rlMessageBox = ViewBindings.findChildViewById(rootView, id);
      if (rlMessageBox == null) {
        break missingId;
      }

      id = R.id.rvChats;
      RecyclerView rvChats = ViewBindings.findChildViewById(rootView, id);
      if (rvChats == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      id = R.id.tvName;
      TextView tvName = ViewBindings.findChildViewById(rootView, id);
      if (tvName == null) {
        break missingId;
      }

      id = R.id.tvStatus;
      TextView tvStatus = ViewBindings.findChildViewById(rootView, id);
      if (tvStatus == null) {
        break missingId;
      }

      return new ActivityChatBinding((ConstraintLayout) rootView, etMessageBox, ivAttachment,
          ivBack, ivCamera, ivProfilePic, rlMessageBox, rvChats, toolbar, tvName, tvStatus);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
