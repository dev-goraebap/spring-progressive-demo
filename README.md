# Goraebap.dev Blog v2

[하이퍼미디어 기반 애플리케이션 (Hypermedia-Driven Application)](https://htmx.org/essays/hypermedia-driven-applications/) 방식으로 개발된 개인 블로그입니다.

> 이전 NestJS 버전: [dev-goraebap-blog-nestjs](https://github.com/dev-goraebap/dev-goraebap-blog-nestjs)

## 기술 스택

Spring Boot 프레임워크를 기반으로, JTE 템플릿 엔진과 HTMX를 사용합니다.

프론트엔드 개발 경험(DX)을 포기하기 어려워 Vite로 에셋 파이프라인을 구성했습니다. Tailwind CSS나 Alpine.js 같은 도구를 더 효율적으로 사용하기 위함입니다. CDN을 사용하면 초기 설정은 간단하지만, 운영 환경에서 좋은 퍼포먼스를 기대하기 어렵습니다.

## HDA 방식은 찍먹할만함

API 서버와 프론트엔드를 분리하는 SPA 방식으로 오랫동안 개발해왔습니다. React를 거쳐 Angular까지, 프레임워크는 바뀌어도 방식은 같았습니다. 

그러다 어느 순간 요구사항 대비 과도한 코드량과 복잡한 상태 관리에 다른 방법을 찾다보니 HDA 방식에 대해 알게되었고 요즘 주된 관심사가 되었습니다.

HDA 방식은 서버가 HTML을 직접 렌더링하고, 필요한 부분만 동적으로 교체하는 접근법입니다. JSON API와 클라이언트 사이드 렌더링 대신, 브라우저 본연의 동작 방식을 활용합니다.

FE/BE를 분리해서 개발하는 방식에 익숙하다면 적응하는 데 시간이 필요합니다. 올해 7월쯤부터 2개의 프로젝트를 진행하면서 이제야 보이는 것들이 있습니다. HDA 방식은 매력적이지만 완벽하진 않습니다.

자바스크립트 사용을 줄여준다고 해서 아예 사용하지 않는 방법을 찾느라 시간을 쏟았지만, 완성도 높은 사용자 경험을 위해선 JS를 적절히 섞어야 합니다. HTMX의 생명주기 이벤트를 활용해 재사용 가능한 코드를 파악하고, 바닐라 자바스크립트에 집착하기보다는 Alpine.js 같은 경량 라이브러리를 활용하는 편이 낫습니다.

그리고 서버 측 에러 처리에 대한 이해가 필요합니다. 브라우저가 직접 받는 HTML 응답과 HTMX가 Ajax로 요청해서 받는 HTML 응답은 처리 방식이 다릅니다. 서버에서 API를 함께 사용하는 경우도 고려하면, 에러 처리 흐름을 먼저 파악해두는 게 수월합니다.

## 왜 Java/Spring으로 전환했나?

원래 이 프로젝트는 회사에서 사용하는 기술 스택을 연습하기 위해 시작했습니다(프로젝트명도 spring-progressive-demo였음). 
하지만 개발을 진행하면서 백엔드 언어와 무관하게 HDA의 UX와 DX를 깊이 경험해보고 싶어졌고, 기존 블로그의 기능을 그대로 마이그레이션하다 보니 자연스럽게 블로그 v2가 되었습니다.

## 아키텍처

저는 [FSD(Feature-Sliced Design)](https://feature-sliced.design/)의 개념을 좋아하는 편이라 대부분의 프로젝트에 해당 방식들이 녹아있습니다. FSD는 논리적인 개념보다 물리적인 디렉토리 구조의 참조관계를 명확하게 합니다. OOP의 의존성 역전 등의 개념을 섞어주면 적은 비용으로 큰 틀의 규칙을 이해할 수 있습니다.

### 레이어 구조

`xyz.goraebap.blog` 패키지 하위에 4개의 레이어가 있습니다. 상위 레이어에서 하위 레이어로만 참조할 수 있습니다.

```
app → contract → infra → shared
```

| 레이어 | 역할 |
|--------|------|
| **app** | 도메인 영역. JPA Entity, Repository, Service, Controller |
| **contract** | 슬라이스 간 통신용 인터페이스 |
| **infra** | 화면 조회 전용. JOOQ QueryService, ViewModel |
| **shared** | 기술 영역. 설정, 보안, 서드파티 통합 |

### 용어

FSD의 용어를 차용했습니다.

- **레이어**: `xyz.goraebap.blog` 하위의 최상위 패키지 (app, contract, infra, shared)
- **슬라이스**: 레이어 내부의 직접 하위 패키지 (app.admin, app.client 등)
- **세그먼트**: 슬라이스 내부의 파일 또는 패키지 (app.admin.domain, app.admin.service 등)

### 참조 규칙

**레이어 간**: 상위 → 하위만 가능

```
app → contract ✓    |    infra → app ✗
app → infra ✓       |    contract → app ✗
app → shared ✓      |    shared → app ✗
```

**슬라이스 간**: 같은 레이어 내 슬라이스끼리는 직접 참조 금지 (shared 제외)

```
app.admin → app.client ✗
```

다른 슬라이스의 기능이 필요하면 `contract` 레이어의 인터페이스를 통해 통신합니다.

### 패키지 구조

```
xyz.goraebap.blog/
├── app/                        # 도메인 (CUD)
│   ├── admin/                  # 관리자 기능
│   │   ├── domain/             # Entity + Repository
│   │   ├── service/            # 비즈니스 로직
│   │   ├── web/                # Controller
│   │   └── dto/                # Request/Response
│   ├── client/                 # 사용자 기능
│   └── auth/                   # 인증
├── contract/                   # 슬라이스 간 계약 인터페이스
├── infra/                      # 화면 조회 (R)
│   ├── service/                # JOOQ QueryService
│   └── view_model/             # ViewModel
└── shared/                     # 기술 영역
    ├── config/                 # 설정
    ├── security/               # 보안
    ├── firebase/               # FCM 통합
    ├── gemini/                 # Gemini API
    └── r2/                     # Cloudflare R2
```

### CUD와 R의 분리

- **CUD (Create, Update, Delete)**: `app` 레이어에서 JPA로 처리
- **R (Read - 화면 조회)**: `infra` 레이어에서 JOOQ로 처리

화면 조회는 여러 도메인이 자연스럽게 섞이기 때문에 별도 레이어로 분리했습니다. JOOQ를 사용해 타입 안전한 쿼리와 컴파일 타임 검증을 확보합니다.