package com.newyorktaxi.entity;

import com.newyorktaxi.avro.model.TaxiMessage;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "failure_message")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FailureMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "key", nullable = false)
    UUID key;

    @Column(name = "topic", nullable = false)
    String topic;

    @Column(name = "message", nullable = false, columnDefinition = "jsonb")
    @Convert(converter = TaxiMessageConvertor.class)
    TaxiMessage message;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    StatusEnum status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FailureMessage that = (FailureMessage) o;
        return getKey() != null && Objects.equals(getKey(), that.getKey());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
