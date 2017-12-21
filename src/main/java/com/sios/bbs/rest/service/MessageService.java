package com.sios.bbs.rest.service;

import com.sios.bbs.rest.dto.MessageDto;

public interface MessageService {

	public MessageDto[] findAll();

	public MessageDto findById(Long id);

	public void save(MessageDto dto);

	public void delete(Long id);

}
