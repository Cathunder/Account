package org.example.account.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.account.aop.AccountLockIdInterface;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LockAopAspect {
    private final LockService lockService;

    // 어떤 경우에 이 aspect를 적용할것인가?

    // AccountLock 애너테이션이 적용된 메서드를 호출할 때,
    // 해당 메서드를 실행하기 전후에 추가적인 동작을 수행하도록 Around 어드바이스를 설정하는 것

    // @annotation(애너테이션 위치): 해당 애너테이션이 붙은 메서드 호출을 가로챈다
    // args(request): 이 메서드의 파라미터로 전달되는 객체중 request란 이름을 가진 객체를 선택
    @Around("@annotation(org.example.account.aop.AccountLock) && args(request)")
    public Object aroundMethod(
            ProceedingJoinPoint pjp,

            // 락을 할 때의 기준으로 accountNumber가 필요
            // 하지만 이 어드바이스를 적용하려는 메서드의 파라미터 타입이 다르다.
            // 때문에 두 타입을 하나로 사용할 수 있게 인터페이스로 만들어서 사용
            AccountLockIdInterface request
    ) throws Throwable {
        // lock 취득 시도 (실행전)
        lockService.lock(request.getAccountNumber());

        try {
            // 실행
            return pjp.proceed();
        } finally {
            // lock 해제 (실행후)
            lockService.unlock(request.getAccountNumber());
        }
    }
}
