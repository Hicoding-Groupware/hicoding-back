package com.hook.hicodingapi.classroom.dto.response;

import com.hook.hicodingapi.classroom.domain.Classroom;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ClassroomResponse {
    private final Long roomCode;
    private final String roomName;

    public static ClassroomResponse from(Classroom classroom) {
        return new ClassroomResponse(
                classroom.getRoomCode(),
                classroom.getRoomName()
        );
    }
}
