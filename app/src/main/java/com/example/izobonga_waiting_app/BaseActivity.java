package com.example.izobonga_waiting_app;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

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
    protected void onDestroy() {
        super.onDestroy();
        //폐기 시 필요없는 리소스를 해체, 액티비티 참조는 모두 정리한다.
        //뷰는 액티비티가 폐기된 다음 Garbage Collection에 의해 자동으로 메모리에서 해체
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //표시 시 (재시작만) -> 보통 아무것도 처리하지 않음.
    }
}
