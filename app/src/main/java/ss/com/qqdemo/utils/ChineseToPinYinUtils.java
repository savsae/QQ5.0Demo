package ss.com.qqdemo.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 汉字转拼音
 */
public class ChineseToPinYinUtils {
    //将汉语搞成拼音并且得到姓氏拼音（第一个汉语）
    public static String chineseToPYAndGetFirst(String chinese){
        HanyuPinyinOutputFormat hanyuPinyinOutputFormat = new HanyuPinyinOutputFormat();
        hanyuPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  //音调
        char[] chars = chinese.toCharArray();
        int count = chars.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0;i < count;i++) {
            char c = chars[i];
            if(Character.isWhitespace(c)){
                continue;
            }
            if(c>=-127&&c<128){
                sb.append(c);
            }else {
                try {
                    String[] pinYin = PinyinHelper.toHanyuPinyinStringArray(c, hanyuPinyinOutputFormat);
                    sb.append(pinYin[0]);
                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                    badHanyuPinyinOutputFormatCombination.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
