package org.example.account.aop;

import java.lang.annotation.*;

@Target(ElementType.METHOD)             // 애너테이션 가능한 것들 지정
@Retention(RetentionPolicy.RUNTIME)     // 애너테이션의 유지 기간 지정
@Documented             // 해당 애너테이션의 문서화
@Inherited              // AccountLock 애너테이션이 적용된 클래스의 자식 클래스에도 이 애너테이션이 적용됨
public @interface AccountLock {         // 커스텀 애너테이션
    long tryLockTime() default 5000L;
}
