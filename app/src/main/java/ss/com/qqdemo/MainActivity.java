package ss.com.qqdemo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.CycleInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import ss.com.qqdemo.drag.DragLayoutActivity;
import ss.com.qqdemo.friendscircle.FriendsCircleActivity;
import ss.com.qqdemo.personcontact.PersonContactActivity;
import ss.com.qqdemo.utils.EvaluateUtils;
import ss.com.qqdemo.view.MainContentLineayLayout;
import ss.com.qqdemo.view.SwipeLayout;

public class MainActivity extends AppCompatActivity {

    private DragLayoutActivity dragLayout;
    private MainContentLineayLayout maincontentlilayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        final ListView leftContentListView = (ListView) findViewById(R.id.leftContentList);
        final ImageView contentHead = (ImageView) findViewById(R.id.mainHead);
        final ImageView leftHead = (ImageView) findViewById(R.id.leftHead);
        maincontentlilayout = (MainContentLineayLayout) findViewById(R.id.maincontentlilayout);
        dragLayout = (DragLayoutActivity) findViewById(R.id.draglayout);
        maincontentlilayout.setDragLayoutActivity(dragLayout);
        dragLayout.setDragStatusListener(new DragLayoutActivity.OnDragStatusListener() {
            @Override
            public void onClose() {
                //图标晃动
                //属性动画
                ObjectAnimator oa = ObjectAnimator.ofFloat(contentHead, "translationX", 3.0f);
                oa.setInterpolator(new CycleInterpolator(2));
                oa.setDuration(1000);
                oa.start();
            }

            @Override
            public void onOpen() {
            }

            @Override
            public void onDraging(float percent) {
                //更新透明度
                ViewHelper.setAlpha(leftHead, EvaluateUtils.evaluate(percent, 0.0f, 1.0f));
                ViewHelper.setAlpha(contentHead, EvaluateUtils.evaluate(percent, 1.0f, 0.0f));
            }
        });
        leftContentListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"QQ会员", "QQ钱包", "个性装扮", "我的收藏", "我的相册", "朋友圈", "联系人"}) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView v = (TextView) super.getView(position, convertView, parent);
                v.setTextColor(Color.WHITE);
                return v;
            }
        });
        leftContentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 6: //联系人
                        Intent intent = new Intent(MainActivity.this, PersonContactActivity.class);
                        startActivity(intent);
                        break;
                    case 5://朋友圈
                        Intent intentFriC = new Intent(MainActivity.this, FriendsCircleActivity.class);
                        startActivity(intentFriC);
                        break;
                }
            }
        });
        SwipeLayout maincontentlilayout = (SwipeLayout) findViewById(R.id.maincontentlilayout);
        maincontentlilayout.setOnSwipeLayoutStatusListener(new SwipeLayout.OnSwipeLayoutStatusListener() {

            @Override
            public void onClose(SwipeLayout swipeLayout) {

            }

            @Override
            public void onOpen(SwipeLayout swipeLayout) {

            }

            @Override
            public void onDraging(SwipeLayout swipeLayout) {

            }

            @Override
            public void onStartClose(SwipeLayout swipeLayout) {

            }

            @Override
            public void onStartOpen(SwipeLayout swipeLayout) {

            }
        });
    }
}
