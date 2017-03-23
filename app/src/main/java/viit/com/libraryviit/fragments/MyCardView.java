package viit.com.libraryviit.fragments;

import android.animation.ValueAnimator;
import android.content.Context;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import viit.com.libraryviit.R;

/**
 * Created by anurag on 20/3/17.
 */

public class MyCardView extends RelativeLayout {

    CardView cardView;

    ValueAnimator increaseAnimation;
    ValueAnimator decreaseAnimation;

    public static final int DURATION = 150;


    public MyCardView(Context context) {
        super(context);
        initialize(context);
        EventBus.getDefault().register(this);
    }

    private void initialize(Context context) {
        View root = inflate(context, R.layout.book_row, this);
        cardView = (CardView) root.findViewById(R.id.item_root);
    }

    @Subscribe
    public void onMessage(ScrollEvent event) {
        int margin = event.getMargin();

        if (margin == 0) {

            if (increaseAnimation != null && increaseAnimation.isRunning())
                increaseAnimation.cancel();

            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) getLayoutParams();
            int marginBottom = layoutParams.bottomMargin;

            decreaseAnimation = ValueAnimator.ofInt(marginBottom, 0);
            decreaseAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) getLayoutParams();
                    layoutParams.bottomMargin = (int) animation.getAnimatedValue();
                    setLayoutParams(layoutParams);
                }
            });
            decreaseAnimation.setDuration(DURATION);
            decreaseAnimation.start();

        } else {

            if (decreaseAnimation != null && decreaseAnimation.isRunning())
                decreaseAnimation.cancel();

            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) getLayoutParams();
            int marginBottom = layoutParams.bottomMargin;

            increaseAnimation = ValueAnimator.ofInt(marginBottom, getResources().getDimensionPixelSize(R.dimen.cardview_max_margin_bottom));
            increaseAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) getLayoutParams();
                    layoutParams.bottomMargin = (int) animation.getAnimatedValue();
                    setLayoutParams(layoutParams);
                }
            });
            increaseAnimation.setDuration(DURATION);
            increaseAnimation.start();

        }

    }

}
