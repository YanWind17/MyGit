package com.yanwind.panta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class HomeActivity extends Activity implements OnClickListener{

	
	Intent intent;//传入GameAcitivity的意图对象
	Button button_single,button_double,button_rule;//主界面下3个按钮
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		startActivity(new Intent(HomeActivity.this,SplashActivity.class));//让启动画面先显示
		
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏显示
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题栏
		
		setContentView(R.layout.activity_home);
		
		initView();//初始化控件
		initListener();//绑定各种监听器
	}

	private void initListener() {
		// TODO Auto-generated method stub
		button_single.setOnClickListener(this);
		button_double.setOnClickListener(this);
		button_rule.setOnClickListener(this);
	}

	private void initView() {
		// TODO Auto-generated method stub
		button_single = (Button) findViewById(R.id.button_single);
		button_double = (Button) findViewById(R.id.button_double);
		button_rule = (Button) findViewById(R.id.button_rule);
		
		intent = new Intent(HomeActivity.this,GameActivity.class);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		
			case R.id.button_single:
				
				intent.putExtra("MODE", "Single");//将数据封装进意图
				startActivity(intent);//启动游戏人机模式
				overridePendingTransition(R.anim.side_from_right, R.anim.side_to_left);//控制动画效果
				break;
				
			case R.id.button_double:
				
				intent.putExtra("MODE", "Double");//将数据封装进意图
				startActivity(intent);//启动游戏双人模式
				overridePendingTransition(R.anim.side_from_right, R.anim.side_to_left);//控制动画效果
				break;
				
			case R.id.button_rule:
					
				new RuleDialog(this).show();//显示游戏规则
				break;
		}
	}
}