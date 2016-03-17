package ss.com.qqdemo.friendscircle;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

import ss.com.qqdemo.utils.EvaluateUtils;

/**
 *朋友圈自定义控件
 */
public class FriendsCircleListView extends ListView{

    private int headImgSourceHeight; //图片原本的高度
    private int headImgHeight;//朋友圈头部图片的高
    private ImageView cimgv;

    public FriendsCircleListView(Context context) {
        this(context, null);
    }

    public FriendsCircleListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FriendsCircleListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    //拿到朋友圈头  的引用    得到高度和宽度
    public void setKHeadView(ImageView imgv) {
        this.cimgv = imgv;
        headImgHeight = imgv.getHeight();
        headImgSourceHeight = imgv.getDrawable().getIntrinsicHeight();
    }

    //超出滑动
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
                                   int scrollRangeX, int scrollRangeY, int maxOverScrollX,
                                   int maxOverScrollY, boolean isTouchEvent) {
        //deltaY竖直方向的偏移量  -代表向下拉
        if (isTouchEvent&&deltaY<0){
            if(cimgv.getHeight()<=headImgSourceHeight){
                int newHeight = cimgv.getHeight() + Math.abs(deltaY);
                cimgv.getLayoutParams().height = newHeight;
                cimgv.requestLayout();
            }
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_UP:
                final int startHeight = cimgv.getHeight();
                final int endHeight = headImgHeight;
                //执行回弹动画
                ValueAnimator va = ValueAnimator.ofInt(1);
                va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float percent = animation.getAnimatedFraction();
                        int newHeight = EvaluateUtils.evaluateInt(percent, startHeight, endHeight);

                        cimgv.getLayoutParams().height = newHeight;
                        cimgv.requestLayout();
                    }
                });
                va.setInterpolator(new OvershootInterpolator(3));
                va.setDuration(500);
                va.start();
                break;
        }
        return super.onTouchEvent(ev);
    }
}
