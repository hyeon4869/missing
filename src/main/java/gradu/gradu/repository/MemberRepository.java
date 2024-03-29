package gradu.gradu.repository;

import gradu.gradu.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserID(String userID);
    Optional<Member> findByUserNameAndUserEmail(String userName, String userEmail);
    Optional<Member> findByUserNameAndUserEmailAndUserID(String userName, String userEmail, String userID);
}
