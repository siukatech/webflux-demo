package com.siukatech.webfluxdemo.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "receipt_submission")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReceiptSubmission {

    @Getter
    public enum ReceiptSubmissionStatus {
        PENDING(0),
        APPROVED(1),
        REJECTED(2)
        ;
        private Integer value;
        private ReceiptSubmissionStatus(Integer value){
            this.value = value;
        }
    }

    @Getter
    public enum PaymentMethod {
        CREDIT_CARD(0),
        MOBILE_PAYMENT(1),
        OTHER_PAYMENT(2)
        ;
        private Integer value;
        private PaymentMethod(Integer value){
            this.value = value;
        }
    }


    @Id
    private Long id;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "last_update_time")
    private LocalDateTime lastUpdateTime;

    @Column(name = "status")
    private ReceiptSubmissionStatus status;

    @Column(name = "member_id")
    private String memberId;

    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "mall_id")
    private Long mallId;

    @Column(name = "merchant_id")
    private Long merchantId;

    @Column(name = "receipt_date")
    private Long receiptDate;

    @Column(name = "receipt_amount")
    private BigDecimal receiptAmount;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "reject_reason", columnDefinition = "TEXT")
    private String rejectReason;

    @OneToMany
    private List<ReceiptImage> receiptImages;

    @OneToMany
    private List<ReceiptImage> payslipImages;

}
