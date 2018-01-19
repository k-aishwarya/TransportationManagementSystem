package com.cab.allocation.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cab.allocation.model.Cab;

public interface CabRepository extends CrudRepository<Cab, Long>{
	List<Cab> findById(String id);
}

