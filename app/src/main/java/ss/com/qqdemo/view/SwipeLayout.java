package ss.com.qqdemo.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 侧滑删除实现
 */
public class SwipeLayout extends FrameLayout{

    private ViewDragHelper viewDragHelper;
    private View leftll,rightll;
    private int viewHeight,viewLWidth,viewRWidth;

    private OnSwipeLayoutStatusListener onSwipeLayoutStatusListener;

    public void setOnSwipeLayoutStatusListener(OnSwipeLayoutStatusListener onSwipeLayoutStatusListener) {
        this.onSwipeLayoutStatusListener = onSwipeLayoutStatusListener;
    }

    public enum Status{
        Close,Open,Draging
    }

    private Status curStatus = Status.Close;

    public Status getCurStatus() {
        return curStatus;
    }

    public void setCurStatus(Status curStatus) {
        this.curStatus = curStatus;
    }

    public interface OnSwipeLayoutStatusListener {
        void onClose(SwipeLayout swipeLayout);
        void onOpen(SwipeLayout swipeLayout);
        void onDraging(SwipeLayout swipeLayout);
        void onStartClose(SwipeLayout swipeLayout);
        void onStartOpen(SwipeLayout swipeLayout);
    }

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        viewDragHelper = ViewDragHelper.create(this, 1.0f, mCallBack);
    }

    ViewDragHelper.Callback mCallBack = new ViewDragHelper.Callback(){

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == leftll){
                if (left>0){
                    return 0;
                }else if(left<-viewRWidth){
                    return -viewRWidth;
                }
            }else if(child==rightll){
                if (left>viewLWidth){
                    return viewLWidth;
                }else if(left < viewLWidth - viewRWidth){
                    return viewLWidth - viewRWidth;
                }
            }
            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if(changedView == leftll){
                rightll.offsetLeftAndRight(dx);
            }else if(changedView == rightll){
                leftll.offsetLeftAndRight(dx);
            }
            Log.e("positionchanged",String.valueOf(left));
            dispatchSwipeEvent();
            invalidate();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if(releasedChild == leftll && xvel == 0 && releasedChild.getLeft()<-viewRWidth/2.0f){
                open();
            }else if(xvel<0){
                open();
            }else{
                close();
            }
        }
    };

    private void dispatchSwipeEvent() {
        onSwipeLayoutStatusListener.onDraging(this);
        //记录上一次状态
        Status lastStatus = curStatus;
        //分发事件
        curStatus = updateStatus(); //更新当前状态
        Log.e("status", curStatus.toString());
        if (onSwipeLayoutStatusListener!=null&&curStatus!=lastStatus){
            if(curStatus==Status.Close){
                onSwipeLayoutStatusListener.onClose(this);
            }else if(curStatus==Status.Open){
                onSwipeLayoutStatusListener.onOpen(this);
            }else if(curStatus==Status.Draging){
                if (lastStatus==Status.Close){
                    onSwipeLayoutStatusListener.onStartOpen(this);
                }else {
                    onSwipeLayoutStatusListener.onStartClose(this);
                }
            }
        }
    }

    private Status updateStatus() {
//        Log.e("left",String.valueOf(leftll.getLeft())+"=="+String.valueOf(viewRWidth));
//        int left = leftll.getLeft();
//        Status tmpStatus = Status.Draging;
//        if(left==0){
//            tmpStatus =  Status.Close;
//        }else if(left==-viewRWidth){
//            tmpStatus =  Status.Open;
//        }
//        Log.e("状态",tmpStatus.toString());
//        return tmpStatus;
        return (leftll.getLeft()==0)?Status.Close:((leftll.getLeft()==-viewRWidth)?Status.Open:Status.Draging);
    }

    private void layoutContent(boolean isOpen){
        layoutLeft(isOpen);
        layoutRight(isOpen);
    }

    public void close() {
        Rect leftL = computerLeftRect(false);
        //平滑动画
        if(viewDragHelper.smoothSlideViewTo(leftll,leftL.left,0)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void open() {
        Rect leftL = computerLeftRect(true);
        //平滑动画
        if(viewDragHelper.smoothSlideViewTo(leftll,leftL.left,0)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(viewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            viewDragHelper.processTouchEvent(event);
        }catch (Exception e){

        }
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        layoutContent(false);
        bringChildToFront(leftll);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        rightll = getChildAt(0);
        leftll = getChildAt(1);
    }

    private void layoutLeft(boolean isOpen){
        Rect leftL = computerLeftRect(isOpen);
        leftll.layout(leftL.left,leftL.top,leftL.right,leftL.bottom);
    }

    private Rect computerLeftRect(boolean isOpen) {
        int nLeft = 0;
        if (isOpen){
            nLeft = -viewRWidth;
        }
        return new Rect(nLeft,0,nLeft+viewLWidth,viewHeight);
    }

    private void layoutRight(boolean isOpen){
        Rect leftR = computerRighttRect(isOpen);
        rightll.layout(leftR.left, leftR.top, leftR.right, leftR.bottom);
    }

    private Rect computerRighttRect(boolean isOpen) {
        int nLeft = viewLWidth;
        if (isOpen){
            nLeft =  viewLWidth - viewRWidth;
        }
        return new Rect(nLeft,0,nLeft+viewRWidth,viewHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        viewLWidth = leftll.getMeasuredWidth();
        viewHeight = leftll.getMeasuredHeight();
        viewRWidth = rightll.getMeasuredWidth();
    }

}
