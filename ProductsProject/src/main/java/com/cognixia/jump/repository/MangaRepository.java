package com.cognixia.jump.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Manga;

@Repository
public interface MangaRepository extends JpaRepository<Manga, Long>{

	@Query("select u from Manga u where u.price > 13")
	public List<Manga> findMangasByPriceOver13();
	
	@Query("select u from Manga u where u.qty = 0")
	public List<Manga> findMangasOutOfStock();
	
	@Query("select u from Manga u where u.qty > 0")
	public List<Manga> findMangasInStock();
	
	@Query("select u from Manga u order by u.name desc")
	public List<Manga> findMangasByAlphabeticalOrderDesc();
	
	@Query("select u from Manga u order by u.name asc")
	public List<Manga> findMangasByAlphabeticalOrderAsc();
	
	
	
}
