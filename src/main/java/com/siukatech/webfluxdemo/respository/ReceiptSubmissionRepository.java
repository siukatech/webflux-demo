package com.siukatech.webfluxdemo.respository;

import com.siukatech.webfluxdemo.entity.ReceiptSubmission;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ReceiptSubmissionRepository extends ReactiveCrudRepository<ReceiptSubmission, Long> {
}
