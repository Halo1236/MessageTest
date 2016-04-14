package com.ayhalo.messagetest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class MainActivity extends Activity implements OnClickListener{

    private static String appKey="1192f44f83721";
    private static String appSecret="aebc784d5e1c58682ccf9dd6c3fd51f6";
    private Button button1;//获取验证码
    private Button button2;//注册
    private EditText PhoneNumber;//手机号码输入框
    private EditText CaptchaNum;//验证码输入框
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupViews();
        initSMS();
        setupViews();
    }
    private void initSMS(){
        SMSSDK.initSDK(this,appKey,appSecret);
        EventHandler eh=new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {

                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
            }
        };
        SMSSDK.registerEventHandler(eh);
    }
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


    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button1:
                String phone = PhoneNumber.getText().toString();
                Toast.makeText(MainActivity.this,phone,Toast.LENGTH_SHORT).show();
                SMSSDK.getVerificationCode("86",phone);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }


}
