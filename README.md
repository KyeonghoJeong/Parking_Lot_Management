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
8. 회고

## 1. 프로젝트 소개

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/a1af6660-f366-4676-8137-401f0dd4e711" width="500"/><br>

#### 줄지 않는 장애인 주차구역 위반 건수 2018년 414,409건 (출처: 보건복지부 제출자료)

장애인 전용 주차구역 단속 인력 부족 문제와 민원 신고에 따른 2차 분쟁 위험 문제를 해결하기 위해 장애인 주차구역을 실시간으로 관리할 수 있는 시스템을 개발하고자 함<br>

주차구역 확인 및 기록 관리를 할 수 있으며 위반 차량 판단에 대한 경고, 안내, 기록까지 자동으로 처리되어 관리자가 직접 현장에 갈 필요 없이 프로그램의 여러 기능을 통해 쉽게 주차구역을 관리할 수 있음

## 2. 목표

- 프로그래밍 언어, 데이터베이스, 네트워크 통신 등 학교에서 배운 전공 지식 활용<br>
- Java Swing GUI 구현<br>
- 소켓 통신, 멀티 스레드 구현<br>
- Python, OpenCV, Tesseract 등 새로운 언어와 툴 학습<br>
- Raspberry Pi를 이용한 사물인터넷 구현<br>
- 이미지 프로세싱을 통한 번호판 인식 구현

## 3. 역할

- #### 정경호
 
    관리자용 PC 프로그램 기능 및 GUI 디자인<br>
    관리자용 PC 프로그램 Java 프로그래밍<br>
    라즈베리파이 동작 과정 설계<br>
    라즈베리파이 Python 프로그래밍<br>

- #### 팀원

    OpenCV & Pytesseract 번호판 인식 및 검출<br>
    모형 디자인 및 제작<br>
    프로그램 디버깅<br>
    프로그램 실행 및 성능 평가<br>
    졸업작품전시회 출품 및 결과 발표<br>
    졸업작품 연구 보고서 작성 및 제출

## 4. 기술 스택

Java, Java Swing, MySQL, Raspberry Pi, Python, OpenCV, Tesseract

## 5. 프로젝트 구조

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/b6537ae0-4e30-45bf-8099-2dab52383b57" width="500"><br>

> 순서도

#### Java 서버
전공 과목으로 수강했던 객체지향프로그래밍을 복습하기 위해 개발 언어로 Java를 선택하였음.<br><br>

구현한 클래스는 총 3개로 a_gui 클래스에서는 GUI 구성 요소와 기능을 구현하고 서버 소켓으로 동작하도록 하였으며 b_server 클래스와 c_manager 클래스는 Runnable 인터페이스를 상속받아 멀티 스레드로 동작하도록 구현하였음. 이를 통해 a_gui 클래스는 GUI 관련 동작과 클라이언트 연결 요청을 둘 다 처리할 수 있게 되었고 b_server 클래스는 각 주차구역(클라이언트) 별로 연결 요청 처리 및 c_manager 클래스의 인스턴스를 생성 할 수 있게 되었음.<br><br>

c_manager 클래스에서는 각 클라이언트와 소켓으로 통신하여 데이터를 주고 받으며 DB 조회, 위반 차량 판단, DB에 주차 기록 저장/수정 및 JTable에 출력 등의 기능을 하도록 구현하였음.

#### GUI
모든 운영체제에 동일한 GUI를 제공하기 위해 네이티브 UI를 사용하지 않는 Swing으로 GUI를 개발하였음.

#### MySQL
로그인, 메뉴별 주차 기록 조회, 차량 정보 등록/조회/수정/삭제, 주차 기록 저장/수정, 위반 여부 기록 등을 구현하는데 있어서 데이터베이스시스템설계 수강 내용을 복습하기 위해 MySQL을 선택하였음.

#### Python 클라이언트
Raspberry Pi 클라이언트(주차구역)는 Java 이외에 새로운 프로그래밍 언어 학습을 위해 Python으로 개발하였으며 주차구역의 기능을 클래스로 구현하고 각 주차구역을 인스턴스로 생성 후 멀티 스레드로 동작하도록 하였음.

#### Raspberry Pi
Raspberry Pi에는 초음파센서(HC-SR04)를 연결하여 거리 변화를 감지 후 주차 및 출차 판단에 활용하였음 또한 웹캠을 연결하여 차량의 사진을 캡처 후 번호판 인식과 비디오 파일 전송에 사용하였으며 LED와 스피커를 연결하여 위반 차량에 대한 경고 알림에 사용하였음.

#### 이미지 프로세싱
OpenCV 라이브러리를 활용하여 차량 이미지에 대해 그레이 스케일을 적용하고 노이즈 제거 후 contour를 찾음.<br><br>

여러 개의 contour 중 사이즈/각도/비율/위치/개수를 고려하여 번호판일 확률이 높은 contour의 나열을 번호판 영역으로 인식함.<br><br>

Tesseract-OCR 엔진을 Python에서 사용할 수 있게 해주는 pytesseract 라이브러리를 활용하여 번호판 영역의 글자를 인식하고 인식한 글자 결과에 대하여 번호판에 올 수 있는 한글로만 이루어져 있는지, 숫자를 포함하고 있는지, 글자 수는 적절한지를 체크하여 번호판 인식 결과를 반환함.

---

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/9a00fffe-adf4-45b7-8aaa-9a706d6579f3" width="800"/><br>

> 주차장 모형 전면

#### 차량 진입 감지
초음파센서로 거리 변화를 감지하여 주차를 판단하고 웹캠과 OpenCV 라이브러리로 차량을 촬영

#### 데이터 전송
클라이언트와 서버가 소켓을 통해 주차구역 번호, 주차시간, 차량번호, 위반 여부, 동영상을 주고받음

#### 일반 차량
위반 사항이 없으면 LED를 초록색으로 점등하고 서버는 주차 기록을 출력

#### 위반 차량
LED 점멸과 음성 경고 후 일정 시간이 지난 뒤에도 그대로 주차구역 위에 있을 경우에는 LED를 빨간색으로 점등하고 서버는 주차 기록을 출력

#### 출차 판단
다시 초음파센서를 동작시켜 거리 변화를 감지해 출차를 판단하고 출차로 판단되면 출차 시간을 서버로 전송하고 서버는 이를 출력

---

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/f9724c4c-1607-46c8-b9a6-38bae6d9a248" width="800"/><br>

> 주차장 모형 후면

라즈베리파이에 초음파센서, 웹캠, 스피커, LED를 연결하여 차량 진입 감지, 사진 및 동영상 촬영, 위반 차량 및 적재물 판단, 음성 경고 및 LED 안내, 실시간 스트리밍을 실시함<br>

라즈베리파이는 촬영한 사진으로부터 번호판 영역을 검출하고 서버로 차량번호를 전송, 서버는 DB에 저장되어 있는 해당 번호의 장애인 등록 여부를 확인하여 위반 차량을 판단함

## 6. 주요 기능

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/6d66b3b0-9267-4718-9fd7-b004ffc48ce0" width="500"/><br>

> 관리자 프로그램 (Java 서버)

- 로그인 기능

---

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/011f207b-b719-40bf-a92b-42ceb97fd17e" width="800"/><br>

> 관리자 프로그램 (Java 서버)

- 위반 차량 판단
- 주차장 전체 스트리밍 재생
- 주차 기록 차량 정보 조회 및 주차 당시 동영상 재생
- 전체/금일/현재/위반/특정 날짜 별 주차 기록 조회
- 차량 정보 조회/등록/수정/삭제

---

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/f43f1365-b712-49e4-b348-cdf26545298d" width="500"/><br>

> 주차구역 (Raspberry Pi Python 클라이언트)

- 주차 판단
- 번호판 인식
- LED 및 음성 메시지 알림
- 출차 판단

## 7. 데모 영상

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/bb201f6c-ee81-4faa-a223-bd2a79efbcc8"/><br>

> 관리자 프로그램 로그인

---

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/9bf6de1a-e2f8-43e0-8696-94eb36addc02"/><br>

> 장애인 주차구역 차량 진입

---

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/8c580442-f367-45fa-83eb-fc1e416f0920"/><br>

> 번호판 인식 및 위반 차량 식별

---

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/1fa7f881-9959-4f41-900a-12a821fd7f64"/><br>

> 위반 차량 영상 및 정보 확인

---

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/aef21537-26af-41ae-bcfa-7fb73e077c28"/><br>

> 주차구역 실시간 스트리밍

---

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/bfa63998-4c68-4ff8-97f7-ce9d5204db6b"/><br>

> 주차기록 조회 옵션 버튼 (전체/금일/현재/위반)

---

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/4103379a-b4aa-4500-856b-c1a6e9bb2e4b"/><br>

> 주차기록 날짜 옵션 조회

---

<img src="https://github.com/KyeonghoJeong/Parking_Lot_Management/assets/128965451/69f83e78-8ac9-45a0-a8f7-e545078ee124"/><br>

> 차량 정보 데이터베이스 조회

---

## 9. 회고

#### 웹캠 연결 문제

프로젝트 진행 중 웹캠이 제대로 동작하지 않는 문제가 있었음.<br>

전력이 부족한 것이 원인이라고 생각하여 유전원 USB 허브를 연결하여 이 문제를 해결하고자 함.<br>

그러나 유전원 USB 허브를 연결한 후에도 여전히 웹캠 4개 중 임의로 1개 이상의 웹캠이 동작 중 갑자기 꺼지는 현상이 발생하였으며 처음부터 동작이 안 되는 경우도 발생하였음.<br>

이 문제점을 개선하기 위해서는 우선 하나의 Raspberry Pi에 연결하는 USB 장치의 수를 제한하는 방법이 있음. 이번 프로젝트에서는 Raspberry Pi 하나에 USB 허브 2개를 이용해 총 4개의 웹캠과 1개의 스피커를 연결하였는데 유전원 USB 허브를 통해 연결했음에도 총 5개의 USB 장치를 연결한 만큼 라즈베리파이 내에서 인식 문제와 전력 문제가 있었던 것으로 생각 됨<br>

따라서 라즈베리파이 1개가 감당할 수 있는 만큼 USB 장치를 연결한다면 이 문제를 해결할 수 있을 것으로 생각하고 있음

#### 번호판 차량번호 인식률 문제

번호판과 차량번호의 인식률은 사진의 품질에 영향을 받는데 웹캠으로 캡처한 사진은 상황에 따라 품질이 일정하지가 않아 같은 차량임에도 사진마다 인식 결과가 달라지는 문제가 있었음. 또한 번호판 영역으로부터 pytesseract로 인식한 차량번호도 결과가 일정하게 나오지 않는 문제가 있었음<br>

문제점을 해결하기 위해 차량의 사진을 10회 캡처하고 캡처한 모든 사진에 대하여 10번의 인식 과정을 거치도록 한 후 나온 결과를 모두 취합하여 가장 많이 나온 차량번호를 결과로 선택하도록 코드를 수정하였음. 사진을 10회 캡처함으로써 품질이 좋지 않은 사진의 인식 결과를 다른 사진의 결과로 대체할 수 있었고 인식률을 높일 수 있었음<br>

하지만 여전히 차량번호 인식이 100% 완벽하지 않다는 문제점이 있었음. 최악의 경우로 10번의 인식 결과가 모두 틀리면 위반 차량 판단을 할 수 없는 상황이 발생하였으며 인식에 성공하더라도 100%에 가까운 인식률로 인식 과정을 1번만 거치는 경우와 비교하면 인식 속도가 크게 떨어지는 문제가 있었음<br>

번호판 영역에 대한 검출은 OpenCV 라이브러리를 통한 이미지 프로세싱으로 큰 문제가 없었지만 대부분의 인식 오류는 번호판 영역 내의 한글 차량번호를 인식하는 데서 발생하였으므로 이 문제점을 개선 할 수 있는 방안으로는 pytesseract에서 참조하는 기존 한글 traineddata를 이번 작품의 목적에 맞게 학습시켜 한글 문자 인식률을 높이는 방법이 있음.<br>

따라서 번호판의 크기와 비율, 차량번호의 크기와 폰트에 대해 기준을 정해놓고 동일한 규격으로 여러 번호판을 제작하여 traineddata를 학습시킨다면 인식률을 크게 높일 수 있을 것으로 생각하고 있음

#### 차량 진입 후 인식 과정을 거쳐 위반 차량 판단까지 걸리는 시간 문제

차량이 주차구역에 진입 시 주차 판단부터 번호판 인식을 거쳐 위반 차량 판단까지 걸리는 시간이 너무 오래걸리는 문제가 있었음<br>

인식률을 높이기 위해 10회 차량 사진을 캡처하고 최대 10번의 인식 과정을 거치도록 한 것이 원인이라고 생각 했으나 정확한 동작을 위해서는 인식률이 가장 중요하다고 생각하여 최대한 다른 방법으로 위반 차량 판단까지 걸리는 시간을 줄이려고 시도하였음<br>

주차 상황 판단을 확실히 하기위해서 초음파센서로 2번 측정하도록 작성한 코드를 1번만 측정하도록 수정하였고 이 방법으로 최종 판단까지 걸리는 시간을 약간 줄일 수는 있었지만 역시 인식 과정에서 걸리는 시간이 너무 길었음<br>

결국에는 인식 과정과 관련한 코드를 수정하기로 하였고 사진 캡처 횟수를 10회 미만으로도 줄여보기로 하였음. 횟수를 줄일수록 인식까지 걸리는 시간은 확실히 줄어들었지만 그만큼 인식률이 떨어졌음<br>

생각을 조금 바꾸어 10회 캡처하되 처음부터 10개 사진 전부에 대하여 총 10번의 인식 과정을 거치게 하는 것이 아니라 첫 번째 캡처 사진에서 먼저 번호판과 차량번호를 인식하고 결과가 조건에 맞지 않을 때에만 다음 사진으로 넘어가 인식 과정을 거치도록 코드를 수정하였음. 즉 이전에는 모든 사진에 대하여 무조건 10번의 인식 과정을 수행했다면 수정 후에는 최소 1번에서 최악의 경우일 때만 최대 10번까지 인식 과정을 수행하도록 하였음. 결과적으로 인식률을 유지하면서 동시에 위반 차량 판단까지 걸리는 시간을 크게 줄일 수 있었음
