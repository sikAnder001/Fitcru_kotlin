// Generated by view binder compiler. Do not edit!
package com.developer.test.chat.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.developer.test.chat.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ItemReceiveBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView ivFeeling;

  @NonNull
  public final ImageView ivReceive;

  @NonNull
  public final LinearLayout llItemReceive;

  @NonNull
  public final TextView tvReceivedMessage;

  private ItemReceiveBinding(@NonNull ConstraintLayout rootView, @NonNull ImageView ivFeeling,
      @NonNull ImageView ivReceive, @NonNull LinearLayout llItemReceive,
      @NonNull TextView tvReceivedMessage) {
    this.rootView = rootView;
    this.ivFeeling = ivFeeling;
    this.ivReceive = ivReceive;
    this.llItemReceive = llItemReceive;
    this.tvReceivedMessage = tvReceivedMessage;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemReceiveBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemReceiveBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_receive, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemReceiveBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.ivFeeling;
      ImageView ivFeeling = ViewBindings.findChildViewById(rootView, id);
      if (ivFeeling == null) {
        break missingId;
      }

      id = R.id.ivReceive;
      ImageView ivReceive = ViewBindings.findChildViewById(rootView, id);
      if (ivReceive == null) {
        break missingId;
      }

      id = R.id.llItemReceive;
      LinearLayout llItemReceive = ViewBindings.findChildViewById(rootView, id);
      if (llItemReceive == null) {
        break missingId;
      }

      id = R.id.tvReceivedMessage;
      TextView tvReceivedMessage = ViewBindings.findChildViewById(rootView, id);
      if (tvReceivedMessage == null) {
        break missingId;
      }

      return new ItemReceiveBinding((ConstraintLayout) rootView, ivFeeling, ivReceive,
          llItemReceive, tvReceivedMessage);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
