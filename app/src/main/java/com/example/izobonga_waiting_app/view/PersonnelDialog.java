package com.example.izobonga_waiting_app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.izobonga_waiting_app.FirebaseApi;
import com.example.izobonga_waiting_app.R;
import com.google.android.gms.tasks.OnSuccessListener;

public class PersonnelDialog extends Dialog {
    FirebaseApi firebaseApi;
    private ImageView minus,plus;
    public TextView personnelNumber;
    private Button previous, next;
    private View.OnClickListener nextButtonListener;
    private View.OnClickListener preButtonListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseApi = new FirebaseApi();
        //다이얼로그 밖의 화면은 흐리게 만들어줌
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.dialog_personnel);

        //셋팅
        minus = findViewById(R.id.personnel_minus);
        plus = findViewById(R.id.personnel_plus);
        personnelNumber = findViewById(R.id.personnel_number);
        previous = findViewById(R.id.personnel_previous);
        next = findViewById(R.id.personnel_next);

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = Integer.parseInt(personnelNumber.getText().toString());
                if(n>0){
                    n = n - 1;
                    personnelNumber.setText(n+"");
                }
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = Integer.parseInt(personnelNumber.getText().toString());
                n = n + 1;
                personnelNumber.setText(n+"");
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersonnelDialog.this.dismiss();
            }
        });
        //클릭 리스너 셋팅 (클릭버튼이 동작하도록 만들어줌.)
        next.setOnClickListener(nextButtonListener);
        previous.setOnClickListener(preButtonListener);
    }
    //생성자 생성
    public PersonnelDialog(@NonNull Context context, View.OnClickListener positiveListener, View.OnClickListener preButtonListener) {
        super(context);
        this.nextButtonListener = positiveListener;
        this.preButtonListener = preButtonListener;
    }


}
