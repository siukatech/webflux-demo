package com.siukatech.webfluxdemo.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "receipt_image")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReceiptImage {

    @Getter
    public enum ReceiptImageType {
        RECEIPT(0),
        PAYSLIP(1);
        private Integer value;

        private ReceiptImageType(Integer value) {
            this.value = value;
        }
    }

    @Id
    private Long id;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "last_update_time")
    private LocalDateTime lastUpdateTime;

    @Column(name = "image_uri")
    private String imageUri;

    @Column(name = "image_type")
    private ReceiptImageType imageType;

    @ManyToOne
    @JoinColumn(name = "receipt_submission_id", insertable = false, updatable = false)
    private ReceiptSubmission receiptSubmission;

}
