package kr.co.company.ProjectA;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NowRunPharmacyActivity extends AppCompatActivity {
    private int i;
    private String nowDay;
    private int nowTime;
    private Context mContext;
    private RecyclerView recyclerView;
    private MyRecycleAdapterPharmacySearch runPharmacyAdapter;
    TextView areaText = null;
    EditText search_dong = null;
    String selectedArea = null;
    ImageView map_seoul = null;
    ImageView map_area = null;
    ImageView return_button = null;
    Button search_button = null;
    int returnPoint = 0;
    RelativeLayout pharmacyWindow;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_runpharmacy);

        areaText = (TextView) findViewById(R.id.area_text);
        areaText.setText("지역을 선택해주세요.");
        areaText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                TextView tv = (TextView)view;
                if(!(tv.getText().equals("지역을 선택해주세요.")))
                    runAnimation(3,1,0);
                return false;
            }
        });
        search_dong = (EditText) findViewById(R.id.search_dong);
        search_button = (Button) findViewById(R.id.search_button);

        map_seoul = (ImageView) findViewById(R.id.map_seoul);
        map_seoul.setOnTouchListener(new OnColorListener());

        map_area = (ImageView) findViewById(R.id.map_area);
        map_area.setOnTouchListener(new OnColorListener());

        return_button = (ImageView) findViewById(R.id.return_button);
        return_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                runAnimation(3,2,0);
                return false;
            }
        });

        pharmacyWindow = (RelativeLayout)findViewById(R.id.pharmacy_list_layout);
        pharmacyWindow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                downKeyboard(getApplicationContext(), search_dong);
                return false;
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
    }

    public static void downKeyboard(Context context, EditText editText) {
        InputMethodManager mInputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    class OnColorListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            ImageView image = (ImageView)view;
            int animationMode = 0;
            int animationId = 0;
            int mapId = 0;

            float eventX = event.getX();
            float eventY = event.getY();
            float[] eventXY = new float[] {eventX, eventY};

            Matrix invertMatrix = new Matrix();
            image.getImageMatrix().invert(invertMatrix);

            invertMatrix.mapPoints(eventXY);
            int x = Integer.valueOf((int)eventXY[0]);
            int y = Integer.valueOf((int)eventXY[1]);

            Drawable imgDrawable = image.getDrawable();
            Bitmap bitmap = ((BitmapDrawable)imgDrawable).getBitmap();

            int touchedRGB = bitmap.getPixel(x, y);

            switch (Integer.toHexString(touchedRGB)){
                /* 서울시 지도 */
                case "fff9353f" : animationMode = 1; animationId=R.anim.dobong; mapId=R.drawable.dobong; returnPoint = R.anim.dobong_rewind; break;// 도봉, 강북, 성북, 노원구
                case "fff4810a" : animationMode = 1; animationId=R.anim.dongdaemoon; mapId=R.drawable.dongdaemoon; returnPoint = R.anim.dongdaemoon_rewind; break;// 동대문, 중랑, 성동, 광진구
                case "fffebe0d" : animationMode = 1; animationId=R.anim.gangdong; mapId=R.drawable.gangdong; returnPoint = R.anim.gangdong_rewind; break;// 강동, 송파구
                case "ff61b71f" : animationMode = 1; animationId=R.anim.seocho; mapId=R.drawable.seocho; returnPoint = R.anim.seocho_rewind; break;// 서초, 강남구
                case "ff32b3d3" : animationMode = 1; animationId=R.anim.dongjak; mapId=R.drawable.dongjak; returnPoint = R.anim.dongjak_rewind; break;// 동작, 관악, 금천구
                case "ff504b9f" : animationMode = 1; animationId=R.anim.guro; mapId=R.drawable.guro; returnPoint = R.anim.guro_rewind; break; // 구로, 강서, 양천, 영등포구
                case "ffad489d" : animationMode = 1; animationId=R.anim.unpyung; mapId=R.drawable.unpyung; returnPoint = R.anim.unpyung_rewind; break;// 은평, 마포, 서대문구
                case "ff999999" : animationMode = 1; animationId=R.anim.jongro; mapId=R.drawable.jongro; returnPoint = R.anim.jongro_rewind; break;// 종로, 중구, 용산구
                case "ff000000" : animationMode = 1; animationId=R.anim.jongro; mapId=R.drawable.jongro; returnPoint = R.anim.jongro_rewind; break;// 종로, 중구, 용산구
                /* 지역구 지도 */
                case "fffe6a6a" : animationMode = 2; selectedArea = "도봉구"; break;
                case "ffff8888" : animationMode = 2; selectedArea = "강북구"; break;
                case "ffffa6a6" : animationMode = 2; selectedArea = "성북구"; break;
                case "ffff4040" : animationMode = 2; selectedArea = "노원구"; break;
                case "ffff8544" : animationMode = 2; selectedArea = "동대문구"; break;
                case "ffff9f6c" : animationMode = 2; selectedArea = "중랑구"; break;
                case "ffff7125" : animationMode = 2; selectedArea = "성동구"; break;
                case "ffff620d" : animationMode = 2; selectedArea = "광진구"; break;
                case "fffffe65" : animationMode = 2; selectedArea = "강동구"; break;
                case "fffefc28" : animationMode = 2; selectedArea = "송파구"; break;
                case "ff2dff4f" : animationMode = 2; selectedArea = "서초구"; break;
                case "ff77ff8d" : animationMode = 2; selectedArea = "강남구"; break;
                case "ffa3ebff" : animationMode = 2; selectedArea = "동작구"; break;
                case "ff71dffe" : animationMode = 2; selectedArea = "관악구"; break;
                case "ff39d2fe" : animationMode = 2; selectedArea = "금천구"; break;
                case "ff9590e1" : animationMode = 2; selectedArea = "구로구"; break;
                case "ffdbd9fb" : animationMode = 2; selectedArea = "강서구"; break;
                case "ffb4b1ec" : animationMode = 2; selectedArea = "양천구"; break;
                case "ff736ec8" : animationMode = 2; selectedArea = "영등포구"; break;
                case "ffebade2" : animationMode = 2; selectedArea = "은평구"; break;
                case "ffb84ca7" : animationMode = 2; selectedArea = "마포구"; break;
                case "fff587e4" : animationMode = 2; selectedArea = "서대문구"; break;
                case "ffd0d0d0" : animationMode = 2; selectedArea = "종로구"; break;
                case "ffb7b7b7" : animationMode = 2; selectedArea = "중구"; break;
                case "ff939393" : animationMode = 2; selectedArea = "용산구"; break;

                default : animationMode = 0; mapId = 0;
            }

            runAnimation(animationMode, animationId, mapId);
            return false;
        }
    }
    void runAnimation(int mode, int aniId, int mapId) {
        final AnimationSet animationSet = new AnimationSet(true);
        final Animation fade_in = new AlphaAnimation(0, 1);
        final Animation fade_out = new AlphaAnimation(1, 0);
        Animation zoom_in = null;
        final int _mapId = mapId;
        if (mode == 1) { // 서울시 지도 -> 지역구 지도
            zoom_in = AnimationUtils.loadAnimation(this, aniId);
            zoom_in.setStartOffset(0);
            zoom_in.setDuration(1200);
            animationSet.addAnimation(zoom_in);

            fade_out.setStartOffset(900);
            fade_out.setDuration(400);
            animationSet.addAnimation(fade_out);

            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    fade_in.setStartOffset(0);
                    fade_in.setDuration(1500);
                    map_area.startAnimation(fade_in);
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    map_seoul.setVisibility(ImageView.GONE);
                    return_button.setVisibility(ImageView.VISIBLE);
                    map_area.setImageResource(_mapId);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            map_seoul.startAnimation(animationSet);
        } else if (mode == 2) { // 지역구 지도 -> 검색 결과
            return_button.setVisibility(ImageView.GONE);
            fade_out.setStartOffset(0);
            fade_out.setDuration(400);
            animationSet.addAnimation(fade_out);

            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    areaText.setEnabled(false);
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    map_area.setImageResource(0);
                    search_dong.setVisibility(EditText.VISIBLE);

                    float value = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getApplicationContext().getResources().getDisplayMetrics());

                    ValueAnimator animator = ValueAnimator.ofFloat(0, value);

                    animator.setDuration(700);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            float animatedValue = (float) valueAnimator.getAnimatedValue();
                            search_dong.setTextSize(animatedValue);
                        }
                    });

                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            areaText.setText("선택 지역 : 서울특별시 "+ selectedArea + "   <재선택>");
                            /* 현재 시간을 받아오는 코드 */
                            long now = System.currentTimeMillis();
                            Date date = new Date(now);
                            SimpleDateFormat sdfNowDay = new SimpleDateFormat("EEE", Locale.KOREAN);
                            SimpleDateFormat sdfNowTime = new SimpleDateFormat("HHmm");
                            nowDay = sdfNowDay.format(date) + "요일";
                            nowTime = Integer.parseInt(sdfNowTime.format(date));
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    GettingXML xmlParser = new GettingXML(new ArrayList<Pharmacy>(), "서울특별시", selectedArea, CommonData.serviceKey, "NAME", nowDay, nowTime, 2); // Mode 2
                                    CommonData.pharmacy_set = xmlParser.getXmlData();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            runPharmacyList();
                                            recyclerView.setVisibility(RecyclerView.VISIBLE);
                                        }
                                    });
                                }
                            }).start();
                        }
                        @Override
                        public void onAnimationEnd(Animator animator) {
                            search_dong.setHint("결과내 재검색 (도로명, 동)");
                            areaText.setEnabled(true);
                        }
                        @Override
                        public void onAnimationCancel(Animator animator) {}
                        @Override
                        public void onAnimationRepeat(Animator animator) {}
                    });
                    search_button.setVisibility(ImageView.VISIBLE);
                    fade_in.setStartOffset(0);
                    fade_in.setDuration(700);
                    search_button.setAnimation(fade_in);

                    animator.start();
                }
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            map_area.startAnimation(animationSet);
        }
        else if(mode == 3){ // 서울시 지도로 되돌아가기
            recyclerView.setVisibility(RecyclerView.GONE);
            ImageView noSearch = (ImageView)findViewById(R.id.fail_search);
            noSearch.setVisibility(ImageView.GONE);
            final Animation zoom_out = AnimationUtils.loadAnimation(this, returnPoint);
            if(aniId == 1){ // 검색 결과 -> 서울시 지도
                zoom_out.setDuration(1200);
                animationSet.addAnimation(zoom_out);

                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                        float value = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getApplicationContext().getResources().getDisplayMetrics());

                        ValueAnimator animator = ValueAnimator.ofFloat(value, 0);
                        animator.setDuration(1000);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                float animatedValue = (float) valueAnimator.getAnimatedValue();
                                search_dong.setTextSize(animatedValue);
                            }
                        });

                        animator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {
                                search_dong.setHint("");
                            }
                            @Override
                            public void onAnimationEnd(Animator animator) {
                                areaText.setText("지역을 선택해주세요.");
                                search_dong.setVisibility(EditText.GONE);
                                search_button.setVisibility(ImageView.GONE);
                            }
                            @Override
                            public void onAnimationCancel(Animator animator) {}
                            @Override
                            public void onAnimationRepeat(Animator animator) {}
                        });
                        fade_out.setStartOffset(0);
                        fade_out.setDuration(700);
                        search_button.setAnimation(fade_out);

                        animator.start();
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });

                map_seoul.setVisibility(ImageView.VISIBLE);
                map_seoul.startAnimation(animationSet);
            }
            else{ // 지역구 지도 -> 서울시 지도
                return_button.setVisibility(ImageView.GONE);
                map_area.setImageResource(0);
                map_seoul.setVisibility(ImageView.VISIBLE);
                zoom_out.setStartOffset(0);
                zoom_out.setDuration(1200);
                map_seoul.startAnimation(zoom_out);
            }
        }
    }
    private void runPharmacyList() {
        ArrayList<Pharmacy> runPharmacy = new ArrayList<Pharmacy>();
        runPharmacy.addAll(CommonData.pharmacy_set);

        ImageView noSearch = (ImageView)findViewById(R.id.fail_search);
        if(runPharmacy.size()==0)
            noSearch.setVisibility(ImageView.VISIBLE);
        else{
            noSearch.setVisibility(ImageView.GONE);
        }
        mContext = getApplicationContext();
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                        getBaseContext(), LinearLayoutManager.VERTICAL, false
                )
        );
        recyclerView.getRecycledViewPool();
        runPharmacyAdapter = new MyRecycleAdapterPharmacySearch(mContext, runPharmacy);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editString = search_dong.getText().toString().toLowerCase();
                runPharmacyAdapter.pharmacyNameFilter(editString);
                downKeyboard(getApplicationContext(), search_dong);
            }
        });
        recyclerView.setAdapter(runPharmacyAdapter);

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                downKeyboard(mContext, search_dong);
                return false;
            }
        });
    }

    class MyRecycleAdapterPharmacySearch extends RecyclerView.Adapter<MyRecycleAdapterPharmacySearch.ViewHolder> { // 약국 검색 어댑터
        private Context context;
        private LayoutInflater inflater;
        private ArrayList<Pharmacy> runPharmacy;
        private ArrayList<Pharmacy> tmpArrayList = null;

        public MyRecycleAdapterPharmacySearch(Context context, ArrayList<Pharmacy> runPharmacy) {
            this.context = context;
            this.runPharmacy = runPharmacy;
            inflater = LayoutInflater.from(mContext);
            this.tmpArrayList = CommonData.pharmacy_set;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemPosition = recyclerView.getChildPosition(v);
                    Pharmacy item = runPharmacy.get(itemPosition);
                    Intent intent = new Intent(NowRunPharmacyActivity.this, PharmacyInfoActivity.class);
                    intent.putExtra("PHARMACY", item);
                    startActivity(intent);
                }
            });
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.txt.setText(runPharmacy.get(position).data_pharmacy_name);
        }

        @Override
        public int getItemCount() {
            return runPharmacy.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView txt;

            public ViewHolder(View itemView) {
                super(itemView);
                txt = (TextView) itemView.findViewById(R.id.txt_vp_item_list);
            }
        }

        public void pharmacyNameFilter(String searchText) {
            searchText = searchText.toLowerCase(Locale.getDefault());
            runPharmacy.clear();
            if (searchText.length() == 0) {
                runPharmacy.addAll(tmpArrayList);
            } else {
                for (Pharmacy pharmacyCheck : tmpArrayList) {
                    String containSearchName = pharmacyCheck.data_pharmacy_address;
                    if (containSearchName.toLowerCase().contains(searchText)) {
                        runPharmacy.add(pharmacyCheck);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
}