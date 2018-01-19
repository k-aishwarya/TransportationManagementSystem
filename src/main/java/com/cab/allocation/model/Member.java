package com.cab.allocation.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cab.allocation.util.Constants;

@Entity
@Table(name =Constants.MEMBER.TABLE)
public class Member implements Serializable {
	private static final long serialVersionUID = -3009157732242241606L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = Constants.MEMBER.DB_ID)
	private long id;	
	
	@Column(name = Constants.MEMBER.TEAM_ID_COL)
	private String memberId;
	
	@Column(name = Constants.MEMBER.GENDER_COL)
	private String gender;

	@Column(name = Constants.MEMBER.DROP_POINT_COL)
	private String dropPoint;

	protected Member() {
	}

	public Member(String id, String gender, String dropPoint) {		
		this.memberId = id;
		this.gender = gender;
		this.dropPoint = dropPoint;
	}
	
	public String getMemberId() {
		return memberId.trim();
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getGender() {
		return gender.trim();
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDropPoint() {
		return dropPoint.trim();
	}

	public void setDropPoint(String dropPoint) {
		this.dropPoint = dropPoint;
	}

	@Override
	public String toString() {
		return String.format("Member[id=%s, gender='%s', dropPoint='%s']", memberId.trim(), gender.trim(), dropPoint.trim());
	}
	
}
