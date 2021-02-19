package com.meme;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@CrossOrigin(origins = "http://localhost:${swagger.port}")
public class MemeController {
	
	@Autowired  
	private MemeRepository memeRepository;
	
	//Get all the available memes from Database
	@GetMapping("/memes")
	@ApiOperation(value = "Get All the memes",notes = "Get list of all the available memes")
	public List<Object> getMeme() {
		return memeRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream().limit(100).collect(Collectors.toList());
	}
	
	//Save a new memes in Database
	@PostMapping(value = "/memes",
            consumes = MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Save a new meme",notes = "Save a meme by sending values in post call. Id is not mandatory.")
	public Map<String,String> saveMeme(@ApiParam(value = "Enter id<optional>,name of the owner,caption and url of the meme",example = "{name:name,caption:caption,url:url}"
	,required = true) @RequestBody MemeDetail memeDetail) {
		
		if(memeRepository.existsByCaption(memeDetail.getCaption())&&memeRepository.existsByName(memeDetail.getName())&&memeRepository.existsByUrl(memeDetail.getUrl())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Resource Exists");
		}else if(memeDetail.getCaption()!=null&&memeDetail.getName()!=null&&memeDetail.getUrl()!=null){
		
		memeRepository.save(memeDetail);
		Map<String,String> memeDetailMap = new HashMap<>();
		memeDetailMap.put("id", memeDetail.getId().toString());
        return memeDetailMap;
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Incomplete resource");
		}		
	}
	
	//Search for a particular meme based on id
	@GetMapping("/memes/{id}")  
	@ApiOperation(value = "Find memes by id",notes = "Provide an id to find a particular meme",
	response = MemeDetail.class)
	private MemeDetail getMemeById(@ApiParam(value = "Enter id of the meme you want to search",example = "1"
			,required = true) @PathVariable("id") int memeId)   
	{  
		Optional<MemeDetail> memeDetailOptional = memeRepository.findById(memeId);
		if(memeDetailOptional.isPresent()) {
			return memeDetailOptional.get();
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not found");
		}	
	}
	
	
	//Delete a meme whose id is given
	@GetMapping("/memes/delete/{id}")  
	@ApiOperation(value = "Delete memes by id",notes = "Provide an id to delete a particular meme",
	response = MemeDetail.class)
	private HttpStatus deleteMemeById(@ApiParam(value = "Enter id of the meme you want to delete",example = "1"
			,required = true) @PathVariable("id") int memeId)   
	{  
		Optional<MemeDetail> memeDetailOptional = memeRepository.findById(memeId);
		if(memeDetailOptional.isPresent()) {
			 memeRepository.deleteById(memeId);
			 return HttpStatus.OK;
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not found");
		}	
	}
	
	//Update a meme whose id is given.
	@PatchMapping(value = "/memes/{id}",
			consumes = MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update an existing meme",notes = "Update a meme which is already existing. User name cannot be edited.")
	private Integer updateMeme(@ApiParam(value = "Enter id which has to be updated,caption and url of the meme",example = "{caption:caption,url:url}"
			,required = true) @RequestBody MemeDetail memeDetail,@PathVariable("id") int memeId) throws JSONException {
		if(memeDetail.getName()!=null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot update user name");
		}
		
		if(memeRepository.existsByCaption(memeDetail.getCaption())&&memeRepository.existsByUrl(memeDetail.getUrl())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Resource Exists");
		}
		
		Optional<MemeDetail> memeDetailOptional = memeRepository.findById(memeId);
		if(memeDetailOptional.isPresent()) {
			MemeDetail memeDetailNew = memeDetailOptional.get();
			memeDetailNew.setCaption(memeDetail.getCaption());
			memeDetailNew.setUrl(memeDetail.getUrl());
			try {
				memeRepository.save(memeDetailNew);
				return 200;
			}catch(Exception e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not save");
			}
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not found");

		}
	}
	
}