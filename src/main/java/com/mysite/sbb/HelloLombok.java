package com.mysite.sbb;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class HelloLombok {
    private final String hello;
    private final int lombok;
    private String nofinal;

    public static void main(String[] args) {
        HelloLombok helloLombok = new HelloLombok("hello",5);
        helloLombok.setNofinal("hello, lombok 속성에 final을 적용하고 롬복의 @RequiredArgsConstructor 애너테이션을 적용하면 해당 속성을 필요로하는 생성자가 롬복에 의해 자동으로 생성된다.");

        System.out.println(helloLombok.getHello());
        System.out.println(helloLombok.getLombok());
        System.out.println(helloLombok.getNofinal());
    }
}
