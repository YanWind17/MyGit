package com.yanwind.panta;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class DoubleSurface extends SingleSurface {
	
	public DoubleSurface(Context context, AttributeSet attrs) {
		
		super(context, attrs);
	}

	@Override
	public void rePaint() {
		//重绘surface
		Canvas canvas = getHolder().lockCanvas();//锁定画布
		canvas.drawColor(Color.rgb(102, 102, 102));//绘制背景色
		
		Paint paint = new Paint();//创建画笔
		paint.setAntiAlias(true);//设置为抗锯齿
		
		for (int i = 0; i < row; i++) {
			
			int leftSpace = diameter / 4;
			leftSpace += (i % 2 == 0) ? 0 : diameter / 2;//根据行数计算左侧留白
			
			for (int j = 0; j < col; j++) {
					
				switch (map[i][j].getStatus()) {
				
					//根据map元素不同的值给paint设置不同颜色
				
					case ACCESS:
						paint.setColor(Color.rgb(181, 181, 181));
						break;
						
					case BAR:
						paint.setColor(Color.rgb(233, 132, 94));
						break;
						
					case ROLE:
						paint.setColor(Color.rgb(0, 204, 71));
						break;
				}
				
				//绘制整个map
				canvas.drawOval(new RectF(leftSpace + diameter * j, diameter * i
						+ topSpace, leftSpace + diameter * (j + 1), diameter
						* (i + 1) + topSpace), paint);
			}
		}
		
		switch (player) {
			//根据玩家身份设置提示点的颜色
			case BAR:
				paint.setColor(Color.rgb(233, 132, 94));
				break;
				
			case ROLE:
				paint.setColor(Color.rgb(0, 204, 71));
				break;
		}
		
		//在上方留白中心绘制1.5倍Point直径的提示点
		float radius = (float) (1.5 * diameter / 2.0);
		float halfWidth = (float) (width / 2.0);
		float halfHeight = (float) (topSpace / 2.0);
		canvas.drawOval(new RectF(halfWidth - radius, halfHeight - radius,
				halfWidth + radius, halfHeight + radius), paint);
		
		//解锁画布并提交
		getHolder().unlockCanvasAndPost(canvas);
	}

	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		// TODO Auto-generated method stub
		
		if(motionEvent.getAction() == motionEvent.ACTION_DOWN){

			int leftSpace = diameter / 4;//默认左侧留白为直径的1/4
			int x = (int) motionEvent.getY() - topSpace;//触摸x坐标减去上方留白
			int y = (int) motionEvent.getX();//获取触摸y坐标
			
			if(x >= 0){
				//防止点击上方留白，产生数组越界异常
				
				x /= diameter;//计算触摸点的行数x
				leftSpace = (x % 2 == 0) ? 0 : diameter / 2;//根据行数设置左侧留白
				y -= leftSpace;//触摸y坐标减去左侧留白
				
				if(y >= 0){
					
					y /= diameter;//计算触摸点的列数y
				}
			}
			else{
				
				x = y = -1;//设定负值，不符合设置路障的条件
			}

			if (!isOver && x >= 0 && x < row && y >= 0 && y < col) {
				
				if(player == Status.BAR && map[x][y].getStatus() == Status.ACCESS){
					
					//设置路障的一些操作
					map[x][y].setStatus(Status.BAR);//设置触摸点point为路障
					goneSet.add(map[x][y]);//触摸点point加入gone集合
					player = Status.ROLE;//当前玩家身份设为Role
					playSound(soundID[0]);//播放Move音效
					step++;//步数自增
				}
				else if(player == Status.ROLE && isCanWalk(map[x][y])){
					
					//Role移动的一些操作
					map[rolePoint.getX()][rolePoint.getY()]
							.setStatus(Status.ACCESS);// 旧的Role坐标设为ACCESSS
					map[x][y].setStatus(Status.ROLE);//当前可走点为新Role
					rolePoint = map[x][y];//重设Role
					playSound(soundID[0]);//播放Move音效
					player = Status.BAR;//当前玩家身份设为Bar
				}
			}
			
			rePaint();//重绘surface
			
			if(isEdge(rolePoint)){
				//Role走到map边缘，游戏失败
				
				isOver = true;//游戏状态为结束
				playSound(soundID[2]);//播放Fail音效
				new FialDialog(getContext(),this).show();//弹出失败对话框
			}
			else{
				
				boolean isSurround = true;//初始化Role被完全围住
				
				for(int i = 0; i < 6; i++){
					
					if(getPerPoint(rolePoint, i).getStatus() != Status.BAR){
						//6个方向有一个为通路，Role没有被完全围住
						
						isSurround = false;
					}
				}
				if(isSurround){
					//Role无路可走，游戏成功
					isOver = true;//游戏状态为结束
					playSound(soundID[1]);//播放Success音效
					new SuccessDialog(getContext(),this).show();//弹出成功对话框
				}
			}
		}	
			
		return true;//事件已消费完
	}

	private boolean isCanWalk(Point point) {
		//判断当前point是否Role可走
		for(int i = 0; i < 6; i++){
			
			Point perPoint = getPerPoint(rolePoint, i);
			
			if(point.equals(perPoint) && point.getStatus() == Status.ACCESS){
				//只有相邻且为通路才可走
				
				return true;
			}
		}
		
		Toast.makeText(getContext(), "距离太远，朕过不去呀！", 1000).show();//提示当前点Role不可走
		
		return false;
	}
}