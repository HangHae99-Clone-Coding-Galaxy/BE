package com.sparta.coding_galaxy_be.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
public class EditMyInfoRequestDto {

    private MultipartFile profileImage;
    private String nickname;
}
