package com.newyorktaxi.repository;

import com.newyorktaxi.entity.FailureMessage;
import com.newyorktaxi.entity.StatusEnum;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface FailureMessageRepository extends CrudRepository<FailureMessage, UUID> {

    List<FailureMessage> findAllByStatus(StatusEnum status);
}
