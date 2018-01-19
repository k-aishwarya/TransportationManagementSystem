package com.cab.allocation.controller;

import java.util.Arrays;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cab.allocation.model.Cab;
import com.cab.allocation.model.Member;
import com.cab.allocation.repo.CabRepository;
import com.cab.allocation.repo.MemberRepository;
import com.cab.allocation.service.CabAllocationService;
import com.cab.allocation.service.DropPointService;
import com.cab.allocation.service.MemberService;
import com.cab.allocation.util.CabRequest;
import com.cab.allocation.util.MemberRequest;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.cab.allocation.util.Constants;
import com.cab.allocation.util.DropPointUtil;

@RestController
@RequestMapping(Constants.CAB_ALLOCATION)
public class WebController {
	
	@Autowired 
	MemberService memberService;

	@Autowired
	CabRepository cabRepository;
	
	@Autowired
	MemberRepository memberRepository;

	@Autowired
	DropPointService dropPointService;
	
	@Autowired
	CabAllocationService cabAllocationService;

	@RequestMapping(value = Constants.PING, method = {RequestMethod.GET}, consumes = MediaType.ALL_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> ping() {
		return new ResponseEntity<>("ping Successful", HttpStatus.OK);
	}

	@RequestMapping(value = Constants.DROP_POINTS, method = {
			RequestMethod.POST }, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> dropPoint(@RequestBody String jsonString) {
		System.out.println(jsonString);
		
		JSONObject jsonObj = DropPointUtil.JSONStringToObject(jsonString);
		if(jsonObj==null){
			System.out.println("Not a JSON object");
			return new ResponseEntity<>("Bad JSON Body", HttpStatus.BAD_REQUEST);
		}
		System.out.println(jsonObj);	
		
		dropPointService.readyUtilArrays(jsonString);

		if (!dropPointService.setDropPoint(jsonObj).getFirst()) {
			String msg = dropPointService.setDropPoint(jsonObj).getSecond();
			dropPointService.removeDropPointMem();
			return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>("Done", HttpStatus.CREATED);
	}

	@RequestMapping(value = Constants.REGISTER, method = {
			RequestMethod.POST }, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> register(@RequestBody MemberRequest memberRequest) {
		System.out.println(memberRequest.toString());
		if(!validateGender(memberRequest.getGender())) {
			System.out.println("invalid Gender Type :" + memberRequest.getGender());
			return new ResponseEntity<>("INVALID GENDER TYPE", HttpStatus.BAD_REQUEST);
		}
		if(!validateDropPoint(memberRequest.getDrop_point()).getFirst()) {
			System.out.println(validateDropPoint(memberRequest.getDrop_point()).getSecond() + " "+ memberRequest.getDrop_point());
			return new ResponseEntity<>(validateDropPoint(memberRequest.getDrop_point()).getSecond(), HttpStatus.BAD_REQUEST);
		}
		
		Member mem = new Member(memberRequest.getTeam_member_id(), memberRequest.getGender(),
				memberRequest.getDrop_point());
		memberRepository.save(mem);
		
		return new ResponseEntity<>("Done", HttpStatus.CREATED);
	}

	@RequestMapping(value = Constants.CABS, method = {
			RequestMethod.POST }, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> cab(@RequestBody CabRequest[] cabRequests) {
		for (CabRequest cabReq : cabRequests) {
			System.out.println(cabReq.toString());
			Cab cab = new Cab(cabReq.getCab_id(), cabReq.getCost(), cabReq.getCapacity());
			cabRepository.save(cab);
		}
		return new ResponseEntity<>("Done", HttpStatus.CREATED);
	}

	@RequestMapping(value = Constants.ROUTE_PLAN, method = {RequestMethod.GET})
	public ResponseEntity<ObjectNode> routePlan() {
		
		try {
			ObjectNode routePlan = cabAllocationService.getRoutePlan();		
			return new ResponseEntity<>(routePlan, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = Constants.FINDALL_MEMBERS, method = {RequestMethod.GET})
	public String findAll() {
		String result = ""; 

		for (Member mem : memberRepository.findAll()) {
			result += mem.toString() + "<br>";
			System.out.println(mem.toString());
		}

		return result;
	}

	@RequestMapping(value = Constants.FIND_MEMBERS_BY_ID, method = {RequestMethod.GET}, consumes = MediaType.ALL_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public String findById(@RequestParam("id") long id) {
		String result = "";
		result = memberRepository.findOne(id).toString();
		return result;
	}
	
	private boolean validateGender(String gender){
		for (Constants.MEMBER.GENDER gen : Constants.MEMBER.GENDER.values()) {
	        if (gen.name().equals(gender)) {
	        		return true;
	        }
	    }
		return false;
	}
	
	private Pair<Boolean,String> validateDropPoint(String dropPoint){
		if(null==DropPointUtil.points) {
			String msg = "No Drop Points registered by the Cab Service Providers";
			return Pair.of(false, msg);
		}
		for (String dp : Arrays.copyOfRange(DropPointUtil.points, 1, DropPointUtil.points.length)) {
	        if (dropPoint.equals(dp)) {
	        	String msg = "OK";
	        	return Pair.of(true, msg);
	        }
	    }
		String msg = "Invalid Drop Point";
		return Pair.of(false, msg);
	}
}
