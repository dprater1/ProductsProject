package com.cognixia.jump.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognixia.jump.exception.InvalidMangaException;
import com.cognixia.jump.model.Manga;
import com.cognixia.jump.repository.MangaRepository;

@Service
public class MangaService {

	@Autowired
	MangaRepository mangaRepo;
	
	public List<Manga> getAllMangas(){
		
		return mangaRepo.findAll();
		
	}
	
	public boolean createManga(Manga manga) {
		
		if(manga != null) {
			mangaRepo.save(manga);
			return true;
		}
		
		
		return false;
	}
	
	public boolean updateManga(Manga manga) throws InvalidMangaException{
		
		if(mangaRepo.existsById(manga.getId())) {
			mangaRepo.save(manga);
			return true;
		}
		else{
			throw new InvalidMangaException("The manga you have attempted to update does not exist.");
		}
	}
	
	public boolean deleteManga(Long id) throws InvalidMangaException{
		
		Optional<Manga> found = mangaRepo.findById(id);
		
		if(found.isEmpty()) {
			throw new InvalidMangaException("Manga with id: " + id + " was not found");
		}
		mangaRepo.deleteById(id);
		return true;
		
	}
	
	
}
