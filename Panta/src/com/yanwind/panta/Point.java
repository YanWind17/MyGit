package com.yanwind.panta;

class Route implements Comparable<Route>{
	//定义路线类
	
	private int index;//路线方向编号
	private int distance;//次路线到index方向障碍或者地图边缘的距离
	
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
		
		//重写comareTo方法，用于TreeMap自动排序
		return this.distance >= route.distance ? 1 : -1;
	}
}

enum Status{
	
	//Point的三个状态，通路、障碍、角色
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
		
		//重写equals方法，用于判断2个Point是否相同
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