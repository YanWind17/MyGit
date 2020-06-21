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

	protected final int row = 9;//��ͼ����
	protected final int col = 9;//��ͼ����
	protected int barrNum = 12;//��ͼ�ϰ���
	
	protected Boolean isOver;//��Ϸ�Ƿ����
	
	protected SoundPool soundPool;//��Ϸ��Ч����
	protected int[] soundID;//��Ϸ��Ч��ԴID
	protected boolean isSound = true;//�Ƿ�ʼ��Ϸ��Ч
	protected int diameter;//point��ֱ��
	protected int topSpace;//��ͼ�Ϸ�����
	protected int width;//surface���
	protected Point rolePoint;//����point
	protected int step;//��Ϸ����
	protected int level;//��Ϸ�ؿ�
	protected Status player = Status.BAR;//��ǰ��Ҫ���е�������

	protected Point[][] map;//��ά����洢��ͼ����
	protected Set<Point> goneSet;//�洢�Ѿ��߹���Point���ϣ�����������Ϸ
	
	public SingleSurface(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		initResources();//��ʼ��һЩ��Դ
		getHolder().addCallback(callback);//���Callback
		setOnTouchListener(this);//�󶨴���������
	}

	private void initResources() {
		
		soundID = new int[3];
		
		//��ֹ�Զ���SurfaceView�޷���xml��Ԥ��
		if(!isInEditMode()){
			//���ظ���������Դ
			soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
			soundID[0] = soundPool.load(getContext(), R.raw.move, 1);
			soundID[1] = soundPool.load(getContext(), R.raw.success, 1);
			soundID[2] = soundPool.load(getContext(), R.raw.fail, 1);
		}
	}
	
	public void playSound(int soundId){
		
		if(isSound){
			//������Ч����
			soundPool.play(soundId, 1, 1, 0, 0, 1);
		}
	}

	public void rePaint(){
		//�ػ�surface
		
		Canvas canvas = getHolder().lockCanvas();//��������
		canvas.drawColor(Color.rgb(102, 102, 102));//���Ʊ���ɫ
		
		Paint paint = new Paint();//��������
		paint.setAntiAlias(true);//����Ϊ�����
		
		for (int i = 0; i < row; i++) {
			
			int leftSpace = diameter / 4;//��ʼ���������
			leftSpace += (i % 2 == 0) ? 0 : diameter / 2;//�������������������
			
			for (int j = 0; j < col; j++) {
					
				switch (map[i][j].getStatus()) {
					//����mapԪ�ز�ͬ��ֵ��paint���ò�ͬ��ɫ
					case ACCESS:
						//ͨ·
						paint.setColor(Color.rgb(181, 181, 181));
						break;
						
					case BAR:
						//·��
						paint.setColor(Color.rgb(233, 132, 94));
						break;
						
					case ROLE:
						//����
						paint.setColor(Color.rgb(0, 204, 71));
						break;
				}
				
				//��������map
				canvas.drawOval(new RectF(leftSpace + diameter * j, diameter * i
						+ topSpace, leftSpace + diameter * (j + 1), diameter
						* (i + 1) + topSpace), paint);
			}
		}
		
		//����Level
		paint.setColor(Color.rgb(250, 250, 250));
		paint.setTextSize(topSpace / 3);
		paint.setTextAlign(Align.LEFT);
		canvas.drawText("Level : " + level, diameter / 4, topSpace / 2, paint);
		
		//����Step
		paint.setTextAlign(Align.RIGHT);
		paint.setColor(Color.rgb(200, 200, 200));
		canvas.drawText("" + step + " ", width - diameter / 4, topSpace / 2, paint);
	
		//�����������ύ
		getHolder().unlockCanvasAndPost(canvas);
	}
	
	public void resetGame(){
		
		goneSet.add(rolePoint);//��RoleҲ���뼯��
		Iterator<Point> iterator = goneSet.iterator();//��ȡgoneSet���������б���
		
		while(iterator.hasNext()){
			
			//������point��״̬��������ΪACCESS
			Point point = iterator.next();
			int x = point.getX();
			int y = point.getY();
			map[x][y].setStatus(Status.ACCESS);
		}
		
		map[4][4].setStatus(Status.ROLE);
		rolePoint = map[4][4];//�����趨Role
		step = 0;//step����
		isOver = false;//��Ϸ״̬��λ
		player = Status.BAR;
		rePaint();//�ػ�
	}

	public void initGame(){
		
		isOver = false;//isOver��λ����Ϸ״̬Ϊδ����
		step = 0;//step����
		map = new Point[row][col];//������ά����
		goneSet = new HashSet<Point>();//�����Ѿ��߹���Point����
		
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				map[i][j] = new Point(i, j);//Ϊmapÿ��Ԫ��ʵ����Point����
			}
		}
		
		map[4][4].setStatus(Status.ROLE);
		rolePoint = map[4][4];//�趨map[4][4]Ϊ��ʼRole
				
		for (int i = 0; i < barrNum;) {
			
			//���ָ��������·��
			int x = (int) ((Math.random() * 100) % row);
			int y = (int) ((Math.random() * 100) % col);
			
			if(map[x][y].getStatus() == Status.ACCESS){
				
				//���������Ԫ������Ϊ·��
				map[x][y].setStatus(Status.BAR);
				i++;
			}
		}
	}
	
	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		// TODO Auto-generated method stub
		
		if(motionEvent.getAction() == motionEvent.ACTION_DOWN){

			int leftSpace = diameter / 4;//Ĭ���������Ϊֱ����1/4
			int x = (int) motionEvent.getY() - topSpace;//����x�����ȥ�Ϸ�����
			int y = (int) motionEvent.getX();//��ȡ����y����
			
			if(x >= 0){
				//��ֹ����Ϸ����ף���������Խ���쳣
				
				x /= diameter;//���㴥���������x
				leftSpace = (x % 2 == 0) ? 0 : diameter / 2;//�������������������
				y -= leftSpace;//����y�����ȥ�������
				
				if(y >= 0){
					
					y /= diameter;//���㴥���������y
				}
			}
			else{
				
				x = y = -1;//�趨��ֵ������������·�ϵ�����
			}

			if (x >= 0 && x < row && y >= 0 && y < col) {
				
				if(!isOver && map[x][y].getStatus() == Status.ACCESS){
					
					step++;//step����
					map[x][y].setStatus(Status.BAR);//���ô�����pointΪ·��
					goneSet.add(map[x][y]);//������point����gone����
					soundPool.play(R.raw.move, 1, 1, 0, 0, 1);
					roleMove();//Role�ƶ�
					playSound(soundID[0]);
				}
			}
			
			rePaint();//�ػ�surface
		}	
				
		return true;//�¼���������
	}
	
	protected Point getPerPoint(Point point,int index){
		//����ָ��point��index�����point
		
		int x = point.getX();//��ȡָ��point����x
		int y = point.getY();//��ȡָ��point����y
		int add = (x % 2 == 0) ? 0 : 1;//���������õ�addֵ
		
		switch (index) {
			
			//��6��������Ϊ0-5���ֱ�Ϊ���ϣ����ϣ��ң����£����£���
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
		//�ж�point�Ƿ�Ϊ�߽�point
		
		int x = point.getX();
		int y = point.getY();
		
		if(x == 0 || y == 0 || x == row - 1 || y == col - 1){
			
			//��ǰpoint����߽�������Ϊ�߽�point
			return true;
		}
		
		return false;
	}

	private int getDistance(int index){
		//�÷�������Role��ָ������߽�ľ������·�ϵľ���
		
		int dis = 1;//��ʼ����Ϊ1
		Point point = rolePoint;//��ʼpointΪRole
		
		while(true){
			
			point = getPerPoint(point, index);//������ȡindex�����point
			if(isEdge(point) && point.getStatus() == Status.ACCESS){
				
				//Ϊ�߽��ҿɴ���ؾ���dis
				return dis;
			}
			else if(point.getStatus() == Status.BAR){
				
				//Ϊ·�ϣ�����-dis
				return dis * (-1);
			}
			else if(point.getStatus() == Status.ACCESS){
				
				//Ϊͨ·��dis����
				dis++;
			}
		}
	}
	
	private int getBestIndex() {
		//�÷���������ѷ���index
		
		TreeSet<Route> available = new TreeSet<Route>();//��ֱ�ӵ����Ե��route����
		TreeSet<Route> unavailable = new TreeSet<Route>();//��·��route����

		for (int i = 0; i < 6; i++) {
			
			int dis = getDistance(i);//�ֱ��ȡ6������dis
			
			if(dis > 0){
				
				//disΪ��������ɴ��Եroute����
				available.add(new Route(i, dis));
				
			}else if(dis < 0){
				
				//disΪ����������·��route����
				unavailable.add(new Route(i, dis));
			}
		}
		
		if(available.size() != 0){
			
			//�ɴ��Ե���Ϸǿգ����ظü��ϵ�һ��Ԫ��route�ķ���index
			return available.first().getIndex();
		}
		else{
			
			//�ɴ��Ե���Ϸ�Ϊ�գ���6���������ϰ�
			
			int dis = unavailable.first().getDistance();//��ȡ�ü��ϵ�һ��Ԫ��route�ľ���dis
			
			if(dis != -1){
				
				//unavailable��Ԫ�ش��ڵ�dis��Ϊ-1��role��·���ߣ����ػ�ȡ�ü��ϵ�һ��Ԫ��route�ķ���index
				return unavailable.first().getIndex();
			}
			
			//unavailable��Ԫ�ص�disȫΪ-1��role��·���ߣ�����-1
			return -1;
		}
	}
	
	protected void roleMove() {
		//�÷�������Role����
		
		int bestIndex = getBestIndex();//��ȡRole�������
		
		if(bestIndex != -1){
			
			Point nextPoint = getPerPoint(rolePoint, bestIndex);//����bestIndex��ȡRole����һ��point
			map[rolePoint.getX()][rolePoint.getY()].setStatus(Status.ACCESS);//�ɵ�Role����Ϊͨ·
			map[nextPoint.getX()][nextPoint.getY()].setStatus(Status.ROLE);//��һ��point����ΪRole
			rolePoint = nextPoint;//��һ��point�趨Ϊ�µ�Role

			if(isEdge(rolePoint)){
				
				//Role�����ͼ��Ե����Ϸ����
				playSound(soundID[2]);//����Fail��Ч
				isOver = true;//��Ϸʧ�ܣ�״̬Ϊ����
				new FialDialog(getContext(),this).show();//����ʧ�ܶԻ���
			}
		}
		else{
			
			playSound(soundID[1]);//����Success��Ч
			isOver = true;//��Ϸ�ɹ���״̬Ϊ����
			new SuccessDialog(getContext(),this).show();//�����ɹ��Ի���
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
			
			width = getWidth();//�����Ļ���
			diameter = width / (col + 1);//������Ļ�������point��ֱ��
			topSpace = (getHeight() - diameter * row) / 2;//������Ļ�߶�����map�Ϸ�����
			level = 1;//level��ʼΪ1
			initGame();//��ʼ����Ϸ
			rePaint();//�ػ�surface
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