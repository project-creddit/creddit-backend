package com.creddit.credditmainserver.dto.request;

import com.creddit.credditmainserver.domain.Image;
import com.creddit.credditmainserver.domain.Member;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class ProfileRequestDto {

    @Size(min=0,max=50)
    private String introduction;

    private Image image;

}
