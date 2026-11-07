package com.klef.soa.service;

import java.util.List;

import com.klef.soa.entity.Supplier;

public interface SupplierService {
	
	 
	 
	 Supplier addSupplier(Supplier s);
	 Supplier updateSupplier(Supplier s);
	 List<Supplier> displayAllSuppliers();
	 Supplier displaySupplierById(Long id);
	 String delecteSupplierById(Long id);
	 List<Supplier> displayByName(String name);
		List<Supplier> displayByType(String type);
	 

}
