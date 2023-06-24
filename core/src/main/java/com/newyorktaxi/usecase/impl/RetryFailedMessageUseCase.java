package com.newyorktaxi.usecase.impl;

import com.newyorktaxi.avro.model.TaxiMessage;
import com.newyorktaxi.entity.FailureMessage;
import com.newyorktaxi.entity.StatusEnum;
import com.newyorktaxi.repository.FailureMessageRepository;
import com.newyorktaxi.service.KafkaProducer;
import com.newyorktaxi.usecase.FunctionalUseCase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RetryFailedMessageUseCase implements FunctionalUseCase<Void, Void> {

    FailureMessageRepository repository;
    KafkaProducer<String, TaxiMessage> kafkaProducer;

    @Override
    public Void execute(Void params) {
        final List<FailureMessage> allByStatus = repository.findAllByStatus(StatusEnum.RETRY);

        if (!allByStatus.isEmpty()) {
            log.info("Retrying {} messages", allByStatus.size());
        }

        allByStatus.forEach(failureMessage -> {
            kafkaProducer.send(failureMessage.getTopic(), failureMessage.getKey().toString(), failureMessage.getMessage());

            failureMessage.setStatus(StatusEnum.SUCCESS);
            repository.save(failureMessage);
            log.info("Message successfully retried: {}", failureMessage);
        });
        return null;
    }
}
