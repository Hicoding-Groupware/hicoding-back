package com.hook.hicodingapi.record.domain.repository;

import com.hook.hicodingapi.record.domain.Record;
import com.hook.hicodingapi.record.domain.type.SignupStatusType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecordRepository extends JpaRepository<Record, Long> {

}
