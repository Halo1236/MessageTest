package com.ayhalo.messagetest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class MainActivity extends Activity implements OnClickListener{

    private Button button1;         //获取验证码
    private Button button2;         //注册
    private EditText PhoneNumber;   //手机号码输入框
    private EditText CaptchaNum;    //验证码输入框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupViews();
        SMSSDK.initSDK(this, "1192f44f83721", "aebc784d5e1c58682ccf9dd6c3fd51f6");

        EventHandler eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                String phone = PhoneNumber.getText().toString().trim();
                Toast.makeText(MainActivity.this,phone,Toast.LENGTH_SHORT).show();
                SMSSDK.getVerificationCode("86",phone);
                CaptchaNum.requestFocus();
                break;

            case R.id.button2:
                String captch = CaptchaNum.getText().toString().trim();
                SMSSDK.submitVerificationCode("86", PhoneNumber.getText().toString().trim(), captch);
                break;


            default:
                break;
        }
    }
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.d("result","event:");
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    Toast.makeText(MainActivity.this, "验证成功",
                            Toast.LENGTH_SHORT).show();
                    //这里写验证成功后程序的流程，一般在这里要调用注册或者登陆接口
                }
                else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    Toast.makeText(MainActivity.this, "验证已发送",
                            Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(MainActivity.this, "验证失败",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };



    /*
    * 初始化界面
    * */
    private void setupViews(){

        button1=(Button)findViewById(R.id.button1);
        button2=(Button)findViewById(R.id.button2);

        PhoneNumber=(EditText)findViewById(R.id.editText1);
        CaptchaNum=(EditText)findViewById(R.id.editText2);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();

    }

}
