package ss.com.qqdemo.personcontact;


import java.util.ArrayList;
import java.util.Collections;

import ss.com.qqdemo.utils.ChineseToPinYinUtils;

public class PersonInfor implements Comparable {
    //汉字
    private String cName;
    //拼音
    private String eName;

    public PersonInfor(String cName, String eName) {
        this.cName = cName;
        this.eName = eName;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    //将名字汉字list处理成personInfor对象
    private static ArrayList<PersonInfor> dealChineses(String[] chineses){
        ArrayList<PersonInfor> arrayList = new ArrayList<PersonInfor>();
        for (String chinese : chineses){
            String eName = ChineseToPinYinUtils.chineseToPYAndGetFirst(chinese);
            PersonInfor pi = new PersonInfor(chinese,eName);
            arrayList.add(pi);
        }
        return arrayList;
    }

    //排序
    public static ArrayList<PersonInfor> fillAndSort(String[] chineses){
        ArrayList<PersonInfor> arrayList = dealChineses(chineses);
        Collections.sort(arrayList);
        return arrayList;
    }

    @Override
    public int compareTo(Object another) {
        PersonInfor pi = (PersonInfor) another;
        return this.eName.compareTo(pi.geteName());
    }

}
