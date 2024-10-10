package ma.marjane.pointage.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AglMatchId implements Serializable {
    private String client;
    private Long voucherNo;
    private Long sequenceNo;
    private String type;

    // Getters, setters, equals, and hashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AglMatchId that = (AglMatchId) o;
        return Objects.equals(client, that.client) &&
                Objects.equals(voucherNo, that.voucherNo) &&
                Objects.equals(sequenceNo, that.sequenceNo) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, voucherNo, sequenceNo, type);
    }
}
