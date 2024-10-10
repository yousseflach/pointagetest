package ma.marjane.pointage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "aglmatch", schema = "agresso")
@IdClass(AglMatchId.class)
@Data
public class AglMatch {

    @Id
    @Column(name = "CLIENT")
    private String client;

    @Id
    @Column(name = "VOUCHER_NO")
    private Long voucherNo;

    @Id
    @Column(name = "SEQUENCE_NO")
    private Long sequenceNo;

    @Id
    @Column(name = "TYPE")
    private String type;

    @Column(name = "SEQUENCE_REF")
    private Long sequenceRef;

    @Column(name = "VOUCHER_REF")
    private Long voucherRef;

    @Column(name = "LAST_UPDATE")
    private LocalDateTime lastUpdate;

    @Column(name = "VOUCHER_DATE")
    private LocalDateTime voucherDate;

    @Column(name = "REST_AMOUNT")
    private BigDecimal restAmount;

    @Column(name = "VOUCHER_TYPE")
    private String voucherType;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "VOU_REF_TYPE")
    private String vouRefType;

    // Getters et setters
}
