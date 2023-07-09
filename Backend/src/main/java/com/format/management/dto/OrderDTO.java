package com.format.management.dto;

import java.sql.Timestamp;

import com.format.management.entity.OrderEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
	private String orderid;
	private String ordername;
	private int totalprice;
	private Timestamp orderdate;
	
	public OrderEntity toEntity(final OrderDTO dto) {
		return OrderEntity.builder()
					.orderid(dto.getOrdername())
					.ordernamelist(dto.getOrdername())
					.totalprice(dto.getTotalprice())
					.orderdate(dto.getOrderdate())
					.build();
	}
	
	public OrderDTO toDTO(final OrderEntity entity) {
			return OrderDTO.builder()
					.orderid(entity.getOrderid())
					.ordername(entity.getOrdernamelist())
					.totalprice(entity.getTotalprice())
					.orderdate(entity.getOrderdate())
					.build();
	}
}
