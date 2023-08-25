package com.softeem.beans;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class CaptchaRes {

    private String id;

    private String imageBase64;

}
