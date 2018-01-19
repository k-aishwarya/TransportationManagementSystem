package com.cab.allocation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cab.allocation.model.Cab;
import com.cab.allocation.repo.CabRepository;

@Service
public class CabService implements CabRepository{

	@Override
	public <S extends Cab> S save(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Cab> Iterable<S> save(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cab findOne(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<Cab> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Cab> findAll(Iterable<Long> ids) {
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
	public void delete(Cab entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Iterable<? extends Cab> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Cab> findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getTotalCapacity() {
		Iterable<Cab> cabs = findAll();
		 long totalCapacity = 0;
		 for (Cab cab : cabs) {
			totalCapacity += cab.getCapacity();
		 }
		return totalCapacity;
	}
	
	

}
