package com.klef.soa.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "supplier_table")
public class Supplier {

	 
	
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "sid")
	    private Long supplierId;

	    @Column(name = "sname", length = 100, nullable = false)
	    private String name;

	    @Column(name = "stype", length = 100, nullable = false)
	    private String type;

	    @Column(name = "sstatus", nullable = false)
	    private boolean status;

	    @Column(name = "slocation", length = 100, nullable = false)
	    private String location;

	    @CreationTimestamp
	    private LocalDateTime createdAt;

	    @UpdateTimestamp
	    private LocalDateTime updatedAt;
	 
	 
	
	 public long getSupplierId() {
		 return supplierId;
	 }
	 public void setSupplierId(long supplierId) {
		 this.supplierId = supplierId;
	 }
	 public String getName() {
		 return name;
	 }
	 public void setName(String name) {
		 this.name = name;
	 }
	 public String getType() {
		 return type;
	 }
	 public void setType(String type) {
		 this.type = type;
	 }
	 public boolean getStatus() {
		 return status;
	 }
	 public void setStatus(boolean status) {
		 this.status = status;
	 }
	 public String getLocation() {
		 return location;
	 }
	 public void setLocation(String location) {
		 this.location = location;
	 }
	 public LocalDateTime getCreatedAt() {
		 return createdAt;
	 }
	 public void setCreatedAt(LocalDateTime createdAt) {
		 this.createdAt = createdAt;
	 }
	 public LocalDateTime getUpdatedAt() {
		 return updatedAt;
	 }
	 
	 public void setUpdatedAt(LocalDateTime updatedAt) {
		 this.updatedAt = updatedAt;
	 }
	 @Override
	 public String toString() {
		return "Supplier [supplierId=" + supplierId + ", name=" + name + ", type=" + type + ", status=" + status
				+ ", location=" + location + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	 }
	 
}
