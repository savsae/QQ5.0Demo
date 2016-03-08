package ss.com.qqdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 联系人的引导条
 */
public class QuickIndexBar extends View{

    private Paint mPaint;
    private int screenWidth;
    private int screenHeight;
    private float cellWidth,cellHeight;
    private OnLetterUpdateListener onLetterUpdateListener;
    private int lastIndex = -1;  //最后一次点的字母

    private static String[] letters={
        "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","#"
    };

    public OnLetterUpdateListener getOnLetterUpdateListener() {
        return onLetterUpdateListener;
    }

    public void setOnLetterUpdateListener(OnLetterUpdateListener onLetterUpdateListener) {
        this.onLetterUpdateListener = onLetterUpdateListener;
    }

    //向外界暴漏选中的字母
    public interface OnLetterUpdateListener{
        void onLetterUpdate(String letter);
    }

    public QuickIndexBar(Context context) {
        this(context,null);
    }

    public QuickIndexBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(30);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int count = letters.length;
        float x,y;
        for(int i = 0; i<count; i++){
            String text = letters[i];
            x = cellWidth / 2.0f - mPaint.measureText(text)/2.0f;
            Rect bounds = new Rect();
            mPaint.getTextBounds(text,0,text.length(),bounds);
            y = cellHeight/2.0f + bounds.height()/2.0f + i * cellHeight;
            mPaint.setColor((lastIndex==i)?Color.RED:Color.WHITE);
            canvas.drawText(text, x, y, mPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = getMeasuredWidth();
        screenHeight = getMeasuredHeight();
        cellWidth = screenWidth;
        cellHeight = (screenHeight*1.0f)/letters.length;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index = 0;
        switch (MotionEventCompat.getActionMasked(event)){
            case MotionEvent.ACTION_DOWN:
                index = (int) (event.getY()/cellHeight);
                if(index>=0&&index<letters.length&&index!=lastIndex){
                    if (onLetterUpdateListener!=null){
                        onLetterUpdateListener.onLetterUpdate(letters[index]);
                    }
                    lastIndex = index;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                index = (int) (event.getY()/cellHeight);
                if(index>=0&&index<letters.length&&index!=lastIndex){
                    if (onLetterUpdateListener!=null){
                        onLetterUpdateListener.onLetterUpdate(letters[index]);
                    }
                    lastIndex = index;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        invalidate();  //重新绘制
        return true;
    }
}
