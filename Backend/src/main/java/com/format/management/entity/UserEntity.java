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
@Entity(name = "manageruser")
public class UserEntity {
	
	@Id
	@Column(nullable = false)
	private String id;
	
	@Column(nullable = false)
	private String pw;

	
}
