package com.cognixia.jump.requestmodels;

public class MangaAndUserReqModel {

	public Long mangaId;
	public Long userId;
	
	public MangaAndUserReqModel(Long mangaId, Long userId) {
		super();
		this.mangaId = mangaId;
		this.userId = userId;
	}

	public Long getMangaId() {
		return mangaId;
	}

	public void setMangaId(Long mangaId) {
		this.mangaId = mangaId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "MangaAndUserReqModel [mangaId=" + mangaId + ", userId=" + userId + "]";
	}
	
	
	
}
