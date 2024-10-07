package ma.marjane.pointage.entity;

import java.io.Serializable;
import java.util.Objects;

public class AglMatchId implements Serializable {
    private Long voucherNo;
    private Long sequenceRef;

    // Default constructor
    public AglMatchId() {}

    public AglMatchId(Long voucherNo, Long sequenceRef) {
        this.voucherNo = voucherNo;
        this.sequenceRef = sequenceRef;
    }

    // Getters and setters

    // hashCode and equals
    @Override
    public int hashCode() {
        return Objects.hash(voucherNo, sequenceRef);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AglMatchId that = (AglMatchId) obj;
        return Objects.equals(voucherNo, that.voucherNo) &&
                Objects.equals(sequenceRef, that.sequenceRef);
    }
}