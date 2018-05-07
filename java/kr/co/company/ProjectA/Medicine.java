package kr.co.company.ProjectA;

import java.io.Serializable;

public class Medicine implements Serializable {
    String data_medicine_name; // 이름
    String data_medicine_categorize; // 식약처 분류
    String data_medicine_manufacturer; // 제조사
    String data_medicine_appearance_info; // 외형정보
    String data_medicine_component_info; // 성분정보
    String data_medicine_save; // 저장방법
    String data_medicine_effect; // 효능효과
    String data_medicine_usage; // 용량/용법
    String data_medicine_caution; // 사용상 주의사항
    String data_medicine_image; // 약 사진 URL

    public Medicine(String data_medicine_name, String data_medicine_categorize, String data_medicine_manufacturer, String data_medicine_appearance_info, String data_medicine_component_info, String data_medicine_save, String data_medicine_effect, String data_medicine_usage, String data_medicine_caution, String data_medicine_image) {
        this.data_medicine_name = data_medicine_name;
        this.data_medicine_categorize = data_medicine_categorize;
        this.data_medicine_manufacturer = data_medicine_manufacturer;
        this.data_medicine_appearance_info = data_medicine_appearance_info;
        this.data_medicine_component_info = data_medicine_component_info;
        this.data_medicine_save = data_medicine_save;
        this.data_medicine_effect = data_medicine_effect;
        this.data_medicine_usage = data_medicine_usage;
        this.data_medicine_caution = data_medicine_caution;
        this.data_medicine_image = data_medicine_image;
    }
    public String getMedicine_name() { // getter
        return data_medicine_name;
    } // 약 이름 getter
    public String getMedicine_effect() { return data_medicine_effect; } // 약 효과(환자 증상) getter
}
