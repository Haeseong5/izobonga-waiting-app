package com.example.izobonga_waiting_app.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.izobonga_waiting_app.R;

public class WaitingDialog extends Dialog {

    private Button mPositiveButton;

    private View.OnClickListener mPositiveListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //다이얼로그 밖의 화면은 흐리게 만들어줌
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.dialog_waiting);

        //셋팅
        mPositiveButton = findViewById(R.id.waiting_dialog_finish_button);

        //클릭 리스너 셋팅 (클릭버튼이 동작하도록 만들어줌.)
        mPositiveButton.setOnClickListener(mPositiveListener);
    }
    //생성자 생성
    public WaitingDialog(@NonNull Context context, View.OnClickListener positiveListener) {
        super(context);
        this.mPositiveListener = positiveListener;
    }

}


