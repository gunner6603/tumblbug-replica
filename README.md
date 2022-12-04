## 간소화한 텀블벅 웹사이트 만들기 프로젝트

### 2022-11-20
* 프로젝트 시작
* 도메인 개발 완료
* 로그인/회원가입 화면 템플릿 작성 및 로직 생성 (인터셉터 적용은 추후에)

### 2022-11-21
* 프로젝트 등록 화면 템플릿 작성 및 로직 생성
* 프로젝트 상세 화면 템플릿 작성
* 네비게이션바 관련 템플릿 작성

### 2022-11-22
* 프로젝트 상세 화면 출력 로직 생성

### 2022-11-23
* 홈 화면 템플릿 작성
* 홈 화면 프로젝트 목록 출력 로직 생성 (4열로 출력)

### 2022-11-24
* 홈 화면 템플릿 작성 마무리 (인기 프로젝트 목록 생성)
* 네비게이션 바 링크 연결 + 로그인 여부에 따라 로그인/프로필 버튼 전환하는 기능 추가

### 2022-11-25
* 프로필 메인 페이지 템플릿 작성 및 로직 생성

### 2022-11-26
* 프로필 수정 기능 추가
* 프로필 올린 프로젝트 목록 출력 기능 생성
* 후원 기능 추가 (결제 기능은 X)

### 2022-11-27
* 프로필 후원 프로젝트 목록 출력 기능 생성
* 회원 팔로우 로직 생성
* 프로필 팔로워/팔로잉 목록 템플릿 작성

### 2022-11-28
* 프로필 사진 등록 기능 생성
* 프로필 팔로워/팔로잉 목록 출력 기능 생성

### 2022-11-30
* 페이징 관련 DTO 생성

### 2022-12-01
* 페이징 로직 완성
* 인기, 신규, 마감임박 페이지 템플릿 생성 및 출력 로직 생성

### 2022-12-02
* 네비게이션바 프로필 출력, 로그인 처리 관련 인터셉터 생성 및 등록
* 카테고리별 프로젝트 페이지 생성

### 2022-12-03
* 팔로우 취소 기능 생성
* 팔로우/팔로잉 버튼 전환 기능 생성

### 2022-12-04
* 검색 기능 생성 (동적 쿼리 작성에 Querydsl 사용)
* 로그인 상태에서 회원가입 페이지 접근 불가능하도록 로직 수정
* 한 유저가 동일 프로젝트를 여러 번 후원해도 프로필 후원 프로젝트 내역에 동일 프로젝트는 한 번만 표시되도록 수정

### 만들어야 하는 기능
* 프로젝트/프로필 수정/삭제 기능
* 커뮤니티 기능