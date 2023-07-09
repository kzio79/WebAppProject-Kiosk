package com.format.management.dto;

import com.format.management.entity.ImageEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileDTO {
	private Long id;
	private String origfilename;
	private String filename;
	private String filepath;
	
	public ImageEntity toEntity(final FileDTO dto) {
		return ImageEntity.builder().
				 fid(dto.getId())
				.origfilename(dto.getOrigfilename())
				.filename(this.filename)
				.filepath(this.filepath)
				.build();
	}
	
	public FileDTO toDTO(final ImageEntity entity) {
		return FileDTO.builder()
				.id(entity.getFid())
				.origfilename(entity.getOrigfilename())
				.filename(entity.getFilename())
				.filepath(entity.getFilepath())
				.build();
				
	}
}
