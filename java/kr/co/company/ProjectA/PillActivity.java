package kr.co.company.ProjectA;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import devlight.io.library.ntb.NavigationTabBar;

public class PillActivity extends Activity {
    Context mContext;
    RecyclerView recyclerView_name, recyclerView_symptom;
    ImageView emptyImageView_name, noSearchImageView_name, emptyImageView_symptom, noSearchImageView_symptom;
    MyRecycleAdapterNameSearch nameAdapter;
    MyRecycleAdapterSymptomSearch symptomAdapter;
    List<String> symptomList = new ArrayList<String>();
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill);
        initUI();
    }

    private void initUI() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
        mContext = getApplicationContext();
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                View view = null;
                ArrayList<Medicine> medicine = new ArrayList<>();
                if (position == 0) { // 이름 검색 view
                    view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.fragment_name_search, null, false);
                    recyclerView_name = (RecyclerView) view.findViewById(R.id.rv);
                    recyclerView_name.setHasFixedSize(true);
                    emptyImageView_name = (ImageView) view.findViewById(R.id.emptyImage_name);
                    noSearchImageView_name = (ImageView) view.findViewById(R.id.noSearchImage_name);
                    emptyImageView_name.setVisibility(ImageView.VISIBLE);
                    noSearchImageView_name.setVisibility(ImageView.GONE);
                    recyclerView_name.setLayoutManager(new LinearLayoutManager(
                                    getBaseContext(), LinearLayoutManager.VERTICAL, false
                            )
                    );
                    nameAdapter = new MyRecycleAdapterNameSearch(mContext, medicine);
                    final EditText searchEditText = (SearchEditText) view.findViewById(R.id.search_bar);
                    linearLayout = (LinearLayout) view.findViewById(R.id.name_layout);
                    linearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            downKeyboard(mContext, searchEditText);
                        }
                    });
                    searchEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void afterTextChanged(Editable editable) { // EditText 변화에 따라 Icon 위치 바꾸는거, 실시간 업데이트에도 사용할 수 있음.
                            if (searchEditText.getText().length() == 0) {
                                emptyImageView_name.setVisibility(ImageView.VISIBLE);
                                recyclerView_name.setVisibility(RecyclerView.GONE);
                            } else {
                                emptyImageView_name.setVisibility(ImageView.GONE);
                                recyclerView_name.setVisibility(RecyclerView.VISIBLE);
                            }
                            nameAdapter.nameFilter(searchEditText.getText().toString().toLowerCase());
                        }
                    });
                    recyclerView_name.setAdapter(nameAdapter);
                } else if (position == 1) { // 증상 검색 view
                    view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.fragment_symptom_search, null, false);
                    recyclerView_symptom = (RecyclerView) view.findViewById(R.id.rv2);
                    recyclerView_symptom.setHasFixedSize(true);
                    emptyImageView_symptom = (ImageView) view.findViewById(R.id.emptyImage_symptom);
                    emptyImageView_symptom.setVisibility(ImageView.VISIBLE);
                    noSearchImageView_symptom = (ImageView) view.findViewById(R.id.noSearchImage_symptom);
                    recyclerView_symptom.setLayoutManager(new LinearLayoutManager(
                                    getBaseContext(), LinearLayoutManager.VERTICAL, false
                            )
                    );

                    symptomAdapter = new MyRecycleAdapterSymptomSearch(mContext, medicine);
                    final SmoothCheckBox headache = (SmoothCheckBox)view.findViewById(R.id.headache);
                    final SmoothCheckBox fever = (SmoothCheckBox)view.findViewById(R.id.fever);
                    final SmoothCheckBox laryngopharyngitis = (SmoothCheckBox)view.findViewById(R.id.laryngopharyngitis);
                    final SmoothCheckBox neuralgia = (SmoothCheckBox)view.findViewById(R.id.neuralgia);
                    final SmoothCheckBox stomache = (SmoothCheckBox)view.findViewById(R.id.stomache);
                    final SmoothCheckBox gastralgia = (SmoothCheckBox)view.findViewById(R.id.gastralgia);
                    final SmoothCheckBox dysmenorrhea = (SmoothCheckBox)view.findViewById(R.id.dysmenorrhea);
                    final SmoothCheckBox hit_pain = (SmoothCheckBox)view.findViewById(R.id.hit_pain);
                    final SmoothCheckBox low_back = (SmoothCheckBox)view.findViewById(R.id.low_back);
                    final SmoothCheckBox toothache = (SmoothCheckBox)view.findViewById(R.id.toothache);
                    final SmoothCheckBox myalgia = (SmoothCheckBox)view.findViewById(R.id.myalgia);
                    final SmoothCheckBox distortion = (SmoothCheckBox)view.findViewById(R.id.distortion);


                    SmoothCheckBox.OnCheckedChangeListener onCheckedChangeListener = new SmoothCheckBox.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                            switch (checkBox.getId()) {
                                case R.id.headache:
                                    if (isChecked) symptomList.add("두통");
                                    else symptomList.remove("두통");
                                    break;
                                case R.id.fever:
                                    if (isChecked) symptomList.add("발열");
                                    else symptomList.remove("발열");
                                    break;
                                case R.id.laryngopharyngitis:
                                    if (isChecked) symptomList.add("인후");
                                    else symptomList.remove("인후");
                                    break;
                                case R.id.neuralgia:
                                    if (isChecked) symptomList.add("신경통");
                                    else symptomList.remove("신경통");
                                    break;
                                case R.id.stomache:
                                    if (isChecked) symptomList.add("복통");
                                    else symptomList.remove("복통");
                                    break;
                                case R.id.gastralgia:
                                    if (isChecked) symptomList.add("위통");
                                    else symptomList.remove("위통");
                                    break;
                                case R.id.dysmenorrhea:
                                    if (isChecked) symptomList.add("생리통");
                                    else symptomList.remove("생리통");
                                    break;
                                case R.id.hit_pain:
                                    if (isChecked) symptomList.add("타박통");
                                    else symptomList.remove("타박통");
                                    break;
                                case R.id.low_back:
                                    if (isChecked) symptomList.add("요통");
                                    else symptomList.remove("요통");
                                    break;
                                case R.id.toothache:
                                    if (isChecked) symptomList.add("치통");
                                    else symptomList.remove("치통");
                                    break;
                                case R.id.myalgia:
                                    if (isChecked) symptomList.add("근육통");
                                    else symptomList.remove("근육통");
                                    break;
                                case R.id.distortion:
                                    if (isChecked) symptomList.add("염좌");
                                    else symptomList.remove("염좌");
                                    break;
                                default:
                                    break;
                            }
                            symptomAdapter.symptomFilter(symptomList);
                        }
                    };
                    headache.setOnCheckedChangeListener(onCheckedChangeListener);
                    fever.setOnCheckedChangeListener(onCheckedChangeListener);
                    laryngopharyngitis.setOnCheckedChangeListener(onCheckedChangeListener);
                    neuralgia.setOnCheckedChangeListener(onCheckedChangeListener);
                    stomache.setOnCheckedChangeListener(onCheckedChangeListener);
                    gastralgia.setOnCheckedChangeListener(onCheckedChangeListener);
                    dysmenorrhea.setOnCheckedChangeListener(onCheckedChangeListener);
                    hit_pain.setOnCheckedChangeListener(onCheckedChangeListener);
                    low_back.setOnCheckedChangeListener(onCheckedChangeListener);
                    toothache.setOnCheckedChangeListener(onCheckedChangeListener);
                    myalgia.setOnCheckedChangeListener(onCheckedChangeListener);
                    distortion.setOnCheckedChangeListener(onCheckedChangeListener);
                    recyclerView_symptom.setAdapter(symptomAdapter);
                }
                container.addView(view);
                return view;
            }
        });

        String[] colors = getResources().getStringArray(R.array.default_preview);
        colors[4] = "#e9642c";

        NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.name_badge),
                        Color.parseColor(colors[4]))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.symptom_badge),
                        Color.parseColor(colors[4]))
                        .build()
        );


        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 0);
        navigationTabBar.setBgColor(Color.parseColor("#fcce3b"));
        navigationTabBar.setIconSizeFraction(1.1f);
        navigationTabBar.setInactiveColor(Color.GRAY);

        //IMPORTANT: ENABLE SCROLL BEHAVIOUR IN COORDINATOR LAYOUT
        navigationTabBar.setBehaviorEnabled(true);

        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(NavigationTabBar.Model model, int index) {
            }

            @Override
            public void onEndTabSelected(NavigationTabBar.Model model, int index) {
                model.hideBadge();
            }
        });
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public static void downKeyboard(Context context, EditText editText) {
        InputMethodManager mInputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    class MyRecycleAdapterNameSearch extends RecyclerView.Adapter<MyRecycleAdapterNameSearch.ViewHolder> { // 이름 검색 어댑터
        private Context context;
        private LayoutInflater inflater;
        private ArrayList<Medicine> medicine;
        private ArrayList<Medicine> arrayList = null;

        public MyRecycleAdapterNameSearch(Context context, ArrayList<Medicine> medicine) {
            this.context = context;
            this.medicine = medicine;
            inflater = LayoutInflater.from(mContext);
            this.arrayList = CommonData.medicine_set;
            if(arrayList == null){
                Toast.makeText(getApplicationContext(), "서버 통신 오류, 잠시 후 재시도바랍니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemPosition = recyclerView_name.getChildPosition(v);
                    Medicine item = medicine.get(itemPosition);
                    Intent intent = new Intent(PillActivity.this, MedicineInfoActivity.class);
                    intent.putExtra("MEDICINE", item);
                    startActivity(intent);
                }
            });
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.txt.setText(medicine.get(position).data_medicine_name);
        }

        @Override
        public int getItemCount() {
            return medicine.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView txt;

            public ViewHolder(View itemView) {
                super(itemView);
                txt = (TextView) itemView.findViewById(R.id.txt_vp_item_list);
            }
        }

        public void nameFilter(String searchText) {
            searchText = searchText.toLowerCase(Locale.getDefault());
            medicine.clear();
            if (searchText.length() == 0) {
                medicine.addAll(arrayList);
                noSearchImageView_name.setVisibility(ImageView.GONE);
            } else {
                emptyImageView_name.setVisibility(ImageView.GONE);
                for (Medicine medicineCheck : arrayList) {
                    String containSearchName = medicineCheck.getMedicine_name();
                    if (containSearchName.toLowerCase().contains(searchText)) {
                        medicine.add(medicineCheck);
                    }
                }
                if (medicine.size() == 0) {
                    recyclerView_name.setVisibility(RecyclerView.GONE);
                    noSearchImageView_name.setVisibility(ImageView.VISIBLE);
                    emptyImageView_name.setVisibility(ImageView.GONE);
                }
                else {
                    recyclerView_name.setVisibility(RecyclerView.VISIBLE);
                    noSearchImageView_name.setVisibility(ImageView.GONE);
                }
            }
            notifyDataSetChanged();
        }
    }

    class MyRecycleAdapterSymptomSearch extends RecyclerView.Adapter<MyRecycleAdapterSymptomSearch.ViewHolder> { // 증상 검색 어댑터
        private Context context;
        private LayoutInflater inflater;
        private ArrayList<Medicine> medicine;
        private ArrayList<Medicine> arrayList = null;

        public MyRecycleAdapterSymptomSearch(Context context, ArrayList<Medicine> medicine) {
            this.context = context;
            this.medicine = medicine;
            inflater = LayoutInflater.from(mContext);
            this.arrayList = CommonData.medicine_set;
            if(arrayList == null){
                Toast.makeText(getApplicationContext(), "서버 통신 오류, 잠시 후 재시도바랍니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemPosition = recyclerView_symptom.getChildPosition(v);
                    Medicine item = medicine.get(itemPosition);
                    Intent intent = new Intent(PillActivity.this, MedicineInfoActivity.class);
                    intent.putExtra("MEDICINE", item);
                    startActivity(intent);
                }
            });
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.txt.setText(medicine.get(position).data_medicine_name);
        }

        @Override
        public int getItemCount() {
            return medicine.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView txt;

            public ViewHolder(View itemView) {
                super(itemView);
                txt = (TextView) itemView.findViewById(R.id.txt_vp_item_list);
            }
        }

        public void symptomFilter(List<String> list) {
            int count;
            medicine.clear();
            if (list.size() == 0) {
                medicine.addAll(arrayList);
                emptyImageView_symptom.setVisibility(ImageView.VISIBLE);
                noSearchImageView_symptom.setVisibility(ImageView.GONE);
                recyclerView_symptom.setVisibility(RecyclerView.GONE);
            } else {
                emptyImageView_symptom.setVisibility(ImageView.GONE);
                for (int i = 0; i < arrayList.size(); i++) {
                    count = 0;
                    for (int j = 0; j < list.size(); j++) {
                        if (arrayList.get(i).getMedicine_effect().contains(list.get(j))) {
                            count++;
                        }
                    }
                    if (count == list.size()) {
                        if (!medicine.contains(arrayList.get(i))) {
                            medicine.add(arrayList.get(i));
                        }
                    }
                }
                if (medicine.size() == 0) {
                    recyclerView_symptom.setVisibility(RecyclerView.GONE);
                    noSearchImageView_symptom.setVisibility(ImageView.VISIBLE);
                }
                else {
                    recyclerView_symptom.setVisibility(RecyclerView.VISIBLE);
                    noSearchImageView_symptom.setVisibility(ImageView.GONE);
                }
            }
            notifyDataSetChanged();
        }
    }
}
