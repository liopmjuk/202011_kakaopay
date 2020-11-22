카카오페이 뿌리기 기능 구현하기
=============================


### 개발 프레임웍
> - spring boot 2.1.5 Release

> - spring-boot-starter-tomcat

> - H2 내장 DB

> - Spring boot Jpa

> - lombok project


### 핵심 문제해결 전략


#### 1. 공통

> - 사용할 DB 데이터를 넣어주기 위해 뿌리기정보, 뿌리기별금액정보 csv 파일을 생성했습니다.

> - 뿌리기정보는 SPEND_INFO, 뿌리기별금액정보는 SPEND_MONEY_DETAIL 테이블에 매핑되도록 작성했습니다.

> - Controller 에서는 spring boot에서 제공하는 @RequestHeader, @ReqeustBody 사용하여 각각 Header, body를 받았습니다.
 
> - Exception 제어는 @ControllerAdvice 어노테이션을 사용하여 SpendExceptionHandler 클래스에서 전반적으로 관리하도록 작성했습니다. 

> - SpendErrorCode Enum 클래스를 작성하여 기능 제약사항에 대한 에러를 공통적으로 관리할 수 있도록 했습니다.


#### 2. 뿌리기 요청 건에 대한 고유 token

> - 예측 불가능한 문자열 구성을 위해 SHA-256 해시 알고리즘을 사용했습니다.

> - 토큰 생성 시각, 대화방ID, salt 값(KAKAOPAY_SPEND) 으로 해시했습니다.

#### 3. 분배 전략 

> - 남은 금액을 절반으로 쪼갠 후, 랜덤한 값을 붙여서 사용자가 받을 수 있는 금액을 생성했습니다.
 
> - T / 2 + ((T / 2) * a)   
>   T : 남은 총 금액, a : 랜덤하게 생성된 계수 ( 0 < a < 0.5 )


#### 4. 테스트

> - 패키지 별, 기능 별로 필요한 단위테스트를 작성했습니다.
 
> - Service 패키지의 단위 테스트에서는 조회 시점의 타임스탬프 데이터를 조정하기 위해서 Mock 데이터를 주입하는 방식으로 작성해서 테스트 했습니다.

> - 실제 H2 데이터베이스에 저장, 조회하는 것은 PretaskApplication을 기동한 후 Postman 을 사용하여 API 별로 테스트했습니다.
