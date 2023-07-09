package com.format.management.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.format.management.dto.OrderDTO;
import com.format.management.entity.OrderEntity;
import com.format.management.persistence.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	 //주문목록 조회
	public List<OrderDTO> togetList(){
		List<OrderEntity> entityList = orderRepository.findAll();
		List<OrderDTO> dto = entityList.stream()
							.map(entity -> OrderDTO.builder()
									.orderid(entity.getOrderid())
									.ordername(entity.getOrderid())
									.totalprice(entity.getTotalprice())
									.orderdate(entity.getOrderdate())
									.build())
									.collect(Collectors.toList());
												
		return dto;
	}
	
	
	
	//주문 추가
	public boolean orderAdd(OrderDTO dto) {
		boolean result = false;
		try {
			String orderid = dto.getOrderid();
			if(orderid == null) {
				log.error("orderid가 존재하지 않습니다.");
				return false;
			}
			
			OrderEntity entity = orderRepository.findByOrderid(orderid);
			if(entity == null) {
				entity = OrderEntity.builder()
						.orderid(dto.getOrderid())
						.ordernamelist(dto.getOrdername())
						.totalprice(dto.getTotalprice())
						.orderdate(dto.getOrderdate())
						.build();
				orderRepository.save(entity);
				result = true;
			}else {
				log.error("이미 존재하는 orderid 입니다.");
				result = false;
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	//주문 삭제
	public void orderDelete(String orderid) {
		try {
			OrderEntity entity = orderRepository.findByOrderid(orderid);
			if(entity.getOrderid() == null) {
				log.error("삭제 실패");
			}else if(entity.getOrderid() != null) {
				orderRepository.delete(entity);
				log.info("삭제 성공");
			}
		} catch (Exception e) {
		
		}
	}
	
	
	//이번달매출
	public int getsaleMonth(){
		int result = orderRepository.getMonthTotal();
		return result;
	}
	
	//달 검색을 통한 매출
	public int getsaleSelMonth(int month) {
		int result = orderRepository.getSelMonthTotal(month);
		return result;
	}
	
	
	
	
	
	
}
