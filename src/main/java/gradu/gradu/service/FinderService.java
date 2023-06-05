package gradu.gradu.service;

import gradu.gradu.domain.Board;
import gradu.gradu.domain.Finder;
import gradu.gradu.repository.FinderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FinderService {

    private final FinderRepository finderRepository;


    public void save(Board board) {
        Finder finder = Finder.builder()
                .id(board.getId())
                .missingName(board.getMissingName())
                .missingAge(board.getMissingAge())
                .missingGender(board.getMissingGender())
                .missingPlace(board.getMissingPlace())
                .missingDate(board.getMissingDate())
                .missingNum(board.getMissingNum())
                .findTime(LocalDateTime.now())
                .build();
        finderRepository.save(finder);
    }

    public Page<Finder> findAll(Pageable pageable) {
        return finderRepository.findAll(pageable);
    }
}
