
package com.klef.soa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.klef.soa.entity.Supplier;
import com.klef.soa.repository.SupplierRepository;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE)
public class SupplierRepositoryTest {

    @Autowired
    private SupplierRepository repository;

    private Supplier createSupplier(String name, String type) {

        Supplier supplier = new Supplier();

        supplier.setName(name);
        supplier.setType(type);
        supplier.setStatus(true);
        supplier.setLocation("Vijayawada");

        return supplier;
    }


    @Test
    void testFindAllSuppliers() {

        repository.save(
                createSupplier("ABC Suppliers", "Medical"));

        List<Supplier> suppliers = repository.findAll();

        assertNotNull(suppliers);
        assertFalse(suppliers.isEmpty());

        System.out.println("Total Suppliers: " + suppliers.size());
    }


    @Test
    void testFindSupplierById() {

        Supplier saved =
                repository.save(
                        createSupplier("ABC Suppliers", "Medical"));

        Optional<Supplier> supplier =
                repository.findById(saved.getSupplierId());

        assertTrue(supplier.isPresent());

        assertEquals(
                saved.getSupplierId(),
                supplier.get().getSupplierId());

        System.out.println("Supplier ID: "
                + supplier.get().getSupplierId());

        System.out.println("Name: "
                + supplier.get().getName());
    }


    @Test
    void testFindSupplierByName() {

        repository.save(
                createSupplier("ABC Suppliers", "Medical"));

        List<Supplier> suppliers =
                repository.findByName("ABC Suppliers");

        assertFalse(suppliers.isEmpty());

        assertEquals(
                "ABC Suppliers",
                suppliers.get(0).getName());

        System.out.println("Supplier Name: "
                + suppliers.get(0).getName());
    }


    @Test
    void testFindSupplierByType() {

        repository.save(
                createSupplier("XYZ Suppliers", "Dental"));

        List<Supplier> suppliers =
                repository.findByType("Dental");

        assertFalse(suppliers.isEmpty());

        assertEquals(
                "Dental",
                suppliers.get(0).getType());

        System.out.println("Supplier Type: "
                + suppliers.get(0).getType());
    }


    @Test
    void testFindSupplierByInvalidName() {

        List<Supplier> suppliers =
                repository.findByName("NotFoundSupplier");

        assertTrue(suppliers.isEmpty());
    }


    @Test
    void testFindSupplierByInvalidType() {

        List<Supplier> suppliers =
                repository.findByType("NotFoundType");

        assertTrue(suppliers.isEmpty());
    }


    @Test
    void testDatabaseConnection() {

        long count = repository.count();

        assertTrue(count >= 0);

        System.out.println(
                "Database Connection Successful");

        System.out.println(
                "Total Suppliers: " + count);
    }


    @Test
    void testSupplierDetails() {

        Supplier saved =
                repository.save(
                        createSupplier("ABC Suppliers", "Medical"));

        Optional<Supplier> supplier =
                repository.findById(saved.getSupplierId());

        assertTrue(supplier.isPresent());

        Supplier foundSupplier = supplier.get();

        assertNotNull(foundSupplier.getName());
        assertNotNull(foundSupplier.getType());
        assertNotNull(foundSupplier.getLocation());

        System.out.println("Name: "
                + foundSupplier.getName());

        System.out.println("Type: "
                + foundSupplier.getType());

        System.out.println("Location: "
                + foundSupplier.getLocation());

        System.out.println("Status: "
                + foundSupplier.getStatus());
    }


    @Test
    void testTotalSupplierCount() {

        repository.save(
                createSupplier("ABC Suppliers", "Medical"));

        repository.save(
                createSupplier("XYZ Suppliers", "Dental"));

        long count = repository.count();

        assertTrue(count >= 2);

        System.out.println(
                "Total Suppliers: " + count);
    }

}
