package com.format.management.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "orderlist")
public class OrderEntity {
	
	@Id
	private String orderid;
	
	private String ordernamelist;
	
	private int totalprice;
	
	private Timestamp orderdate;
}
