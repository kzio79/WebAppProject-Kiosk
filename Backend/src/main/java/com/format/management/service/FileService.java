package com.format.management.service;


import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;


import com.format.management.dto.FileDTO;
import com.format.management.entity.ImageEntity;
import com.format.management.persistence.ImageRepository;

import lombok.RequiredArgsConstructor;


@Service
public class FileService {
	
	@Autowired
	private ImageRepository imageRepository;
	
	
	@Value("${spring.servlet.multipart.location}")
	private String filepath;
	
	
	
	//파일 업로드
	public FileDTO saveImage(MultipartFile file)throws IOException{
		String origFilename = file.getOriginalFilename();
		String filename = UUID.randomUUID().toString() +"_"+origFilename;
		
		filepath = this.filepath + filename;
		
		File saveFile = Paths.get(filepath).toFile();
		System.out.println(saveFile.getName());
		file.transferTo(saveFile);
		
		FileDTO fileDTO = FileDTO.builder()
						.origfilename(origFilename)
						.filename(filename)
						.filepath(filepath)
						.build();
		
		imageRepository.save(fileDTO.toEntity(fileDTO));
		
		return fileDTO;
	}
	
	

	
}
