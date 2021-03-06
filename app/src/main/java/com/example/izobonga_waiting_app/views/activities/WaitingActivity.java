package com.example.izobonga_waiting_app.views.activities;

import androidx.databinding.DataBindingUtil;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;


import com.example.izobonga_waiting_app.BaseActivity;
import com.example.izobonga_waiting_app.FireBaseHelper;
import com.example.izobonga_waiting_app.R;
import com.example.izobonga_waiting_app.databinding.ActivityWaitingBinding;
import com.example.izobonga_waiting_app.interfaces.WaitingActivityView;
import com.example.izobonga_waiting_app.services.WaitingService;
import com.example.izobonga_waiting_app.views.dialogs.PersonnelDialog;
import com.example.izobonga_waiting_app.views.dialogs.TicketDialog;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Locale;

public class WaitingActivity extends BaseActivity implements WaitingActivityView {
    private final String TAG = WaitingActivity.class.getName();
    FireBaseHelper firebaseHelper;
    ActivityWaitingBinding binding;
    ArrayList<String> numbers;
    TicketDialog mTicketDialog;
    PersonnelDialog mTotalDialog, mChildDialog;
    public TextToSpeech tts;

    int mChild;
    int mTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        hideSoftKey();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_waiting);
        binding.setActivity(this);
        firebaseHelper = new FireBaseHelper();
        setWaitingListener();
        binding.waitingPhoneText.addTextChangedListener(new PhoneNumberFormattingTextWatcher()); //입력하면 phone number form 으로 만들기
        binding.waitingPhoneText.setEnabled(false); //editText 사용 불가능하게 만들기
        binding.waitingPhoneText.setFocusable(false);
        numbers = new ArrayList<>();

        binding.setClickCallback(clickListener);

        if (tts == null){
            tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int i) {
                    tts.setLanguage(Locale.KOREA);
                }
            });
        }

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
            String personnelNumber = mTotalDialog.mTvNumber.getText().toString();
            mTotal = Integer.parseInt(personnelNumber);
            if ( mTotal < 2){
                printToast("2인 이상부터 예약 가능합니다,");
            }else {
                showChildDialog();
            }
        }
    };

    //아동 인원 수 선택 Dialog 다음 버튼 클릭 시 이벤트 처리
    private View.OnClickListener childNextListener = new View.OnClickListener() {
        public void onClick(View v) {
            mChild = Integer.parseInt(mChildDialog.mTvNumber.getText().toString());
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
                mChildDialog.dismissDialog();
            }
        }
    };

    private void showTotalDialog(){
        if (mTotalDialog == null) {
            mTotalDialog = new PersonnelDialog(WaitingActivity.this, personnelNextListener, totalPreListener, getString(R.string.dialog_title), getString(R.string.dialog_sub_title));
            mTotalDialog.setCancelable(false);
            mTotalDialog.setCanceledOnTouchOutside(false);
        }
        mTotalDialog.show();
    }
    private void showChildDialog(){
        if (mChildDialog == null){
            mChildDialog = new PersonnelDialog(WaitingActivity.this, childNextListener, childPreListener, getString(R.string.child_dialog_title), getString(R.string.child_dialog_sub_title));
            mChildDialog.setCancelable(false);
            mChildDialog.setCanceledOnTouchOutside(false);
        }
        mChildDialog.show();
    }
    private void showTicketDialog(int ticket){
        if (mTicketDialog == null){
            mTicketDialog = new TicketDialog(WaitingActivity.this, ticketListener, ticket);
            mTicketDialog.setCancelable(false);
            mTicketDialog.setCanceledOnTouchOutside(false);
        }
        mTicketDialog.show();
    }

    public void setWaitingListener(){
        WaitingService waitingService = new WaitingService(this);
        waitingService.setWaitingEventListener();
    }



    //NumberKeyPad ClickListener
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
                case R.id.waiting_btn_delete:
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
                            //request server(폰번호);
                            showTotalDialog();
                        }
                    }else{
                        printToast("서비스 이용약관에 동의해주셔야 합니다!");
                    }
                    break;
            }
        }
    };

    public void hideStatusBar(){
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
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
        binding.waitingCountText.setText(size + "팀");
    }

    @Override
    public void speak(String ticket) {
        if (tts != null){
            String message = String.format(getString(R.string.tts_message),ticket);
            Log.d(TAG, message);

            tts.setSpeechRate(1.0f); //읽는 속도 설정 : 기본
            tts.setPitch((float) 1);      // 음량
//        tts.setSpeechRate(""); //톤 조절
            tts.speak(message , TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mTTsService.removeTTS();
        if(tts != null){
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }
}
