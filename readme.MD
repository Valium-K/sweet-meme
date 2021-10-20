# SweetMeme

-------------------

쉬는시간 달콤한 밈 한조각 하고가세요!

## 기술 스택

-------------------
BuildTool
* gradle

Library
* Spring Boot
* Spring Web MVC
* Spring Data JPA
* H2
* PostgreSQL
* Lombok
* Spring mail
* Spring Boot Devtools
* Thymeleaf

Front 
* BootStrap 5
* JQuery

## 프로젝트 설명

---------------------



### UML
![uml](readMeRes/comment.png)

### Main
최신순, 트랜딩, Top 포스트 및 섹션별 포스트를 Async Paging으로 불러온다.
![img_10.png](readMeRes/img_10.png)

### Login
Spring Security를 이용한 Login 및 remember-me, Spring mail을 이용한 패스워드 찾기 기능.
![img_1.png](readMeRes/img_1.png)

### Sign up
회원가입 기능.

![img_2.png](readMeRes/img_2.png)

### My Profile
모든 종합항목, 자신의 포스트, 코맨트, 업보트한 포스트를 볼 수 있다.
![img_3.png](readMeRes/img_3.png)

### Settings - account
닉네임 및 알림 설정 기능.

![img_4.png](readMeRes/img_4.png)

### Settings - profile
아바타, 설명 및 닉네임과 함께 표시될 국가를 고를 수 있다.

![img_5.png](readMeRes/img_5.png)

### Settings - password
비밀번호 변경 기능.

![img_6.png](readMeRes/img_6.png)

### Notification
navbar의 알림 미리보기 및 알림페이지.

![img_8.png](readMeRes/img_8.png)

### Upload
포스팅 페이지. mp4외의 Image파일은 webp로 변환한다.

![img_9.png](readMeRes/img_9.png)

### Post View
포스트 코맨트, 답글, 업|다운 보트 기능. 코맨트, 답글에 paging 적용.    

![img11.jpeg](readMeRes/img11.jpeg)

### Tag to Search
포스트의 태그를 클릭해 해당태그를 가진 포스트를 검색한다.
![img12.jpeg](readMeRes/img12.jpeg)