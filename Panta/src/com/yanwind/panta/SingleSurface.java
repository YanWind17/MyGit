package com.yanwind.panta;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class SingleSurface extends SurfaceView implements OnTouchListener{

	protected final int row = 9;//地图行数
	protected final int col = 9;//地图列数
	protected int barrNum = 12;//地图障碍数
	
	protected Boolean isOver;//游戏是否结束
	
	protected SoundPool soundPool;//游戏音效播放
	protected int[] soundID;//游戏音效资源ID
	protected boolean isSound = true;//是否开始游戏音效
	protected int diameter;//point的直径
	protected int topSpace;//地图上方留白
	protected int width;//surface宽度
	protected Point rolePoint;//主角point
	protected int step;//游戏步数
	protected int level;//游戏关卡
	protected Status player = Status.BAR;//当前需要进行的玩家身份

	protected Point[][] map;//二维数组存储地图数据
	protected Set<Point> goneSet;//存储已经走过的Point集合，用于重置游戏
	
	public SingleSurface(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		initResources();//初始化一些资源
		getHolder().addCallback(callback);//添加Callback
		setOnTouchListener(this);//绑定触摸监听器
	}

	private void initResources() {
		
		soundID = new int[3];
		
		//防止自定义SurfaceView无法再xml中预览
		if(!isInEditMode()){
			//加载各个声音资源
			soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
			soundID[0] = soundPool.load(getContext(), R.raw.move, 1);
			soundID[1] = soundPool.load(getContext(), R.raw.success, 1);
			soundID[2] = soundPool.load(getContext(), R.raw.fail, 1);
		}
	}
	
	public void playSound(int soundId){
		
		if(isSound){
			//控制音效播放
			soundPool.play(soundId, 1, 1, 0, 0, 1);
		}
	}

	public void rePaint(){
		//重绘surface
		
		Canvas canvas = getHolder().lockCanvas();//锁定画布
		canvas.drawColor(Color.rgb(102, 102, 102));//绘制背景色
		
		Paint paint = new Paint();//创建画笔
		paint.setAntiAlias(true);//设置为抗锯齿
		
		for (int i = 0; i < row; i++) {
			
			int leftSpace = diameter / 4;//初始化左侧留白
			leftSpace += (i % 2 == 0) ? 0 : diameter / 2;//根据行数计算左侧留白
			
			for (int j = 0; j < col; j++) {
					
				switch (map[i][j].getStatus()) {
					//根据map元素不同的值给paint设置不同颜色
					case ACCESS:
						//通路
						paint.setColor(Color.rgb(181, 181, 181));
						break;
						
					case BAR:
						//路障
						paint.setColor(Color.rgb(233, 132, 94));
						break;
						
					case ROLE:
						//主角
						paint.setColor(Color.rgb(0, 204, 71));
						break;
				}
				
				//绘制整个map
				canvas.drawOval(new RectF(leftSpace + diameter * j, diameter * i
						+ topSpace, leftSpace + diameter * (j + 1), diameter
						* (i + 1) + topSpace), paint);
			}
		}
		
		//绘制Level
		paint.setColor(Color.rgb(250, 250, 250));
		paint.setTextSize(topSpace / 3);
		paint.setTextAlign(Align.LEFT);
		canvas.drawText("Level : " + level, diameter / 4, topSpace / 2, paint);
		
		//绘制Step
		paint.setTextAlign(Align.RIGHT);
		paint.setColor(Color.rgb(200, 200, 200));
		canvas.drawText("" + step + " ", width - diameter / 4, topSpace / 2, paint);
	
		//解锁画布并提交
		getHolder().unlockCanvasAndPost(canvas);
	}
	
	public void resetGame(){
		
		goneSet.add(rolePoint);//把Role也加入集合
		Iterator<Point> iterator = goneSet.iterator();//获取goneSet迭代器进行遍历
		
		while(iterator.hasNext()){
			
			//集合内point的状态将被重置为ACCESS
			Point point = iterator.next();
			int x = point.getX();
			int y = point.getY();
			map[x][y].setStatus(Status.ACCESS);
		}
		
		map[4][4].setStatus(Status.ROLE);
		rolePoint = map[4][4];//重新设定Role
		step = 0;//step清零
		isOver = false;//游戏状态复位
		player = Status.BAR;
		rePaint();//重绘
	}

	public void initGame(){
		
		isOver = false;//isOver复位，游戏状态为未结束
		step = 0;//step清零
		map = new Point[row][col];//创建二维数组
		goneSet = new HashSet<Point>();//创建已经走过的Point集合
		
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				map[i][j] = new Point(i, j);//为map每个元素实例化Point对象
			}
		}
		
		map[4][4].setStatus(Status.ROLE);
		rolePoint = map[4][4];//设定map[4][4]为初始Role
				
		for (int i = 0; i < barrNum;) {
			
			//随机指定个数的路障
			int x = (int) ((Math.random() * 100) % row);
			int y = (int) ((Math.random() * 100) % col);
			
			if(map[x][y].getStatus() == Status.ACCESS){
				
				//把随机到的元素设置为路障
				map[x][y].setStatus(Status.BAR);
				i++;
			}
		}
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

			if (x >= 0 && x < row && y >= 0 && y < col) {
				
				if(!isOver && map[x][y].getStatus() == Status.ACCESS){
					
					step++;//step自增
					map[x][y].setStatus(Status.BAR);//设置触摸点point为路障
					goneSet.add(map[x][y]);//触摸点point加入gone集合
					soundPool.play(R.raw.move, 1, 1, 0, 0, 1);
					roleMove();//Role移动
					playSound(soundID[0]);
				}
			}
			
			rePaint();//重绘surface
		}	
				
		return true;//事件已消费完
	}
	
	protected Point getPerPoint(Point point,int index){
		//返回指定point的index方向的point
		
		int x = point.getX();//获取指定point行数x
		int y = point.getY();//获取指定point列数y
		int add = (x % 2 == 0) ? 0 : 1;//根据行数得到add值
		
		switch (index) {
			
			//给6个方向编号为0-5，分别为左上，右上，右，右下，左下，左
			case 0:return map[x - 1][y - 1 + add];
				
			case 1:return map[x - 1][y + add];
				
			case 2:return map[x][y + 1];
				
			case 3:return map[x + 1][y + add];
				
			case 4:return map[x + 1][y - 1 + add];
				
			case 5:return map[x][y - 1];
		}
		
		return null;
	}
	
	protected Boolean isEdge(Point point){
		//判断point是否为边界point
		
		int x = point.getX();
		int y = point.getY();
		
		if(x == 0 || y == 0 || x == row - 1 || y == col - 1){
			
			//当前point满足边界条件即为边界point
			return true;
		}
		
		return false;
	}

	private int getDistance(int index){
		//该方法返回Role到指定方向边界的距离或者路障的距离
		
		int dis = 1;//初始距离为1
		Point point = rolePoint;//初始point为Role
		
		while(true){
			
			point = getPerPoint(point, index);//迭代获取index方向的point
			if(isEdge(point) && point.getStatus() == Status.ACCESS){
				
				//为边界且可达，返回距离dis
				return dis;
			}
			else if(point.getStatus() == Status.BAR){
				
				//为路障，返回-dis
				return dis * (-1);
			}
			else if(point.getStatus() == Status.ACCESS){
				
				//为通路，dis自增
				dis++;
			}
		}
	}
	
	private int getBestIndex() {
		//该方法返回最佳方向index
		
		TreeSet<Route> available = new TreeSet<Route>();//可直接到达边缘的route集合
		TreeSet<Route> unavailable = new TreeSet<Route>();//有路障route集合

		for (int i = 0; i < 6; i++) {
			
			int dis = getDistance(i);//分别获取6个方向dis
			
			if(dis > 0){
				
				//dis为正，加入可达边缘route集合
				available.add(new Route(i, dis));
				
			}else if(dis < 0){
				
				//dis为负，加入有路障route集合
				unavailable.add(new Route(i, dis));
			}
		}
		
		if(available.size() != 0){
			
			//可达边缘集合非空，返回该集合第一个元素route的方向index
			return available.first().getIndex();
		}
		else{
			
			//可达边缘集合非为空，即6个方向都有障碍
			
			int dis = unavailable.first().getDistance();//获取该集合第一个元素route的距离dis
			
			if(dis != -1){
				
				//unavailable内元素存在的dis不为-1，role有路可走，返回获取该集合第一个元素route的方向index
				return unavailable.first().getIndex();
			}
			
			//unavailable内元素的dis全为-1，role无路可走，返回-1
			return -1;
		}
	}
	
	protected void roleMove() {
		//该方法控制Role走向
		
		int bestIndex = getBestIndex();//获取Role最佳走向
		
		if(bestIndex != -1){
			
			Point nextPoint = getPerPoint(rolePoint, bestIndex);//根据bestIndex获取Role的下一跳point
			map[rolePoint.getX()][rolePoint.getY()].setStatus(Status.ACCESS);//旧的Role设置为通路
			map[nextPoint.getX()][nextPoint.getY()].setStatus(Status.ROLE);//下一跳point设置为Role
			rolePoint = nextPoint;//下一跳point设定为新的Role

			if(isEdge(rolePoint)){
				
				//Role到达地图边缘，游戏结束
				playSound(soundID[2]);//播放Fail音效
				isOver = true;//游戏失败，状态为结束
				new FialDialog(getContext(),this).show();//弹出失败对话框
			}
		}
		else{
			
			playSound(soundID[1]);//播放Success音效
			isOver = true;//游戏成功，状态为结束
			new SuccessDialog(getContext(),this).show();//弹出成功对话框
		}	
	}

	Callback callback = new Callback() {
		
		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			// TODO Auto-generated method stub	
		}
		
		@Override
		public void surfaceCreated(SurfaceHolder arg0) {
			// TODO Auto-generated method stub
			
			width = getWidth();//获得屏幕宽度
			diameter = width / (col + 1);//根据屏幕宽度适配point的直径
			topSpace = (getHeight() - diameter * row) / 2;//根据屏幕高度适配map上方留白
			level = 1;//level初始为1
			initGame();//初始化游戏
			rePaint();//重绘surface
		}
		
		@Override
		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
		}
	};
	
	public int getStep() {
		
		return step;
	}

	public void setStep(int step) {
		
		this.step = step;
	}

	public int getLevel() {
		
		return level;
	}

	public void setLevel(int level) {
		
		this.level = level;
	}
}