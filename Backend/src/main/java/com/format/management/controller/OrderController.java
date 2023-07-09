package com.format.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.format.management.dto.ItemDTO;
import com.format.management.dto.OrderDTO;
import com.format.management.dto.ResponseDTO;
import com.format.management.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	
	@GetMapping("/getlist")
	public ResponseEntity<?> togetList(){
			
		try {
			List<OrderDTO> dto = orderService.togetList();
			return ResponseEntity.ok().body(dto);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseDTO responseDTO = ResponseDTO.builder()
						.error("Load Fail")
						.build();
			return ResponseEntity.badRequest().body(responseDTO);		
		}	
	}
	
	
	//주문 아이템 추가
	@PostMapping("/additem")
	public ResponseEntity<?> addItem(@RequestBody OrderDTO dto) {
		
		try {
			boolean result = orderService.orderAdd(dto);
			return ResponseEntity.ok().body(result);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseDTO responseDTO = ResponseDTO.builder()
						.error("Add Fail")
						.build();
			
			return ResponseEntity.badRequest().body(responseDTO);
			
			
		}
	}
	
	//선택한달 매출
	@GetMapping("/selmonthtotal")
	public ResponseEntity<?> selectMonthTotalPrice(@RequestBody int month){
		try {
			int selectprice = orderService.getsaleSelMonth(month);
			return ResponseEntity.ok().body(selectprice);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseDTO responseDTO = ResponseDTO.builder()
						.error("Load Fail")
						.build();
			
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	
	
	//현재달 매출
	@GetMapping("monthtotal")
	public ResponseEntity<?> MonthTotalPirce(){
		try {
			int currentprice = orderService.getsaleMonth();
			return ResponseEntity.ok().body(currentprice);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseDTO responseDTO = ResponseDTO.builder()
						.error("Load Fail")
						.build();
			
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
		
}
