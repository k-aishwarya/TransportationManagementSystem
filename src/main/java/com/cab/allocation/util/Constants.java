package com.cab.allocation.util;

public interface Constants {
		
	//Member DB
	public interface MEMBER{
		public String TABLE = "MEMBER";		
		public String DB_ID = "id";
		public String TEAM_ID_COL = "team_member_id";
		public String GENDER_COL = "gender";
		public String DROP_POINT_COL = "drop_point";
		public enum GENDER{
			M,F;
		}
	}
	
	//Cab DB
	public interface CAB{
		public String TABLE = "CAB";	
		public String DB_ID = "id";
		public String ID = "cab_id";
		public String COST = "cost";
		public String CAPACITY = "capacity";
	}
	
	//DropPoints DB
	public interface DROP_POINT{
		public String TABLE = "DROP_POINT";
		public String TARGET_HEADQUARTER = "target_headquarter";
		public String FIRST_COLMN_NAME = "points";
	}
	
	//Request Pattern
	public String CAB_ALLOCATION = "/cab-allocation";
	public String PING = "/ping";		
	public String DROP_POINTS = "/drop_points";
	public String REGISTER = "/register";
	public String CABS = "/cabs";
	public String ROUTE_PLAN = "/route_plan";
	public String FINDALL_MEMBERS = "/findall_members";
	public String FIND_MEMBERS_BY_ID = "/findbyid";
}
