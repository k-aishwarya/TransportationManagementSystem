package com.cab.allocation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cab.allocation.model.Member;
import com.cab.allocation.repo.MemberRepository;
import com.cab.allocation.util.Constants;
import com.cab.allocation.util.Constants.MEMBER.GENDER;;

@Service
public class MemberService implements MemberRepository{

	@Override
	public List<Member> findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Member> S save(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Member> Iterable<S> save(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Member findOne(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<Member> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Member> findAll(Iterable<Long> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Member entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Iterable<? extends Member> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}
	
	public long getFemaleCount() {
		Iterable<Member> mems = findAll();
		long femaleCount = 0;
		for(Member mem: mems) {
			if(mem.getGender().equals(Constants.MEMBER.GENDER.F)) {
				femaleCount += 1;
			}
		}
		return femaleCount;
	}

}
