package com.format.management.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.format.management.dto.FileDTO;
import com.format.management.dto.ItemDTO;
import com.format.management.dto.ResponseDTO;
import com.format.management.dto.UserDTO;
import com.format.management.entity.ImageEntity;
import com.format.management.service.FileService;
import com.format.management.service.ItemService;

@RestController
@RequestMapping("/item")
public class ItemController {
	
	static String frontEnd = "http://localhost:8000/";
	RedirectView redirectview = new RedirectView();
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private FileService fileService;
	
	
	
	
	//주문 아이템 추가
	@PostMapping("/additem")
	public ResponseEntity<?> addItem(@RequestBody ItemDTO dto) {
		
		try {
			boolean result = itemService.itemAdd(dto);
			return ResponseEntity.ok().body(result);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseDTO responseDTO = ResponseDTO.builder()
						.error("Add Fail")
						.build();
			return ResponseEntity.badRequest().body(responseDTO);
			
		}
		
	}
	
	
	
	//리스트 얻기
	
	@GetMapping("/getlist")
	public ResponseEntity<?> togetList(){
			
		try {
			List<ItemDTO> dto = itemService.togetList();
			return ResponseEntity.ok().body(dto);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseDTO responseDTO = ResponseDTO.builder()
						.error("Load Fail")
						.build();
			return ResponseEntity.badRequest().body(responseDTO);
			
		}
		
		
	}
	
	
	//상세정보 조회
	@GetMapping("/getcontent")
	public ResponseEntity<?> getcontent(@RequestParam(value = "code", required = false)
	String code){
		try {
			ItemDTO dto = itemService.getContent(code);
			return ResponseEntity.ok().body(dto);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseDTO responseDTO = ResponseDTO.builder()
					.error("Load Fail")
						.build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	
	
	//아이템 수정
	@PutMapping("/updateitem")
	public ResponseEntity<?> itemUpdate(@RequestBody ItemDTO dto) {
		try {
		itemService.itemUpdate(dto);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			ResponseDTO responseDTO = ResponseDTO.builder()
								.error("Update Fail")
								.build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	
	
	//아이템 삭제하기
	@DeleteMapping("/deleteitem")
	public ResponseEntity<?> itemDelete(@RequestParam ("code") String code){
		
		try {
			itemService.itemDelete(code);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			ResponseDTO responseDTO = ResponseDTO.builder()
								.error("Delete Fail")
								.build();
			return ResponseEntity.badRequest().body(responseDTO);
	  }
	}

	
	
	
	//이미지 받아서 저장하기
	@PostMapping("/imgupload")
	public ResponseEntity<?> uploadImage(@RequestParam(value = "code") String code,@RequestPart MultipartFile file){
		System.out.println("이름 : " + file.getOriginalFilename() + file.getSize());
		try {
			FileDTO dto = fileService.saveImage(file);
			return ResponseEntity.ok().body(dto.getFilepath());
		} catch (Exception e) {
			e.printStackTrace();
			ResponseDTO responseDTO = ResponseDTO.builder()
					.error("Upload Fail")
					.build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	
	//aws에 이미지 저장하기
//	@PostMapping("imgupload2")
//	public ResponseEntity<?> uploadImaget(@RequestPart MultipartFile file){
//		 try {
//			 String url = fileService.uploadImage(file);
//			 System.out.println("주소 : " + url);
//			 return ResponseEntity.ok().body(url);
//		 } catch (Exception e) {
//			e.printStackTrace();
//			ResponseDTO responseDTO = ResponseDTO.builder()
//							.error("Upload Fail")
//							.build();
//			return ResponseEntity.badRequest().body(responseDTO);
//		}
//		 
//	}
	
}
