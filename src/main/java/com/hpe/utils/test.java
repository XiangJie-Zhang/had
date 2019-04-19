package com.hpe.utils;

import com.hpe.mapper.BehaviorMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class test {

@Autowired
private BehaviorMapper b;
    public static void main(String[] args) throws Exception{
        for (String a:
        StringTools.get7Days()) {
            System.out.println(a);
        }
    }


    @Test
    public void test1() throws Exception{
        System.out.println(b);
    }

}
