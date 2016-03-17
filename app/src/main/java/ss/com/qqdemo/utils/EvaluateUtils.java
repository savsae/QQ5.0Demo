package ss.com.qqdemo.utils;

import android.animation.ArgbEvaluator;

/**
 * 计算估值量
 */
public class EvaluateUtils {

    public static Float evaluate(float percent,Number startValue,Number endValue){
        return startValue.floatValue() + percent * (endValue.floatValue() - startValue.floatValue());
    }

    //颜色估值器
    public static Object evaluateColor(float percent,Object startValue,Object endValue){
        ArgbEvaluator argbEvaluator = new ArgbEvaluator();
        return argbEvaluator.evaluate(percent,startValue,endValue);
    }

    //int 估值器
    public static Integer evaluateInt(float percent,Integer startValue,Integer endValue){
        int startInt = startValue;
        return (int)(startInt + percent * (endValue - startInt));
    }

}
