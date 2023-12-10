package com.hook.hicodingapi.board.dto.response;

import com.hook.hicodingapi.board.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostQueryResponse {
    private Long postNo;
    private Post parent;
    private Long postNo2;
    private Post parent2;


}
