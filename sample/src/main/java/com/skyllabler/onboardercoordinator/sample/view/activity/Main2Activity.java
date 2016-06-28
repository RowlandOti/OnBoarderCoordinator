package com.skyllabler.onboardercoordinator.sample.view.activity;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.skyllabler.onboardercoordinator.sample.R;
import com.skyllabler.onboardercoordinator.sample.animators.ChatAvatarsAnimator;
import com.skyllabler.onboardercoordinator.sample.animators.InSyncAnimator;
import com.skyllabler.onboardercoordinator.sample.animators.RocketAvatarsAnimator;
import com.skyllabler.onboardercoordinator.sample.view.inflater.OnBoarderPageDescriptor;
import com.skyllabler.onboardercoordinator.view.adapter.OnBoarderPagerAdapter;
import com.skyllabler.onboardercoordinator.view.widget.OnBoarderCoordinatorLayout;
import com.skyllabler.onboardercoordinator.view.widget.OnBoarderCoordinatorViewPager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main2Activity extends AppCompatActivity {

    private boolean animationReady = false;
    private ValueAnimator backgroundAnimator;
    OnBoarderPagerAdapter onBoarderPagerAdapter;
    @Bind(R.id.viewpager)
    OnBoarderCoordinatorViewPager coordinatorViewPager;
    private RocketAvatarsAnimator rocketAvatarsAnimator;
    private ChatAvatarsAnimator chatAvatarsAnimator;
    private InSyncAnimator inSyncAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        initializeListeners();
        initializePages();
        initializeBackgroundTransitions();
    }

    private void initializePages() {
        onBoarderPagerAdapter = new OnBoarderPagerAdapter(this, new OnBoarderPageDescriptor());
        coordinatorViewPager.setAdapter(onBoarderPagerAdapter);
    }

    private void initializeListeners() {
        coordinatorViewPager.setOnPageScrollListener(new OnBoarderCoordinatorViewPager.OnPageScrollListener() {
            @Override
            public void onScrollPage(View v, float progress, float maximum) {
                if (!animationReady) {
                    animationReady = true;
                    backgroundAnimator.setDuration((long) maximum);
                }
                backgroundAnimator.setCurrentPlayTime((long) progress);
            }

            @Override
            public void onPageSelected(View v, int pageSelected) {
                switch (pageSelected) {
                    case 0:
                        if (rocketAvatarsAnimator == null) {
                            rocketAvatarsAnimator = new RocketAvatarsAnimator(coordinatorViewPager);
                            rocketAvatarsAnimator.play();
                        }
                        break;
                    case 1:
                        if (chatAvatarsAnimator == null) {
                            chatAvatarsAnimator = new ChatAvatarsAnimator(coordinatorViewPager);
                            chatAvatarsAnimator.play();
                        }
                        break;
                    case 2:
                        if (inSyncAnimator == null) {
                            inSyncAnimator = new InSyncAnimator(coordinatorViewPager);
                            inSyncAnimator.play();
                        }
                        break;
                }
            }
        });
    }

    private void initializeBackgroundTransitions() {
        final Resources resources = getResources();
        final int colorPage1 = ResourcesCompat.getColor(resources, R.color.page1, getTheme());
        final int colorPage2 = ResourcesCompat.getColor(resources, R.color.page2, getTheme());
        final int colorPage3 = ResourcesCompat.getColor(resources, R.color.page3, getTheme());

        backgroundAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorPage1, colorPage2, colorPage3);
        backgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                coordinatorViewPager.setBackgroundColor((int) animation.getAnimatedValue());
            }
        });
    }

    @OnClick(R.id.skip)
    void skip() {

    }
}
