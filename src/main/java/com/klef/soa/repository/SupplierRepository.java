package com.klef.soa.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


import org.springframework.stereotype.Repository;

import com.klef.soa.entity.Supplier;



@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>
{
    List<Supplier> findByName(String name);
    List<Supplier> findByType(String type);
}