package ss.com.qqdemo.personcontact;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ss.com.qqdemo.R;
import ss.com.qqdemo.adapter.PersonContactListAdapter;
import ss.com.qqdemo.view.QuickIndexBar;

public class PersonContactActivity extends AppCompatActivity {

    private QuickIndexBar quickIndexBar;
    private ListView nameListView;
    private TextView show_select;
    private ArrayList<PersonInfor> pis;
    private static final String[] MING_ZI = {"小","李","飞","刀","古","张","李**$##","小","李","飞","刀","古","张","李**$##","小","李","飞","刀","古","张","李**$##"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_contact);
        initViews();
    }

    private void initViews(){
        nameListView = (ListView) findViewById(R.id.nameListView);
        quickIndexBar = (QuickIndexBar) findViewById(R.id.quickindexbar);
        show_select = (TextView) findViewById(R.id.show_select);
        pis = PersonInfor.fillAndSort(MING_ZI);
        quickIndexBar.setOnLetterUpdateListener(new QuickIndexBar.OnLetterUpdateListener() {
            @Override
            public void onLetterUpdate(String letter) {
                //根据选择的字母跳转到指定位置
                showLetter(letter);
                for (PersonInfor pi : pis) {
                    String f = pi.geteName().charAt(0) + "";
                    if (f.equalsIgnoreCase(letter)) {
                        int index = pis.indexOf(pi);
                        nameListView.setSelection(index);
                        break;
                    }
                }
            }
        });
        nameListView.setAdapter(new PersonContactListAdapter(this, pis));
    }

    private Handler h = new Handler();

    private void showLetter(String letter) {
        show_select.setVisibility(View.VISIBLE);
        show_select.setText(letter);
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                show_select.setVisibility(View.GONE);
            }
        },1500);
    }

}
