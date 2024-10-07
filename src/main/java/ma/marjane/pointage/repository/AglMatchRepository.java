package ma.marjane.pointage.repository;

import ma.marjane.pointage.entity.AglMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AglMatchRepository extends JpaRepository<AglMatch, Long> {
}
