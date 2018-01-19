package com.cab.allocation.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.cab.allocation.model.Member;

public interface MemberRepository extends CrudRepository<Member, Long>{
	List<Member> findById(String id);
}
