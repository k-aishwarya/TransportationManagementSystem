package com.cab.allocation.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cab.allocation.model.Cab;
import com.cab.allocation.model.Member;
import com.cab.allocation.model.Route;
import com.cab.allocation.repo.CabRepository;
import com.cab.allocation.repo.MemberRepository;
import com.cab.allocation.util.DropPointUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class CabAllocationService {
	
	@Autowired
	MemberRepository memberRepository;

	@Autowired
	CabRepository cabRepository;
	
	public ObjectNode getRoutePlan() {
		
		Iterable<Cab> cabDb = cabRepository.findAll();
		Cab[] cabs = new Cab[(int) cabRepository.count()];
		int i = 0;
		int totalCapacity = 0 ;
		for(Cab cab : cabDb) {		
			cabs[i] = cab;
			totalCapacity += cab.getCapacity();
			i++;
		}		
		
		long totalMembers = memberRepository.count();
		if(totalCapacity<totalMembers) {
			JsonNodeFactory factory = null;
			ObjectMapper mapper = new ObjectMapper();
			JsonNode msgNode = mapper.valueToTree("Total Capacity is less than the total Members");
			Map<String,JsonNode> msg = new HashMap<String,JsonNode>();
			msg.put("Error", msgNode);	
			ObjectNode finalObj = new ObjectNode(factory , msg);
			return finalObj;	
		}
		
		
		
		// Putting the cabs in decending order of the capacity/cost
		Arrays.sort(cabs, (b , a) -> 
				((Float)((float)a.getCapacity()/a.getCost()))
				.compareTo
				((Float)((float)b.getCapacity()/b.getCost()))
				);
		
		for(Cab cab : cabs) {
			System.out.print("cabId: "+cab.getCabId()+ " Capacity/Cost: ");
			System.out.println(((float)cab.getCapacity()/cab.getCost()));
		}
		
		
		//Sort the DropPointTable by their Values in decending order
		for(String key: DropPointUtil.DropPointTable.keySet()) {
			Map<String,Integer> valueMap = DropPointUtil.DropPointTable.get(key);
			Map<String,Integer> sortedMap = sortByValue(valueMap);
			DropPointUtil.DropPointTable.put(key, sortedMap);
			System.out.println("FirstKey: "+key);
			printMap(sortedMap);
			System.out.println();			
		}
		
		
		//Clone dpMem for calculation 
		Map<String, Map<String, Integer>> dPTableCloned = DropPointUtil.DropPointTable;
		printMap(dPTableCloned);
		
		//Get Member and DropPoint relationship
		Map<String,List<Member>> dPMem = new HashMap<>();
		Iterable<Member> mems = memberRepository.findAll();
		for(int k=1; k<DropPointUtil.points.length; k++) {
			dPMem.put(DropPointUtil.points[k], new LinkedList<Member>());
		}
		for(Member mem : mems) { 
			List<Member> list = dPMem.get(mem.getDropPoint());
			list.add(mem);
			dPMem.put(mem.getDropPoint(), list);
		}
		printMap(dPMem);
		System.out.println();
		
		//Clone dpMem for calculation 
		Map<String,List<Member>> dPMemCloned = dPMem;
		printMap(dPMemCloned);
		
		
		long membersDropped = 0;
		long totalCost = 0;
		//Lets start assigning the route to each cab		
		List<Route> routes = new LinkedList<Route>();
		for(Cab cab : cabs){
			int curRouteindex = 0;
			int curCapacity = cab.getCapacity();	
			Route route = new Route();
			route.setCabId(cab.getCabId());
			//Getting routes ( drop points)
			while(curCapacity>0) {
				String curDropPoint = route.getRoute().get(curRouteindex);
				Map<String,Integer> sortedMap = dPTableCloned.get(curDropPoint);
				Map.Entry<String,Integer> entry = sortedMap.entrySet().iterator().next();
				String drop_point = entry.getKey();
				Integer dp_dist = entry.getValue();
				route.addCost(dp_dist*cab.getCost());
				totalCost += dp_dist*cab.getCost();
				route.getRoute().add(drop_point);
				curRouteindex++;
				List<Member> members = dPMemCloned.get(drop_point);
				List<Member> memFromDp = new LinkedList<Member>();
				for(Member mem : members) {
					memFromDp.add(mem);
				}
				
				//Drop point has no member
				if(members.size()<=curCapacity) {
					curCapacity = curCapacity - members.size();
					for(Member mem : memFromDp) {
						membersDropped++;
						route.getMemberTeamIds().add(mem.getMemberId());
						dPMemCloned = looseDroppedMembers(drop_point, mem.getMemberId(), dPMemCloned);
					}
					dPTableCloned = looseDropPointInfo(drop_point,dPTableCloned);
					
				}
				//It still has member
				else {					 
					for(int j=0 ; j<curCapacity; j++) {
						membersDropped++;
						String memId = memFromDp.get(j).getMemberId();
						route.getMemberTeamIds().add(memId);
						dPMemCloned = looseDroppedMembers(drop_point, memId, dPMemCloned);
					}
					curCapacity = 0;
				}
				
			}
			System.out.println(route);
			routes.add(route);
			if(membersDropped==totalMembers)
				break;
		}
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode routesNode = mapper.valueToTree(routes);
		JsonNode totalCostNode = mapper.valueToTree(totalCost);
		JsonNodeFactory factory = null;
		
		Map<String,JsonNode> nodeMap = new HashMap<String,JsonNode>();
		nodeMap.put("total_cost", totalCostNode);
		nodeMap.put("routes", routesNode);	
		ObjectNode finalObj = new ObjectNode(factory , nodeMap);	
		return finalObj;		
		
	}		
	
	private static Map<String, Integer> sortByValue(Map<String, Integer> unsortMap) {

        List<Map.Entry<String, Integer>> list =
                new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());
        
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o2,
                               Map.Entry<String, Integer> o1) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        
        return sortedMap;
    }

    public static <K, V> void printMap(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey()
                    + " Value : " + entry.getValue());
        }
    }
    
    public Map<String, Map<String, Integer>> looseDropPointInfo(String dp, Map<String, Map<String, Integer>> dPTableCloned ) {
    		for(Map<String, Integer> entry : dPTableCloned.values()) {
    			entry.remove(dp);
    		}
    		return dPTableCloned;
    }
    
    public Map<String,List<Member>> looseDroppedMembers(String dp , String memId, Map<String,List<Member>> dPMemCloned){
    			List<Member> mems = dPMemCloned.get(dp);
            for(Member member : mems) {
            		if(member.getMemberId().equals(memId)) {
            			mems.remove(member);
            			break;
            		}
            }
            return dPMemCloned;
    }    
    

}
	
	

