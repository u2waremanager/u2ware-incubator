package com.example.demo.userEvent;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserEventRepository  extends PagingAndSortingRepository<UserEvent, Long> {

}
