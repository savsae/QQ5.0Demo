package ss.com.qqdemo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ss.com.qqdemo.R;
import ss.com.qqdemo.personcontact.PersonInfor;

/**
 * 通讯录list迭代器
 */
public class PersonContactListAdapter extends BaseAdapter {
    private Context cx;
    private ArrayList<PersonInfor> personInfors;

    public PersonContactListAdapter(Context cx, ArrayList<PersonInfor> personInfors) {
        this.cx = cx;
        this.personInfors = personInfors;
    }

    private static class ViewHolder {
        TextView title;
        TextView name;
    }

    @Override
    public int getCount() {
        return personInfors.size();
    }

    @Override
    public Object getItem(int position) {
        return personInfors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(cx, R.layout.person_contact_item, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.item_head);
            viewHolder.name = (TextView) convertView.findViewById(R.id.item_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PersonInfor pi = personInfors.get(position);
        String showStr = "";
        if (pi != null) {
            //根据上一个字母决定title是否显示
            String currTitleFStr = pi.geteName().charAt(0) + "";
            if(position == 0){
                showStr = currTitleFStr;
            }else {
                showStr = personInfors.get(position - 1).geteName().charAt(0) + "";
            }
            if (currTitleFStr.equals(showStr)&&position!=0){
                showStr = "";
            }else{
                showStr = currTitleFStr;
            }
            if (TextUtils.equals(showStr,"")) {
                viewHolder.title.setVisibility(View.GONE);
            }else{
                viewHolder.title.setVisibility(View.VISIBLE);
                viewHolder.title.setText(showStr);
            }
            viewHolder.name.setText(pi.getcName());
        }
        return convertView;
    }
}
