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
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// ȫ����ʾ
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// ȡ��������

		setContentView(R.layout.activity_main);

		loadSurface();//������ͼ��̬������Ӧ��Surface
		initView();//��ʼ���ؼ�
		initListener();//�󶨸��ּ�����
	}

	private void loadSurface() {
		//������ͼ��̬������Ӧ��Surface
		
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);//������Բ��ֵĲ��ֲ�������
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,
				RelativeLayout.TRUE);//��Ӳ��ֲ���
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.layout_game);// ��ó�ʼgame��Բ���

		switch (getIntent().getStringExtra("MODE")) {
			// ͨ����ͼ��ȡHomeActivity����������Ϸģʽ����
	
			case "Single":
				// �˻�ģʽ������SingleSurface����
				pantaSurface = new SingleSurface(this, null);
				break;
	
			case "Double":
				// ˫��ģʽ������DoubleSurface����
				pantaSurface = new DoubleSurface(this, null);
				break;
		}

		relativeLayout.addView(pantaSurface, layoutParams);// ����ӦpantaSurface��ӵ�������
	}

	private void initView() {

		//��ȡ3����ť
		button_setting = (Button) findViewById(R.id.button_setting);
		button_reload = (Button) findViewById(R.id.button_reload);
		button_sound = (Button) findViewById(R.id.button_sound);

		//��3����ť���ڶ��㣬��ֹ��Surface����
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

		finish();// �����ؼ�������ǰActivity
		overridePendingTransition(R.anim.side_from_left, R.anim.side_to_right);// ���ƶ���Ч��
	};

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {

		case R.id.button_setting:

			//��ʾsettingDialog
			new SettingDialog(this,pantaSurface).show();
			break;

		case R.id.button_reload:
			//������Ϸ
			pantaSurface.resetGame();
			break;

		case R.id.button_sound:
			//������Ϸ��Ч�Ƿ���
			if (pantaSurface.isSound) {
				//�ر���Ч������ͼƬ
				pantaSurface.isSound = false;
				button_sound.setBackgroundResource(R.drawable.button_sound_off);
			} else {
				//����Ч������ͼƬ
				pantaSurface.isSound = true;
				button_sound.setBackgroundResource(R.drawable.button_sound_on);
			}
			break;
		}
	}
}