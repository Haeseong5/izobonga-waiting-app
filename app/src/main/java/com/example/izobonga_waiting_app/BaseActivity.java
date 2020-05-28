package com.example.izobonga_waiting_app;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.izobonga_waiting_app.view.dialog.TicketDialog;
import com.gun0912.tedpermission.PermissionListener;

import java.util.List;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true); //무한진행상태 표시.
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }
    /*
    BadTokenException의 이유를 한문장으로 정리하면 ,
     예외 메시지에 ”is your activity running?” 이라고 명시되어 있듯 종료된 Activity의 context를 인자로 다이얼로그 창을 표시하려고 할 때 발생한다.
    다이얼로그 창을 표시할 Activity가 없기 때문에 안드로이드 런타임이 나쁜 토큰(Bad Token1))이라는 예외를 던진다.
     */

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void onClickListener(View view){

    }

    public PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
//                Toast.makeText(Activity1.this, "권한 허가", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {

        }
    };
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void printToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    public void printLog(String TAG, String log){
        Log.d(TAG, log);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //최전면 표시 -> 필요한 애니메이션 실행 등의 화면갱신처리
    }
    @Override
    protected void onPause() {
        super.onPause();
        //일부 표시 or (정지상태) -> 화면 갱신 처리를 정지 또는 일시정지할 때
        //필요없는 리소스를 해제하거나 필요한 데이터를 영속화
    }

    @Override
    protected void onStart() {
        super.onStart();
        //비표시 시 -> 통신이나 센서처리 시작
    }
    @Override
    protected void onStop() {
        super.onStop();
        //비표시 시(정지상태) 통신이나 센서처리를 정지
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //표시 시 (재시작만) -> 보통 아무것도 처리하지 않음.
    }
}
