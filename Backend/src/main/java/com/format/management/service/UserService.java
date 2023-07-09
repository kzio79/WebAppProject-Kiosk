package com.format.management.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.format.management.entity.UserEntity;
import com.format.management.persistence.UserRepository;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public UserEntity getByCredentials(final String id, String pw) {
		return userRepository.findByIdAndPw(id, pw);
	}
	
	public boolean checkId(final String id) {
		return userRepository.existsByid(id);
	}
	
	
}
