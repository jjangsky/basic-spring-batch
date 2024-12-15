> 참고 : [인프런 스프링 배치 입문 - 예제로 배우는 핵심 Spring Batch](https://www.inflearn.com/course/%EC%98%88%EC%A0%9C%EB%A1%9C-%EB%B0%B0%EC%9A%B0%EB%8A%94-%ED%95%B5%EC%8B%AC-%EC%8A%A4%ED%94%84%EB%A7%81-%EB%B0%B0%EC%B9%98)
# Spring Batch

### Batch작업이란?

배치 작업과 대비되는 작업은 리얼타임 작업 즉, `실시간 작업` 이라고 할 수 있다.

실시간으로 결제가 되고 이후에는 응답을 받아 화면에 주문 내역을 실시간으로 받아 올 수 있다.

이렇게 실시간 작업은 **즉시**  이루어 지지만 배치작업은 그렇지 않다.

`배치 작업`은 특정한 작업들을 한 번에 모아서 처리를 하는 것이다.

예를 들어, 상품을 주문 했을 때 바로 물건을 보내는 것이 아닌 고객의 단순 변심으로 인해 취소를 할 수 있기 때문에 즉시 발송하지 않고 특정 시점에 한번에 처리한다.

>💡**배치 작업** <br>
특정 주기마다  데이터를 처리하는 작업으로 예를 들어서 쿠폰 발생, 가맹점 정산, 거래 명세서 출력 등 실시간으로 처리하기에 무리가 있는 기능에 대해서 사용

</aside>

### Spring Batch 구조

![image (3).png](..%2F..%2FUsers%2Fjangskyyy%2FDownloads%2Fimage%20%283%29.png)

Spring Batch는 `Job Repository`, `Launcher`, `Job`, `Step` 으로 구성되어 있으며 구중 Step 하위에 `ItemReader`, `Processor`, `Writer` 의 모습으로 구성되어 있다.

`Job Repository` 는 배치가 수행될 때 수행되는 메타데이터를 관리하고 시작 시간 및 종료 시간 job의 상태 등 배치 수행 관련 데이터들이 저장됨

`Launcher`  는 `Job`을 실행 시켜 주는 역할을 함

주로 개발하는 영역은 `Job` 과 `Step` 및 그 하위 영역에서 개발이 진행 된다.

---

[Print Hello World - 간단하게 문자열 찍는 스프링 배치 프로그램](https://www.notion.so/Print-Hello-World-158739c36ee480748f26e17760d8384e?pvs=21)

[Validated - 스프링 배치 실행 시, 파라미터 검증 처리](https://www.notion.so/Validated-158739c36ee480c49993cdb4564eb2aa?pvs=21)

[Job-Listener - 스프링 배치 실행 전, 후로 추가 작업 처리](https://www.notion.so/Job-Listener-159739c36ee4803e87bbc2ebc12e5ec0?pvs=21)

[File-Read-Write - csv파일을 읽고 데이터를 가공하여 txt로 반출](https://www.notion.so/File-Read-Write-159739c36ee48026b62cce0ad03a6dbf?pvs=21)

[MultipleStep - 하나의 Job에서 여러 개의 Step 실행](https://www.notion.so/MultipleStep-15a739c36ee480feb3d1ca8520b25bac?pvs=21)

[Batch Test Code - 좋은 배치 프로그램을 만들기 위한 테스트 코드 작성](https://www.notion.so/Batch-Test-Code-15d739c36ee480bb84e3c458b531f030?pvs=21)

