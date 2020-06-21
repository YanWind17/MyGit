package com.yanwind.panta;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class GameActivity extends Activity implements OnClickListener {

	SingleSurface pantaSurface;
	Button button_setting, button_reload, button_sound;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 全屏显示
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 取消标题栏

		setContentView(R.layout.activity_main);

		loadSurface();//根据意图动态加载相应的Surface
		initView();//初始化控件
		initListener();//绑定各种监听器
	}

	private void loadSurface() {
		//根据意图动态加载相应的Surface
		
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);//创建相对布局的布局参数对象
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,
				RelativeLayout.TRUE);//添加布局参数
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.layout_game);// 获得初始game相对布局

		switch (getIntent().getStringExtra("MODE")) {
			// 通过意图获取HomeActivity传过来的游戏模式数据
	
			case "Single":
				// 人机模式，创建SingleSurface对象
				pantaSurface = new SingleSurface(this, null);
				break;
	
			case "Double":
				// 双人模式，创建DoubleSurface对象
				pantaSurface = new DoubleSurface(this, null);
				break;
		}

		relativeLayout.addView(pantaSurface, layoutParams);// 将相应pantaSurface添加到布局里
	}

	private void initView() {

		//获取3个按钮
		button_setting = (Button) findViewById(R.id.button_setting);
		button_reload = (Button) findViewById(R.id.button_reload);
		button_sound = (Button) findViewById(R.id.button_sound);

		//将3个按钮置于顶层，防止被Surface覆盖
		button_setting.bringToFront();
		button_reload.bringToFront();
		button_sound.bringToFront();
	}

	private void initListener() {
		// TODO Auto-generated method stub

		button_setting.setOnClickListener(this);
		button_reload.setOnClickListener(this);
		button_sound.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {

		finish();// 按返回键结束当前Activity
		overridePendingTransition(R.anim.side_from_left, R.anim.side_to_right);// 控制动画效果
	};

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {

		case R.id.button_setting:

			//显示settingDialog
			new SettingDialog(this,pantaSurface).show();
			break;

		case R.id.button_reload:
			//重置游戏
			pantaSurface.resetGame();
			break;

		case R.id.button_sound:
			//控制游戏音效是否开启
			if (pantaSurface.isSound) {
				//关闭音效，重设图片
				pantaSurface.isSound = false;
				button_sound.setBackgroundResource(R.drawable.button_sound_off);
			} else {
				//打开音效，重设图片
				pantaSurface.isSound = true;
				button_sound.setBackgroundResource(R.drawable.button_sound_on);
			}
			break;
		}
	}
}