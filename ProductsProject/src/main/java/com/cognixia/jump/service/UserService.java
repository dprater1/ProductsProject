package com.cognixia.jump.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cognixia.jump.exception.DuplicateMangaException;
import com.cognixia.jump.exception.InvalidMangaException;
import com.cognixia.jump.exception.InvalidUserException;
import com.cognixia.jump.exception.OutOfOrderException;
import com.cognixia.jump.model.Manga;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.MangaRepository;
import com.cognixia.jump.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Service
public class UserService {

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	MangaRepository mangaRepo;
	
	@Autowired
	PasswordEncoder encoder;
	

	public List<User> getAllUsers(){
		
		return userRepo.findAll();
	}
	
	public Set<Manga> getAllUserManga(String username){
		
		
		return userRepo.findMangasByUsername(username);
		
	}
	
	public boolean createUser(User user) {
		
		if(user != null) {
			user.setId(null);
			//each password for a new user will get encoded
			user.setPassword(encoder.encode(user.getPassword()));
			userRepo.save(user);
			return true;
		}
		
		return false;
	}
	
	public boolean deleteMangafromUser(Long mangaId, Long userId) throws InvalidMangaException{
		Optional<Manga> mangaAdd = mangaRepo.findById(mangaId);
		Optional<User> userAdd = userRepo.findById(userId);

		if (userAdd.isPresent() && mangaAdd.isPresent()) {
			// checks if manga added is already in the list
			if (userAdd.get().getMangas().contains(mangaAdd.get())) {
				userAdd.get().getMangas().remove(mangaAdd.get());
				userRepo.save(userAdd.get());
				return true;
			}

		} else {
			throw new InvalidMangaException("This manga is not in this person's list!");
		}
		return false;
	}
	
	public boolean addMangaToUser(Long mangaId, Long userId) throws OutOfOrderException, DuplicateMangaException{
		
		Optional<Manga> mangaAdd = mangaRepo.findById(mangaId);
		Optional<User> userAdd = userRepo.findById(userId);

		if (userAdd.isPresent() && mangaAdd.isPresent()) {
			// checks if manga added is already in the list
			if (userAdd.get().getMangas().contains(mangaAdd.get())) {
				throw new DuplicateMangaException("The manga " + mangaAdd.get().getName() + " is already in the list.");
			}

			mangaAdd.get().setQty(mangaAdd.get().getQty() - 1);

			// checks if item was sold out before hand
			if (mangaAdd.get().getQty() < 0) {
				mangaAdd.get().setQty(0);
				throw new OutOfOrderException("The Manga " + mangaAdd.get().getName() + " is sadly out of stock");
			
			} else {
				userAdd.get().addManga(mangaAdd.get());
				mangaAdd.get().addUser(userAdd.get());
				userRepo.save(userAdd.get());
				mangaRepo.save(mangaAdd.get());
			}
			return true;
		}

		return false;
	}
	
	public User byUsername(String username) {
		
		Optional<User> user = userRepo.findByUsername(username);
		User thisUser;
		
		if(user.get() != null) {
			thisUser = user.get();
			return thisUser;
		}
		
		return null;
	}
	
	public boolean updateUsername(User user, String username) throws InvalidUserException {
		Optional<User> changingUser = userRepo.findById(user.getId());
		User thisUser;
		if(changingUser == null){
			throw new InvalidUserException("This user was not found.");
		}
		
			thisUser = changingUser.get();
			thisUser.setUsername(username);
			
			
			
			userRepo.save(thisUser);
			return true;
		
	}
	
	public boolean updatePassword(User user ,String password) throws InvalidUserException {
		Optional<User> changingUser = userRepo.findByUsername(user.getUsername());
		Optional<User> changingUser2 = userRepo.findById(user.getId());
		User thisUser;
		if(changingUser == null) {
			throw new InvalidUserException("This name user does not exist");
		}
		//fix how the InvalidUserException works.
//		if(changingUser.get().getUsername() != changingUser2.get().getUsername()){
//			throw new InvalidUserException("the user " + user.getUsername() + " is incorrect.");
//		}
			thisUser = changingUser2.get();
			thisUser.setPassword(password);
			userRepo.save(thisUser);
			thisUser.setPassword(encoder.encode(thisUser.getPassword()));
			userRepo.save(thisUser);
			return true;
	}
	
	public boolean deleteUser(Long id) throws InvalidUserException {
		
		Optional<User> found = userRepo.findById(id);
		
		if(found.isEmpty()) {
			throw new InvalidUserException("User with id: " + id + " was not found");
		}
		userRepo.deleteById(id);
		return true;
		
	}
	
	public User loadUserById(Long id) {
		Optional<User> user = userRepo.findById(id);
		
		//exception will only throw if id is not found
		if(user.isEmpty()) {
			throw new UsernameNotFoundException("User with id: " + id + " not found");
		}
		
		return user.get();
	}
	
}
