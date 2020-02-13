package com.example.izobonga_waiting_app;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.izobonga_waiting_app.view.LoadingBarDialog;

public class BaseActivity extends AppCompatActivity {
    LoadingBarDialog loadingBarDialog;

    //onCreate() : 생성시 -> 초기화 처리, 뷰생성
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        showDialog();
    }

    void initView(){
        loadingBarDialog = new LoadingBarDialog(BaseActivity.this);
    }
    void showDialog(){
        loadingBarDialog.show();
    }
    void dismissDialog(){
        loadingBarDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismissDialog();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        return super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
//        /**
//         * getMenuInflater()를 통해 Inflater 객체를 가져와 앞서 정의했던 Menu Resource의 ID 값을 인자로 넘겨주게 됩니다.
//         * 그러면 전개자가 리소스의 내용을 읽어 Menu Item에 대한 객체를 생성하고 해당 메뉴를 App Bar에 반영하게 됩니다.
//         */
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        switch (item.getItemId())
//        {
//            case R.id.toolbar_menu_icon1 :
//                printToast("clicked1!");
//                return true;
//            case R.id.toolbar_menu_icon2 :
//                printToast("clicked2!");
//                return true;
//            case R.id.toolbar_menu_item1 :
//                printToast("clicked3!");
//                return true;
//            case R.id.toolbar_menu_item2 :
//                printToast("clicked4!");
//                return true;
//            case R.id.toolbar_menu_item3 :
//                printToast("clicked5!");
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

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
