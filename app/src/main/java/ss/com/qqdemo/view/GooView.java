package ss.com.qqdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * qq列表未读消息拖拽view
 */
public class GooView extends View{
    private Paint aPaint;
    public GooView(Context context) {
        this(context,null);
    }

    public GooView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GooView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        aPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        aPaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //链接部分
        Path aPath = new Path();
        aPath.moveTo(250f,250f);
        aPath.quadTo(150f, 300f, 50f, 250f);
        aPath.lineTo(50f,350f);
        aPath.quadTo(150f,300f,250f,350f);
        aPath.close();;
        canvas.drawPath(aPath,aPaint);
        //拖拽圆
        canvas.drawCircle(80f, 80f, 20f, aPaint);
        //固定圆
        canvas.drawCircle(150f, 150f, 14f, aPaint);
    }
}
