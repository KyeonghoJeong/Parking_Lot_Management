<h1><img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/6e555a40-3758-43a5-8fdf-33c6bd0122d0" width="25"/> 번호판 인식을 통한 장애인 주차구역 관리 시스템</h1>

목차
----
1. 프로젝트 소개
2. 목표
3. 담당 역할
4. 기술 스택
5. 프로젝트 구조
6. 주요 기능
7. 데모 영상
8. 회고

## 1. 프로젝트 소개

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/a1af6660-f366-4676-8137-401f0dd4e711" width="500"/><p>

> 줄지 않는 장애인 주차구역 위반 건수 2018년 414,409건 (출처: 보건복지부 제출자료)

장애인 전용 주차구역 단속 인력 부족 문제와 민원 신고에 따른 2차 분쟁 위험 문제를 해결하기 위해 장애인 주차구역을 실시간으로 관리할 수 있는 시스템을 개발하고자 함.<p><p>

주차구역 확인 및 기록 관리를 할 수 있으며 위반 차량 판단에 대한 경고, 안내, 기록까지 자동으로 처리되어 관리자가 직접 현장에 갈 필요 없이 프로그램의 여러 기능을 통해 쉽게 주차구역을 관리할 수 있음.

## 2. 목표

- 프로그래밍 언어, 데이터베이스, 네트워크 통신 등 학교에서 배운 전공 지식 활용
- Java Swing GUI 구현
- 소켓 통신, 멀티 스레드 구현
- Python, OpenCV, Tesseract 등 새로운 언어와 툴 학습
- Raspberry Pi를 이용한 사물인터넷 구현
- 이미지 프로세싱을 활용한 번호판 인식 구현

## 3. 담당 역할

- #### 정경호
 
    관리자 프로그램 기능 구상 및 GUI 디자인<br>
    관리자 프로그램 Java 프로그래밍<br>
    Raspberry Pi 동작 과정 설계<br>
    Raspberry Pi Python 프로그래밍

- #### 팀원

    OpenCV & Tesseract 번호판 인식<br>
    모형 디자인 및 제작<br>
    프로그램 디버깅<br>
    프로그램 실행 및 성능 평가<br>
    졸업작품전시회 출품 및 결과 발표<br>
    졸업작품 연구 보고서 작성 및 제출

## 4. 기술 스택

Java, Java Swing, MySQL, Raspberry Pi, Python, OpenCV, Tesseract

## 5. 프로젝트 구조

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/b6537ae0-4e30-45bf-8099-2dab52383b57" width="500"><p>

> 순서도

### Java 서버

관리자 프로그램은 GUI 인스턴스와 관련 기능으로 구성되어 있으며 동시에 서버 소켓으로 동작함. Runnable 인터페이스와 Thread 클래스를 사용하여 각 주차구역(Python 클라이언트) 별로 연결 요청을 처리하고 주차 기록을 저장/수정하며 차량번호로 DB를 조회하여 해당 차량이 위반 차량인지 판단함.

### Java Swing
모든 운영체제에 동일한 GUI를 제공하기 위해 네이티브 UI를 사용하지 않는 Swing으로 GUI를 개발하였음.

### MySQL
로그인 기능, 메뉴별 주차 기록 조회, 차량 정보 등록/조회/수정/삭제, 주차 기록 저장/수정, 위반 여부 기록 등의 기능을 구현할 때 데이터베이스를 사용하였으며 DBMS로 MySQL을 활용하였음.

### Python 클라이언트
Raspberry Pi의 Python 클라이언트(주차구역)는 주차구역의 기능을 하나의 클래스로 구현하고 각 주차구역을 인스턴스로 생성하여 멀티 스레드로 동작하도록 하였음.

### Raspberry Pi
Raspberry Pi에 초음파센서(HC-SR04)를 연결하고 거리 변화를 감지하여 이를 주차 및 출차 판단에 활용하였음.<p><p>
웹캠을 연결하여 주차 차량의 사진을 캡처하고 이를 번호판 인식에 활용하였음. 또한 주차 차량의 모습을 확인할 수 있도록 비디오 파일로 저장하여 관리자 프로그램에서 확인할 수 있도록 하였으며 MJPG-Streamer를 사용하여 주차장 전체 모습을 스트리밍으로 확인할 수 있도록 하였음.<p><p>
LED와 스피커를 연결하여 안내 및 위반 차량에 대한 경고 알림에 활용하였음.

### OpenCV
주차 차량 이미지에 대해 OpenCV 라이브러리를 활용한 이미지 프로세싱으로 번호판 영역을 인식하도록 하였음.<p><p>
번호판 영역을 찾아낸 후에는 Tesseract-OCR 엔진을 Python에서 사용할 수 있게 해주는 pytesseract 라이브러리를 활용하여 해당 영역에서 글자를 인식하여 인식 결과로 차량번호를 반환하도록 하였음.

---

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/9a00fffe-adf4-45b7-8aaa-9a706d6579f3" width="800"/><p>

> 주차장 모형 전면

---

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/f9724c4c-1607-46c8-b9a6-38bae6d9a248" width="800"/><p>

> 주차장 모형 후면

---

## 6. 주요 기능

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/6d66b3b0-9267-4718-9fd7-b004ffc48ce0" width="500"/><p>

> 관리자 프로그램 (Java 서버)

- 로그인 기능

---

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/011f207b-b719-40bf-a92b-42ceb97fd17e" width="800"/><p>

> 관리자 프로그램 (Java 서버)

- 위반 차량 판단
- 주차장 전체 스트리밍 재생
- 주차 기록 차량 정보 조회 및 주차 당시 동영상 재생
- 전체/금일/현재/위반/특정 날짜 별 주차 기록 조회
- 차량 정보 조회/등록/수정/삭제

---

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/f43f1365-b712-49e4-b348-cdf26545298d" width="500"/><p>

> 주차구역 (Raspberry Pi Python 클라이언트)

- 주차 판단
- 번호판 인식
- LED 안내 및 음성 경고
- 출차 판단

## 7. 데모 영상

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/bb201f6c-ee81-4faa-a223-bd2a79efbcc8"/><p>

> 관리자 프로그램 로그인

---

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/9bf6de1a-e2f8-43e0-8696-94eb36addc02"/><p>

> 장애인 주차구역 차량 진입

---

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/8c580442-f367-45fa-83eb-fc1e416f0920"/><p>

> 번호판 인식 및 위반 차량 식별

---

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/1fa7f881-9959-4f41-900a-12a821fd7f64"/><p>

> 위반 차량 영상 및 정보 확인

---

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/aef21537-26af-41ae-bcfa-7fb73e077c28"/><p>

> 주차장 실시간 스트리밍

---

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/bfa63998-4c68-4ff8-97f7-ce9d5204db6b"/><p>

> 주차기록 조회 옵션 버튼 (전체/금일/현재/위반)

---

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/4103379a-b4aa-4500-856b-c1a6e9bb2e4b"/><p>

> 주차기록 날짜 옵션 조회

---

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/69f83e78-8ac9-45a0-a8f7-e545078ee124"/><p>

> 차량 정보 데이터베이스 조회

---

## 8. 회고

### 주차/출차 판단

초음파센서(HC-SR04)에서 Trig로 초음파를 보낸 후 Echo에 전압이 들어올 때의 시간, 초음파가 물체에 반사되어 돌아오는 것을 감지한 후 Echo의 전압이 나갈 때의 시간 이 둘의 시간 차이와 초음파의 속도(340m/s)를 거리 = 속력 x 시간에 적용하여 거리를 측정하였음.<p><p>
센서 앞에 오는 차량의 거리를 측정하여 일정 거리 이하일 때 주차 상태로 판단하도록 하였으며 주차 판단 이후에는 다시 측정을 시작하여 일정 거리 이상으로 측정되는 경우 출차 상태로 판단하도록 하였음.

---

### 이미지 프로세싱을 활용한 번호판 인식

OpenCV 라이브러리를 활용하여 차량 이미지에 대해 그레이 스케일을 적용하고 노이즈 제거 후 contour를 찾아 이 중 사이즈, 각도, 비율, 위치, 개수를 고려하여 번호판일 확률이 높은 contour의 나열을 번호판 영역으로 인식하도록 하였음.<p><p>
Tesseract-OCR 엔진을 Python에서 사용할 수 있도록 해주는 pytesseract 라이브러리를 활용하여 번호판 영역의 글자를 인식하고 인식한 글자 결과에 대하여 번호판에 올 수 있는 한글로만 이루어져 있는지, 숫자를 포함하고 있는지, 
글자 수는 적절한지를 체크하여 번호판 인식 결과로 차량번호를 반환하도록 하였음.

---

### 웹캠 연결

Raspberry Pi에 연결한 웹캠이 제대로 동작하지 않는 문제가 있었음. 전력이 부족한 것이 원인이라고 생각하여 유전원 USB 허브를 사용하였으나 여전히 웹캠 4개 중 임의로 1개 이상의 웹캠이 처음부터 전혀 동작하지 않거나 동작 중 갑자기 꺼지는 경우가 발생하였음.<p><p>
Raspberry Pi 하나에 총 4개의 웹캠과 1개의 스피커를 연결하였는데 유전원 USB 허브를 사용하더라도 하나의 기기에 너무 많은 장치를 연결하면서 인식 문제와 전력 문제가 발생한 것으로 생각됨. 따라서 Raspberry Pi 하나가 감당할 수 있는 만큼 장치를 연결한다면 이 문제를 해결할 수 있을 것으로 생각하고 있음.

---

### 번호판 차량번호 인식률

같은 차량임에도 사진마다 번호판 인식 결과(차량번호)가 달라지는 문제가 있었음. 인식률은 사진의 품질에 영향을 받는데 웹캠으로 캡처한 사진은 상황에 따라 품질이 일정하지 않았고 이 때문에 인식 결과 또한 일정하지 않은 걸로 생각됨. 이를 해결하기 위해 차량의 사진을 10회 캡처하고 캡처한 모든 사진에 대하여 총 10번의 인식 과정을 거치도록 한 후 나온 결과를 모두 체크하여 가장 많이 나온 차량번호를 최종 결과로 선택하도록 코드를 수정하였음. 이를 통해 품질이 좋지 않은 사진의 인식 결과를 다른 사진의 결과로 대체할 수 있었고 인식률을 높일 수 있었음.<p><p>
하지만 여전히 차량번호 인식이 100% 완벽하지 않다는 문제점이 있었음. 최악의 경우 10번의 인식 결과가 모두 틀리는 경우도 발생했으며 인식에 성공하더라도 100%에 가까운 인식률로 인식 과정을 1번만 거치는 경우와 비교하면 인식 속도가 크게 떨어지는 단점이 있었음. 결국 근본적으로 이 문제를 해결하려면 pytesseract의 인식률 자체를 향상시킬 필요가 있었음. 번호판과 차량번호의 기준을 정해놓고 동일한 규격으로 traineddata를 학습시킨다면 차량번호 인식률을 크게 높일 수 있을 것으로 생각하고 있음.

---

### 주차 후 번호판 인식까지 걸리는 시간

주차 후 번호판 인식까지 걸리는 시간이 너무 오래 걸리는 문제가 있었음. 이를 해결하기 위해 주차 판단 시 초음파센서로 2번 측정하도록 작성한 코드를 1번만 측정하도록 수정하였고 이를 통해 시간을 조금 줄일 수 있었음.<p><p>
하지만 여전히 시간이 필요 이상으로 걸리는 문제가 있었음. 이를 해결하기 위해 모든 사진에 대하여 총 10번의 인식 과정을 거치게 했던 기존 코드를 인식 결과가 조건에 맞지 않을 경우에만 다음 사진으로 넘어가 인식 과정을 수행하도록 수정하였음. 즉 이전에는 모든 사진에 대하여 반드시 10번의 인식 과정을 수행했다면 수정 후에는 최소 1번에서 최악의 경우일 때만 최대 10번까지 인식 과정을 수행하도록 하였음. 이를 통해 주차 후 번호판 인식까지 걸리는 시간을 크게 줄일 수 있었음.<p><p>

---
