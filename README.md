# YoriZori 
경북대학교 졸업프로젝트 요리조리 Back-End

## 개요
요리조리는 사용자가 냉장고에 있는 재료를 관리하고, 이를 바탕으로 맞춤형 레시피를 추천해주는 서비스입니다. 백엔드는 Spring Boot로 개발되었으며, 사용자 관리, 레시피 추천, 재료 추적 등의 API를 제공합니다.

프론트엔드의 GitHub 주소 : https://github.com/zzihoos/cookingcooking

## 주요 기능
- Spring Security와 JWT를 사용한 사용자 인증 및 인가.
- 사용자 냉장고의 재료 관리: 재료 추가, 업데이트 및 삭제 기능.
- 재료 소비기한 디데이 제공.
- 냉장고 속 재료들을 기반으로 한 레시피 추천.
- 오늘의 추천 레시피
- 식약처 API 연동을 통해 레시피 데이터 가져오기 및 필요 재료 목록 정규화.
- 레시피 별 부족한 재료 개수와 목록 제공
- 장바구니 기능
- 사용자가 즐겨찾기한 레시피 북마크 기능.


## 기술 스택
- **Java**: 개발에 사용된 프로그래밍 언어.
- **Spring Boot**: 백엔드 애플리케이션 구축을 위한 프레임워크.
- **Spring Security**: 애플리케이션 보안을 위한 프레임워크.
- **JWT (JSON Web Tokens)**: 인증 및 인가 처리.
- **Spring Data JPA**: 데이터베이스 상호작용.
- **Spring Scheduler, Spring Cache**: 오늘의 추천 레시피 갱신
- **MySQL**: 애플리케이션 데이터 저장을 위한 데이터베이스.
- **AWS EC2**: 애플리케이션 호스팅.
- **AWS RDS**: MySQL 데이터베이스 관리.
- **식약처 API**: 레시피 데이터 가져오기.

## ERD
<img width="854" alt="image" src="https://github.com/seunggi99/YoriZori/assets/94459503/b1280339-21b0-42f8-9c08-40b13215c6e0">

## 시작하기
### 사전 준비
- JDK 17
- MySQL
- Gradle

### 설치 방법
1.  레포지토리 클론:
   ```sh
   git clone https://github.com/seunggi99/YoriZori.git
   cd YoriZori
   ```
2.	application.properties 파일을 업데이트하여 MySQL 데이터베이스 자격 증명 및 기타 설정을 입력합니다.
   
3.	Gradle을 사용하여 프로젝트 빌드:
   ```sh
   ./gradlew build
   ```
4.  애플리케이션 실행:
   ```sh
   ./gradlew bootRun
   ```

### API 문서 

API 문서는 Swagger를 통해 제공됩니다. 애플리케이션이 실행된 후 다음 URL에서 확인할 수 있습니다:
http://localhost:8080/swagger-ui.html
