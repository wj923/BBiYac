package kr.co.company.ProjectA;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;

public class NearPharmacyActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener {
    public final int MY_LOCATION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private Double latitude;
    private Double longitude;
    private GPSInfo myGps;
    private LatLng myLocation;
    private MarkerOptions pMarkerOption;
    private Marker[] pMarker;
    private String userAdminArea; // 요청변수 Q0(시도)
    private String userLocality; // 요청변수 Q1(시군구)
    private int i;
    private Button myLocationBt, runPharmacyBt;
    private Context mContext;
    private RecyclerView recyclerView_pharmacy;
    private MyRecycleAdapterPharmacySearch pharmacyAdapter;
    private ArrayList<Pharmacy> pharmacyList = new ArrayList<Pharmacy>();
    private SupportMapFragment mapFragment;
    private RelativeLayout pharmacyWindow;
    boolean isUp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_pharmacy);
        myLocationBt = (Button) findViewById(R.id.my_location);
        runPharmacyBt = (Button) findViewById(R.id.run_pharmacy);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) { // 승인
                Toast.makeText(getApplicationContext(), "GPS 권한을 허용하였습니다.", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(NearPharmacyActivity.this, NearPharmacyActivity.class));
            } else { // 거부
                Toast.makeText(getApplicationContext(), "GPS 권한을 허용하지 않으셨습니다.", Toast.LENGTH_SHORT).show();
            }
        }
        return;
    }

    @Override
    protected void onStart() {
        super.onStart();
        myGps = new GPSInfo(NearPharmacyActivity.this);
        if (!myGps.isGetLocation) {
            myGps.showSettingsAlert();
        } else {
            latitude = myGps.getLatitude();
            longitude = myGps.getLongitude();
            myGps.getAddress(latitude, longitude);
            userAdminArea = myGps.AdminArea;
            userLocality = myGps.Locality;
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this); // 지도 셋팅
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(new Intent(NearPharmacyActivity.this, NearPharmacyActivity.class));
    }

    @Override
    protected void onDestroy() { // 액티비티 종료 시 GPS 종료
        super.onDestroy();
        myGps.stopUsingGPS();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) { // 첫 화면(내 위치)
        mMap = googleMap;
        myLocation = new LatLng(latitude, longitude);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) { // 허용을 거부했을 때 첫 화면
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_LOCATION_REQUEST_CODE);
            } else { // 허용이 안되있다면 첫 화면
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_LOCATION_REQUEST_CODE);
            }
        }
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16)); // 줌
        myLocationBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CameraPosition cp = new CameraPosition.Builder().target(myLocation).zoom(mMap.getCameraPosition().zoom).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
            }
        });
        runPharmacyBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NearPharmacyActivity.this, NowRunPharmacyActivity.class));
            }
        });
        if (userAdminArea != null && userLocality != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    GettingXML xmlParser = new GettingXML(pharmacyList, userAdminArea, userLocality, CommonData.serviceKey, "ADDR", latitude, longitude, 1);
                    pharmacyList = xmlParser.getXmlData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pharmacyList();
                        }
                    });
                }
            }).start();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) { // 마커 클릭 시 약국 위치 이동 이벤트
        Pharmacy markerLocation = null;
        for (i = 0; i<pharmacyList.size(); i++) {
            if (marker.getTitle().equals(pharmacyList.get(i).data_pharmacy_name)) {
                markerLocation = pharmacyList.get(i);
                break;
            }
            if (marker.getTitle().equals("현재 위치")) {
                return false;
            }
        }

        final CameraPosition cp = new CameraPosition.Builder().target((new LatLng(markerLocation.
                data_pharmacy_latitude, markerLocation.data_pharmacy_longitude))).zoom(mMap.getCameraPosition().zoom).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
        marker.showInfoWindow();
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) { // 마커 말풍선 클릭 시 약국 정보 팝업 이벤트
        Pharmacy markerInfo = null;
        for (i = 0; i<pharmacyList.size(); i++) {
            if (marker.getTitle().equals(pharmacyList.get(i).data_pharmacy_name)) {
                markerInfo = pharmacyList.get(i);
                break;
            }
            if (marker.getTitle().equals("현재 위치")) {
                return;
            }
        }
        Intent intent = new Intent(NearPharmacyActivity.this, PharmacyInfoActivity.class);
        intent.putExtra("PHARMACY", markerInfo);
        startActivity(intent);
        return;
    }

    private void pharmacyList() {
        PharmacyArraySort srt = new PharmacyArraySort();
        Collections.sort(pharmacyList, srt); // 거리순 sort
        pMarker = new Marker[pharmacyList.size()];
        for (i=0; i<pharmacyList.size(); i++) { // 약국들 마커 표시
            pMarkerOption = new MarkerOptions();
            pMarkerOption.position(new LatLng(pharmacyList.get(i).data_pharmacy_latitude, pharmacyList.get(i).data_pharmacy_longitude));
            pMarkerOption.title(pharmacyList.get(i).data_pharmacy_name); // 약국 마커 타이틀(이름)
            pMarkerOption.snippet(pharmacyList.get(i).data_pharmacy_distance); // 약국 마커 서브타이틀(사용자로부터 거리)
            pMarker[i] = mMap.addMarker(pMarkerOption);
        }
        mMap.setOnMarkerClickListener(this); // 마커 클릭 이벤트 추가
        mMap.setOnInfoWindowClickListener(this); // 마커 말풍선 클릭 이벤트 추가
        mContext = getApplicationContext();
        recyclerView_pharmacy = (RecyclerView) findViewById(R.id.pharmacy);
        recyclerView_pharmacy.setHasFixedSize(true);
        recyclerView_pharmacy.setLayoutManager(new LinearLayoutManager(
                        getBaseContext(), LinearLayoutManager.VERTICAL, false
                )
        );
        recyclerView_pharmacy.getRecycledViewPool(); // clear 후 다시 add (약국 정보 팝업 후 재실행 때문)
        pharmacyAdapter = new MyRecycleAdapterPharmacySearch(mContext, pharmacyList);
        pharmacyAdapter.notifyDataSetChanged(); // clear 후 다시 add (약국 정보 팝업 후 재실행 때문)
        recyclerView_pharmacy.setAdapter(pharmacyAdapter);
    }

    class MyRecycleAdapterPharmacySearch extends RecyclerView.Adapter<MyRecycleAdapterPharmacySearch.ViewHolder> { // 약국 검색 어댑터
        private Context context;
        private LayoutInflater inflater;
        private ArrayList<Pharmacy> pharmacy;

        public MyRecycleAdapterPharmacySearch(Context context, ArrayList<Pharmacy> pharmacy) {
            this.context = context;
            this.pharmacy = pharmacy;
            inflater = LayoutInflater.from(mContext);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemPosition = recyclerView_pharmacy.getChildPosition(v);
                    final Pharmacy item = pharmacy.get(itemPosition);
                    int tmp = 0;
                    for (i = 0; i<pMarker.length; i++) {
                        if (pMarker[i].getTitle().equals(item.data_pharmacy_name)) {
                            tmp = i; break;
                        }
                    }
                    pMarker[tmp].showInfoWindow();
                    if(isUp == true){
                        final Button upDownButton = (Button) findViewById(R.id.up_down_button);
                        isUp = false;
                        Animation list_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.list_down);
                        list_down.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                upDownButton.setEnabled(false);
                            }
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Display display = ((WindowManager) getSystemService(getApplicationContext().WINDOW_SERVICE)).getDefaultDisplay();
                                        final int height = (int) (display.getHeight() * 0.8);
                                        pharmacyWindow.setTranslationY(height);
                                    }
                                },2);
                                upDownButton.setBackgroundResource(R.drawable.list_up_button);
                                upDownButton.setEnabled(true);
                                final CameraPosition cp = new CameraPosition.Builder().target((new LatLng(item.
                                        data_pharmacy_latitude, item.data_pharmacy_longitude))).zoom(mMap.getCameraPosition().zoom).build();

                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
                            }
                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                        pharmacyWindow.startAnimation(list_down);
                    }
                    else{
                        final CameraPosition cp = new CameraPosition.Builder().target((new LatLng(item.
                                data_pharmacy_latitude, item.data_pharmacy_longitude))).zoom(mMap.getCameraPosition().zoom).build();

                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
                    }
                }
            });
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyRecycleAdapterPharmacySearch.ViewHolder holder, int position) {
            holder.txt.setText(pharmacy.get(position).data_pharmacy_name + " (" + pharmacy.get(position).data_pharmacy_distance + ")");
        }

        @Override
        public int getItemCount() {
            return pharmacy.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView txt;

            public ViewHolder(View itemView) {
                super(itemView);
                txt = (TextView) itemView.findViewById(R.id.txt_vp_item_list);
            }
        }
    }
    public void listUpDown(View v){
        final Button upDownButton = (Button) findViewById(R.id.up_down_button);
        pharmacyWindow = (RelativeLayout) findViewById(R.id.pharmacy_window);
        Display display = ((WindowManager) getSystemService(getApplicationContext().WINDOW_SERVICE)).getDefaultDisplay();
        final int height = (int) (display.getHeight() * 0.8);

        if(!isUp){
            isUp = true;
            Animation list_up = AnimationUtils.loadAnimation(this, R.anim.list_up);
            list_up.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pharmacyWindow.setTranslationY(0);
                        }
                    },2);
                    upDownButton.setBackgroundResource(R.drawable.list_down_button);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            pharmacyWindow.startAnimation(list_up);
        }
        else{
            isUp = false;
            Animation list_down = AnimationUtils.loadAnimation(this, R.anim.list_down);
            list_down.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    upDownButton.setEnabled(false);
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pharmacyWindow.setTranslationY(height);
                        }
                    },2);
                    upDownButton.setBackgroundResource(R.drawable.list_up_button);
                    upDownButton.setEnabled(true);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            pharmacyWindow.startAnimation(list_down);
        }
    }
}
