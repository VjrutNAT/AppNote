package taro.rikkeisoft.com.assignment.base;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

/**
 * Created by VjrutNAT on 10/27/2017.
 */

public class CustomCardView extends CardView {

    public CustomCardView(Context context) {
        super(context);
    }

    public CustomCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
