package com.cab.allocation.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cab.allocation.util.Constants;

@Entity
@Table(name = Constants.CAB.TABLE)
public class Cab {
	private static final long serialVersionUID = -3009157732242241606L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = Constants.CAB.DB_ID)
	private long id;	

	@Column(name = Constants.CAB.ID)
	private String cabId;
	
	@Column(name = Constants.CAB.COST)
	private int cost;

	@Column(name = Constants.CAB.CAPACITY)
	private int capacity;

	protected Cab() {
	}

	public Cab(String id, int cost, int capacity) {
		this.cabId = id;
		this.cost = cost;
		this.capacity = capacity;
	}
	
	public String getCabId() {
		return cabId.trim();
	}

	public void setCabId(String cabId) {
		this.cabId = cabId;
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
		return String.format("Cab[id=%s, gender='%s', dropPoint='%s']", cabId.trim(), cost, capacity);
	}
}
