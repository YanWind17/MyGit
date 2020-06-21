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
		//�ػ�surface
		Canvas canvas = getHolder().lockCanvas();//��������
		canvas.drawColor(Color.rgb(102, 102, 102));//���Ʊ���ɫ
		
		Paint paint = new Paint();//��������
		paint.setAntiAlias(true);//����Ϊ�����
		
		for (int i = 0; i < row; i++) {
			
			int leftSpace = diameter / 4;
			leftSpace += (i % 2 == 0) ? 0 : diameter / 2;//�������������������
			
			for (int j = 0; j < col; j++) {
					
				switch (map[i][j].getStatus()) {
				
					//����mapԪ�ز�ͬ��ֵ��paint���ò�ͬ��ɫ
				
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
				
				//��������map
				canvas.drawOval(new RectF(leftSpace + diameter * j, diameter * i
						+ topSpace, leftSpace + diameter * (j + 1), diameter
						* (i + 1) + topSpace), paint);
			}
		}
		
		switch (player) {
			//����������������ʾ�����ɫ
			case BAR:
				paint.setColor(Color.rgb(233, 132, 94));
				break;
				
			case ROLE:
				paint.setColor(Color.rgb(0, 204, 71));
				break;
		}
		
		//���Ϸ��������Ļ���1.5��Pointֱ������ʾ��
		float radius = (float) (1.5 * diameter / 2.0);
		float halfWidth = (float) (width / 2.0);
		float halfHeight = (float) (topSpace / 2.0);
		canvas.drawOval(new RectF(halfWidth - radius, halfHeight - radius,
				halfWidth + radius, halfHeight + radius), paint);
		
		//�����������ύ
		getHolder().unlockCanvasAndPost(canvas);
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

			if (!isOver && x >= 0 && x < row && y >= 0 && y < col) {
				
				if(player == Status.BAR && map[x][y].getStatus() == Status.ACCESS){
					
					//����·�ϵ�һЩ����
					map[x][y].setStatus(Status.BAR);//���ô�����pointΪ·��
					goneSet.add(map[x][y]);//������point����gone����
					player = Status.ROLE;//��ǰ��������ΪRole
					playSound(soundID[0]);//����Move��Ч
					step++;//��������
				}
				else if(player == Status.ROLE && isCanWalk(map[x][y])){
					
					//Role�ƶ���һЩ����
					map[rolePoint.getX()][rolePoint.getY()]
							.setStatus(Status.ACCESS);// �ɵ�Role������ΪACCESSS
					map[x][y].setStatus(Status.ROLE);//��ǰ���ߵ�Ϊ��Role
					rolePoint = map[x][y];//����Role
					playSound(soundID[0]);//����Move��Ч
					player = Status.BAR;//��ǰ��������ΪBar
				}
			}
			
			rePaint();//�ػ�surface
			
			if(isEdge(rolePoint)){
				//Role�ߵ�map��Ե����Ϸʧ��
				
				isOver = true;//��Ϸ״̬Ϊ����
				playSound(soundID[2]);//����Fail��Ч
				new FialDialog(getContext(),this).show();//����ʧ�ܶԻ���
			}
			else{
				
				boolean isSurround = true;//��ʼ��Role����ȫΧס
				
				for(int i = 0; i < 6; i++){
					
					if(getPerPoint(rolePoint, i).getStatus() != Status.BAR){
						//6��������һ��Ϊͨ·��Roleû�б���ȫΧס
						
						isSurround = false;
					}
				}
				if(isSurround){
					//Role��·���ߣ���Ϸ�ɹ�
					isOver = true;//��Ϸ״̬Ϊ����
					playSound(soundID[1]);//����Success��Ч
					new SuccessDialog(getContext(),this).show();//�����ɹ��Ի���
				}
			}
		}	
			
		return true;//�¼���������
	}

	private boolean isCanWalk(Point point) {
		//�жϵ�ǰpoint�Ƿ�Role����
		for(int i = 0; i < 6; i++){
			
			Point perPoint = getPerPoint(rolePoint, i);
			
			if(point.equals(perPoint) && point.getStatus() == Status.ACCESS){
				//ֻ��������Ϊͨ·�ſ���
				
				return true;
			}
		}
		
		Toast.makeText(getContext(), "����̫Զ���޹���ȥѽ��", 1000).show();//��ʾ��ǰ��Role������
		
		return false;
	}
}