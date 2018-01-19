package com.cab.allocation.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.cab.allocation.util.Constants;
import com.cab.allocation.util.DropPointUtil;

@Component
public class DropPointService {
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private void DropPointService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    private void dropTable() throws PSQLException {
    		String sql = "DROP TABLE "+ Constants.DROP_POINT.TABLE +";";
    		System.out.println(sql);
    		jdbcTemplate.execute(sql);
    }
    
    private void createTable() throws PSQLException { 
    	
    		StringBuilder sql = new StringBuilder();
    		sql.append("CREATE TABLE "+ Constants.DROP_POINT.TABLE +"( ");
    		sql.append(DropPointUtil.column[0]);
    		sql.append(" CHAR(50) PRIMARY KEY NOT NULL,");
        for(int i=1;i<DropPointUtil.column.length;i++) {
        		sql.append(DropPointUtil.column[i]);
        		sql.append(" INT NOT NULL");
        		if(i<DropPointUtil.column.length-1)
        			sql.append(",");
        }
        sql.append(" );");   
    		System.out.println(sql);
    		jdbcTemplate.execute(sql.toString());
    }
    
    private void insert(String point, int[] dist) throws PSQLException {
	    	StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO "+ Constants.DROP_POINT.TABLE +" VALUES( ");
		sql.append("'"+point + "',");
		for(int i = 0 ;i< dist.length ; i++) {
	    		sql.append(dist[i]);
	    		if(i<dist.length-1)
	    			sql.append(",");
	    }
	    sql.append(" );");   
		System.out.println(sql);
		jdbcTemplate.execute(sql.toString());
    }
    
    public Pair<Boolean,String> setDropPoint(JSONObject jsonObj) {
    		JSONArray keys = jsonObj.names();
		Map<String, String> rows = new HashMap<String, String>();
		for (int i = 0; i < keys.length(); ++i) {
			String key = null;
			try {
				key = keys.getString(i);
				rows.put(key,jsonObj.getString(key));
			} catch (JSONException e) {
				e.printStackTrace();
				String msg = e.getMessage();
				return Pair.of(false, msg);
			}			
		}
		
		try {
			dropTable();
		} catch (Exception e) {
			System.out.println("ERROR: table DROP_POINT does not exist");
		}
		
		try {
			createTable();
		} catch (Exception e) {
			String msg = "ERROR: Unable to create the table DROP_POINT";
			System.out.println(msg);
			return Pair.of(false, msg);
		}		
		
		DropPointUtil.DropPointTable = new HashMap<>();
		for (int i=0 ; i < DropPointUtil.points.length ; i++ ) {
		    System.out.println("key : " + DropPointUtil.points[i]);
		    String[] distStr = rows.get(DropPointUtil.points[i]).split(",");	
		    int[] distInt = new int[distStr.length];
		    Map<String,Integer> distMap = new HashMap<>();
		    for(int j =0; j<distInt.length ; j++ ){
		    		distInt[j]=Integer.parseInt(distStr[j]);
		    		distMap.put(DropPointUtil.column[j+1], distInt[j]);
		    }
		    try {
		    		DropPointUtil.DropPointTable.put(DropPointUtil.points[i], distMap);
				insert(DropPointUtil.points[i],distInt);
			} catch (Exception e) {
				String msg = "ERROR: Unable to insert into the table DROP_POINT";
				System.out.println(msg);
				return Pair.of(false, msg);
			}
		}
		
		readyUtilTable();
		String msg = "OK";
		return Pair.of(true,msg);
    }
    
    @PostConstruct
    public void postConstruct() {
    		readyUtilArraysFromDatabase();
    		readyUtilTable();
    }
    
    public void readyUtilTable(){
    		if(DropPointUtil.points==null) {
    			System.out.println("Util Arrays are not intialized");
    			return;
    		}
    	
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		for(int i = 0 ; i < DropPointUtil.column.length ; i++) {
			sql.append(DropPointUtil.column[i]);
			if(i<DropPointUtil.column.length-1)
				sql.append(", ");
		}
		sql.append(" from "+ Constants.DROP_POINT.TABLE +"");
		System.out.println(sql);
		EntityManager session = entityManagerFactory.createEntityManager();       
		try {
	    		Query query = (Query)session.createNativeQuery(sql.toString());
	    		
	    		List<Object[]> list = query.getResultList();
	    		DropPointUtil.dropPointMap = new HashMap<>();
	    		DropPointUtil.DropPointTable = new HashMap<>();
	    		if(!list.isEmpty()) {
	    			for(int i=0; i <list.size(); i++) {
	    				Object[] k = list.get(i);
	    				String firstKey = k[0].toString().trim();
	    				System.out.print(k[0].toString());
	    				Map<String,Integer> distMap = new HashMap<>();
	    				for(int j=1; j <k.length; j++) {
	    					Integer val = (Integer) k[j];
	    					System.out.print(" "+val);
	    					Pair<String,String> p = Pair.of(firstKey,DropPointUtil.column[j]);
	    					DropPointUtil.dropPointMap.put(p,val);
	    					distMap.put(DropPointUtil.column[j], val);
	    				}
	    				DropPointUtil.DropPointTable.put(firstKey, distMap);
	    				System.out.println();
	    			}
	    		}
	    		else {
	    			System.out.println("Empty data set of Drop Point Table found in DROP_POINT DB");
    				System.out.println("Util Arrays are not ready. Call drop_point API.");
	    		}
		}
		catch (Exception e){
			System.out.println("Exception in finding data set of Drop Point Table in DROP_POINT DB");
			System.out.println("Util Arrays are not ready. Call drop_point API.");
		}
		finally {
		if(session.isOpen()) 
        		session.close();
		}
    }
    
    public void readyMemberGroupByDP() {
	    	if(DropPointUtil.points==null) {
				System.out.println("Util Arrays are not intialized");
				return;
		}
	    	StringBuilder sql = new StringBuilder();
		sql.append("select " +Constants.MEMBER.DROP_POINT_COL+ " " +Constants.MEMBER.TEAM_ID_COL + " " + Constants.MEMBER.GENDER_COL);
		sql.append(" from "+ Constants.MEMBER.TABLE +" GROUP BY " + Constants.MEMBER.DROP_POINT_COL +";");
		System.out.println(sql);
		EntityManager session = entityManagerFactory.createEntityManager();       
		try {
	    		Query query = (Query)session.createNativeQuery(sql.toString());
	    		
	    		List<Object[]> list = query.getResultList();
	    		if(!list.isEmpty()) {
	    			System.out.println(list);
//	    			for(int i=0; i <list.size(); i++) {
//	    				Object[] k = list.get(i);
//	    				String firstKey = k[0].toString().trim();
//	    				System.out.print(k[0].toString());
//	    				Map<String,Integer> distMap = new HashMap<>();
//	    				for(int j=1; j <k.length; j++) {
//	    					
//	    				}
//	    				DropPointUtil.DropPointTable.put(firstKey, distMap);
//	    				System.out.println();
//	    			}
	    		}
	    		else {
	    			System.out.println("Empty data set of Drop Point Table found in MEMBER table");
	    		}
		}
		catch (Exception e){
			System.out.println("Exception in finding data set of Drop Point Table in MEMBER table");
		}
		finally {
		if(session.isOpen()) 
        		session.close();
		}
    }
    
    public void readyUtilArrays(String jsonString) {
		String tempString=jsonString.replaceAll("\\s+","");
		tempString=tempString.replaceAll("\",\"","\";\"");
		tempString=tempString.replaceAll("[{|}|,|:|[0-9]+|\"]","");
		String[] dps =  tempString.split(";");
		System.out.println(tempString);
		
		JSONObject jsonObj = DropPointUtil.JSONStringToObject(jsonString);
		if(jsonObj!=null) {
			JSONArray keys = jsonObj.names();
			DropPointUtil.points = new String[keys.length()];
			DropPointUtil.column = new String[keys.length()];
			DropPointUtil.column[0] = Constants.DROP_POINT.FIRST_COLMN_NAME;
		
			for(int i=0; i<keys.length(); i++) {
    				DropPointUtil.points[i] = dps[i];   		
			}
			for(int i=0; i<keys.length()-1; i++) {
				DropPointUtil.column[i+1] = dps[i+1];
			}
    		}
		else {
			System.out.println("Util Arrays are not ready");
		}
    	
	}
    
    public void readyUtilArraysFromDatabase(){
    		String sql = ("select "+ Constants.DROP_POINT.FIRST_COLMN_NAME + " from " + Constants.DROP_POINT.TABLE);
		EntityManager session = entityManagerFactory.createEntityManager();
		try {
    			Query query = (Query)session.createNativeQuery(sql);
    			List list = query.getResultList();
    			if (!list.isEmpty()){ 
    				DropPointUtil.points = new String[list.size()];
    				DropPointUtil.column = new String[list.size()];
    				DropPointUtil.column[0] = Constants.DROP_POINT.FIRST_COLMN_NAME;
    			    for(int i=0; i< list.size(); i++) {
    			    		String str = (String)list.get(i).toString().trim(); 
    	    				DropPointUtil.points[i] = str;
    	    				System.out.println(DropPointUtil.points[i]);
    			    }
    			    for(int i=0; i<list.size()-1; i++) {
    					DropPointUtil.column[i+1] = list.get(i+1).toString().trim();
    					System.out.println(DropPointUtil.column[i+1]);
    			    }
    			}
    			else {
    				System.out.println("Empty data set of Drop Point Table found in DROP_POINT DB");
    				System.out.println("Util Arrays are not ready. Call drop_point API.");
    			}
		}
		catch (Exception e){
			System.out.println("Exception in finding data set of Drop Point Table in DROP_POINT DB");
			System.out.println("Util Arrays are not ready. Call drop_point API.");
		}
		finally {
			if(session.isOpen()) 
				session.close();
		}
    }
    
    public void removeDropPointMem(){
    		try {
			dropTable();
		} catch (Exception e) {
			String msg = "ERROR: table DROP_POINT does not exist";
		}
    		DropPointUtil.points=null;                                        
    		DropPointUtil.column=null;                                             
    		DropPointUtil.dropPointMap=null;	
    		DropPointUtil.DropPointTable=null;
    }
    

}
