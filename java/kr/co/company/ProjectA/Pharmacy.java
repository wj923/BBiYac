package kr.co.company.ProjectA;

import java.io.Serializable;

public class Pharmacy implements Serializable {
    String data_pharmacy_name; // 약국 이름
    String data_pharmacy_address; // 약국 주소
    String data_pharmacy_tel; // 약국 번호
    String data_pharmacy_time; // 영업 시간
    String data_pharmacy_Info; // 약국 정보
    String data_pharmacy_etc; // 약국 특이사항
    String data_pharmacy_mapimg; // 간이약도
    Double data_pharmacy_latitude; // 약국 위도
    Double data_pharmacy_longitude; // 약국 경도
    String data_pharmacy_distance; // 거리
    int mode;
    int between_dis;

    public Pharmacy(String data_pharmacy_name, String data_pharmacy_address, String data_pharmacy_tel, String data_pharmacy_time, String data_pharmacy_Info, String data_pharmacy_etc, String data_pharmacy_mapimg, Double data_pharmacy_latitude, Double data_pharmacy_longitude, String data_pharmacy_distance, int mode) {
        this.data_pharmacy_name = data_pharmacy_name;
        this.data_pharmacy_address = data_pharmacy_address;
        this.data_pharmacy_tel = data_pharmacy_tel;
        this.data_pharmacy_time = data_pharmacy_time;
        this.data_pharmacy_Info = data_pharmacy_Info;
        this.data_pharmacy_etc = data_pharmacy_etc;
        this.data_pharmacy_mapimg = data_pharmacy_mapimg;
        this.data_pharmacy_latitude = data_pharmacy_latitude;
        this.data_pharmacy_longitude = data_pharmacy_longitude;
        this.mode = mode;
        if (mode == 1) {
            between_dis = Integer.parseInt(data_pharmacy_distance);
            if (between_dis < 1000) {
                this.data_pharmacy_distance = data_pharmacy_distance + "m";
            }
            else {
                double tmp = (double) between_dis / 1000;
                this.data_pharmacy_distance = Double.toString(tmp) + "km";
            }
        }
        else if (mode == 2) this.data_pharmacy_distance = null;
    }
}
