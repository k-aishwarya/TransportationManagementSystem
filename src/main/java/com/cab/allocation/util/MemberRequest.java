package com.cab.allocation.util;

import org.junit.validator.ValidateWith;

public class MemberRequest {
	
	private String team_member_id;
	private String gender;
	private String drop_point;
	
	
	public String getTeam_member_id() {
		return team_member_id;
	}


	public void setTeam_member_id(String team_member_id) {
		this.team_member_id = team_member_id;
	}


	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}


	public String getDrop_point() {
		return drop_point;
	}


	public void setDrop_point(String drop_point) {
		this.drop_point = drop_point;
	}


	@Override
    public String toString() {
        return "MemberRequest = team_member_id=\"" + team_member_id + "\"" +
                ", gender=\"" + gender + "\""+
                ", drop_point=\"" + drop_point + "\"";
    }
}
