package com.cab.allocation.model;

import java.util.LinkedList;
import java.util.List;

import com.cab.allocation.util.Constants;

public class Route {
	private String cabId;
	private List<String> memberTeamIds;
	private List<String> route;
	private int cost;
	
	public Route() {
		route = new LinkedList<String>();
		route.add(Constants.DROP_POINT.TARGET_HEADQUARTER);
		cost=0;
		memberTeamIds = new LinkedList<String>();
	}
	
	public String getCabId() {
		return cabId;
	}
	public void setCabId(String cabId) {
		this.cabId = cabId;
	}
	public List<String> getMemberTeamIds() {
		return memberTeamIds;
	}
	public void setMemberTeamIds(List<String> memberTeamIds) {
		this.memberTeamIds = memberTeamIds;
	}
	public List<String> getRoute() {
		return route;
	}
	public void setRoute(List<String> route) {
		this.route = route;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public void addCost(int cost) {
		this.cost+=cost;
	}
	
	 @Override
	    public String toString() {
	        return " cabId=\"" + cabId + "\"" +
	                ", cost=\"" + cost + "\""+
	                ", memberTeamIds=\"" + memberTeamIds + "\""+
	                ", route=\"" + route + "\"";
	    }
}
