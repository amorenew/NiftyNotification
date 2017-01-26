package com.gitonway.lee.niftynotification.lib;
/*
 * Copyright 2014 gitonway
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class NiftyNotificationView {

    private static final String NULL_PARAMETERS_ARE_NOT_ACCEPTED = "Null parameters are not accepted";

    private static final int TEXT_ID = android.R.id.message;

    private static final int IMAGE_ID = android.R.id.icon;
    private final CharSequence text;
    private final Effects effects;
    private NotifyConfiguration notifyConfiguration = null;
    private Activity activity;

    private ViewGroup viewGroup;

    private FrameLayout notifyView;

    private Drawable iconDrawable;

    private int iconRes;

    private boolean isDefault;

    private View.OnClickListener onClickListener;


    private NiftyNotificationView(Activity activity, CharSequence text, Effects effects, ViewGroup viewGroup) {
        if ((activity == null) || (text == null)) {
            throw new IllegalArgumentException(NULL_PARAMETERS_ARE_NOT_ACCEPTED);
        }
        isDefault = true;
        this.activity = activity;
        this.text = text;
        this.effects = effects;
        this.viewGroup = viewGroup;
        this.notifyConfiguration = new NotifyConfiguration.Builder().build();
        init(effects);
    }

    private NiftyNotificationView(Activity activity, CharSequence text, Effects effects, ViewGroup viewGroup, NotifyConfiguration notifyConfiguration) {
        if ((activity == null) || (text == null) || (notifyConfiguration == null)) {
            throw new IllegalArgumentException(NULL_PARAMETERS_ARE_NOT_ACCEPTED);
        }
        isDefault = false;
        this.activity = activity;
        this.text = text;
        this.effects = effects;
        this.viewGroup = viewGroup;
        this.notifyConfiguration = notifyConfiguration;
        init(effects);
    }

    public static NiftyNotificationView build(Activity activity, CharSequence text, Effects effects, int viewGroupResId) {
        return new NiftyNotificationView(activity, text, effects, (ViewGroup) activity.findViewById(viewGroupResId));
    }

    public static NiftyNotificationView build(Activity activity, CharSequence text, Effects effects, int viewGroupResId, NotifyConfiguration notifyConfiguration) {
        return new NiftyNotificationView(activity, text, effects, (ViewGroup) activity.findViewById(viewGroupResId), notifyConfiguration);
    }

    public static NiftyNotificationView build(Activity activity, CharSequence text, Effects effects) {
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) activity
                .findViewById(android.R.id.content)).getChildAt(0);
        return new NiftyNotificationView(activity, text, effects, viewGroup);
    }

    public static NiftyNotificationView build(Activity activity, CharSequence text, Effects effects, NotifyConfiguration notifyConfiguration) {
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) activity
                .findViewById(android.R.id.content)).getChildAt(0);
        return new NiftyNotificationView(activity, text, effects, viewGroup, notifyConfiguration);
    }

    private void init(Effects effects) {
        this.iconDrawable = null;
        this.iconRes = 0;
    }

    public long getInDuration() {
        return effects.getAnimator().getDuration();
    }

    public long getOutDuration() {
        return effects.getAnimator().getDuration();
    }

    public long getDispalyDuration() {
        return this.notifyConfiguration.displayDuration;
    }

    public Effects getEffects() {
        return effects;
    }

    public NotifyConfiguration getNotifyConfiguration() {
        return notifyConfiguration;
    }

    Activity getActivity() {
        return activity;
    }

    boolean isShowing() {
        return (null != activity) && isNotifyViewNotNull();
    }

    private boolean isNotifyViewNotNull() {

        return (null != notifyView) && (null != notifyView.getParent());

    }

    void detachActivity() {
        activity = null;
    }

    void detachViewGroup() {
        viewGroup = null;
    }

    ViewGroup getViewGroup() {
        return viewGroup;
    }

    View getView() {

        if (null == this.notifyView) {
            initializeNotifyView();
        }

        return notifyView;
    }

    private void initializeNotifyView() {
        if (this.activity != null) {
            this.notifyView = initContainerView();
            LinearLayout contentView = initToastView();
            this.notifyView.addView(contentView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        }
    }

    private FrameLayout initContainerView() {
        FrameLayout notifyView = (FrameLayout) getActivity()
                .getLayoutInflater().inflate(R.layout.view_toast_container, null);
        if (null != onClickListener) {
            notifyView.setOnClickListener(onClickListener);
        }
        return notifyView;
    }

    private LinearLayout initToastView() {
        LinearLayout toastLayout = (LinearLayout) getActivity()
                .getLayoutInflater()
                .inflate(R.layout.toast_view, null);
        if ((null != iconDrawable) || (0 != iconRes)) {
            initToastImage(toastLayout);
        } else {
            toastLayout.removeView(toastLayout.findViewById(IMAGE_ID));
        }
        initToastText(toastLayout);
        return toastLayout;
    }

    private TextView initToastText(LinearLayout toastLayout) {
        int padding = px2dip(this.notifyConfiguration.textPadding);
        int viewHeight = px2dip(this.notifyConfiguration.viewHeight);
        int viewWidth = px2dip(this.notifyConfiguration.viewWidth);
        int textSize = px2dip(this.notifyConfiguration.textSize);
        TextView text = (TextView) toastLayout.findViewById(TEXT_ID);
        text.setMinimumHeight(viewHeight);
        text.setMinimumWidth(viewWidth);
        text.setId(TEXT_ID);
        text.setText(this.text);
        text.setTextSize(textSize);
        text.setMaxLines(this.notifyConfiguration.textLines);
        text.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        if (padding > 0)
            text.setPadding(padding * 2, padding, padding * 2, padding);
        text.setTextColor(Color.parseColor(this.notifyConfiguration.textColor));
        text.setBackgroundColor(Color.parseColor(this.notifyConfiguration.backgroundColor));

        if ((null != iconDrawable) || (0 != iconRes)) {
            text.setMinHeight(viewHeight);
            text.setGravity(isDefault ? Gravity.CENTER_VERTICAL : this.notifyConfiguration.textGravity);
        } else {
            text.setGravity(isDefault ? Gravity.CENTER : this.notifyConfiguration.textGravity);
        }
        return text;
    }

    private ImageView initToastImage(LinearLayout toastLayout) {
        int maxValue = px2dip(this.notifyConfiguration.viewHeight);
        ImageView image = (ImageView) toastLayout.findViewById(IMAGE_ID);
        image.setMinimumHeight(maxValue);
        image.setMinimumWidth(maxValue);
        image.setMaxWidth(maxValue);
        image.setMaxHeight(maxValue);
        image.setBackgroundColor(Color.parseColor(this.notifyConfiguration.iconBackgroundColor));
        image.setAdjustViewBounds(true);
        image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        if (null != iconDrawable) {
            image.setImageDrawable(iconDrawable);
        }
        if (iconRes != 0) {
            image.setImageResource(iconRes);
        }
        return image;
    }

    public int px2dip(float pxValue) {
        final float scale = activity.getResources().getDisplayMetrics().density;
        return (int) (pxValue * scale + 0.5f);
    }

    /*******************
     * Call these methods
     ************************/


    public NiftyNotificationView setIcon(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
        return this;
    }

    public NiftyNotificationView setIcon(int iconRes) {
        this.iconRes = iconRes;
        return this;
    }

    public NiftyNotificationView setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    public void show() {

        show(true);
    }

    public void show(boolean repeat) {

        Manager.getInstance().add(this, repeat);
    }

    public void showSticky() {

        Manager.getInstance().addSticky(this);
    }

    //only remove sticky notification
    public void removeSticky() {
        Manager.getInstance().removeSticky();
    }

    public void hide() {

        Manager.getInstance().removeNotify(this);
    }


}
