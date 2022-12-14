package com.cognixia.jump.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.exception.InvalidMangaException;
import com.cognixia.jump.model.Manga;
import com.cognixia.jump.repository.MangaRepository;
import com.cognixia.jump.service.MangaService;

@RestController
@RequestMapping("/manga")
public class MangaController {

	@Autowired
	MangaService mangaService;
	
	@Autowired
	MangaRepository mangaRepo;
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllMangas(){
		return new ResponseEntity<>(mangaService.getAllMangas(), HttpStatus.OK);
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addManga(@RequestBody Manga manga){
		
		if (mangaService.createManga(manga)) {
			return new ResponseEntity<>("Created manga: " + manga, HttpStatus.CREATED);
		}
		return new ResponseEntity<>("Failed to create manga: " + manga, HttpStatus.NOT_ACCEPTABLE);
		
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateManga(@RequestBody Manga manga) throws InvalidMangaException{
		if (mangaService.updateManga(manga)) {
			return new ResponseEntity<>("Updated manga: " + manga, HttpStatus.CREATED);
		}
		return new ResponseEntity<>("Failed to update manga: " + manga, HttpStatus.NOT_ACCEPTABLE);
			
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteManga(@PathVariable Long id) throws InvalidMangaException{
		
		if(mangaService.deleteManga(id)) {
			return new ResponseEntity<>("Deleted manga from list ", HttpStatus.OK);
		}
		
		return new ResponseEntity<>("Failed to delete manga. ", HttpStatus.NOT_ACCEPTABLE);
	}
	
	
	
	@GetMapping("/price13")
	public ResponseEntity<?> findMangaByPriceOver13(){
		return new ResponseEntity<>(mangaRepo.findMangasByPriceOver13(), HttpStatus.OK);
	}
	
	@GetMapping("/outofstock")
	public ResponseEntity<?> findMangaOutOfStock(){
		return new ResponseEntity<>(mangaRepo.findMangasOutOfStock(), HttpStatus.OK);
	}
	
	@GetMapping("/instock")
	public ResponseEntity<?> findMangaInStock(){
		return new ResponseEntity<>(mangaRepo.findMangasInStock(), HttpStatus.OK);
	}
	
	@GetMapping("/desc")
	public ResponseEntity<?> findMangaByAlphabeticalOrderDesc(){
		return new ResponseEntity<>(mangaRepo.findMangasByAlphabeticalOrderDesc(), HttpStatus.OK);
	}
	
	@GetMapping("/asc")
	public ResponseEntity<?> findMangaByAlphabeticalOrderAsc(){
		return new ResponseEntity<>(mangaRepo.findMangasByAlphabeticalOrderAsc(), HttpStatus.OK);
	}
	
	
	
}
