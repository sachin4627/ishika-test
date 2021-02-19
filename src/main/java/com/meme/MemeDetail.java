package com.meme;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;  

@Entity
@Table(name = "memes")  
public class MemeDetail {
	
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="pk_meme_id", unique = true, nullable = false)
	private Integer id;
	@Column(name = "caption")
	private String caption;
	@Column(name = "meme_owner")
	private String name;
	@Column(name = "meme_url")
	private String url;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	

}
