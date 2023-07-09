package com.format.management.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.format.management.dto.ItemDTO;
import com.format.management.entity.ItemEntity;
import com.format.management.persistence.ItemRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ItemService {
	
	@Autowired
	private ItemRepository itemRepository;
	
	
	//현재 이미지 업로드만 안되고 있음.
	
	//item리스트 보내기
	public List<ItemDTO> togetList() throws Exception{
			
			List<ItemEntity> entityList = itemRepository.findAll(); 
			List<ItemDTO> dto = entityList.stream()
								.map(entity -> ItemDTO.builder()
												.code(entity.getItemcode())
												.name(entity.getItemname())
												.content(entity.getItemcontent())
												.image(entity.getItemimage())
												.price(entity.getItemprice())
												.state(entity.isItemstate()).build())
												.collect(Collectors.toList());
						
			return dto;
	}
	
	
	//품절 아이템 리스트(itemstate가 true인 것만 저장하기)
	public List<ItemDTO> solditemList() {
		
		try {
			List<ItemEntity> tentity = itemRepository.findAll();
			List<ItemEntity> soldentity = new ArrayList<ItemEntity>();
			for(ItemEntity temp : tentity) {
				if(temp.isItemstate() == true) {
						soldentity.add(temp);					
				}
			}
			
			List<ItemDTO> dto = soldentity.stream()
					.map(entity -> ItemDTO.builder()
									.code(entity.getItemcode())
									.name(entity.getItemname())
									.content(entity.getItemcontent())
									.image(entity.getItemimage())
									.price(entity.getItemprice())
									.state(entity.isItemstate()).build())
									.collect(Collectors.toList());
			
			return dto;
		
		} catch (Exception e) {
			e.printStackTrace();
			return null;	
		}
		
		
	}
	
	
	
	//item 추가
	public boolean itemAdd(ItemDTO dto) {
	    boolean result = false;
	    
	    dto.getCode().trim();
        dto.getName().trim();
        dto.getContent().trim();
        dto.getImage().trim();
	     
	    try {
	        String itemcode = dto.getCode();
	        if (itemcode == null) {
	            log.error("itemcode가 없습니다.");
	            return false;
	        }
	        
	        
	        
	        
	        ItemEntity entity = itemRepository.findByItemcode(itemcode);
	        if (entity == null) {
	            entity = ItemEntity.builder()
	                            .itemcode(itemcode)
	                            .itemname(dto.getName())
	                            .itemimage(dto.getImage())
	                            .itemcontent(dto.getContent())
	                            .itemprice(dto.getPrice())
	                            .itemstate(dto.isState())
	                            .build();
	            itemRepository.save(entity);
	            log.info("저장 성공");
	            result = true;
	        } else {
	            System.out.println("이미 존재하는 itemcode입니다.");
	            result = false;
	        }

	        return result;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	
	//컨텐츠 조회하기
	public ItemDTO getContent(String code) {
			try {				
				ItemEntity entity = itemRepository.findByItemcode(code);	
				System.out.println(entity.getItemcode());
				ItemDTO dto = ItemDTO.builder()
							.code(entity.getItemcode())
							.name(entity.getItemname())
							.image(entity.getItemimage())
							.content(entity.getItemcontent())
							.price(entity.getItemprice())
							.state(entity.isItemstate()).build();
					System.out.println(dto.getCode());			
			return dto;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	//itemcode번호로 아이템 삭제하기
	public void itemDelete(String code){
		try {
			ItemEntity entity = itemRepository.findByItemcode(code);
			if(entity.getItemcode() == null) {
				log.error("삭제 실패");
			}else if(entity.getItemcode() != null) {
				itemRepository.delete(entity);
				log.info("삭제 성공");
				}
			}catch (Exception e) {
				e.printStackTrace();
			}		
	}
	
	
	
	
	//아이템 수정하기
	public void itemUpdate(ItemDTO dto) {
									
									
				try {
					Optional<ItemEntity> ioptional = itemRepository.findById(dto.getCode());
					if(ioptional.isPresent()) {
						ItemEntity entity = ioptional.get();
						entity.setItemname(dto.getName());
						entity.setItemprice(dto.getPrice());
						if(dto.getImage() != null) {
							entity.setItemimage(dto.getImage());
						}
						if(dto.getContent() != null) {
							entity.setItemcontent(dto.getContent());
						}
						if(dto.isState()) {
							entity.setItemstate(dto.isState());
						}	
						itemRepository.save(entity);
						log.info("수정 성공");
					}
				} catch (Exception e) {
					 e.printStackTrace();
				}
		}
	
	
	
}
