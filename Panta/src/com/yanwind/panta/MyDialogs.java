package com.yanwind.panta;

import java.util.Random;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.Window;
import android.widget.Toast;

class SuccessDialog extends AlertDialog.Builder{

	SingleSurface singleSurface;
	
	protected SuccessDialog(Context context,SingleSurface singleSurface) {
		super(context);
		this.singleSurface = singleSurface; 
		initDialog();//初始化对话框
	}
	
	private void initDialog(){
		
		int step = singleSurface.getStep();//获取步数
		setTitle("").setMessage("\n太秀了，你用" + step + "步就抓到了他！");//设置内容

		setNegativeButton("溜了溜了", new DialogInterface.OnClickListener() {
			
			//添加按钮，无响应事件
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		
		setPositiveButton("再抓一次", new DialogInterface.OnClickListener() {
			//添加按钮，重开game
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
				int level = singleSurface.getLevel();//获取游戏当前level
				singleSurface.setLevel(level + 1);//设置level+1
				
				singleSurface.initGame();//初始化游戏
				singleSurface.rePaint();//重绘surface
			}
		});
	}
}

class SettingDialog {
	
	Context context;
	SingleSurface singleSurface;
	final String[] items = { "随机", "简单", "普通", "困难", "地狱" };
	public SettingDialog(Context context,SingleSurface singleSurface) {
		
		this.context = context;
		this.singleSurface = singleSurface;
	}
	
	public void show(){
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle("请选择难度等级：");//设置内容
		
		dialog.setItems(items, new DialogInterface.OnClickListener() {
			//设置选择列表
			@Override
			public void onClick(DialogInterface dialog, int which) {

				//根据用户选择设置相应路障个数
				switch (which) {
					
					case 0:
						singleSurface.barrNum = new Random().nextInt(13) + 8;
						break;
						
					case 1:
						singleSurface.barrNum = 20;
						break;
						
					case 2:
						singleSurface.barrNum = 16;
						break;
						
					case 3:
						singleSurface.barrNum = 12;
						break;
						
					case 4:
						singleSurface.barrNum = 8;
						break;
				}
				
				singleSurface.initGame();//初始化游戏
				singleSurface.rePaint();//重绘Surface
			}
		});
		
		dialog.show();//显示dialog
	}
}

class RuleDialog {
	
	Context context;

	public RuleDialog(Context context) {
		
		this.context = context;
	}
	
	public void show(){
		
		//一些游戏介绍
		String rule = "传说在很久很久以前.......\n" 
		+ "好吧扯远了，这是个策略小游戏，" 
				+ "你的任务是设置路障让他无路可走，如果他成功逃到了地图边缘，那么你就输了！\n嗯，我假装你已经听懂了！\n";
		
       AlertDialog dialog = new AlertDialog.Builder(context) 
       .setTitle("游戏规则：").setMessage(rule).create();//设置标题和内容并创建 
       Window window = dialog.getWindow();//获取window
       window.setGravity(Gravity.CENTER);//设置dialog显示的位置   
       window.setWindowAnimations(R.style.dialog_style);//添加动画
       dialog.show(); //显示dialog
	}
}

class FialDialog extends AlertDialog.Builder{

	SingleSurface singleSurface;
	
	protected FialDialog(Context context,SingleSurface dissHimSurface) {
		super(context);
		this.singleSurface = dissHimSurface; 
		initDialog();//初始化对话框
	}
	
	private void initDialog(){
		
		setTitle("").setMessage("\n(￣へ￣) 卧槽，居然让他跑了！");//设置内容
		
		setNegativeButton("不玩了", new DialogInterface.OnClickListener() {
			
			//添加按钮，无响应事件
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		
		setPositiveButton("盘他！盘他！", new DialogInterface.OnClickListener() {
			
			//添加按钮，重开一局
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
				singleSurface.initGame();//初始化游戏
				singleSurface.rePaint();//重绘surface
			}
		});
	}
}