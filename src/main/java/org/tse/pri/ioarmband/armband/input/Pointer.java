package org.tse.pri.ioarmband.armband.input;


public class Pointer {
	private Integer id;
	private Float x;
	private Float y;
	private Float dist;
	private Float size;
	private Float dx;
	private Float dy;
	private Float dz;
	private String direction;
	private Float lastUpdate;
	private boolean isVisibleNow;
	
	public Pointer(){
		this(0, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
	}
	public Pointer(Integer id, Float x, Float y, 
			Float dist, Float size,
			Float dx, Float dy, Float dz, 
			Float lastUpdate) {
		super();
		this.id = id;
		this.x = x;
		this.y = y;
		this.dist = dist;
		this.size = size;
		this.dx = dx;
		this.dy = dy;
		this.dz = dz;
		this.lastUpdate = lastUpdate;
		isVisibleNow = true;
		updateDirection();
	}
	public Pointer(Pointer p) {
		super();
		this.id = p.id;
		this.x = p.x;
		this.y = p.y;
		this.dist = p.dist;
		this.size = p.size;
		this.dx = p.dx;
		this.dy = p.dy;
		this.dz = p.dz;
		this.lastUpdate = p.lastUpdate;
		isVisibleNow = p.isVisibleNow;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Float getX() {
		return x;
	}
	public void setX(Float x) {
		this.x = x;
	}
	public Float getY() {
		return y;
	}
	public void setY(Float y) {
		this.y = y;
	}
	public Float getSize() {
		return size;
	}
	public void setSize(Float size) {
		this.size = size;
	}
	public Float getDx() {
		return dx;
	}
	public void setDx(Float dx) {
		this.dx = dx;
		updateDirection();
	}
	public Float getDy() {
		return dy;
	}
	public void setDy(Float dy) {
		this.dy = dy;
		updateDirection();
	}
	public Float getDz() {
		return dz;
	}
	public void setDz(Float dz) {
		this.dz = dz;
	}
	public Float getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Float lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public Float getDist() {
		return dist;
	}
	public void setDist(Float dist) {
		this.dist = dist;
	}
	@Override
	public String toString() {
		return "Pointer [id=" + id + ", x=" + x + ", y=" + y + ", dist=" + dist
				+ ", size=" + size + ", dx=" + dx + ", dy=" + dy + ", dz=" + dz
				+ ", lastUpdate=" + lastUpdate + "]";
	}
	public boolean isVisibleNow() {
		return isVisibleNow;
	}
	public void setVisibleNow(boolean isVisibleNow) {
		this.isVisibleNow = isVisibleNow;
	}
	public String getDirection() {
		return direction;
	}
	
	private void updateDirection(){
		if(dx == dy){
			direction = "none";
			return;
		}
		if( Math.abs(dx) > Math.abs(dy) ){
			if(dx > 0)
				direction = "right";
			else
				direction = "left";
		}else{
			if(dy > 0)
				direction = "up";
			else
				direction = "down";
		}
	}
	
	
}
