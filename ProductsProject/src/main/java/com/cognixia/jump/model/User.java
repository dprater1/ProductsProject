package com.cognixia.jump.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.cognixia.jump.model.User.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class User implements Serializable{

	private static final long serialVersionUID = 1L;

	public static enum Role {
		ROLE_USER, ROLE_ADMIN
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // incrementation will use auto increment
	private Long id;
	
	@Column(unique = true, nullable = false)
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String email;
	
	//Will store role as a string in the database
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@Column(columnDefinition = "boolean default true")
	private boolean enabled;

	@JsonIgnoreProperties(value = {"qty"})
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(
			name = "orders",
			joinColumns = {@JoinColumn(name = "user_id")},
			inverseJoinColumns = {@JoinColumn(name = "manga_id")}
	)
	private Set<Manga> mangas = new HashSet<>();
	
	public User() {

	}
	
	public User(Long id, String username, String password, String email, Role role, boolean enabled) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
		this.enabled = enabled;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	

	public Set<Manga> getMangas() {
		return mangas;
	}

	public void setMangas(Set<Manga> mangas) {
		this.mangas = mangas;
	}
	
	public void addManga(Manga manga) {
		this.mangas.add(manga);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email) && enabled == other.enabled && Objects.equals(id, other.id)
				&& Objects.equals(mangas, other.mangas) && Objects.equals(password, other.password)
				&& role == other.role && Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email + ", role="
				+ role + ", enabled=" + enabled + "]";
	}
	
	
}
