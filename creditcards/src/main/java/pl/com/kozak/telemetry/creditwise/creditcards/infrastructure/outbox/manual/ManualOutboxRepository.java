package pl.com.kozak.telemetry.creditwise.creditcards.infrastructure.outbox.manual;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

interface ManualOutboxRepository extends JpaRepository<OutboxMessage, Long> {
    Page<OutboxMessage> findAllByPublishedFalse(Pageable pageable);

}
