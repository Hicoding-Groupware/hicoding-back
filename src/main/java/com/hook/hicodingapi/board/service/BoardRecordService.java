package com.hook.hicodingapi.board.service;

import com.hook.hicodingapi.board.domain.BoardRecord;
import com.hook.hicodingapi.board.domain.repository.BoardRecordRepository;
import com.hook.hicodingapi.board.domain.type.BoardRecordType;
import com.hook.hicodingapi.common.exception.CustomException;
import com.hook.hicodingapi.member.domain.Member;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.hook.hicodingapi.common.exception.type.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardRecordService {

    private final BoardRecordRepository boardRecordRepository;

    private static final int RECORD_LIMIT = 60;

    // 회원을 BoardRecord에 기록 및 조회수나 좋아요 누적시키는 로직
    public boolean isPossibleRecord(final BoardRecordType boardRecordType,
                                    final Member member,
                                    final Long postNo) {

        boolean isPossibleRecord = false;

        // 기록자가 있는가?
        final BoardRecord recorderBoardRecord = boardRecordRepository.findByRecordTypeAndRecorderMemberNoAndPostNo(
                boardRecordType, member.getMemberNo(), postNo
        ).orElse(null);

        // 1. 없다면 생성 후 통과 시킨다.
        if (recorderBoardRecord == null) {
            final BoardRecord newBoardRecord = BoardRecord.of(boardRecordType, member, postNo)
                    .orElseThrow(() -> new CustomException(NOT_FOUND_BOARD_RECORD_CODE));

            boardRecordRepository.save(newBoardRecord);

            isPossibleRecord = true;
        } else {

            // 있다면 현재 시간과 이전 읽은 시점의 시간의 차가 지정한 시간을 확인한다.
            final LocalDateTime preReadTime = recorderBoardRecord.getModifiedAt();
            final LocalDateTime currReadTime = LocalDateTime.now();
            final Long secDiff = Duration.between(preReadTime, currReadTime).toSeconds();

            // 지정 시간을 넘겼다면 이전 읽은 시점을 갱신 후 통과시킨다.
            if (RECORD_LIMIT <= secDiff) {
                recorderBoardRecord.setModifiedAt(currReadTime);

                isPossibleRecord = true;
            }
        }

        return isPossibleRecord;
    }

    public List<BoardRecord> findBoardRecordList(final Long postNo, final BoardRecordType boardRecordType) {
            final List<BoardRecord> boardRecordList = boardRecordRepository.findAllByRecordTypeAndPostNo(boardRecordType, postNo)
                    .orElseThrow(() -> new CustomException(NOT_FOUND_BOARD_RECORDS_CODE));

            return boardRecordList;
    }
}
