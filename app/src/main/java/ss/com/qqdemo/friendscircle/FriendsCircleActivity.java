package ss.com.qqdemo.friendscircle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import ss.com.qqdemo.R;

public class FriendsCircleActivity extends AppCompatActivity {

    @Bind(R.id.fclv)
    FriendsCircleListView fcLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_circle);
        initView();
    }

    private void initView() {
        ButterKnife.bind(this);
        fcLv.setAdapter(new ArrayAdapter<String>(FriendsCircleActivity.this, android.R.layout.simple_list_item_1, new String[]{"圈1", "圈2", "圈3"}));
        final View mHeaderView  = View.inflate(this,R.layout.friends_circle_head_layout,null);
        fcLv.addHeaderView(mHeaderView);
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //当图形元素加载完毕的时候此方法会调用
                fcLv.setKHeadView((ImageView) mHeaderView.findViewById(R.id.fcheadimg));
                fcLv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }
}
