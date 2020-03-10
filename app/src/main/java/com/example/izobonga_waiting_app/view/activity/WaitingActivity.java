package com.example.izobonga_waiting_app.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.izobonga_waiting_app.FireBaseApi;
import com.example.izobonga_waiting_app.R;
import com.example.izobonga_waiting_app.databinding.ActivityWaitingBinding;
import com.example.izobonga_waiting_app.model.WaitingData;
import com.example.izobonga_waiting_app.view.dialog.ChildDialog;
import com.example.izobonga_waiting_app.view.dialog.PersonnelDialog;
import com.example.izobonga_waiting_app.view.dialog.WaitingDialog;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class WaitingActivity extends AppCompatActivity {
    FireBaseApi firebaseApi;
    ActivityWaitingBinding binding;
    RecyclerView recyclerView;
    ArrayList<String> numbers;
    TextView tvNumber;
    WaitingDialog waitingDialog;
    PersonnelDialog personnelDialog;
    ChildDialog childDialog;

    int mChild;
    int mPersonnel;
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

    private void addCustomer(int personnelNumber, int childNumber){
        firebaseApi.increaseWaitingCount( //대기번호 늘리기
                Timestamp.now(),
                binding.waitingPhoneText.getText().toString(),
                personnelNumber,
                childNumber
        );
    }
    private View.OnClickListener positiveListener = new View.OnClickListener() {
        public void onClick(View v) {
            waitingDialog.dismiss();
            binding.waitingPhoneText.setText("010-");
            binding.waitingPhoneText.addTextChangedListener(new PhoneNumberFormattingTextWatcher()); //입력하면 phone number form 으로 만들기
//            binding.waitingPhoneText.addTextChangedListener(new PhoneNumberFormattingTextWatcher()); //입력하면 phone number form 으로 만들기
        }
    };

    //총 인원 수 선택 Dialog 다음 버튼 클릭 시 이벤트 처리
    private View.OnClickListener personnelNextListener = new View.OnClickListener() {
        public void onClick(View v) {
            String personnelNumber = personnelDialog.personnelNumber.getText().toString();
            mPersonnel = Integer.parseInt(personnelNumber);
            if ( mPersonnel < 2){
                printToast("2인 이상부터 예약 가능합니다,");
            }else {
                if (personnelDialog != null){
                    personnelDialog.dismiss();
                }
                showChildDialog();
            }
        }
    };

    //아동 인원 수 선택 Dialog 다음 버튼 클릭 시 이벤트 처리
    private View.OnClickListener childNextListener = new View.OnClickListener() {
        public void onClick(View v) {
            mChild= Integer.parseInt(childDialog.childNumber.getText().toString());
            addCustomer(mPersonnel, mChild);
            if (childDialog != null){
                childDialog.dismiss();
            }
            showWaitingDialog();
        }
    };


    private View.OnClickListener personnelPreListener = new View.OnClickListener() {
        public void onClick(View v) {
            mPersonnel = 0;
            if (personnelDialog != null){
                personnelDialog.dismiss();
            }

        }
    };


    //아동 인원 수 선택 Dialog 다음 이전 클릭 시 이벤트 처리
    private View.OnClickListener childPreListener = new View.OnClickListener() {
        public void onClick(View v) {
            mChild = 0;
            if (childDialog != null){
                childDialog.dismiss();
            }
        }
    };

    private void showPersonnelDialog(){
        personnelDialog = new PersonnelDialog(WaitingActivity.this, personnelNextListener, personnelPreListener);
        personnelDialog.show();
    }
    private void showChildDialog(){
        childDialog = new ChildDialog(WaitingActivity.this, childNextListener, childPreListener);
        childDialog.show();
    }
    private void showWaitingDialog(){
        waitingDialog = new WaitingDialog(WaitingActivity.this, positiveListener);
        waitingDialog.show();
    }


    void printToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void waitingListener(){
        final DocumentReference docRef = firebaseApi.db.collection("manager").document("waiting");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("waitingListener", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("waitingListener", "Current data: " + snapshot.getData());
                    WaitingData waitingData = snapshot.toObject(WaitingData.class);
                    int waiting = waitingData.getQueue().size();
                    binding.waitingCountText.setText(waiting+"");
                } else {
                    Log.d("waitingListener", "Current data: null");
                }
            }
        });
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
                            showPersonnelDialog();
                        }
                    }else{
                        printToast("disagree permission");
                    }

                    break;
            }
        }
    };

}
