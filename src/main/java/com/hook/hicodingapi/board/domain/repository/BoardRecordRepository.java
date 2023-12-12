package com.hook.hicodingapi.board.domain.repository;

import com.hook.hicodingapi.board.domain.BoardRecord;
import com.hook.hicodingapi.board.domain.type.BoardRecordType;
import com.hook.hicodingapi.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface BoardRecordRepository  extends JpaRepository<BoardRecord, Long> {
    Optional<BoardRecord> findByRecordTypeAndRecorderMemberNoAndPostNo (BoardRecordType recordType, Long recorderNo, Long postCode);
    Optional<List<BoardRecord>> findAllByRecordTypeAndPostNo(BoardRecordType recordType, Long postNo);
}
