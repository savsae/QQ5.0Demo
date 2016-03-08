package ss.com.qqdemo.drag;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nineoldandroids.view.ViewHelper;

import ss.com.qqdemo.utils.EvaluateUtils;

/*
侧滑面板
 */
public class DragLayoutActivity extends FrameLayout {

    private ViewDragHelper sViewDragHelper;
    private ViewGroup vLeftContent;
    private ViewGroup vMainContent;
    private int screenWidth;
    private int screenHeight;
    private int leftMWidth;
    private OnDragStatusListener vDragStatusListener;
    private Status vStatus = Status.Close;

    public enum Status{
        Close,Open,Draging
    }

    public Status getvStatus() {
        return vStatus;
    }

    public void setvStatus(Status vStatus) {
        this.vStatus = vStatus;
    }

    public interface OnDragStatusListener {
        void onClose();
        void onOpen();
        void onDraging(float percent);
    }

    public void setDragStatusListener(OnDragStatusListener dragStatusListener){
        //状态监听事件
        this.vDragStatusListener = dragStatusListener;

    }

    public DragLayoutActivity(Context context) {
        this(context, null);
    }

    public DragLayoutActivity(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayoutActivity(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        sViewDragHelper = ViewDragHelper.create(this, sCallback);
    }

    private ViewDragHelper.Callback sCallback = new ViewDragHelper.Callback() {

        //根据返回结果 判断view是否可以拖拽  区分多点触控
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == vMainContent; //主面板可以拖拽
        }

        //根据建议值修正将要移动到哪个位置
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //left新位置建议值
            //dx 位置变化量
            if (child == vMainContent) {
                if (left < 0) {
                    return 0;
                } else if (left > leftMWidth) {
                    return leftMWidth;
                }
            }
            return left;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            invalidate(); //兼容低版本  每次修改重绘制
            execute();
        }

        //已经被捕获调用
        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
        }

        //当view被释放  执行动画
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (xvel == 0 && yvel == 0 && vMainContent.getLeft() > leftMWidth / 2.0f) {
                open();
            } else if (xvel > 0) {
                open();
            } else {
                close();
            }
        }

        //限定左右移动位置  仅仅决定对动画的执行速度
        @Override
        public int getViewHorizontalDragRange(View child) {
            return leftMWidth;
        }
    };

    private void execute(){
        float percent = vMainContent.getLeft() * 1.0f / leftMWidth;
        //执行动画
        executeAmimation(percent);
        //状态事件
        executeStatus(percent);
    }

    private void executeAmimation(float percent) {
        //左面板缩放、平移、透明动画
//        vLeftContent.setScaleX(0.5f*(percent+1));
//        vLeftContent.setScaleY(0.5f*(percent+1));
        ViewHelper.setScaleX(vLeftContent, EvaluateUtils.evaluate(percent, 0.5f, 1.0f));
        ViewHelper.setScaleY(vLeftContent, 0.5f * (percent + 1));
        ViewHelper.setTranslationX(vLeftContent, EvaluateUtils.evaluate(percent, 0 - screenWidth / 2, 0));
        ViewHelper.setAlpha(vLeftContent, EvaluateUtils.evaluate(percent, 0.5f, 1.0f));
        //主页面
        ViewHelper.setScaleX(vMainContent, EvaluateUtils.evaluate(percent, 1.0f, 0.8f));
        ViewHelper.setScaleY(vMainContent, EvaluateUtils.evaluate(percent, 1.0f, 0.8f));
        //背景层动画
        getBackground().setColorFilter((Integer) EvaluateUtils.evaluateColor(percent, Color.BLACK, Color.TRANSPARENT), PorterDuff.Mode.SRC_OVER);
    }

    private void executeStatus(float percent) {
        if (vDragStatusListener != null){
            vDragStatusListener.onDraging(percent);
        }
        Status lastStatus = vStatus;
        if (percent == 0){
            vStatus = Status.Close;
        }else if(percent == 1){
            vStatus = Status.Open;
        }else{
            vStatus = Status.Draging;
        }
        if (vStatus != lastStatus){
            //状态发生了变化
            if (vStatus == Status.Close){
                if (vDragStatusListener != null){
                    vDragStatusListener.onClose();
                }
            }else if (vStatus == Status.Open){
                if (vDragStatusListener != null){
                    vDragStatusListener.onOpen();
                }
            }
        }
    }

    //触摸事件
    //是否拦截事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //传递给sViewDragHelper
        return sViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //接受touchevent
        try {
            sViewDragHelper.processTouchEvent(event);
        } catch (Exception e) {
            //防止多点触摸出错
            System.out.println("触摸出错!");
        }
        //持续接收返回true
        return true;
    }

    //布局结束后调用
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //至少俩child
        if (getChildCount() >= 2) {
            if (getChildAt(0) instanceof ViewGroup && getChildAt(1) instanceof ViewGroup) {
                vLeftContent = (ViewGroup) getChildAt(0);
                vMainContent = (ViewGroup) getChildAt(1);
            }

        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = getMeasuredWidth();
        screenHeight = getMeasuredHeight();
        leftMWidth = (int) (screenWidth * 0.6f);
    }

    public void close() {
        int fLeft = 0;
        if (sViewDragHelper.smoothSlideViewTo(vMainContent, fLeft, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
//        int fTop = 0;
//        vMainContent.layout(fLeft, fTop, fLeft + screenWidth, fTop + screenHeight);
    }

    private void open() {
        int fLeft = leftMWidth;
        if(sViewDragHelper.smoothSlideViewTo(vMainContent, fLeft, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
//        int fTop = 0;
//        vMainContent.layout(fLeft, fTop, fLeft + screenWidth, fTop + screenHeight);
    }

    //持续平滑动画
    @Override
    public void computeScroll() {
        if (sViewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
