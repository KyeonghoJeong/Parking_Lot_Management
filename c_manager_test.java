package parking_lot_management_test;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class c_manager_test implements Runnable{
	Socket _client;

	DefaultTableModel __dtm_all, __dtm_today, __dtm_now, __dtm_violation;
	
	String search_all = "select * from parking_record";
	String search_today = "select * from parking_record where parking_in_date >= curdate() and parking_in_date < date_add(curdate(), interval 1 day)";
	String search_now = "select * from parking_record where parking_out_date is null";
	String search_violation = "select * from parking_record where violation = '위반'";
	
	public c_manager_test (Socket client, DefaultTableModel _dtm_all, DefaultTableModel _dtm_today, DefaultTableModel _dtm_now, DefaultTableModel _dtm_violation) {
		_client = client;

		__dtm_all = _dtm_all;
		__dtm_today = _dtm_today;
		__dtm_now = _dtm_now;
		__dtm_violation = _dtm_violation;
	}

	public void run() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/parking_lot_management?serverTimezone=Asia/Seoul&useSSL=false", "root", "0000");
			Statement stmt = conn.createStatement();
			
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
							_client.getInputStream(), "UTF-8"
					)
			);
		
			PrintWriter pw = new PrintWriter(_client.getOutputStream(), true);

			// 주차 감지 후 주차장(클라이언트)으로부터 주차장 번호, 차량 번호, 주차 시간 수신
			String parking_lot_number = br.readLine();
			String parked_car_license_plate_number = br.readLine();
			String parking_in_date = br.readLine();
			String parking_out_date;
			String response_from_violator;
			
			// 수신 받은 차량 번호로 차량 정보 테이블을 조회하여 장애인 차량 칼럼 데이터 받기 
			String search = "select handicap_check from car_information where license_plate_number"+"="+"'"+parked_car_license_plate_number+"'";
			ResultSet rs;
			rs = stmt.executeQuery(search);
			
			String result = "";
			
			while(rs.next()) {
				result = rs.getString("handicap_check");
			}
			
			// 수신 받은 차량 번호로 차량 정보 테이블을 조회하여 등록 차량 칼럼 데이터 받기 
			String search_registration = "select registration from car_information where license_plate_number"+"="+"'"+parked_car_license_plate_number+"'";
			rs = stmt.executeQuery(search_registration);
			
			String result_registration = "";
			
			while(rs.next()) {
				result_registration = rs.getString("registration");
			}

			// 장애인 차량 칼럼 데이터가 장애인이거나 비장애인이면서 1번 주차장(일반 주차장)인 경우 주차 허용
			if(result.equals("장애인") || (result.equals("비장애인") && parking_lot_number.equals("1"))) {
				// 주차장 번호, 차량 번호, 주차 날짜, 등록 차량 여부를 record 메소드를 통해 주차 기록 테이블에 기록하고 refresh 메소드를 통해 테이블을 갱신한다 
				record(stmt, parking_lot_number, parked_car_license_plate_number, parking_in_date, result, result_registration);
				
				refresh(stmt, __dtm_all, search_all);
				refresh(stmt, __dtm_today, search_today);
				refresh(stmt, __dtm_now, search_now);
				refresh(stmt, __dtm_violation, search_violation);
				
				// 주차장(클라이언트)으로 pass라는 스트링 데이터를 보내고 video 메소드를 통해 주차 차량의 동영상을 수신한다
				pw.print("pass");
				pw.flush();
				
				video(br, pw, parking_lot_number, parked_car_license_plate_number, parking_in_date);
				
				// 주차장(클라이언트)에서 출차 감지 후 출차 시간을 보내면 수신한다 그 후 이전의 수신한 해당 차량의 정보와 modify 메소드를 통해 출차 시간을 db에 기록하고 refresh 메소드로 테이블을 갱신한다 
				parking_out_date = br.readLine();
				
				modify(stmt, parking_lot_number, parked_car_license_plate_number, parking_in_date, parking_out_date);
			
				refresh(stmt, __dtm_all, search_all);
				refresh(stmt, __dtm_today, search_today);
				refresh(stmt, __dtm_now, search_now);
				refresh(stmt, __dtm_violation, search_violation);
				
				_client.close();
			}
			else {
				// 위에 해당하는 경우가 아니면 위반 차량의 주차 시도로 간주한다
				String message = "";
							
				if (result.equals("")) {
					// 장애인 차량 칼럼의 조회 결과가 없으면 db에 등록된 차량이 아니므로 적재물로 처리하고 주차장(클라이언트)으로 load violation이라는 스트링 데이터를 보낸다
					parked_car_license_plate_number = "적재물";
					
					message = "적재물";
					
					pw.print("load violation");
					pw.flush();
				}
				else {
					// 조회 결과는 있으나 위반 차량이므로 주차장(클라이언트)으로 handicap violation이라는 스트링 데이터를 보낸다
					message = "위반 차량";
					
					pw.print("handicap violation");
					pw.flush();
				}
				
				// 주차장(클라이언트)에서 경고 후 다시 한번 주차 판단을 하고 주차 결과를 서버로 보내면 수신한다	
				response_from_violator = br.readLine();
				
				if(response_from_violator.equals("위반")) {
					// 만약 수신한 스트링 데이터가 "위반"일 경우 record 메소드로 주차 정보 테이블에 주차 정보를 기록하고 check 메소드를 통해 위반 사항에 위반이라고 기록한다 그 후 refresh 메소드로 테이블을 갱신한다
					record(stmt, parking_lot_number, parked_car_license_plate_number, parking_in_date, result, result_registration);
					check(stmt, parking_lot_number, parked_car_license_plate_number, parking_in_date, result, result_registration);
					
					refresh(stmt, __dtm_all, search_all);
					refresh(stmt, __dtm_today, search_today);
					refresh(stmt, __dtm_now, search_now);
					refresh(stmt, __dtm_violation, search_violation);
					
					// 주차장에 적재물 또는 위반 차량이 있음을 프로그램 사용자에게 알린다
					Toolkit.getDefaultToolkit().beep();
					//JOptionPane.showMessageDialog(null, ""+parking_lot_number+"번 주차장에 " + message + "이 있습니다", "알림", JOptionPane.WARNING_MESSAGE);
					
					// video 메소드로 주차장의 현 상황을 동영상으로 받고 위반 차량 또는 적재물이 주차장에서 빠지면 빠진 시간을 수신하고 modify 메소드로 기록한 후 refresh 메소드로 테이블을 갱신한다
					video(br, pw, parking_lot_number, parked_car_license_plate_number, parking_in_date);
					
					parking_out_date = br.readLine();
					
					modify(stmt, parking_lot_number, parked_car_license_plate_number, parking_in_date, parking_out_date);

					refresh(stmt, __dtm_all, search_all);
					refresh(stmt, __dtm_today, search_today);
					refresh(stmt, __dtm_now, search_now);
					refresh(stmt, __dtm_violation, search_violation);
					
					_client.close();
				}
				
				_client.close();
			}
			
		} catch (IOException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void record(Statement stmt, String parking_lot_number, String parked_car_license_plate_number, String parking_in_date, String handicap, String registration) {
		try {
			// 주차장 번호, 차량 번호, 주추 시간, 장애인 여부, 등록 여부를 매개변수로 받아 주차 기록 테이블에 기록한다
			String rec = "insert into parking_record (parking_lot_number, parked_car_license_plate_number, parking_in_date, handicap, registration) values ("+parking_lot_number+",'"+parked_car_license_plate_number+"','"+parking_in_date+"','"+handicap+"','"+registration+"')"; 
			stmt.executeUpdate(rec);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void refresh (Statement stmt, DefaultTableModel dtm, String search) {
		// 마지막 행의 데이터 삭제
		while(dtm.getRowCount() > 0) {
			dtm.removeRow(0);
		}
		
		// search로 입력 받은 쿼리문에 따라 전체, 금일, 현재, 위반에 해당하는 주차 기록의 칼럼 정보를 받고 해당 테이블에 행을 추가한다
		try {
			ResultSet rs = stmt.executeQuery(search);
			
			// 조건에 해당하는 모든 주차 기록을 테이블에 출력하기 위해 while 문 안에 변수를 선언하여 초기화하고 addRow를 둔다
			while(rs.next()) {
				String pln = rs.getString("parking_lot_number");
				String pclpn = rs.getString("parked_car_license_plate_number");
				Date pid = rs.getTimestamp("parking_in_date");
				Date pod = rs.getTimestamp("parking_out_date");
				String h = rs.getString("handicap");
				String v = rs.getString("violation");
				String r = rs.getString("registration");
				String f = rs.getString("fee");
				
				DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
				String _pid = df.format(pid);
				
				if(pod != null) {
					// 출차 시간 조회가 안 되는 경우 (위반 차량 기록을 처음 기록 시) 가져온 출차 시간을 스트링 데이터로 변환 하여 전체를 스트링 형으로 행을 추가한다
					String _pod = df.format(pod);
					String[] data = {pln, pclpn, _pid, _pod, h, v, r, f};
					dtm.addRow(data);
				} 
				else {
					// 출차 시간 조회가 되는 경우 가져온 출차 시간을 날짜 타입 그대로, 나머지는 스트링 형으로 행 추가한다
					Object[] data = {pln, pclpn, _pid, pod, h, v, r, f};
					dtm.addRow(data);
				}  
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void video(BufferedReader br, PrintWriter pw, String parking_lot_number, String parked_car_license_plate_number, String parking_in_date) {
		try {
			// 주차장(클라이언트)으로 부터 동영상 크기를 먼저 받는다 주차 시간 형식을 yyyyMMddHHmmss으로 바꾼다
			String video_size = br.readLine();
			
			pw.print("Server received video size");
			pw.flush();
			
			Date pid = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(parking_in_date);
			SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			parking_in_date = transFormat.format(pid);
			
			// 주차장 번호 - 주차 시간 - 차량 번호로 동영상 파일의 이름을 만든다 DataInputStream으로 스트림을 받고 FileOutputStream으로 지정한 이름의 파일에 동영상 데이터를 쓴다
			DataInputStream dis = new DataInputStream(_client.getInputStream());
			FileOutputStream fos = new FileOutputStream("+parking_lot_number+" - "+parking_in_date+" - "+parked_car_license_plate_number+".avi");
			
			// 1바이트 씩 파일에 쓰다가 남은 길이가 17(뒤에 받을 스트링 데이터)이 되면 쓰기를 멈춘다
			int tot = Integer.parseInt(video_size);
			byte[] buffer = new byte[1];
			int len = 0;
			while ((len = dis.read(buffer)) > 0) {
				tot = tot - len;
				if (tot == 17) {
					break;
				}
			    fos.write(buffer, 0, len);
			}
			fos.close();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void modify(Statement stmt, String parking_lot_number, String parked_car_license_plate_number, String parking_in_date, String parking_out_date) {
		try {
			// 출차 시간 수신 후 이전의 데이터를 이용하여 해당하는 테이블의 행에 출차 시간을 추가한다
			String mod = "update parking_record set parking_out_date = '"+parking_out_date+"' where parking_lot_number = "+parking_lot_number+" and parked_car_license_plate_number = '"+parked_car_license_plate_number+"' and parking_in_date = '"+parking_in_date+"'"; 
			stmt.executeUpdate(mod);
			
			// 차량 번호를 이용하여 해당 차량이 주차장에 등록된 차량인지 조회한다
			String cal = "select * from car_information where license_plate_number"+"="+"'"+parked_car_license_plate_number+"'";
			ResultSet rs = stmt.executeQuery(cal);
			
			String reg = "";
			
			while(rs.next()) {
				reg = rs.getString("registration");
			}
			
			if (reg.equals("미등록")) {
				// 주차장에 등록된 차량이 아닌 경우 요금을 계산한다 주차 시간과 출차 시간을 형변환하고 시간 차이를 구하여 시간에 요금을 곱한다
				SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date pid = transFormat.parse(parking_in_date);
				Date pod = transFormat.parse(parking_out_date);
			
				long time_difference = pod.getTime() - pid.getTime();

				// 보여주기 용 라인 1초 주차에 2원
				long sec = time_difference / 1000;
				int fee = (int)(long)sec * 2;
				
				/* 주차 시 부터 3천원 30분 마다 +3000원
			    long calDateDays = time_difference / 60000; 
			    //calDateDays = Math.abs(calDateDays);
			    int fee = (((int)(long)(calDateDays) / 30) + 1) * 3000;
			    */
				
				// 요금 계산 후 이전의 데이터를 이용하여 해당하는 테이블의 요금 칼럼에 요금을 기록한다
			    String mod2 = "update parking_record set fee = '"+fee+"' where parking_lot_number = "+parking_lot_number+" and parked_car_license_plate_number = '"+parked_car_license_plate_number+"' and parking_in_date = '"+parking_in_date+"' and parking_out_date = '"+parking_out_date+"'"; 
				stmt.executeUpdate(mod2);
			}
			
		} catch (SQLException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void check(Statement stmt, String parking_lot_number, String parked_car_license_plate_number, String parking_in_date, String handicap, String registration) {
		try {
			// 주차장 번호, 차량 번호, 주차 시간, 장애인 여부, 등록 여부로 기록하고자 하는 주차 기록에 위반 사항을 기록한다
			String che = "update parking_record set violation = '위반' where parking_lot_number = "+parking_lot_number+" and parked_car_license_plate_number = '"+parked_car_license_plate_number+"' and parking_in_date = '"+parking_in_date+"' and handicap = '"+handicap+"' and registration = '"+registration+"'"; 
			stmt.executeUpdate(che);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
