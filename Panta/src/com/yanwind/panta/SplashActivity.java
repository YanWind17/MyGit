package com.yanwind.panta;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);//ȫ����ʾ
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//ȡ��������
		
		setContentView(R.layout.activity_splash);
		
        new Handler().postDelayed(new Runnable() {
        	//�ӳ�2��ִ������
            public void run() {
                
            	finish();//�����Լ�����ʾHomeActivity
            	overridePendingTransition(R.anim.side_from_left, R.anim.side_to_right);//���ƶ���Ч��
            }
        }, 3000);
	}
}