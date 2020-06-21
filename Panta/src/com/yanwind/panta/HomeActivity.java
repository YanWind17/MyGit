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

	
	Intent intent;//����GameAcitivity����ͼ����
	Button button_single,button_double,button_rule;//��������3����ť
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		startActivity(new Intent(HomeActivity.this,SplashActivity.class));//��������������ʾ
		
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);//ȫ����ʾ
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//ȡ��������
		
		setContentView(R.layout.activity_home);
		
		initView();//��ʼ���ؼ�
		initListener();//�󶨸��ּ�����
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
				
				intent.putExtra("MODE", "Single");//�����ݷ�װ����ͼ
				startActivity(intent);//������Ϸ�˻�ģʽ
				overridePendingTransition(R.anim.side_from_right, R.anim.side_to_left);//���ƶ���Ч��
				break;
				
			case R.id.button_double:
				
				intent.putExtra("MODE", "Double");//�����ݷ�װ����ͼ
				startActivity(intent);//������Ϸ˫��ģʽ
				overridePendingTransition(R.anim.side_from_right, R.anim.side_to_left);//���ƶ���Ч��
				break;
				
			case R.id.button_rule:
					
				new RuleDialog(this).show();//��ʾ��Ϸ����
				break;
		}
	}
}