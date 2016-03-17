package ss.com.qqdemo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ss.com.qqdemo.R;
import ss.com.qqdemo.view.SwipeLayout;

/**
 * 主页面侧滑listview adapter
 */
public class SwipeAdapter extends BaseAdapter{
    private static final String[] NAMES = {"张三","王五","李强"};
    private Context cx;
    private ArrayList<SwipeLayout> openItems;
    public SwipeAdapter(Context cx) {
        this.cx = cx;
        openItems = new ArrayList<SwipeLayout>();
    }

    @Override
    public int getCount() {
        return NAMES.length;
    }

    @Override
    public Object getItem(int position) {
        return NAMES[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(convertView==null){
            v=View.inflate(cx, R.layout.swipe_layout,null);
        }
        ViewHolder vh = ViewHolder.getHolder(v);
        SwipeLayout sl = (SwipeLayout) v;
        sl.setOnSwipeLayoutStatusListener(new SwipeLayout.OnSwipeLayoutStatusListener() {
            @Override
            public void onClose(SwipeLayout swipeLayout) {
                //被关闭移除集合
                openItems.remove(swipeLayout);
            }

            @Override
            public void onOpen(SwipeLayout swipeLayout) {
                openItems.add(swipeLayout);//记录当前打开的item
            }

            @Override
            public void onDraging(SwipeLayout swipeLayout) {

            }

            @Override
            public void onStartClose(SwipeLayout swipeLayout) {

            }

            @Override
            public void onStartOpen(SwipeLayout swipeLayout) {
                //先遍历开启条目，逐个关闭
                for (SwipeLayout sl : openItems) {
                    sl.close();
                }
                openItems.clear();
            }
        });
        return v;
    }

    private static class ViewHolder{
        private TextView fightTV,deleteTV;
        private ImageView titleIV;
        private TextView nameTV;

        public static ViewHolder getHolder(View v) {
            Object tag = v.getTag();
            if(tag!=null){
                return (ViewHolder)tag;
            }else{
                ViewHolder vh = new ViewHolder();
                vh.fightTV = (TextView) v.findViewById(R.id.fighttv);
                vh.deleteTV = (TextView) v.findViewById(R.id.deletetv);
                vh.titleIV = (ImageView) v.findViewById(R.id.left_title);
                vh.nameTV = (TextView) v.findViewById(R.id.tv_name);
                tag = vh;
                v.setTag(vh);
                return (ViewHolder) tag;
            }
        }
    }
}
