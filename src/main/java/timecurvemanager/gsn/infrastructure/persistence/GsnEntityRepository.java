package timecurvemanager.gsn.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GsnEntityRepository extends JpaRepository<GsnEntity, Long> {

  @Query("select g from GsnEntity g "
      + "where g.id = (select max(f.id) from GsnEntity f)")
  GsnEntity findLast();

}
