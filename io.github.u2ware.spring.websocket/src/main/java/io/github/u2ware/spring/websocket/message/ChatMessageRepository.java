package io.github.u2ware.spring.websocket.message;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ChatMessageRepository extends PagingAndSortingRepository<ChatMessage, UUID>, JpaSpecificationExecutor<ChatMessage>{

	ChatMessage findOneByRoomAndLastIsTrue(@Param("room") String room);

}
