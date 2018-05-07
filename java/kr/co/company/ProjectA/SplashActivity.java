package kr.co.company.ProjectA;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
    AQuery aq = new AQuery(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aq.ajax("http://letsgo.woobi.co.kr/junhyuk/medicine.php/", JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                if (object != null) {
                    try {
                        JSONArray results = object.getJSONArray("results");
                        CommonData.num = results.length();
                        CommonData.medicine_set = new ArrayList<Medicine>(); // DB에 등록한 약 개수만큼 data class의 객체 할당
                        for (int i = 0; i < results.length(); ++i) {
                            JSONObject temp = results.getJSONObject(i);
                            CommonData.medicine_set.add(i, new Medicine((String) temp.get("name"), (String) temp.get("categorize"), (String) temp.get("manufacturer"), (String) temp.get("appearance_info"), (String) temp.get("component_info"), (String) temp.get("save"), (String) temp.get("effect"), (String) temp.get("usage"), (String) temp.get("caution"), (String) temp.get("image"))); // i번째 data class 객체에 약 정보 등록
                        }
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    } catch (Exception e) {

                    }
                }
            }
        });

        setContentView(R.layout.activity_splash);
    }
}
