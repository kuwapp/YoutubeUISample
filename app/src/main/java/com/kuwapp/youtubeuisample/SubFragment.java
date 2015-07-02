package com.kuwapp.youtubeuisample;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * Created by yusukehata on 15/07/01.
 */
public class SubFragment extends Fragment implements View.OnTouchListener {

    private static final float TO_SCALE = 0.5f;
    private static final int MOVIE_MARGIN = 20;

    private View mParent;
    private View mContent;
    private View mMovie;
    private View mBody;

    private float mMaxTranslationY;
    private float mPreTouchY;

    public static SubFragment newInstance() {
        return new SubFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sub, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mParent = view.findViewById(R.id.parent);
        mContent = view.findViewById(R.id.content);

        mMovie = view.findViewById(R.id.movie);
        mMovie.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= 16) {
                    mMovie.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mMovie.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                float width = mMovie.getWidth();
                float height = mMovie.getHeight();
                mMovie.setPivotX(width - MOVIE_MARGIN);
                mMovie.setPivotY(height - MOVIE_MARGIN);

                float winHeight = SysUtils.getWindowSize(getActivity()).y;
                float beginY = ViewUtils.getScreenY(mMovie);
                float endY = winHeight - height;
                mMaxTranslationY = endY - beginY;
            }
        });
        mMovie.setOnTouchListener(this);

        mBody = view.findViewById(R.id.body);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPreTouchY = event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                float diff = event.getRawY() - mPreTouchY;
                mPreTouchY = event.getRawY();

                // 移動
                float nextTranslationY = MathUtils.clamp(mContent.getTranslationY() + diff, 0, mMaxTranslationY);
                mContent.setTranslationY(nextTranslationY);
                // スケール
                float scale = 1.0f - TO_SCALE * (nextTranslationY / mMaxTranslationY);
                ViewUtils.setScale(mMovie, scale);
                // 透過
                final float alpha = 1.0f - (nextTranslationY / mMaxTranslationY);
                int color = (int) (0xFF * alpha) << 24 | getResources().getColor(R.color.black) & 0xFFFFFF;
                mBody.setAlpha(alpha);
                mParent.setBackgroundColor(color);
                break;

            case MotionEvent.ACTION_UP:
                float destY = 0;
                float destScale = 1.0f;
                float destAlpha = 1.0f;
                if ((mMaxTranslationY - mContent.getTranslationY()) < mContent.getTranslationY()) {
                    destY = mMaxTranslationY;
                    destScale = TO_SCALE;
                    destAlpha = 0;
                }

                ObjectAnimator moveAnim = ObjectAnimator.ofFloat(mContent, "translationY", mContent.getTranslationY(),destY);
                ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(mMovie, "scaleX", mMovie.getScaleX(), destScale);
                ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(mMovie, "scaleY", mMovie.getScaleY(), destScale);
                ValueAnimator alphaAnim = ValueAnimator.ofFloat(mBody.getAlpha(), destAlpha);
                alphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float val = (float)animation.getAnimatedValue();
                        int color = (int) (0xFF * val) << 24 | getResources().getColor(R.color.black) & 0xFFFFFF;
                        mBody.setAlpha(val);
                        mParent.setBackgroundColor(color);
                    }
                });

                AnimatorSet anim = new AnimatorSet();
                anim.play(moveAnim).with(scaleXAnim).with(scaleYAnim).with(alphaAnim);
                anim.setDuration(200);
                anim.start();

                break;

            case MotionEvent.ACTION_CANCEL:
                break;

            default:
                break;
        }

        return true;
    }
}
