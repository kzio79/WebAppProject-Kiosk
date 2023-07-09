package com.format.management.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.format.management.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {
	
	OrderEntity findByOrderid(String orderid);
	List<OrderEntity> findAll();
	
	
	//선택한 달의 토탈 매출
	@Query("SELECT SUM(o.totalprice) AS monthtotal FROM orderlist o WHERE YEAR(o.orderdate) = YEAR(CURRENT_DATE) AND MONTH(o.orderdate) = :month")
	int getSelMonthTotal(@Param("month") int month);
	
	
	//현재 달의 토탈 매출
	@Query("SELECT SUM(o.totalprice) AS monthtotal FROM orderlist o WHERE YEAR(o.orderdate) = YEAR(CURRENT_DATE) AND MONTH(o.orderdate) = MONTH(CURRENT_DATE)")
	int getMonthTotal();
	
	//현재 달 주문 건수
	@Query("SELECT count(o.orderid) FROM orderlist o WHERE YEAR(o.orderdate) = YEAR(CURRENT_DATE) AND MONTH(o.orderdate) = MONTH(CURRENT_DATE)")
	int findCurrentMonthCount();
}
