package com.example.izobonga_waiting_app.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.izobonga_waiting_app.R;

import java.util.Objects;

public class CheckDialog extends Dialog {

    private View.OnClickListener nextButtonListener;
    private View.OnClickListener preButtonListener;
    TextView tvTitle;
    TextView tvSub;
    Button btCancel;
    Button btCheck;

    public CheckDialog(@NonNull Context context, View.OnClickListener positiveListener, View.OnClickListener preButtonListener) {
        super(context);
        this.nextButtonListener = positiveListener;
        this.preButtonListener = preButtonListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        Objects.requireNonNull(getWindow()).setAttributes(layoutParams);

        setContentView(R.layout.dialog_check);
        setCancelable(false);

        //initView
        tvTitle = findViewById(R.id.check_dialog_tv_title);
        tvSub = findViewById(R.id.check_dialog_tv_sub_title);
        btCancel = findViewById(R.id.check_bt_cancel);
        btCheck = findViewById(R.id.check_bt_check);
    }
}
