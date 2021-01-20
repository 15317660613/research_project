package com.adc.da.gen.rsa;


import com.adc.da.util.utils.RSAUtils;
import org.junit.Test;

import java.util.Map;

public class RSATest {
    @Test
    public void getRSA(){
        Map<String,String> map = RSAUtils.createKeys(512);
        System.out.println("publicKey:   "+map.get("publicKey"));
        System.out.println("privateKey:  "+map.get("privateKey"));
//        System.out.println("model:       "+map.get("model"));
    }
}
