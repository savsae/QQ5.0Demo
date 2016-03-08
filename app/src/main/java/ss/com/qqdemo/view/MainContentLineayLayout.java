package ss.com.qqdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import ss.com.qqdemo.drag.DragLayoutActivity;

/**
 自定义主界面的linearLayout  主要作用是当左边界面已经被打开，组织主窗体滑动
 */
public class MainContentLineayLayout extends LinearLayout{

    private DragLayoutActivity dragLayoutActivity;

    public void setDragLayoutActivity(DragLayoutActivity dragLayoutActivity) {
        this.dragLayoutActivity = dragLayoutActivity;
    }

    public MainContentLineayLayout(Context context) {
        this(context, null);
    }

    public MainContentLineayLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainContentLineayLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (dragLayoutActivity.getvStatus()== DragLayoutActivity.Status.Close){
            return super.onInterceptTouchEvent(ev);
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (dragLayoutActivity.getvStatus()== DragLayoutActivity.Status.Close){
            return super.onTouchEvent(event);
        }
        if (event.getAction() == MotionEvent.ACTION_UP){
            dragLayoutActivity.close();
        }
        return true;
    }
}
