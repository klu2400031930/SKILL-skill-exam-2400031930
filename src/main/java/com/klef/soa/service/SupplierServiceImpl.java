package com.klef.soa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klef.soa.entity.Supplier;
import com.klef.soa.repository.SupplierRepository;

@Service
public class SupplierServiceImpl  implements SupplierService
{
	
	@Autowired  
	private SupplierRepository repo;
	@Override
	public Supplier addSupplier(Supplier s) {
		return repo.save(s);
	}
	@Override
	public Supplier updateSupplier(Supplier s) {
		Optional<Supplier> optional = repo.findById(s.getSupplierId());
				if(optional.isPresent())
				{
					Supplier supplier = optional.get();
					
					supplier.setName(s.getName());
					supplier.setType(s.getType());
					supplier.setStatus(s.getStatus());
					supplier.setLocation(s.getLocation());
					supplier.setCreatedAt(s.getCreatedAt());
					supplier.setUpdatedAt(s.getUpdatedAt());
					
					return repo.save(supplier);
				}
				else
				{
					return null;
				}
	}
	
	@Override
	public List<Supplier> displayByName(String name) 
	{
		
		return repo.findByName(name);
	}

	@Override
	public List<Supplier> displayByType(String type) 
	{
		return repo.findByType(type);
	}
	@Override
	public List<Supplier> displayAllSuppliers() {
		return repo.findAll();
	}
	@Override
	public Supplier displaySupplierById(Long id) {
		
		return repo.findById(id).orElse(null);
	}
	@Override
	public String delecteSupplierById(Long id) {
		
		boolean flag = repo.existsById(id);
		if(flag)
		{
			repo.deleteById(id);
			return "Supplier Deleted Successfully";
		}
		else
		{
			return "Supplier ID Not Found";
		}
		
		
	}
}
	
	


