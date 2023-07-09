package com.format.management.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "itemlist")
public class ItemEntity {
	
	@Id
	@Column(nullable = false)
	private String itemcode;
	
	@Column(nullable = false)
	private String itemname;
	
	private String itemcontent;
	private String itemimage;
	
	@Column(nullable = false)
	private int itemprice;
	
	private boolean itemstate;
	
	
}
