package com.format.management.dto;

import com.format.management.entity.ItemEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
	
	private String code;
	private String name;
	private String image;
	private String content;
	private int price;
	private boolean state;
	
	public ItemEntity toEntity(final ItemDTO dto) {
		return ItemEntity.builder().
				itemcode(dto.getCode())
				.itemname(dto.getName())
				.itemcontent(dto.getContent())
				.itemimage(dto.getImage())
				.itemprice(dto.getPrice())
				.itemstate(dto.isState())
				.build();			
	}
	
	public ItemDTO toDTO(final ItemEntity entity) {
		return ItemDTO.builder()
				.code(entity.getItemcode())
				.name(entity.getItemname())
				.image(entity.getItemimage())
				.content(entity.getItemcontent())
				.price(entity.getItemprice())
				.state(entity.isItemstate())
				.build();
	}
	
	
}
