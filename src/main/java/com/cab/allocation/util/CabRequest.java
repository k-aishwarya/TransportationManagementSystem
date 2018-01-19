package com.cab.allocation.util;

public class CabRequest {
	private String cab_id;
	private int cost;
	private int capacity;
	
	public String getCab_id() {
		return cab_id;
	}
	public void setCab_id(String cab_id) {
		this.cab_id = cab_id;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	@Override
    public String toString() {
        return "CabRequest =  cab_id=\"" + cab_id + "\"" +
                ", cost=\"" + cost + "\""+
                ", capacity=\"" + capacity + "\"";
    }
}
