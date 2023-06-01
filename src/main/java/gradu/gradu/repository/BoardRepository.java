package gradu.gradu.repository;

import gradu.gradu.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findByMissingGenderContaining(String searchGender, Pageable pageable);
    Page<Board> findByMissingPlaceContaining(String h_area1, Pageable pageable);
    Page<Board> findByMissingGenderContainingAndMissingPlaceContaining(String searchGender, String h_area1, Pageable pageable);
    Page<Board> findByMissingNameContaining(String searchKey, Pageable pageable);
    Page<Board> findByMissingNameContainingAndMissingGenderContaining(String searchKey, String searchGender, Pageable pageable);
    Page<Board> findByMissingNameContainingAndMissingGenderContainingAndMissingPlaceContaining(String searchKey, String searchGender, String hArea1, Pageable pageable);

    Page<Board> findByMissingNameContainingAndMissingPlaceContaining(String searchKey, String hArea1, Pageable pageable);
}
