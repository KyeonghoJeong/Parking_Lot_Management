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
	String search_violation = "select * from parking_record where violation = '����'";
	
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

			// ���� ���� �� ������(Ŭ���̾�Ʈ)���κ��� ������ ��ȣ, ���� ��ȣ, ���� �ð� ����
			String parking_lot_number = br.readLine();
			String parked_car_license_plate_number = br.readLine();
			String parking_in_date = br.readLine();
			String parking_out_date;
			String response_from_violator;
			
			// ���� ���� ���� ��ȣ�� ���� ���� ���̺��� ��ȸ�Ͽ� ����� ���� Į�� ������ �ޱ� 
			String search = "select handicap_check from car_information where license_plate_number"+"="+"'"+parked_car_license_plate_number+"'";
			ResultSet rs;
			rs = stmt.executeQuery(search);
			
			String result = "";
			
			while(rs.next()) {
				result = rs.getString("handicap_check");
			}
			
			// ���� ���� ���� ��ȣ�� ���� ���� ���̺��� ��ȸ�Ͽ� ��� ���� Į�� ������ �ޱ� 
			String search_registration = "select registration from car_information where license_plate_number"+"="+"'"+parked_car_license_plate_number+"'";
			rs = stmt.executeQuery(search_registration);
			
			String result_registration = "";
			
			while(rs.next()) {
				result_registration = rs.getString("registration");
			}

			// ����� ���� Į�� �����Ͱ� ������̰ų� ��������̸鼭 1�� ������(�Ϲ� ������)�� ��� ���� ���
			if(result.equals("�����") || (result.equals("�������") && parking_lot_number.equals("1"))) {
				// ������ ��ȣ, ���� ��ȣ, ���� ��¥, ��� ���� ���θ� record �޼ҵ带 ���� ���� ��� ���̺� ����ϰ� refresh �޼ҵ带 ���� ���̺��� �����Ѵ� 
				record(stmt, parking_lot_number, parked_car_license_plate_number, parking_in_date, result, result_registration);
				
				refresh(stmt, __dtm_all, search_all);
				refresh(stmt, __dtm_today, search_today);
				refresh(stmt, __dtm_now, search_now);
				refresh(stmt, __dtm_violation, search_violation);
				
				// ������(Ŭ���̾�Ʈ)���� pass��� ��Ʈ�� �����͸� ������ video �޼ҵ带 ���� ���� ������ �������� �����Ѵ�
				pw.print("pass");
				pw.flush();
				
				video(br, pw, parking_lot_number, parked_car_license_plate_number, parking_in_date);
				
				// ������(Ŭ���̾�Ʈ)���� ���� ���� �� ���� �ð��� ������ �����Ѵ� �� �� ������ ������ �ش� ������ ������ modify �޼ҵ带 ���� ���� �ð��� db�� ����ϰ� refresh �޼ҵ�� ���̺��� �����Ѵ� 
				parking_out_date = br.readLine();
				
				modify(stmt, parking_lot_number, parked_car_license_plate_number, parking_in_date, parking_out_date);
			
				refresh(stmt, __dtm_all, search_all);
				refresh(stmt, __dtm_today, search_today);
				refresh(stmt, __dtm_now, search_now);
				refresh(stmt, __dtm_violation, search_violation);
				
				_client.close();
			}
			else {
				// ���� �ش��ϴ� ��찡 �ƴϸ� ���� ������ ���� �õ��� �����Ѵ�
				String message = "";
							
				if (result.equals("")) {
					// ����� ���� Į���� ��ȸ ����� ������ db�� ��ϵ� ������ �ƴϹǷ� ���繰�� ó���ϰ� ������(Ŭ���̾�Ʈ)���� load violation�̶�� ��Ʈ�� �����͸� ������
					parked_car_license_plate_number = "���繰";
					
					message = "���繰";
					
					pw.print("load violation");
					pw.flush();
				}
				else {
					// ��ȸ ����� ������ ���� �����̹Ƿ� ������(Ŭ���̾�Ʈ)���� handicap violation�̶�� ��Ʈ�� �����͸� ������
					message = "���� ����";
					
					pw.print("handicap violation");
					pw.flush();
				}
				
				// ������(Ŭ���̾�Ʈ)���� ��� �� �ٽ� �ѹ� ���� �Ǵ��� �ϰ� ���� ����� ������ ������ �����Ѵ�	
				response_from_violator = br.readLine();
				
				if(response_from_violator.equals("����")) {
					// ���� ������ ��Ʈ�� �����Ͱ� "����"�� ��� record �޼ҵ�� ���� ���� ���̺� ���� ������ ����ϰ� check �޼ҵ带 ���� ���� ���׿� �����̶�� ����Ѵ� �� �� refresh �޼ҵ�� ���̺��� �����Ѵ�
					record(stmt, parking_lot_number, parked_car_license_plate_number, parking_in_date, result, result_registration);
					check(stmt, parking_lot_number, parked_car_license_plate_number, parking_in_date, result, result_registration);
					
					refresh(stmt, __dtm_all, search_all);
					refresh(stmt, __dtm_today, search_today);
					refresh(stmt, __dtm_now, search_now);
					refresh(stmt, __dtm_violation, search_violation);
					
					// �����忡 ���繰 �Ǵ� ���� ������ ������ ���α׷� ����ڿ��� �˸���
					Toolkit.getDefaultToolkit().beep();
					//JOptionPane.showMessageDialog(null, ""+parking_lot_number+"�� �����忡 " + message + "�� �ֽ��ϴ�", "�˸�", JOptionPane.WARNING_MESSAGE);
					
					// video �޼ҵ�� �������� �� ��Ȳ�� ���������� �ް� ���� ���� �Ǵ� ���繰�� �����忡�� ������ ���� �ð��� �����ϰ� modify �޼ҵ�� ����� �� refresh �޼ҵ�� ���̺��� �����Ѵ�
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
			// ������ ��ȣ, ���� ��ȣ, ���� �ð�, ����� ����, ��� ���θ� �Ű������� �޾� ���� ��� ���̺� ����Ѵ�
			String rec = "insert into parking_record (parking_lot_number, parked_car_license_plate_number, parking_in_date, handicap, registration) values ("+parking_lot_number+",'"+parked_car_license_plate_number+"','"+parking_in_date+"','"+handicap+"','"+registration+"')"; 
			stmt.executeUpdate(rec);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void refresh (Statement stmt, DefaultTableModel dtm, String search) {
		// ������ ���� ������ ����
		while(dtm.getRowCount() > 0) {
			dtm.removeRow(0);
		}
		
		// search�� �Է� ���� �������� ���� ��ü, ����, ����, ���ݿ� �ش��ϴ� ���� ����� Į�� ������ �ް� �ش� ���̺� ���� �߰��Ѵ�
		try {
			ResultSet rs = stmt.executeQuery(search);
			
			// ���ǿ� �ش��ϴ� ��� ���� ����� ���̺� ����ϱ� ���� while �� �ȿ� ������ �����Ͽ� �ʱ�ȭ�ϰ� addRow�� �д�
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
					// ���� �ð� ��ȸ�� �� �Ǵ� ��� (���� ���� ����� ó�� ��� ��) ������ ���� �ð��� ��Ʈ�� �����ͷ� ��ȯ �Ͽ� ��ü�� ��Ʈ�� ������ ���� �߰��Ѵ�
					String _pod = df.format(pod);
					String[] data = {pln, pclpn, _pid, _pod, h, v, r, f};
					dtm.addRow(data);
				} 
				else {
					// ���� �ð� ��ȸ�� �Ǵ� ��� ������ ���� �ð��� ��¥ Ÿ�� �״��, �������� ��Ʈ�� ������ �� �߰��Ѵ�
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
			// ������(Ŭ���̾�Ʈ)���� ���� ������ ũ�⸦ ���� �޴´� ���� �ð� ������ yyyyMMddHHmmss���� �ٲ۴�
			String video_size = br.readLine();
			
			pw.print("Server received video size");
			pw.flush();
			
			Date pid = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(parking_in_date);
			SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			parking_in_date = transFormat.format(pid);
			
			// ������ ��ȣ - ���� �ð� - ���� ��ȣ�� ������ ������ �̸��� ����� DataInputStream���� ��Ʈ���� �ް� FileOutputStream���� ������ �̸��� ���Ͽ� ������ �����͸� ����
			DataInputStream dis = new DataInputStream(_client.getInputStream());
			FileOutputStream fos = new FileOutputStream("+parking_lot_number+" - "+parking_in_date+" - "+parked_car_license_plate_number+".avi");
			
			// 1����Ʈ �� ���Ͽ� ���ٰ� ���� ���̰� 17(�ڿ� ���� ��Ʈ�� ������)�� �Ǹ� ���⸦ �����
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
			// ���� �ð� ���� �� ������ �����͸� �̿��Ͽ� �ش��ϴ� ���̺��� �࿡ ���� �ð��� �߰��Ѵ�
			String mod = "update parking_record set parking_out_date = '"+parking_out_date+"' where parking_lot_number = "+parking_lot_number+" and parked_car_license_plate_number = '"+parked_car_license_plate_number+"' and parking_in_date = '"+parking_in_date+"'"; 
			stmt.executeUpdate(mod);
			
			// ���� ��ȣ�� �̿��Ͽ� �ش� ������ �����忡 ��ϵ� �������� ��ȸ�Ѵ�
			String cal = "select * from car_information where license_plate_number"+"="+"'"+parked_car_license_plate_number+"'";
			ResultSet rs = stmt.executeQuery(cal);
			
			String reg = "";
			
			while(rs.next()) {
				reg = rs.getString("registration");
			}
			
			if (reg.equals("�̵��")) {
				// �����忡 ��ϵ� ������ �ƴ� ��� ����� ����Ѵ� ���� �ð��� ���� �ð��� ����ȯ�ϰ� �ð� ���̸� ���Ͽ� �ð��� ����� ���Ѵ�
				SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date pid = transFormat.parse(parking_in_date);
				Date pod = transFormat.parse(parking_out_date);
			
				long time_difference = pod.getTime() - pid.getTime();

				// �����ֱ� �� ���� 1�� ������ 2��
				long sec = time_difference / 1000;
				int fee = (int)(long)sec * 2;
				
				/* ���� �� ���� 3õ�� 30�� ���� +3000��
			    long calDateDays = time_difference / 60000; 
			    //calDateDays = Math.abs(calDateDays);
			    int fee = (((int)(long)(calDateDays) / 30) + 1) * 3000;
			    */
				
				// ��� ��� �� ������ �����͸� �̿��Ͽ� �ش��ϴ� ���̺��� ��� Į���� ����� ����Ѵ�
			    String mod2 = "update parking_record set fee = '"+fee+"' where parking_lot_number = "+parking_lot_number+" and parked_car_license_plate_number = '"+parked_car_license_plate_number+"' and parking_in_date = '"+parking_in_date+"' and parking_out_date = '"+parking_out_date+"'"; 
				stmt.executeUpdate(mod2);
			}
			
		} catch (SQLException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void check(Statement stmt, String parking_lot_number, String parked_car_license_plate_number, String parking_in_date, String handicap, String registration) {
		try {
			// ������ ��ȣ, ���� ��ȣ, ���� �ð�, ����� ����, ��� ���η� ����ϰ��� �ϴ� ���� ��Ͽ� ���� ������ ����Ѵ�
			String che = "update parking_record set violation = '����' where parking_lot_number = "+parking_lot_number+" and parked_car_license_plate_number = '"+parked_car_license_plate_number+"' and parking_in_date = '"+parking_in_date+"' and handicap = '"+handicap+"' and registration = '"+registration+"'"; 
			stmt.executeUpdate(che);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
