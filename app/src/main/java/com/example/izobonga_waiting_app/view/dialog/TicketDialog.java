package com.example.izobonga_waiting_app.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.izobonga_waiting_app.R;

public class TicketDialog extends Dialog {

    private Button mPositiveButton;
    private TextView tvWaitingNumber;
    private int mTicket;

    private View.OnClickListener mPositiveListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //다이얼로그 밖의 화면은 흐리게 만들어줌
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.dialog_ticket);
        tvWaitingNumber = findViewById(R.id.waiting_dialog_number);
        mPositiveButton = findViewById(R.id.waiting_dialog_finish_button);
        String ticket = String.valueOf(mTicket);
        tvWaitingNumber.setText(ticket);
        mPositiveButton.setOnClickListener(mPositiveListener);
    }
    //생성자 생성
    public TicketDialog(@NonNull Context context, View.OnClickListener positiveListener, int mTicket) {
        super(context);
        this.mPositiveListener = positiveListener;
        this.mTicket = mTicket;
    }

    public void setTicket(int ticket){
        tvWaitingNumber.setText(String.valueOf(ticket));
    }
}


