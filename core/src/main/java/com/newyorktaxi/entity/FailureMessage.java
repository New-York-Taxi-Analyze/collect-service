package com.newyorktaxi.entity;

import io.r2dbc.postgresql.codec.Json;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "failure_message")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FailureMessage {

    @Id
    @Column("key")
    UUID key;

    @Column("topic")
    String topic;

    @Column("message")
    Json message;

    @Column("status")
    StatusEnum status;
}
