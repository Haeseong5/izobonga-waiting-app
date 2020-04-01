package com.example.izobonga_waiting_app.view;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;


import com.example.izobonga_waiting_app.BaseActivity;
import com.example.izobonga_waiting_app.FireBaseApi;
import com.example.izobonga_waiting_app.R;
import com.example.izobonga_waiting_app.databinding.ActivityWaitingBinding;
import com.example.izobonga_waiting_app.interfaces.WaitingActivityView;
import com.example.izobonga_waiting_app.service.WaitingService;
import com.example.izobonga_waiting_app.view.dialog.ChildDialog;
import com.example.izobonga_waiting_app.view.dialog.TotalDialog;
import com.example.izobonga_waiting_app.view.dialog.TicketDialog;
import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class WaitingActivity extends BaseActivity implements WaitingActivityView {
    private final String TAG = WaitingActivity.class.getName();
    FireBaseApi firebaseApi;
    ActivityWaitingBinding binding;
    ArrayList<String> numbers;
    TicketDialog mTicketDialog;
    TotalDialog mTotalDialog;
    ChildDialog mChildDialog;

    int mChild;
    int mTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_waiting);
        binding.setActivity(this);
        firebaseApi = new FireBaseApi();
        waitingListener();
        binding.waitingPhoneText.addTextChangedListener(new PhoneNumberFormattingTextWatcher()); //입력하면 phone number form 으로 만들기
        binding.waitingPhoneText.setEnabled(false); //editText 사용 불가능하게 만들기
        binding.waitingPhoneText.setFocusable(false);
        numbers = new ArrayList<>();

        binding.setClickCallback(clickListener);
    }

    private void tryWaiting(int personnelNumber, int childNumber){
        WaitingService waitingService = new WaitingService(WaitingActivity.this);
        waitingService.increaseWaitingCount(
                Timestamp.now(),
                binding.waitingPhoneText.getText().toString(),
                personnelNumber,
                childNumber);
    }
    private View.OnClickListener ticketListener = new View.OnClickListener() {
        public void onClick(View v) {
            mTicketDialog.dismiss();
            mTicketDialog = null;
            binding.waitingPhoneText.setText("010-");
            binding.waitingPhoneText.addTextChangedListener(new PhoneNumberFormattingTextWatcher()); //입력하면 phone number form 으로 만들기
        }
    };

    //총 인원 수 선택 Dialog 다음 버튼 클릭 시 이벤트 처리
    private View.OnClickListener personnelNextListener = new View.OnClickListener() {
        public void onClick(View v) {
            String personnelNumber = mTotalDialog.totalNumber.getText().toString();
            mTotal = Integer.parseInt(personnelNumber);
            if ( mTotal < 2){
                printToast("2인 이상부터 예약 가능합니다,");
            }else {
//                if (mTotalDialog != null && mTotalDialog.isShowing()){
//                    mTotalDialog.dismiss();
//                }
                showChildDialog();
            }
        }
    };

    //아동 인원 수 선택 Dialog 다음 버튼 클릭 시 이벤트 처리
    private View.OnClickListener childNextListener = new View.OnClickListener() {
        public void onClick(View v) {
            mChild = Integer.parseInt(mChildDialog.childNumber.getText().toString());
            showProgressDialog();
            tryWaiting(mTotal, mChild);
        }
    };


    private View.OnClickListener totalPreListener = new View.OnClickListener() {
        public void onClick(View v) {
            mTotal = 0;
            if (mTotalDialog != null){
                mTotalDialog.dismissDialog();
            }
        }
    };

    //아동 인원 수 선택 Dialog 다음 이전 클릭 시 이벤트 처리
    private View.OnClickListener childPreListener = new View.OnClickListener() {
        public void onClick(View v) {
            mChild = 0;
            if (mChildDialog != null && mChildDialog.isShowing()){
//                mChildDialog.dismiss();
                mChildDialog.dismissDialog();
            }
        }
    };

    private void showTotalDialog(){
        Log.d("total, ", String.valueOf(mTotal));
        if (mTotalDialog == null) {
            mTotalDialog = new TotalDialog(WaitingActivity.this, personnelNextListener, totalPreListener);
            mTotalDialog.setCancelable(false);
            mTotalDialog.setCanceledOnTouchOutside(false);
        }
        mTotalDialog.show();
    }
    private void showChildDialog(){
        Log.d("child, ", String.valueOf(mChild));
        if (mChildDialog == null){
            mChildDialog = new ChildDialog(WaitingActivity.this, childNextListener, childPreListener);
            mChildDialog.setCancelable(false);
            mChildDialog.setCanceledOnTouchOutside(false);
        }
        mChildDialog.show();
    }
    private void showTicketDialog(int ticket){
        Log.d("ticket", String.valueOf(ticket));
        if (mTicketDialog == null){
            mTicketDialog = new TicketDialog(WaitingActivity.this, ticketListener, ticket);
            mTicketDialog.setCancelable(false);
            mTicketDialog.setCanceledOnTouchOutside(false);
        }
        mTicketDialog.show();
    }

    public void waitingListener(){
        WaitingService waitingService = new WaitingService(this);
        waitingService.waitingListener();
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v == null)
                return;
            switch(v.getId()) {
                case R.id.btn1:
                    binding.waitingPhoneText.append("1");
                    break;
                case R.id.btn2:
                    binding.waitingPhoneText.append("2");
                    break;
                case R.id.btn3:
                    binding.waitingPhoneText.append("3");
                    break;
                case R.id.btn4:
                    binding.waitingPhoneText.append("4");
                    break;
                case R.id.btn5:
                    binding.waitingPhoneText.append("5");
                    break;
                case R.id.btn6:
                    binding.waitingPhoneText.append("6");
                    break;
                case R.id.btn7:
                    binding.waitingPhoneText.append("7");
                    break;
                case R.id.btn8:
                    binding.waitingPhoneText.append("8");
                    break;
                case R.id.btn9:
                    binding.waitingPhoneText.append("9");
                    break;
                case R.id.btn_delete:
                    int length = binding.waitingPhoneText.getText().length();
                    if (length > 3) {
                        binding.waitingPhoneText.getText().delete(length - 1, length);
                    }
                    break;
                case R.id.btn0:
                    binding.waitingPhoneText.append("0");
                    break;
                case R.id.btn_input:
                    String phoneNumber = binding.waitingPhoneText.getText().toString();
                    boolean isCheck = binding.checkbox.isChecked();
                    if (isCheck){
                        if(phoneNumber.length() != 13){
                            printToast("잘 못된 휴대폰 번호입니다. 다시 입력해주세요.");
                        }else{
                            //finish input
                            printToast(binding.waitingPhoneText.getText().toString());
                            //request server(폰번호);
                            showTotalDialog();
                        }
                    }else{
                        printToast("disagree permission");
                    }

                    break;
            }
        }
    };
//

    @Override
    public void validateSuccess(String message, int ticket) {
        hideProgressDialog();
        if (mChildDialog != null  && mChildDialog.isShowing()){
                mChildDialog.dismissDialog();
        }
        if (mTotalDialog != null  && mTotalDialog.isShowing()){
            mTotalDialog.dismissDialog();
        }

        showTicketDialog(ticket);
    }

    @Override
    public void validateFailure(String message) {
        hideProgressDialog();
        printToast(message);
        printLog(TAG, message);
    }

    @Override
    public void modified(long size) {
        binding.waitingCountText.setText(String.valueOf(size));
    }
}
