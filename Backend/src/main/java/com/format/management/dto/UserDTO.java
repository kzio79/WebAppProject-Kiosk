package com.format.management.dto;

import com.format.management.entity.ItemEntity;
import com.format.management.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private String userid;
	private String userpw;
	private String token;
	
	public UserEntity toEntity(final ItemDTO dto) {
		return UserEntity.builder()
				.id(userid)
				.pw(userpw)
				.build();
					
	}
	
	public UserDTO toDTO(final UserEntity entity) {
		return UserDTO.builder()
				.userid(entity.getId())
				.userpw(entity.getPw())
				.build();
	}
}
