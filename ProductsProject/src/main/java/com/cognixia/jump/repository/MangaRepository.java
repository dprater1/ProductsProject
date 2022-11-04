package com.cognixia.jump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Manga;

@Repository
public interface MangaRepository extends JpaRepository<Manga, Long>{

	
	
}
