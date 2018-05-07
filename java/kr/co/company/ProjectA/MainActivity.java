package kr.co.company.ProjectA;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ImageView mainAnimation;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainAnimation= (AnimatedImageView) findViewById(R.id.animation);
        mainAnimation.setImageResource(R.drawable.anim);

        ImageView pill = (ImageView)findViewById(R.id.button_pill);
        ImageView pharmacy = (ImageView)findViewById(R.id.button_pharmacy);
        pill.setOnTouchListener(mTouchListener);
        pharmacy.setOnTouchListener(mTouchListener);
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            ImageView v = (ImageView)view;
            BitmapDrawable img = null;

            int act = motionEvent.getAction();

            if(act == MotionEvent.ACTION_DOWN){
                if(v.getId() == R.id.button_pharmacy){
                    img = (BitmapDrawable)getResources().getDrawable(R.drawable.button_pharmacy_big);
                    startActivity(new Intent(MainActivity.this, NearPharmacyActivity.class));
                }
                else if(v.getId() == R.id.button_pill){
                    img = (BitmapDrawable)getResources().getDrawable(R.drawable.button_pill_big);
                    startActivity(new Intent(MainActivity.this, PillActivity.class));
                }
                v.setImageDrawable(img);
            }
            else if(act == MotionEvent.ACTION_UP){
                if(v.getId() == R.id.button_pharmacy){
                    img = (BitmapDrawable)getResources().getDrawable(R.drawable.button_pharmacy);
                }
                else if(v.getId() == R.id.button_pill){
                    img = (BitmapDrawable)getResources().getDrawable(R.drawable.button_pill);
                }
                v.setImageDrawable(img);
            }
            return true;
        }
    };

    public void onBackPressed(){
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            finish();
        }
        else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "\'뒤로\' 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
