	package com.klef.soa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.klef.soa.entity.Supplier;
import com.klef.soa.service.SupplierService;

@RestController
@RequestMapping("/supplier")
public class SupplierController 
{
	@Autowired
	private SupplierService service;
	
	@GetMapping("/")
	private String demo()
	{
		return "Spring Boot Project";
	}
	
	@PostMapping("/add")
	public ResponseEntity<Supplier> addSupplier(@RequestBody Supplier s)
	{
		Supplier supplier = service.addSupplier(s);
		return ResponseEntity.status(201).body(supplier);
		
	}
	
	
	
	
	@GetMapping("/displayall")
	public ResponseEntity<List<Supplier>> displayAllSuppliers()
	{
		List<Supplier> suppliers = service.displayAllSuppliers();
		return ResponseEntity.status(200).body(suppliers);
	}
	
	@GetMapping("/displaybyid/{id}")
	public ResponseEntity<?> displaySupplierById(@PathVariable Long id)
	{
		Supplier supplier = service.displaySupplierById(id);

		if (supplier != null)
		{
			return ResponseEntity.status(200).body(supplier);
		}
		else
		{
			return ResponseEntity.status(404).body("Supplier ID Not Found");
		}
	}
	
	
	@GetMapping("/displaybyname/{name}")
	public ResponseEntity<List<Supplier>> displaySupplierByName(@PathVariable String name)
	{
		List<Supplier> suppliers = service.displayByName(name);
		return ResponseEntity.ok().body(suppliers);
	}
	
	@GetMapping("/displaybytype/{type}")
	public ResponseEntity<List<Supplier>> displaySupplierByType(@PathVariable String type)
	{
		List<Supplier> suppliers = service.displayByType(type);
		return ResponseEntity.ok().body(suppliers);
		
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateSupplier(@RequestBody Supplier supplier)
	{
		Supplier s = service.updateSupplier(supplier);

		if (s != null)
		{
			return ResponseEntity.ok(s);
		}
		else
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Supplier ID Not Found");
		}
	}


	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteSupplierById(@PathVariable Long id)
	{
		String message = service.delecteSupplierById(id);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	
	
	
}





























