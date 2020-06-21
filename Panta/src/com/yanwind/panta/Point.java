package com.yanwind.panta;

class Route implements Comparable<Route>{
	//����·����
	
	private int index;//·�߷�����
	private int distance;//��·�ߵ�index�����ϰ����ߵ�ͼ��Ե�ľ���
	
	public Route(int index, int distance) {

		this.index = index;
		this.distance = distance;
	}

	public int getIndex() {
		
		return index;
	}
	
	public void setIndex(int index) {
		
		this.index = index;
	}
	
	public int getDistance() {
		
		return distance;
	}
	
	public void setDistance(int distance) {
		
		this.distance = distance;
	}
	
	@Override
	public int compareTo(Route route) {
		
		//��дcomareTo����������TreeMap�Զ�����
		return this.distance >= route.distance ? 1 : -1;
	}
}

enum Status{
	
	//Point������״̬��ͨ·���ϰ�����ɫ
	ACCESS,BAR,ROLE
}

public class Point {

	private int x;
	private int y;
	private Status status;
	
	public Point(int x, int y) {
		
		this.x = x;
		this.y = y;
		this.status = Status.ACCESS;
	}
	
	@Override
	public boolean equals(Object o) {
		
		//��дequals�����������ж�2��Point�Ƿ���ͬ
		Point point = (Point)o;
		if(this.x == point.x && this.y == point.y){
			
			return true;
		}
		
		return false;
	}

	public int getX() {
		
		return x;
	}

	public void setX(int x) {
		
		this.x = x;
	}

	public int getY() {
		
		return y;
	}

	public void setY(int y) {
		
		this.y = y;
	}

	public Status getStatus() {
		
		return status;
	}

	public void setStatus(Status status) {
		
		this.status = status;
	}
}