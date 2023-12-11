package com.hook.hicodingapi.msg.domain.repository;

import com.hook.hicodingapi.msg.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findByMemberNo(Pageable pageable, Long memberNo);
}
