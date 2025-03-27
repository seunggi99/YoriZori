# YoriZori 
<img width="200" alt="image" src="https://github.com/user-attachments/assets/b6ce6854-dd0a-4cad-9f8a-12755c358e94">


경북대학교 졸업프로젝트 요리조리 Back-End

발표 및 시연 영상 : https://youtu.be/BGEUAKd8r6s


## 개요

**요리조리**는 냉장고 관리와 레시피 추천을 도와주는 반응형 웹사이트입니다. 주요 기능은 다음과 같습니다.

1. 사용자의 냉장고 **식재료 관리**
2. 보유 식재료를 바탕으로 **레시피 추천**
3. 장을 볼 때 참고할 수 있는 **장바구니 리스트**

프론트엔드 : https://github.com/psun0610/YoriZori

## 기획 동기

자취생으로 살면서 항상 **오늘 뭐먹지**가 가장 큰 고민이었습니다.
지금 내 냉장고에 있는 재료를 한 눈에 관리하면서 레시피도 추천받을 수 있는 앱이 있으면 자취생들에게 아주 편할 것 같았습니다.

개발을 하기 전에 먼저 기존 시스템을 찾아보았습니다.
만개의 레시피, 원더 프리지와 같은 냉장고 어플들을 위주로 찾아보았습니다.
많은 앱들이 냉장고 재료를 관리해주는 기능은 있으나 그 재료를 기반으로 레시피 추천을 해주진 않는다거나, 반대로 레시피 추천은 많지만 재료 기반 추천 기능은 없었습니다.

그래서 **나의** 냉장고 관리와 **내가 가진 재료**를 이용한 레시피 추천 기능을 합친 **요리조리**를 개발하게 되었습니다.

## 주요 기능 ( 백엔드 )
### 회원
- 회원 가입 및 로그인
- Spring Security와 JWT를 사용한 사용자 인증 및 인가
- 기피 재료 등록/삭제
### 레시피
- 냉장고 속 재료들을 기반으로 한 레시피 추천
- 기피 재료가 포함 된 레시피 필터링
- 레시피 별 부족한 재료 개수와 목록 제공
- 식약처 API 연동을 통해 레시피 데이터 가져오기 및 필요 재료 목록 정규화
- 카테고리 
- 북마크 된 개수 제공
### 나의 냉장고
- 재료 CRUD
- 재료 소비기한 디데이 제공
- 보관 장소 구분
### 장바구니
- 장바구니 CRUD
### 메인 페이지
- 오늘의 추천 레시피



## 기술 스택
 - 백엔드<br/>
 
    <img src="https://img.shields.io/badge/JAVA-FF7800?style=for-the-badge&logo=Java&logoColor=white"/> <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=Springboot&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Springsecurity&logoColor=white"/> <img src="https://img.shields.io/badge/spring scheduler-6DB33F?style=for-the-badge&logo=springscheduler&logoColor=white"/>
    <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"/> <img src="https://img.shields.io/badge/spring Data JPA-000000?style=for-the-badge&logo=spring&logoColor=white"/>
    <img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=for-the-badge&logo=AmazonEC2&logoColor=white"/> <img src="https://img.shields.io/badge/Amazon RDS-527FFF?style=for-the-badge&logo=Amazon RDS&logoColor=white"/>
      
  - 프론트엔드
    
    <img src="https://img.shields.io/badge/Typescript-3178C6?style=for-the-badge&logo=Typescript&logoColor=white"/><img src="https://img.shields.io/badge/Javascript-F7DF1E?style=for-the-badge&logo=Javascript&logoColor=black"/> <img src="https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=React&logoColor=black"/> <img src="https://img.shields.io/badge/Prettier-F7B93E?style=for-the-badge&logo=Prettier&logoColor=darkred"/> <img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white"/> <img src="https://img.shields.io/badge/css3-1572B6?style=for-the-badge&logo=css3&logoColor=white"/> <img src="https://img.shields.io/badge/styledcomponents-DB7093?style=for-the-badge&logo=styledcomponents&logoColor=white"/> <img src="https://img.shields.io/badge/vercel-000000?style=for-the-badge&logo=vercel&logoColor=white"/>
    
  - 협업
  
    <img src="https://img.shields.io/badge/GITHUB-181717?style=for-the-badge&logo=github&logoColor=white"/> <img src="https://img.shields.io/badge/GITHUB-5865F2?style=for-the-badge&logo=discord&logoColor=white"/> <img src="https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white"/>


## ERD
<img width="854" alt="image" src="https://github.com/seunggi99/YoriZori/assets/94459503/b1280339-21b0-42f8-9c08-40b13215c6e0">

## 시스템 아키텍처
<img width="854" alt="image" src="https://github.com/user-attachments/assets/914fba5f-9be9-4b37-9090-0592bf6a4d9b">


### API 문서 

API 문서는 Swagger를 통해 제공됩니다. 다음 URL에서 확인할 수 있습니다:


https://app.swaggerhub.com/apis/NINNY9988/YoriZori-api/1.0.0#/ 


혹은


애플리케이션이 실행된 후 : http://localhost:8080/swagger-ui.html


