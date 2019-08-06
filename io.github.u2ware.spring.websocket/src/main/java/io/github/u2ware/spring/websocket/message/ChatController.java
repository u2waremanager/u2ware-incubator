package io.github.u2ware.spring.websocket.message;


import java.util.Set;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.PredicateBuilder;
import org.springframework.data.rest.webmvc.AbstractRestController;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.rest.webmvc.support.DefaultedPageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@BasePathAwareController
public class ChatController extends AbstractRestController<ChatMessage, UUID>{

    private final Log logger = LogFactory.getLog(getClass());
	
	@Autowired 
	private ChatMessageRepository chatMessageRepository;
	
	@RequestMapping(value = "/chat", method = RequestMethod.OPTIONS)
	public ResponseEntity<?> optionsForCollectionResource() {
		return super.optionsForAllResource();
	}
	@RequestMapping(value = "/chat", method = RequestMethod.HEAD)
	public ResponseEntity<?> headForCollectionResource() throws HttpRequestMethodNotSupportedException {
		return super.headForAllResource();
	}
	
	@RequestMapping(value = "/chat", method = RequestMethod.POST)
	public ResponseEntity<?> postCollectionResource(@RequestBody ChatMessage chatMessage, PersistentEntityResourceAssembler assembler) throws Exception {
		if(chatMessage.getSender() == null) return ResponseEntity.notFound().build();

    	Object result = openAndCreate(null, chatMessage);
    	return super.getItemResource(result, assembler);
	}


	@RequestMapping(value = "/chat/{room}", method = RequestMethod.PUT)
	public ResponseEntity<?> putItemResource(@RequestBody ChatMessage chatMessage, @PathVariable String room, PersistentEntityResourceAssembler assembler) throws HttpRequestMethodNotSupportedException {

		
		String contentType = chatMessage.getContentType();
		chatMessage.setRoom(room);
		
		ChatMessage system = chatMessageRepository.findOneByRoomAndLastIsTrue(room.toString());
		
		logger.info(contentType+": "+system);
		logger.info(contentType+": "+chatMessage);

		Object result = null;
		if("JOIN".equalsIgnoreCase(contentType)) {
			result = joinAndCreate(system, chatMessage);
		}else if("INVITE".equalsIgnoreCase(contentType)) {
			result = inviteAndCreate(system, chatMessage);
		}else if("DISCONNECT".equalsIgnoreCase(contentType)) {
			result = disconnectAndCreate(system, chatMessage);
		}else if("LEAVE".equalsIgnoreCase(contentType)) {
			result = leaveAndCreate(system, chatMessage);
		}else if("READ".equalsIgnoreCase(contentType)) {
			result = readAndUpdate(system, chatMessage);
		} else {
			result = create(system, chatMessage);
		}
    	return super.getItemResource(result, assembler);
	}
	
	@RequestMapping(value = "/chat/{room}", method = RequestMethod.GET)
	public ResponseEntity<?> getCollectionResource(@RequestParam MultiValueMap<String, Object> parameters, @PathVariable String room, DefaultedPageable pageable, PersistentEntityResourceAssembler assembler) throws ResourceNotFoundException, HttpRequestMethodNotSupportedException {
		
		ChatMessage s = chatMessageRepository.findOneByRoomAndLastIsTrue(room.toString());
		if(s == null) throw new ResourceNotFoundException();
		
		@SuppressWarnings("serial")
		Iterable<ChatMessage> source = chatMessageRepository.findAll(new Specification<ChatMessage>() {
			@Override
			public Predicate toPredicate(Root<ChatMessage> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				return new PredicateBuilder<>(root, query, builder)
						.and().eq("room", room.toString())
						.build();
			}
		}, pageable.getPageable());
		
		return ResponseEntity.ok(super.getCollectionResource(source, assembler, pageable));
	}
	
	
	@RequestMapping(value = "/chat", method = RequestMethod.GET)
	public ResponseEntity<?> getCollectionResource(@RequestParam String user, DefaultedPageable pageable, PersistentEntityResourceAssembler assembler) throws ResourceNotFoundException, HttpRequestMethodNotSupportedException {

		@SuppressWarnings("serial")
		Iterable<ChatMessage> source = chatMessageRepository.findAll(new Specification<ChatMessage>() {
			@Override
			public Predicate toPredicate(Root<ChatMessage> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				return new PredicateBuilder<>(root, query, builder)
						.and().eq("last", true)
						.andStart()
							.in("invitedMembers", user)
							.or()
							.in("joinedMembers", user)
						.andEnd()
						.build();
			}
		}, pageable.getPageable());
		
		return ResponseEntity.ok(super.getCollectionResource(source, assembler, pageable));
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////
	//
	///////////////////////////////////////////////////////////////////////////
	private ChatMessage openAndCreate(ChatMessage system, ChatMessage message){
		
		//system is null.
		Set<String> invitedMembers = StringUtils.commaDelimitedListToSet(message.getContent());
		invitedMembers.add(message.getSender());
		Set<String> joinedMembers = StringUtils.commaDelimitedListToSet(null);
		Set<String> connectedMembers = StringUtils.commaDelimitedListToSet(null);
		Set<String> readMembers = StringUtils.commaDelimitedListToSet(null);

		//
		ChatMessage m = new ChatMessage();
		m.setId(message.getId() != null ? message.getId() : UUID.randomUUID());
		m.setRoom(UUID.randomUUID().toString());
		m.setSender(message.getSender());
		m.setContentType("OPEN");
		m.setContent(message.getContent());
		m.setTimestamp(System.currentTimeMillis());
		m.setLast(true);
		m.setInvitedMembers(invitedMembers);
		m.setJoinedMembers(joinedMembers);
		m.setConnectedMembers(connectedMembers);
		m.setReadMembers(readMembers);

    	return chatMessageRepository.save(m);
	}
	
	private ChatMessage joinAndCreate(ChatMessage system, ChatMessage message){
		
		String room = null;
		Set<String> invitedMembers = null;
		Set<String> joinedMembers = null;
		Set<String> connectedMembers = null;
		Set<String> readMembers = StringUtils.commaDelimitedListToSet(null);
		
		if(system != null) {
			
			if( ! containsInvitedMembers(system, message)) {
				if( ! containsJoinedMembers(system, message)) {
					logger.info("invitedMembers or joinedMembers not contains error: ");
					throw new ResourceNotFoundException();
				}
			}
			
			system.setLast(false);
			system = chatMessageRepository.save(system);
			
	    	room = system.getRoom();
			invitedMembers = system.getInvitedMembers();
			invitedMembers.remove(message.getSender());
			joinedMembers = system.getJoinedMembers();
			joinedMembers.add(message.getSender());
			connectedMembers = system.getJoinedMembers();
			connectedMembers.add(message.getSender());
		}else {
			room = message.getRoom();
			invitedMembers = StringUtils.commaDelimitedListToSet("*");
			joinedMembers = StringUtils.commaDelimitedListToSet(null);
			joinedMembers.add(message.getSender());
			connectedMembers = StringUtils.commaDelimitedListToSet(null);
			connectedMembers.add(message.getSender());
		}
		
		ChatMessage m = new ChatMessage();
		m.setId(message.getId() != null ? message.getId() : UUID.randomUUID());
		m.setRoom(room);
		m.setSender(message.getSender());
		m.setContentType(message.getContentType());
		m.setContent(message.getContent());
		m.setTimestamp(System.currentTimeMillis());
		m.setLast(true);
		m.setInvitedMembers(invitedMembers);
		m.setJoinedMembers(joinedMembers);
		m.setConnectedMembers(connectedMembers);
		m.setReadMembers(readMembers);

    	return chatMessageRepository.save(m);
	}
	
	private ChatMessage inviteAndCreate(ChatMessage system, ChatMessage message){
		
		if(system == null) throw new ResourceNotFoundException();
		
		if( ! containsJoinedMembers(system, message)) {
			logger.info("joinedMembers not contains error: ");
			throw new ResourceNotFoundException();
		}
		
		///////////////////////////////////////////////////////////
		system.setLast(false);
		system = chatMessageRepository.save(system);

		String room = system.getRoom();
    	Set<String> invitedMembers = system.getInvitedMembers();
		Set<String> joinedMembers = system.getJoinedMembers();
		Set<String> connectedMembers = system.getConnectedMembers();
		Set<String> readMembers = StringUtils.commaDelimitedListToSet(null);

		invitedMembers.addAll(StringUtils.commaDelimitedListToSet(message.getContent()));
		
		///////////////////////////////////////////////////////////
		ChatMessage m = new ChatMessage();
		m.setId(message.getId() != null ? message.getId() : UUID.randomUUID());
		m.setRoom(room);
		m.setSender(message.getSender());
		m.setContentType(message.getContentType());
		m.setContent(message.getContent());
		m.setTimestamp(System.currentTimeMillis());
		m.setLast(true);
		m.setInvitedMembers(invitedMembers);
		m.setJoinedMembers(joinedMembers);
		m.setConnectedMembers(connectedMembers);
		m.setReadMembers(readMembers);
		
    	return chatMessageRepository.save(m);
	}
	
	private ChatMessage disconnectAndCreate(ChatMessage system, ChatMessage message) {
		if(system == null) throw new ResourceNotFoundException();
		
		if( ! containsJoinedMembers(system, message)) {
			logger.info("joinedMembers not contains error: ");
			throw new ResourceNotFoundException();
		}
		
		///////////////////////////////////////////////////////////
		system.setLast(false);
		system = chatMessageRepository.save(system);

		String room = system.getRoom();
    	Set<String> invitedMembers = system.getInvitedMembers();
		Set<String> joinedMembers = system.getJoinedMembers();
		Set<String> connectedMembers = system.getConnectedMembers();
		Set<String> readMembers = StringUtils.commaDelimitedListToSet(null);

		connectedMembers.remove(message.getSender());
		
		///////////////////////////////////////////////////////////
		ChatMessage m = new ChatMessage();
		m.setId(message.getId() != null ? message.getId() : UUID.randomUUID());
		m.setRoom(room);
		m.setSender(message.getSender());
		m.setContentType(message.getContentType());
		m.setContent(message.getContent());
		m.setTimestamp(System.currentTimeMillis());
		m.setLast(true);
		m.setInvitedMembers(invitedMembers);
		m.setJoinedMembers(joinedMembers);
		m.setConnectedMembers(connectedMembers);
		m.setReadMembers(readMembers);
		
    	return chatMessageRepository.save(m);
	}
	
	
	
	private ChatMessage leaveAndCreate(ChatMessage system, ChatMessage message){
		
		if(system == null) throw new ResourceNotFoundException();
		
		if( ! containsJoinedMembers(system, message)) {
			logger.info("joinedMembers not contains error: ");
			throw new ResourceNotFoundException();
		}
		
		///////////////////////////////////////////////////////////
		system.setLast(false);
		system = chatMessageRepository.save(system);

		String room = system.getRoom();
    	Set<String> invitedMembers = system.getInvitedMembers();
    	Set<String> joinedMembers = system.getJoinedMembers();
		Set<String> connectedMembers = system.getConnectedMembers();
		Set<String> readMembers = StringUtils.commaDelimitedListToSet(null);

		joinedMembers.remove(message.getSender());
		connectedMembers.remove(message.getSender());
		
		///////////////////////////////////////////////////////////
		ChatMessage m = new ChatMessage();
		m.setId(message.getId() != null ? message.getId() : UUID.randomUUID());
		m.setRoom(room);
		m.setSender(message.getSender());
		m.setContentType(message.getContentType());
		m.setContent(message.getContent());
		m.setTimestamp(System.currentTimeMillis());
		m.setLast(true);
		m.setInvitedMembers(invitedMembers);
		m.setJoinedMembers(joinedMembers);
		m.setConnectedMembers(connectedMembers);
		m.setReadMembers(readMembers);
		
    	return chatMessageRepository.save(m);
	}
	
	private ChatMessage readAndUpdate(ChatMessage system, ChatMessage message){
		
		if(system == null) throw new ResourceNotFoundException();
		
		Set<String> joinedMembers = system.getJoinedMembers();
		if(! CollectionUtils.contains(joinedMembers.iterator(), message.getSender())) {
			logger.info("joinedMembers not contains error: "+joinedMembers);
			throw new ResourceNotFoundException();
		}

		UUID id = UUID.fromString(message.getContent());
		
		ChatMessage m = chatMessageRepository.findById(id).get();
		if(m == null) throw new ResourceNotFoundException();
		
		Set<String> readMembers = m.getReadMembers();
		readMembers.add(message.getSender());
		
		m.setReadMembers(readMembers);
    	return chatMessageRepository.save(m);
	}
	
	private ChatMessage create(ChatMessage system, ChatMessage message){
		
		if(system == null) throw new ResourceNotFoundException();
		
		///////////////////////////////////////////////////////////
		system.setLast(false);
		system = chatMessageRepository.save(system);
		
		String room = system.getRoom();
		Set<String> invitedMembers = system.getInvitedMembers();
    	Set<String> joinedMembers = system.getJoinedMembers();
    	Set<String> connectedMembers = system.getConnectedMembers();
		Set<String> readMembers = StringUtils.commaDelimitedListToSet(null);
		
		///////////////////////////////////////////////////////////
		ChatMessage m = new ChatMessage();
		m.setId(message.getId() != null ? message.getId() : UUID.randomUUID());
		m.setRoom(room);
		m.setSender(message.getSender());
		m.setContentType(message.getContentType());
		m.setContent(message.getContent());
		m.setInvitedMembers(invitedMembers);
		m.setJoinedMembers(joinedMembers);
		m.setReadMembers(readMembers);
		m.setConnectedMembers(connectedMembers);
		m.setTimestamp(System.currentTimeMillis());
		m.setLast(true);
    	
    	return chatMessageRepository.save(m);
	}
	
	
	private boolean containsJoinedMembers(ChatMessage system, ChatMessage message) {
		Set<String> joinedMembers = system.getJoinedMembers();
		return CollectionUtils.contains(joinedMembers.iterator(), message.getSender());
	}
	private boolean containsInvitedMembers(ChatMessage system, ChatMessage message) {
    	Set<String> invitedMembers = system.getInvitedMembers();
    	if(CollectionUtils.contains(invitedMembers.iterator(), "*")) {
    		return true;
    	}else {
    		return CollectionUtils.contains(invitedMembers.iterator(), message.getSender());
    	}
	}
	
	
}
