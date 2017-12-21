package com.sios.bbs.rest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sios.bbs.rest.dto.MessageDto;
import com.sios.bbs.rest.entity.MessageEntity;
import com.sios.bbs.rest.repository.MessageRepository;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	MessageRepository messageRepository;
	
	@Override
	public MessageDto[] findAll() {

		Iterable<MessageEntity> list = messageRepository.findAll();

		List<MessageDto> messageDtoList = new ArrayList<MessageDto>();

		for (MessageEntity messageEntity : list) {

			MessageDto messageDto = new MessageDto();
			messageDto.setId(messageEntity.getId());
			messageDto.setTitle(messageEntity.getTitle());
			messageDto.setComment(messageEntity.getComment());
			messageDto.setAuthor(messageEntity.getAuthor());

			messageDtoList.add(messageDto);

		}

		return messageDtoList.toArray(new MessageDto[messageDtoList.size()]);
	}

	@Override
	public MessageDto findById(Long id) {
		MessageEntity messageEntity = messageRepository.findOne(id);

		MessageDto messageDto = new MessageDto();
		messageDto.setId(messageEntity.getId());
		messageDto.setTitle(messageEntity.getTitle());
		messageDto.setComment(messageEntity.getComment());
		messageDto.setAuthor(messageEntity.getAuthor());

		return messageDto;
	}

	@Override
	public void save(MessageDto dto) {

		MessageEntity entity = new MessageEntity();

		if (dto.getId() != null)
			entity.setId(dto.getId());
		entity.setTitle(dto.getTitle());
		entity.setComment(dto.getComment());
		entity.setAuthor(dto.getAuthor());

		messageRepository.saveAndFlush(entity);

	}

	@Override
	public void delete(Long id) {

		messageRepository.delete(id);

	}

}
