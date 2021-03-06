package com.example.soenapp;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterSchoolActivity extends AppCompatActivity {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(RetrofitService.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    RetrofitService retrofitService = retrofit.create(RetrofitService.class);
    HashMap<String, Object> input = new HashMap<>();
    SchoolData schoolBody;
    RegisterData registerBody;


    private RecyclerView schoolList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    TextWatcher textWatcher;
    EditText editText;
    Button next;

    String currentSchoolCode;
    String currentSchoolName;

    int gradeInt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_school);

        next = findViewById(R.id.next);

        editText = findViewById(R.id.input);
        schoolList = findViewById(R.id.school_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        schoolList.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        schoolList.setLayoutManager(layoutManager);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorWhite));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            Timer timer = new Timer();
            final int DELAY = 200;

            @Override
            public void afterTextChanged(final Editable s) {

                timer.cancel();
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                retrofitService.getSchool(s.toString()).enqueue(new Callback<SchoolData>() {
                                    @Override
                                    public void onResponse(@NonNull Call<SchoolData> call, @NonNull Response<SchoolData> response) {
                                        if (response.isSuccessful()) {
                                            schoolBody = response.body();
                                            if (Objects.requireNonNull(schoolBody).message.equals("success")) {

                                                // specify an adapter (see also next example)
                                                mAdapter = new RegisterSchoolAdapter(schoolBody);
                                                schoolList.setAdapter(mAdapter);

                                                System.out.println(schoolBody.toString());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<SchoolData> call, @NonNull Throwable t) {

                                    }
                                });
                            }
                        },
                        DELAY
                );
            }
        });

        final GestureDetector gestureDetector = new GestureDetector(getApplicationContext(),new GestureDetector.SimpleOnGestureListener() {

            //누르고 뗄 때 한번만 인식하도록 하기위해서
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        RecyclerView.OnItemTouchListener onItemTouchListener = new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                //손으로 터치한 곳의 좌표를 토대로 해당 Item의 View를 가져옴
                View childView = rv.findChildViewUnder(e.getX(),e.getY());

                //터치한 곳의 View가 RecyclerView 안의 아이템이고 그 아이템의 View가 null이 아니라
                //정확한 Item의 View를 가져왔고, gestureDetector에서 한번만 누르면 true를 넘기게 구현했으니
                //한번만 눌려서 그 값이 true가 넘어왔다면
                if(childView != null && gestureDetector.onTouchEvent(e)){

                    //현재 터치된 곳의 position을 가져오고
                    int currentPosition = rv.getChildAdapterPosition(childView);

                    //해당 위치의 Data를 가져옴
                    SchoolData.Result currentItemSchool = schoolBody.results[currentPosition];
                    currentSchoolCode = currentItemSchool.SCHUL_CODE;
                    currentSchoolName = currentItemSchool.SCHUL_NM;
                    Toast.makeText(getApplicationContext(), currentItemSchool.SCHUL_NM, Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        };

        schoolList.addOnItemTouchListener(onItemTouchListener);

        final Intent getIntent = getIntent();
        final Intent newIntent = new Intent(getApplicationContext(), RegisterCompletedActivity.class);

        final String name = Objects.requireNonNull(getIntent.getExtras()).getString("name");
        final String id = getIntent.getExtras().getString("id");
        final String pw = getIntent.getExtras().getString("pw");
        final String birth = getIntent.getExtras().getString("birth");
        final int grade = getIntent.getExtras().getInt("grade");
        final String schoolType = getIntent.getExtras().getString("school_type");
        final int actualGrade = getIntent.getExtras().getInt("actual_grade");
        final String gender = getIntent.getExtras().getString("gender");
        final int classValue = getIntent.getExtras().getInt("class");


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newIntent.putExtra("name", name);
                newIntent.putExtra("id", id);
                newIntent.putExtra("pw", pw);
                newIntent.putExtra("birth", birth);
                newIntent.putExtra("grade", grade);
                newIntent.putExtra("school_type", schoolType);
                newIntent.putExtra("actual_grade", actualGrade);
                newIntent.putExtra("gender", gender);
                newIntent.putExtra("class", classValue);
                newIntent.putExtra("school_code", currentSchoolCode);
                newIntent.putExtra("school_name", currentSchoolName);


                // 회원가입
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(RetrofitService.URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RetrofitService retrofitService = retrofit.create(RetrofitService.class);
                HashMap<String, Object> input = new HashMap<>();

                input.put("name", name);
                input.put("id", id);
                input.put("pw", pw);
                input.put("school_code", currentSchoolCode);
                input.put("school_name", currentSchoolName);
                input.put("birth", birth);
                input.put("grade", grade);
                input.put("gender", gender);
                input.put("school_type", schoolType);
                input.put("actual_grade", actualGrade);
                input.put("class", classValue);

                retrofitService.register(input).enqueue(new Callback<RegisterData>() {
                    @Override
                    public void onResponse(@NonNull Call<RegisterData> call, @NonNull Response<RegisterData> response) {
                        if (response.isSuccessful()) {
                            registerBody = response.body();
                            if (Objects.requireNonNull(registerBody).message.equals("success")) {
                                startActivity(newIntent);
                            } else if (registerBody.message.equals("fail")) {
                                Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<RegisterData> call, @NonNull Throwable t) {
                        Toast.makeText(getApplicationContext(), "서버를 찾을 수 없음", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
