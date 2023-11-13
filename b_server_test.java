package parking_lot_management_test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.table.DefaultTableModel;

public class b_server_test implements Runnable {
	ServerSocket _server;
   
	DefaultTableModel _dtm_all, _dtm_today, _dtm_now, _dtm_violation;
	
	public b_server_test(ServerSocket server, DefaultTableModel dtm_all, DefaultTableModel dtm_today, DefaultTableModel dtm_now, DefaultTableModel dtm_violatio) {
		_server = server;

		_dtm_all = dtm_all;
		_dtm_today = dtm_today;
		_dtm_now = dtm_now ;
		_dtm_violation = dtm_violatio;
	}
		
	public void run() {
		try {
			while(true) {
				// 주차장(클라이언트) 스레드로 처리
				Socket client = _server.accept();
				Runnable r2 = new c_manager_test(client, _dtm_all, _dtm_today, _dtm_now, _dtm_violation);
				Thread t2 = new Thread(r2);
				t2.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
