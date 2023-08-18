package com.ricoandilet.springtools.jwt;

import lombok.Data;

import java.time.LocalDateTime;

/**
* @author rico
* 2022年8月18日 上午9:49:04
*/
@Data
public class JwtPayload<T> {

    private String id;
    private T userInfo;
    private LocalDateTime expiration;
}
