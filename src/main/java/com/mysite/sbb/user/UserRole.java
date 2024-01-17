package com.mysite.sbb.user;

import lombok.Getter;

@Getter
public enum UserRole {
    /*
    * ADMIN 권한(관리자 권한)을 지닌 사용자가 다른 사람이
    * 작성한 질문이나 답변을 수정 가능하도록 만들 수 있을 것
    * */
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    UserRole(String value) {
        this.value = value;
    }

    private String value;
}
/*
* UserRole은 enum 자료형(열거 자료형)으로 작성했다.
* 관리자를 의미하는 ADMIN과 사용자를 의미하는 USER라는 상수를 만들었다.
* 그리고 ADMIN은 ‘ROLE_ADMIN’, USER는 ‘ROLE_USER’라는 값을 부여했다.
* 그리고 UserRole의 ADMIN과 USER 상수는 값을 변경할 필요가 없으므로
* @Setter 없이 @Getter만 사용할 수 있도록 했다.
* */
