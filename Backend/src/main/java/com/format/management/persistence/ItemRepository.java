package com.format.management.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.format.management.entity.ItemEntity;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, String> {
	

	ItemEntity findByItemcode(String itemcode);
	ItemEntity findByItemname(String itemname);	
	List<ItemEntity> findAll();
	ItemEntity findByItemstate(boolean itemstate);
	
	List<ItemEntity> findByItemcodeStartingWith(String itemcode);
	
//	Page<ItemEntity> findAll(Pageable pageable);
	
}
