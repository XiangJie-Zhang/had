package com.hpe.entity;

import lombok.Data;

import java.util.List;

@Data
public class resourceVo {
    String leftTime;

    String rightTime;

    List<Resources> res;

}
