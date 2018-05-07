package kr.co.company.ProjectA;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PharmacyInfoActivity extends FragmentActivity {
    private GoogleMap mMap;

    Pharmacy pharmacy_detail = null;
    Button pharmacyLocation = null;

    Double latitude; // 약국 위도
    Double longitude; // 약국 경도

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pharmacy_info);

        Display display = ((WindowManager) getSystemService(getApplicationContext().WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int) (display.getWidth() * 0.9); // Display 사이즈의 90%
        int height = (int) (display.getHeight() * 0.9);  //Display 사이즈의 90%
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

        Intent intent = getIntent();
        pharmacy_detail = (Pharmacy) intent.getSerializableExtra("PHARMACY");

        latitude = pharmacy_detail.data_pharmacy_latitude;
        longitude = pharmacy_detail.data_pharmacy_longitude;

        setMap();

        TextView name = (TextView)findViewById(R.id.name);
        TextView address = (TextView)findViewById(R.id.address);
        TextView tel = (TextView)findViewById(R.id.tel);
        TextView time = (TextView)findViewById(R.id.time);
        TextView directions = (TextView)findViewById(R.id.directions);
        directions.setSingleLine(true);
        directions.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        directions.setSelected(true);
        TextView info = (TextView)findViewById(R.id.info);
        TextView etc = (TextView)findViewById(R.id.etc);

        name.setText(pharmacy_detail.data_pharmacy_name);
        address.setText(pharmacy_detail.data_pharmacy_address);
        tel.setText(pharmacy_detail.data_pharmacy_tel);
        if(pharmacy_detail.data_pharmacy_mapimg.equals("")){
            TextView as = (TextView)findViewById(R.id.directions_assistant);
            as.setTextSize(0);
            directions.setTextSize(0);
        }
        directions.setText(pharmacy_detail.data_pharmacy_mapimg);
        if(pharmacy_detail.data_pharmacy_Info.equals(""))
            info.setTextSize(0);
        if(pharmacy_detail.data_pharmacy_etc.equals(""))
            etc.setTextSize(0);
        info.setText(pharmacy_detail.data_pharmacy_Info);
        etc.setText(pharmacy_detail.data_pharmacy_etc);

        String []devide = pharmacy_detail.data_pharmacy_time.split("\\n");
        StringBuffer sb;
        String pharmacy_time ="";
        for(int i=1; i<devide.length; i++){
            sb = new StringBuffer(devide[i]);
            sb.insert(8,':');
            sb.insert(16,':');
            pharmacy_time += sb.toString();
            if(i != devide.length-1)
                pharmacy_time += "\n";
        }
        time.setText(pharmacy_time);
        ImageView exit = (ImageView) findViewById(R.id.exit_window);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void setMap(){
        GooglePlayServicesUtil.isGooglePlayServicesAvailable(PharmacyInfoActivity.this);
        mMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        LatLng address = new LatLng(latitude,longitude);

        MarkerOptions optFirst = new MarkerOptions();
        optFirst.position(address);
        optFirst.icon(BitmapDescriptorFactory.fromResource(R.drawable.here));
        mMap.addMarker(optFirst).showInfoWindow();

        final CameraPosition cp = new CameraPosition.Builder().target((address)).zoom(16).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        pharmacyLocation = (Button)findViewById(R.id.my_location);
        pharmacyLocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
                return false;
            }
        });
    }
}
