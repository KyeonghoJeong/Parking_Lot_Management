<h1><img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/6e555a40-3758-43a5-8fdf-33c6bd0122d0" width="25"/> 번호판 인식을 통한 장애인 주차구역 관리 시스템</h1>

목차
----
1. 프로젝트 소개
2. 목표
3. 역할
4. 기술 스택
5. 프로젝트 구조
6. 주요 기능
7. 데모 영상
8. 프로젝트 평가
9. 회고

## 1. 프로젝트 소개

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/a1af6660-f366-4676-8137-401f0dd4e711" width="500"/><br>

<strong>줄지 않는 장애인 주차구역 위반 건수 2018년 414,409건 (출처: 보건복지부 제출자료)</strong><br>

장애인 전용 주차구역 단속 인력 부족 문제와 민원 신고에 따른 2차 분쟁 위험 문제를 해결하기 위해 장애인 주차구역을 실시간으로 관리할 수 있는 시스템을 개발하고자 함<br>

주차구역 확인 및 기록 관리를 할 수 있으며 위반 차량 판단에 대한 경고, 안내, 기록까지 자동으로 처리되어 관리자가 직접 현장에 갈 필요 없이 프로그램의 여러 기능을 통해 쉽게 주차구역을 관리할 수 있음

## 2. 목표

프로그래밍 언어, 데이터베이스, 네트워크 통신 등 학교에서 배운 전공 지식 활용<br>
Java Swing GUI 구현<br>
소켓 통신, 멀티 스레드 구현<br>
Python, OpenCV, Tesseract 등 새로운 언어와 툴 학습<br>
Raspberry Pi를 이용한 사물인터넷 구현<br>
이미지 프로세싱을 통한 번호판 인식 구현

## 3. 역할

- 본인

관리자용 PC 프로그램 기능 및 GUI 디자인<br>
관리자용 PC 프로그램 Java 프로그래밍<br>
라즈베리파이 동작 과정 설계<br>
라즈베리파이 Python 프로그래밍

- 팀원

OpenCV & Pytesseract 번호판 인식 및 검출<br>
모형 디자인 및 제작<br>
프로그램 디버깅<br>
프로그램 실행 및 성능 평가<br>
졸업작품전시회 출품 및 결과 발표<br>
졸업작품 연구 보고서 작성 및 제출

## 4. 기술 스택

Java, Java Swing, MySQL, Raspberry Pi, Python, OpenCV, Tesseract

## 5. 프로젝트 구조

- 순서도

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/b6537ae0-4e30-45bf-8099-2dab52383b57" width="500"><br>

Java 서버: 소켓으로 연결된 각 주차구역 클라이언트를 멀티 스레드로 처리<br>

Python 클라이언트: 주차구역을 클래스로 정의하고 각 주차구역을 인스턴스로 생성하여 멀티 스레드로 처리<br>

MySQL: 로그인 처리, 조건에 맞는 주차기록을 테이블에 출력, 차량 정보 등록/조회/수정/삭제, 위반 여부 기록<br>

GUI: Java Swing WindowBuilder로 구현<br>

동영상 재생: EmbeddedMediaPlayerComponent 객체 생성으로 재생

---

- 주차장 모형 전면

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/9a00fffe-adf4-45b7-8aaa-9a706d6579f3" width="800"/><br>

차량 진입 감지: 초음파센서로 거리 변화를 감지하여 주차를 판단하고 웹캠과 OpenCV 라이브러리로 차량을 촬영<br>

데이터 전송: 클라이언트와 서버가 소켓을 통해 주차구역 번호, 주차시간, 차량번호, 위반 여부, 동영상을 주고받음<br>

일반 차량: 위반 사항이 없으면 LED를 초록색으로 점등하고 서버는 주차 기록을 출력<br>

위반 차량: LED 점멸과 음성 경고 후 일정 시간이 지난 뒤에도 그대로 주차구역 위에 있을 경우에는 LED를 빨간색으로 점등하고 서버는 주차 기록을 출력<br>

출차 판단: 다시 초음파센서를 동작시켜 거리 변화를 감지해 출차를 판단하고 출차로 판단되면 출차 시간을 서버로 전송하고 서버는 이를 출력

---

- 주차장 모형 후면

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/f9724c4c-1607-46c8-b9a6-38bae6d9a248" width="800"/><br>

라즈베리파이에 초음파센서, 웹캠, 스피커, LED를 연결하여 차량 진입 감지, 사진 및 동영상 촬영, 위반 차량 및 적재물 판단, 음성 경고 및 LED 안내, 실시간 스트리밍을 실시함<br>

라즈베리파이는 촬영한 사진으로부터 번호판 영역을 검출하고 서버로 차량번호를 전송, 서버는 DB에 저장되어 있는 해당 번호의 장애인 등록 여부를 확인하여 위반 차량을 판단함

## 6. 주요 기능

- 관리자 프로그램 (Java 서버)

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/6d66b3b0-9267-4718-9fd7-b004ffc48ce0" width="500"/><br>
<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/011f207b-b719-40bf-a92b-42ceb97fd17e" width="800"/><br>

로그인 기능<br>
위반 차량 판단<br>
주차장 전체 스트리밍 재생<br>
주차 기록 차량 정보 조회 및 주차 당시 동영상 재생<br>
전체/금일/현재/위반/특정 날짜 별 주차 기록 조회<br>
차량 정보 조회/등록/수정/삭제

---

- 주차구역 (Raspberry Pi Python 클라이언트)

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/f43f1365-b712-49e4-b348-cdf26545298d" width="500"/><br>

주차 판단<br>
번호판 인식<br>
LED 및 음성 메시지 알림<br>
출차 판단

## 7. 데모 영상

- 관리자 프로그램 로그인

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/bb201f6c-ee81-4faa-a223-bd2a79efbcc8"/><br>

---

- 장애인 주차구역 차량 진입

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/9bf6de1a-e2f8-43e0-8696-94eb36addc02"/><br>

---

- 번호판 인식 및 위반 차량 식별

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/8c580442-f367-45fa-83eb-fc1e416f0920"/><br>

---

- 위반 차량 영상 및 정보 확인

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/1fa7f881-9959-4f41-900a-12a821fd7f64"/><br>

---

- 주차구역 실시간 스트리밍

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/aef21537-26af-41ae-bcfa-7fb73e077c28"/><br>

---

- 주차기록 조회 옵션 버튼 (전체/금일/현재/위반)

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/bfa63998-4c68-4ff8-97f7-ce9d5204db6b"/><br>

---

- 주차기록 날짜 옵션 조회

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/4103379a-b4aa-4500-856b-c1a6e9bb2e4b"/><br>

---

- 차량 정보 데이터베이스 조회

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/69f83e78-8ac9-45a0-a8f7-e545078ee124"/><br>

---

## 8. 프로젝트 평가
