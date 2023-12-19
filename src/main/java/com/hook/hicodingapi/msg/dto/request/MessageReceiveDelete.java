package com.hook.hicodingapi.msg.dto.request;

import lombok.Getter;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
public class MessageReceiveDelete {

    @NotEmpty
    private List<Long> msgNos;

}