package com.example.chapter3.homework;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.airbnb.lottie.LottieAnimationView;

public class PlaceholderFragment extends Fragment {

    private LottieAnimationView lottieAnimationView;
    private ListView fri_lv;
    private static final String[] items = {"item1","item2","item3","item4","item5","item6","item7","item8","item9",};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO ex3-3: 修改 fragment_placeholder，添加 loading 控件和列表视图控件
        View view = inflater.inflate(R.layout.fragment_placeholder, container, false);
        lottieAnimationView = view.findViewById(R.id.lottie_AV);
        fri_lv = view.findViewById(R.id.fri_lv);
        try {
            fri_lv.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1, items));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 这里会在 5s 后执行
                // TODO ex3-4：实现动画，将 lottie 控件淡出，列表数据淡入
                ObjectAnimator lAV_Animator = ObjectAnimator.ofFloat(lottieAnimationView,
                        "alpha", 1f, 0f);
//                lAV_Animator.setRepeatCount(ValueAnimator.INFINITE);
//                lAV_Animator.setInterpolator(new LinearInterpolator());
                lAV_Animator.setDuration(2000);
//                lAV_Animator.setRepeatMode(ValueAnimator.REVERSE);

                fri_lv.setAlpha(0f);
                fri_lv.setVisibility(View.VISIBLE);

                ObjectAnimator frilv_Animator = ObjectAnimator.ofFloat(fri_lv,
                        "alpha", 0f, 1f);
//                frilv_Animator.setRepeatCount(ValueAnimator.INFINITE);
//                frilv_Animator.setInterpolator(new LinearInterpolator());
                frilv_Animator.setDuration(2000);
//                frilv_Animator.setRepeatMode(ValueAnimator.REVERSE);


                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playSequentially(lAV_Animator, frilv_Animator);
                animatorSet.start();

                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        lottieAnimationView.setVisibility(View.GONE);
                    }
                });

            }
        }, 5000);
    }
}
