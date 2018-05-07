package kr.co.company.ProjectA;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class GPSInfo extends Service implements LocationListener {

    private final Context mContext;
    boolean isGPSEnabled = false; // 현재 GPS 사용유무
    boolean isNetworkEnabled = false; // 네트워크 사용유무
    boolean isGetLocation = false; // GPS 상태값
    Location location;
    double lat; // 위도
    double lon; // 경도
    private static final long MIN_DISTANCE_UPDATES = 10; // GPS 정보 업데이트 거리 10미터
    private static final long MIN_TIME_UPDATES = 1000 * 60 * 1; // GPS 정보 업데이트 시간 1/1000
    protected LocationManager locationManager;
    String AdminArea = null;
    String Locality = null;

    public GPSInfo(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation(){
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && ! isNetworkEnabled) {
                this.isGetLocation = false;
            } else
            { //네트워크 정보로부터 위치값 가져오기
                this.isGetLocation = true;
                if(isNetworkEnabled){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_DISTANCE_UPDATES, MIN_TIME_UPDATES, this);
                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(location != null){
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                        }
                    }
                }
                if (isGPSEnabled) {
                    if(location == null) { //GPS정보로 위치값 가져오기
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_DISTANCE_UPDATES, MIN_TIME_UPDATES, this);
                        if(locationManager != null){
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if(location != null){
                                Log.e("12345", "isGPSEnabled");
                                lat = location.getLatitude();
                                lon = location.getLongitude();
                            }
                        }
                    }
                }

            }
        }catch(SecurityException e){
            e.printStackTrace();
        }
        return location;
    }

    public void stopUsingGPS() { // GPS 종료
        if (locationManager != null) {
            try {
                locationManager.removeUpdates(GPSInfo.this);
            } catch (SecurityException e) {
                Log.e("PERMISSION_EXCEPTION","PERMISSION_NOT_GRANTED");
            }
        }
    }

    public double getLatitude() { // 위도값
        if (location != null) {
            lat = location.getLatitude();
        }
        return lat;
    }

    public double getLongitude() { // 경도값
        if (location != null) {
            lon = location.getLongitude();
        }
        return lon;
    }

    public boolean isGetLocation() {
        return this.isGetLocation;
    }

    public void showSettingsAlert() { //  GPS 정보를 가져오지 못했을때 설정값으로 갈지 물어보는 alert 창
        Log.e("12345", "showSettingsAlert()");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                mContext);

        alertDialog.setTitle("GPS 사용유무셋팅");
        alertDialog
                .setMessage("GPS 셋팅이 되지 않았을수도 있습니다.\n 설정창으로 가시겠습니까?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.cancel();
                        Toast.makeText(mContext, "위치 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
        alertDialog.show();
    }

    public void getAddress(double lat, double lng) { // 내 위치 주소값 얻어오기
        List<Address> list = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            list = geocoder.getFromLocation(lat, lng, 1);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(list == null){
            Log.e("getAddress", "주소 데이터 얻기 실패");
            return;
        }
        if(list.size() > 0){
            Address addr = list.get(0);
            AdminArea = addr.getAdminArea(); // 시도
            Locality = addr.getLocality(); // 시군구
        }
        return;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
    }

    public void onStatusChanged(String provider, int status,
                                Bundle extras) {
        // TODO Auto-generated method stub
    }

    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }
}
