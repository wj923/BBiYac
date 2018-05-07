package kr.co.company.ProjectA;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MedicineInfoActivity extends AppCompatActivity{
    Medicine medicine_detail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFFFCCE3B));
        actionBar.setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.back);
        //upArrow.setColorFilter(getResources().getColor(R.color.Black), PorterDuff.Mode.SRC_ATOP);
        actionBar.setHomeAsUpIndicator(upArrow);

        final LinearLayout simple = (LinearLayout) findViewById(R.id.simple);
        final ScrollView detail = (ScrollView) findViewById(R.id.detail);

        final TextView detail_info = (TextView) findViewById(R.id.medicine_info);
        final TextView pill_name = (TextView) findViewById(R.id.pill_name);
        final TextView group = (TextView) findViewById(R.id.group);
        final TextView made_in = (TextView) findViewById(R.id.made_in);
        final TextView appearance = (TextView) findViewById(R.id.appearance);
        final TextView component = (TextView) findViewById(R.id.component);
        final TextView save = (TextView) findViewById(R.id.save);

        ImageView iv = (ImageView) findViewById(R.id.medicine_image);
        final Button infoButton = (Button) findViewById(R.id.info); // 약 정보 및 저장방법 버튼
        final Button effectButton = (Button) findViewById(R.id.effect); // 약 효능효과 버튼
        final Button usageButton = (Button) findViewById(R.id.usage); // 약 용법용량 버튼
        final Button cautionButon = (Button) findViewById(R.id.caution); // 약 사용상 주의사항 버튼
        Intent intent = getIntent();
        medicine_detail = (Medicine) intent.getSerializableExtra("MEDICINE");
        Glide.with(this).load(medicine_detail.data_medicine_image).into(iv);

        actionBar.setTitle("상세 정보 - " + medicine_detail.data_medicine_name);

        pill_name.setText(medicine_detail.data_medicine_name);
        group.setText(medicine_detail.data_medicine_categorize);
        made_in.setText(medicine_detail.data_medicine_manufacturer);
        appearance.setText(medicine_detail.data_medicine_appearance_info);
        component.setText(medicine_detail.data_medicine_component_info);
        save.setText(medicine_detail.data_medicine_save);

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoButton.setBackgroundResource(R.drawable.round_left_button_selected);
                effectButton.setBackgroundResource(R.drawable.middle_button);
                usageButton.setBackgroundResource(R.drawable.middle_button);
                cautionButon.setBackgroundResource(R.drawable.round_right_button);
                simple.setVisibility(ScrollView.VISIBLE);
                detail.setVisibility(ScrollView.GONE);
            }
        });
        effectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoButton.setBackgroundResource(R.drawable.round_left_button);
                effectButton.setBackgroundResource(R.drawable.middle_button_selected);
                usageButton.setBackgroundResource(R.drawable.middle_button);
                cautionButon.setBackgroundResource(R.drawable.round_right_button);
                detail_info.setText(medicine_detail.data_medicine_effect);
                simple.setVisibility(ScrollView.GONE);
                detail.setVisibility(ScrollView.VISIBLE);
            }
        });
        usageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoButton.setBackgroundResource(R.drawable.round_left_button);
                effectButton.setBackgroundResource(R.drawable.middle_button);
                usageButton.setBackgroundResource(R.drawable.middle_button_selected);
                cautionButon.setBackgroundResource(R.drawable.round_right_button);
                detail_info.setText(medicine_detail.data_medicine_usage);
                simple.setVisibility(ScrollView.GONE);
                detail.setVisibility(ScrollView.VISIBLE);
            }
        });
        cautionButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoButton.setBackgroundResource(R.drawable.round_left_button);
                effectButton.setBackgroundResource(R.drawable.middle_button);
                usageButton.setBackgroundResource(R.drawable.middle_button);
                cautionButon.setBackgroundResource(R.drawable.round_right_button_selected);
                detail_info.setText(medicine_detail.data_medicine_caution);
                simple.setVisibility(ScrollView.GONE);
                detail.setVisibility(ScrollView.VISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_button) {
            Intent mapIntent = new Intent(MedicineInfoActivity.this, NearPharmacyActivity.class);
            startActivity(mapIntent);
            return true;
        }

        if (id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
