// Generated by view binder compiler. Do not edit!
package com.developer.test.chat.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.developer.test.chat.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentStatusBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final FloatingActionButton fabAddStatus;

  @NonNull
  public final ShimmerRecyclerView rvStatus;

  private FragmentStatusBinding(@NonNull RelativeLayout rootView,
      @NonNull FloatingActionButton fabAddStatus, @NonNull ShimmerRecyclerView rvStatus) {
    this.rootView = rootView;
    this.fabAddStatus = fabAddStatus;
    this.rvStatus = rvStatus;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentStatusBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentStatusBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_status, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentStatusBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.fabAddStatus;
      FloatingActionButton fabAddStatus = ViewBindings.findChildViewById(rootView, id);
      if (fabAddStatus == null) {
        break missingId;
      }

      id = R.id.rvStatus;
      ShimmerRecyclerView rvStatus = ViewBindings.findChildViewById(rootView, id);
      if (rvStatus == null) {
        break missingId;
      }

      return new FragmentStatusBinding((RelativeLayout) rootView, fabAddStatus, rvStatus);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
