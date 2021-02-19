package com.meme;

import org.springframework.data.jpa.repository.JpaRepository;
//Performs all CRUD operations
public interface MemeRepository extends JpaRepository<MemeDetail, Integer> {
	Boolean existsByName(String name); 
	Boolean existsByCaption(String caption); 
	Boolean existsByUrl(String url); 
}
