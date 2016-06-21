package cn.carbs.android.indicatorview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ActivityMain extends AppCompatActivity implements View.OnClickListener{

    private Button mButton1;
    private Button mButton2;
    private Button mButton3;
    private Button mButton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton1 = (Button)this.findViewById(R.id.button_1);
        mButton2 = (Button)this.findViewById(R.id.button_2);
        mButton3 = (Button)this.findViewById(R.id.button_3);
        mButton4 = (Button)this.findViewById(R.id.button_4);

        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_1:
                gotoActivity(1);
                break;
            case R.id.button_2:
                gotoActivity(2);
                break;
            case R.id.button_3:
                gotoActivity(3);
                break;
            case R.id.button_4:
                gotoActivity(4);
                break;
        }
    }

    private void gotoActivity(int index){
        Class c = null;
        switch (index){
            case 1:
                c = ActivitySimple.class;
                break;
            case 2:
                c = ActivityNormalEven.class;
                break;
            case 3:
                c = ActivityNormalUneven.class;
                break;
            case 4:
                c = ActivityAll.class;
                break;
        }
        Intent it = new Intent(this, c);
        startActivity(it);
    }
}