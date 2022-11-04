package com.cognixia.jump.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Manga;
import com.cognixia.jump.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	public Optional<User> findByUsername(String username);
	
	@Query("Select u.mangas from User u where u.username = ?1")
	public Set<Manga> findMangasByUsername(String username);
}
