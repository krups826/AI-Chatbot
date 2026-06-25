package com.chatbot.Repository;

import com.chatbot.Entity.ConversionSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConversionSummaryRepository extends JpaRepository<ConversionSummary,Long> {

    ConversionSummary save(ConversionSummary conversionSummary);

    Optional<ConversionSummary> findById(Long aLong);

    ConversionSummary findTopByUserIdOrderByCreatedAtDesc(long userId);

    ;
}
