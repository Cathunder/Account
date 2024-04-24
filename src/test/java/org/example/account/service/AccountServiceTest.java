package org.example.account.service;

import org.example.account.domain.Account;
import org.example.account.domain.AccountUser;
import org.example.account.dto.AccountDto;
import org.example.account.exception.AccountException;
import org.example.account.repository.AccountUserRepository;
import org.example.account.type.AccountStatus;
import org.example.account.repository.AccountRepository;
import org.example.account.type.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)     // mockito 기능을 쓰기위해 추가
class AccountServiceTest {

    @Mock   // 가짜 accountRepository 생성 (mock)
    private AccountRepository accountRepository;

    @Mock
    private AccountUserRepository accountUserRepository;

    @InjectMocks  // 위에서 생성한 가짜 mock들을 주입
    private AccountService accountService;

    @Test
    void createAccountSuccess() {
        //given
        AccountUser user = AccountUser.builder()
                .id(12L)
                .name("Pobi").build();

        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(user));

        given(accountRepository.findFirstByOrderByIdDesc())
                .willReturn(Optional.of(Account.builder()
                                .accountNumber("1000000012").build()));

        given(accountRepository.save(any()))
                .willReturn(Account.builder()
                        .accountUser(user)
                        .accountNumber("1000000015").build());  // 임의의 값 리턴. 테스트와 관련x

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);

        //when
        AccountDto accountDto = accountService.createAccount(1L, 1000L);

        //then
        verify(accountRepository, times(1)).save(captor.capture());
        assertEquals(12L, accountDto.getUserId());
        assertEquals("1000000013", captor.getValue().getAccountNumber());
    }

    @Test
    void createFirstAccount() {
        //given
        AccountUser user = AccountUser.builder()
                .id(15L)
                .name("Pobi").build();

        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(user));

        given(accountRepository.findFirstByOrderByIdDesc())
                .willReturn(Optional.empty());

        given(accountRepository.save(any()))
                .willReturn(Account.builder()
                        .accountUser(user)
                        .accountNumber("1000000015").build());

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);

        //when
        AccountDto accountDto = accountService.createAccount(1L, 1000L);

        //then
        verify(accountRepository, times(1)).save(captor.capture());
        assertEquals(15L, accountDto.getUserId());
        assertEquals("1000000000", captor.getValue().getAccountNumber());
    }

    @Test
    @DisplayName("해당 유저 없음 - 계좌 생성 실패")
    void createAccount_UserNotFound() {
        //given
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.createAccount(1L, 1000L));

        //then
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("유저당 최대 계좌는 10개")
    void createAccount_maxAccountIs10() {
        //given
        AccountUser user = AccountUser.builder()
                .id(15L)
                .name("Pobi").build();

        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(user));

        given(accountRepository.countByAccountUser(any()))
                .willReturn(10);

        //when
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.createAccount(1L, 1000L));

        //then
        assertEquals(ErrorCode.MAX_ACCOUNT_PER_USER_10, exception.getErrorCode());
    }








    // =========== 아래는 실습용 ===========
    // mokito를 사용하면
    // db내 데이터의 변경, accountRepository 코드 변경과는 상관없이
    // getAccount() 테스트만 진행할 수 있게 됨
    // 진짜로 테스트할 로직만 테스트할 수 있음
    @Test
    @DisplayName("계좌 조회 성공")
    void testXXX() {
        //given
        given(accountRepository.findById(anyLong()))
                .willReturn(Optional.of(Account.builder()
                        .accountStatus(AccountStatus.UNREGISTERED)
                        .accountNumber("65789")
                        .build()));

        ArgumentCaptor<Long> captor =
                ArgumentCaptor.forClass(Long.class); // long 타입의 인자를 capture 하기 위한 captor라는 객체 생성

        //when
        Account account = accountService.getAccount(4555L);

        //then
        // findById 메서드가 호출될때 전달되는 Long 타입 인자를 capture 해서 captor에 저장
        verify(accountRepository, times(1)).findById(captor.capture());
        verify(accountRepository, times(0)).save(any());
        assertEquals(4555L, captor.getValue());         // getValue()로 꺼내 사용가능
        assertNotEquals(4555123L, captor.getValue());
        assertEquals("65789", account.getAccountNumber());
        assertEquals(AccountStatus.UNREGISTERED, account.getAccountStatus());
    }

    @Test
    @DisplayName("계좌 조회 실패 - 음수로 조회")
    void testFailedToSearchAccount() {
        //given
        //when
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> accountService.getAccount(-10L));
        // () -> accountService.getAccount(-10L) 이 코드가 동작하면 RuntimeException이 반환될 것이다.

        //then
        assertEquals("Minus", exception.getMessage());
    }

    @Test
    @DisplayName("테스트 이름 변경")
    void testGetAccount() {
        //given
        given(accountRepository.findById(anyLong()))
                .willReturn(Optional.of(Account.builder()
                        .accountStatus(AccountStatus.UNREGISTERED)
                        .accountNumber("65789")
                        .build()));

        //when
        Account account = accountService.getAccount(4555L);

        //then
        assertEquals("65789", account.getAccountNumber());
        assertEquals(AccountStatus.UNREGISTERED, account.getAccountStatus());
    }

    @Test
    void testGetAccount2() {
        //given
        given(accountRepository.findById(anyLong()))
                .willReturn(Optional.of(Account.builder()
                        .accountStatus(AccountStatus.UNREGISTERED)
                        .accountNumber("65789")
                        .build()));

        //when
        Account account = accountService.getAccount(4555L);

        //then
        assertEquals("65789", account.getAccountNumber());
        assertEquals(AccountStatus.UNREGISTERED, account.getAccountStatus());
    }
}

//@SpringBootTest     // 모든 빈들을 생성해서 등록함
//class AccountServiceTest {
//
//    @Autowired  // 모든 빈들이 등록되어 있으니 주입도 가능
//    private AccountService accountService;
//
//    @BeforeEach     // 매번 테스트를 진행하기전 실행됨
//    void init() {
//        accountService.createAccount();
//    }
//
//    @Test
//    @DisplayName("테스트 이름 변경")
//    void testGetAccount() {
//        Account account = accountService.getAccount(2L);
//
//        assertEquals("40000", account.getAccountNumber());
//        assertEquals(AccountStatus.IN_USER , account.getAccountStatus());
//    }
//
//    @Test
//    void testGetAccount2() {
//        Account account = accountService.getAccount(2L);
//
//        assertEquals("40000", account.getAccountNumber());
//        assertEquals(AccountStatus.IN_USER , account.getAccountStatus());
//    }
//}