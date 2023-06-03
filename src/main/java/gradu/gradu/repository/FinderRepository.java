package gradu.gradu.repository;

import gradu.gradu.domain.Finder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinderRepository extends JpaRepository<Finder, Long> {
}
