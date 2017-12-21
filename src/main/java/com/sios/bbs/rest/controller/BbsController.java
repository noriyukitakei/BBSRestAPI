package com.sios.bbs.rest.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.sios.bbs.rest.dto.AuthInfo;
import com.sios.bbs.rest.dto.AuthResult;
import com.sios.bbs.rest.dto.MessageDto;
import com.sios.bbs.rest.service.MessageService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class BbsController {

	@Autowired
	MessageService messageService;
	
	//@CrossOrigin
	@PostMapping(value = "/auth",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
			+ ";charset=utf-8")
	public ResponseEntity<AuthResult> auth(@RequestBody @Valid AuthInfo authInfo) {

		if (authInfo.getId().equals("user") && authInfo.getPassword().equals("password")) {
	        HttpStatus status = HttpStatus.OK;
	        AuthResult authResult = new AuthResult();

	        //7日後の日時
	        LocalDateTime now = LocalDateTime.now();
	        ZonedDateTime expirationDate = now.plusHours(8).atZone(ZoneId.systemDefault());
	        //ZonedDateTime expirationDate = now.plusSeconds(2).atZone(ZoneId.systemDefault());
	        
	        String compactJws = null;
	        try {
				 compactJws = Jwts.builder()
				        .setSubject("user")
				        .setExpiration(Date.from(expirationDate.toInstant()))
				        .signWith(SignatureAlgorithm.HS512, "secret".getBytes("UTF-8"))
				        .compact();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        authResult.setAccess_token(compactJws);
	        authResult.setExpires_in(8 * 3600);

	        return new ResponseEntity<AuthResult>(authResult,status);	
		} else {
	        HttpStatus status = HttpStatus.UNAUTHORIZED;
	        return new ResponseEntity<>(status);
		}
	}

	//@CrossOrigin
	@GetMapping(value = "/messages")
	public MessageDto[] index(HttpServletRequest req) {
		MessageDto[] messageDtos = messageService.findAll();


		return messageDtos;

	}

	//@CrossOrigin
	@GetMapping(value = "/messages/{id}")
	public MessageDto getMessage(@PathVariable Long id) {

		MessageDto messageDto = messageService.findById(id);


		return messageDto;

	}

	//@CrossOrigin
	@PostMapping(value = "/messages/new",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
			+ ";charset=utf-8")
	public ResponseEntity<String> add(@RequestBody @Valid MessageDto messageDto) {

		messageService.save(messageDto);
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(status);
	}

	//@CrossOrigin
	@PostMapping(value = "/messages/update",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
			+ ";charset=utf-8")
	public ResponseEntity<String> update(@RequestBody @Valid MessageDto messageDto) {

		messageService.save(messageDto);
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(status);
	}

	//@CrossOrigin
	@DeleteMapping(value = "/messages/{id}")
	public ResponseEntity<String> delete(@PathVariable long id) {

		messageService.delete(id);

        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(status);

	}

}
