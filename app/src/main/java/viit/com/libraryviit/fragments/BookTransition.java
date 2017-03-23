package viit.com.libraryviit.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;
import android.util.AttributeSet;

/**
 * Created by anurag on 21/3/17.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class BookTransition extends TransitionSet {
    public BookTransition() {
        init();
    }

    /**
     * This constructor allows us to use this transition in XML
     */
    public BookTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrdering(ORDERING_TOGETHER);
        addTransition(new ChangeBounds()).
                addTransition(new ChangeTransform());
    }
}
