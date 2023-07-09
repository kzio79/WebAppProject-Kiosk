package com.format.management.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.format.management.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
	UserEntity findByid(String id);
	Boolean existsByid(String id);
	UserEntity findByIdAndPw(String id, String pw);
}
