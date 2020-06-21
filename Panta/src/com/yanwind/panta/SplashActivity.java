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
				WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏显示
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题栏
		
		setContentView(R.layout.activity_splash);
		
        new Handler().postDelayed(new Runnable() {
        	//延迟2秒执行任务
            public void run() {
                
            	finish();//销毁自己，显示HomeActivity
            	overridePendingTransition(R.anim.side_from_left, R.anim.side_to_right);//控制动画效果
            }
        }, 3000);
	}
}