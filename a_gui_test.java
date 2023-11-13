package parking_lot_management_test;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

public class a_gui_test {

	private JFrame frame;
	
	ServerSocket server = null;
	
	// video �гΰ� video Ȯ�� �г��� ��ư �ؽ�Ʈ�� ���� �Ǵ� ������� �ٲٴ� �� ����� ���� ����
	int count_video = 0;
	int count_cctv = 0;
	int count_expansion_cctv = 0;
	
	String stream_addr = "00.00.00.00";
	
	String input_modify = "";

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					a_gui_test window = new a_gui_test();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public a_gui_test() {
		initialize();
	}

	private void initialize() {
		start();
	}
	
	public void start() {
		try {
			// db ����
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/parking_lot_management?serverTimezone=Asia/Seoul&useSSL=false&allowPublicKeyRetrieval=true&useSSL=false", "root", "0000");
			Statement stmt = conn.createStatement();
			
			// �α��� ȭ�� ������, �г�, ��, �ؽ�Ʈ�ʵ� ��ġ
			frame = new JFrame("�α���");
			frame.setBounds(0, 0, 456, 319);
			frame.getContentPane().setLayout(null);
			frame.setResizable(false); // ũ�� ���� ���� �Ұ�
			frame.setLocationRelativeTo(null); // ó�� ���� �� �߾� ��ġ
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // ������ �ݱ� ��ư ��Ȱ��ȭ
			ImageIcon icon = new ImageIcon("icon.png");
			frame.setIconImage(icon.getImage());
			
			JPanel panel_login = new JPanel();
			panel_login.setBounds(3, 3, 445, 285);
			panel_login.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			panel_login.setLayout(null);
			frame.getContentPane().add(panel_login);
			
			JLabel label_login = new JLabel("  Login");
			label_login.setBounds(0, 0, 445, 76);
			label_login.setFont(new Font("Arial", Font.BOLD, 50));
			label_login.setForeground(Color.WHITE);
			label_login.setBackground(new Color(54, 57, 76));
			panel_login.add(label_login);
			
			ImageIcon background = new ImageIcon("background.jpg");
			JLabel label_login_background = new JLabel(background);
			label_login_background.setBounds(1, 0, 442, 76);
			panel_login.add(label_login_background);
			
			JLabel label_message = new JLabel("    Enter your ID and password");
			label_message.setBounds(0, 82, 444, 52);
			label_message.setFont(new Font("Arial", Font.BOLD, 20));
			panel_login.add(label_message);
			
			JLabel label_id = new JLabel("            ID");
			label_id.setBounds(0, 139, 133, 29);
			label_id.setFont(new Font("Arial", Font.PLAIN, 16));
			panel_login.add(label_id);
			
			JLabel label_password = new JLabel("            Password");
			label_password.setFont(new Font("Arial", Font.PLAIN, 16));
			label_password.setBounds(0, 187, 133, 29);
			panel_login.add(label_password);

			JTextField textfield_id = new JTextField();
			textfield_id.setBounds(139, 139, 285, 29);
			textfield_id.setFont(new Font("Arial", Font.PLAIN, 16));
			panel_login.add(textfield_id);
			
			JTextField textfield_password = new JPasswordField();
			textfield_password.setBounds(139, 187, 285, 29);
			textfield_password.setFont(new Font("Arial", Font.PLAIN, 16));
			panel_login.add(textfield_password);

			JButton button_ok = new JButton("OK");
			button_ok.setBounds(163, 235, 120, 30);
			button_ok.setBackground(new java.awt.Color(238, 238, 238));
			button_ok.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			button_ok.setFont(new Font("Arial", Font.PLAIN, 14));
			button_ok.setFocusPainted(false);
			panel_login.add(button_ok);
			
			JButton button_cancel = new JButton("Cancel");
			button_cancel.setBounds(305, 235, 118, 30);
			button_cancel.setFont(new Font("Arial", Font.PLAIN, 14));
			button_cancel.setBackground(new java.awt.Color(238, 238, 238));
			button_cancel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			button_cancel.setFocusPainted(false);
			panel_login.add(button_cancel);
			
			// ���� Ű -> ENTER �׼� -> login �׼� -> main �޼ҵ� ȣ��� ����Ű�� ���� �α��� �� �� �ְ� ��
			Action login = new AbstractAction() {
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent arg0) {
					main(stmt, panel_login, textfield_id, textfield_password);
				}
			};
		 
			KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
			textfield_id.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, "ENTER");
			textfield_id.getActionMap().put("ENTER", login);
			textfield_password.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, "ENTER");
			textfield_password.getActionMap().put("ENTER", login);
			
			// ���� Ű �Ӹ� �ƴ϶� ���콺�� ok ��ư�� Ŭ�������ν� �α��� �� �� ����
			button_ok.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					main(stmt, panel_login, textfield_id, textfield_password);
				}
			});
			
			// cancel ��ư Ŭ�� �� ���α׷� ����
			button_cancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void main(Statement stmt, JPanel panel_login, JTextField textfield_id, JTextField textfield_password) {
		if(login(stmt, textfield_id.getText(), textfield_password.getText()) == 1) {
			ImageIcon icon = new ImageIcon("icon.png");
			// �Է� ���� id�� ��й�ȣ�� login �޼ҵ�� ������ db ��ȸ �� ��ġ�ϸ� �α��� �г��� ���߰� ���� ������ ������ �缳��
			panel_login.setVisible(false);

			frame.setTitle("����� �������� ���� ���� ���α׷�");
			frame.setBounds(0, 0, 1084, 583);
			frame.setLocationRelativeTo(null);
			
			// ���� Ŭ�� �� ��� ������, �г�, ������Ʈ, ��ư ����
			JFrame f_cctv = new JFrame("CCTV");
			f_cctv.setBounds(0, 0, 874, 523);
			f_cctv.setLayout(null);
			f_cctv.setResizable(false);
			f_cctv.setLocationRelativeTo(null);
			f_cctv.setVisible(false);
			f_cctv.setIconImage(icon.getImage());
			
			EmbeddedMediaPlayerComponent empc_expansion_cctv = new EmbeddedMediaPlayerComponent();
			empc_expansion_cctv.setBounds(4, 4, 854, 480);
			
			// Ȯ�� cctv vlcj ������Ʈ�� count_expansion_cctv ���� ���� �����Ͽ� ����� ȭ���� �ٲ���
			empc_expansion_cctv.videoSurfaceComponent().addMouseListener(new MouseAdapter() {
			    public void mouseClicked(MouseEvent e) {
			    	if (e.getClickCount() == 2) {
			    		if (count_expansion_cctv == 1) {
			    			empc_expansion_cctv.mediaPlayer().media().play("default.png");
			    			empc_expansion_cctv.mediaPlayer().controls().setRepeat(true);
							
							count_expansion_cctv = 0;
						} else {
							empc_expansion_cctv.mediaPlayer().media().play("http://" + stream_addr + ":8000/?action=stream");

							count_expansion_cctv = 1;
						}
			    	}
			    }
			});
			
			JPanel p_cctv = new JPanel();
			p_cctv.setBounds(3, 3, 863, 489);
			p_cctv.setLayout(null);
			p_cctv.setBackground(new Color(238, 238, 238));
			p_cctv.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			p_cctv.add(empc_expansion_cctv);
			f_cctv.getContentPane().add(p_cctv);
			
			f_cctv.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					empc_expansion_cctv.mediaPlayer().controls().stop();
					f_cctv.setVisible(false);
				}
			});
			
			// ���̺� ���� Ŭ�� �� ��� ������, �г�, ������Ʈ, ��ư ����
			JFrame f_video = new JFrame("Video");
			f_video.setBounds(0, 0, 874, 523);
			f_video.setLayout(null);
			f_video.setResizable(false);
			f_video.setLocationRelativeTo(null);
			f_video.setVisible(false);
			f_video.setIconImage(icon.getImage());
			
			EmbeddedMediaPlayerComponent empc_expansion_video = new EmbeddedMediaPlayerComponent();
			empc_expansion_video.setBounds(4, 4, 854, 480);
			
			empc_expansion_video.videoSurfaceComponent().addMouseListener(new MouseAdapter() {
			    public void mouseClicked(MouseEvent e) {
			    	if (e.getClickCount() == 2) {
			    		empc_expansion_video.mediaPlayer().controls().pause();
			    	}
			    }
			});
			
			JPanel p_video = new JPanel();
			p_video.setBounds(3, 3, 863, 489);
			p_video.setLayout(null);
			p_video.setBackground(new Color(238, 238, 238));
			p_video.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			p_video.add(empc_expansion_video);
			f_video.getContentPane().add(p_video);
			
			f_video.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					empc_expansion_video.mediaPlayer().controls().stop();
					f_video.setVisible(false);
				}
			});
			
			// ������(Ŭ���̾�Ʈ)�� ��Ʈ������ ����� vlc ������Ʈ�� ������ ����
			EmbeddedMediaPlayerComponent empc_cctv = new EmbeddedMediaPlayerComponent();
			empc_cctv.setBounds(4, 4, 387, 218);
			
			empc_cctv.videoSurfaceComponent().addMouseListener(new MouseAdapter() {
			    public void mouseClicked(MouseEvent e) {
			    	if (e.getClickCount() == 2) {
			    		f_cctv.setVisible(true);
			    		empc_expansion_cctv.mediaPlayer().media().play("http://" + stream_addr + ":8000/?action=stream");
			    		
			    		count_expansion_cctv = 1;
			    	}
			    }
			});
			
			// ��Ʈ������ ����� �г�
			JPanel panel_cctv = new JPanel();
			panel_cctv.setBounds(678, 3, 397, 275);
			panel_cctv.setLayout(null);
			panel_cctv.setBackground(new Color(238, 238, 238));
			panel_cctv.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			panel_cctv.add(empc_cctv);
			frame.getContentPane().add(panel_cctv);
			
			frame.setVisible(true);
			empc_cctv.mediaPlayer().media().play("default.png");
			empc_cctv.mediaPlayer().controls().setRepeat(true);
			
			JButton button_cctv_stop = new JButton("���");
			button_cctv_stop.setBounds(4, 225, 387, 45);
			button_cctv_stop.setBackground(new Color(238, 238, 238));
			button_cctv_stop.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			button_cctv_stop.setFocusPainted(false);
			panel_cctv.add(button_cctv_stop);
			
			button_cctv_stop.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// ���� ��� ��� ��  �Ͻ����� ��ư�� ������ count_video ���� ���� �����Ͽ� ��� ���¿� ��ư ��� �޽����� �ٲ�
					// table ���� Ŭ�� �� �����ϴ� �׼� �޼ҵ� ������ ������ ������ ������
					if (count_cctv == 1) {
						empc_cctv.mediaPlayer().media().play("default.png");
						empc_cctv.mediaPlayer().controls().setRepeat(true);
						
						button_cctv_stop.setText("���");
						
						count_cctv = 0;
					} else {
						empc_cctv.mediaPlayer().media().play("http://" + stream_addr + ":8000/?action=stream");
						button_cctv_stop.setText("����");
						
						count_cctv = 1;
					}
					
					//empc_cctv.mediaPlayer().media().play("default.png");
					//empc_cctv.mediaPlayer().controls().setRepeat(true);
				}
			});
			
			panel_cctv.setVisible(true);

			// ������(Ŭ���̾�Ʈ)���� ���� ���� ���� ������ ����� ���� vlc ������Ʈ�� �г� �������� ��Ʈ���� ������Ʈ, �гΰ� �����ϴ�
			EmbeddedMediaPlayerComponent empc_video = new EmbeddedMediaPlayerComponent();
			empc_video.setBounds(4, 4, 387, 218);
			
			JPanel panel_video = new JPanel();
			panel_video.setBounds(678, 3, 397, 275);
			panel_video.setLayout(null);
			panel_video.setBackground(new Color(238, 238, 238));
			panel_video.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			panel_video.add(empc_video);
			frame.getContentPane().add(panel_video);
			
			JButton button_video_stop = new JButton("�Ͻ�����");
			button_video_stop.setBounds(4, 225, 387, 45);
			button_video_stop.setBackground(new Color(238, 238, 238));
			button_video_stop.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			button_video_stop.setFocusPainted(false);
			panel_video.add(button_video_stop);
			
			button_video_stop.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// ���� ��� ��� ��  �Ͻ����� ��ư�� ������ count_video ���� ���� �����Ͽ� ��� ���¿� ��ư ��� �޽����� �ٲ�
					// table ���� Ŭ�� �� �����ϴ� �׼� �޼ҵ� ������ ������ ������ ������
					if (count_video == 1) {
						empc_video.mediaPlayer().controls().pause();
						button_video_stop.setText("���");
						
						count_video = 0;
					} else {
						empc_video.mediaPlayer().controls().play();
						button_video_stop.setText("�Ͻ�����");
						
						count_video = 1;
					}
					
					//empc_video.mediaPlayer().media().play("default.png");
					//empc_video.mediaPlayer().controls().setRepeat(true);
				}
			});
			
			panel_video.setVisible(false);

			// ���̺��� ���� ���� ���� ���� Ŭ�� �� ���� ���� ����� ���� �г�, ��, �ؽ�Ʈ�ʵ� ����
			JPanel panel_information = new JPanel();
			panel_information.setBounds(678, 280, 397, 271);
			panel_information.setLayout(null);
			panel_information.setBackground(new Color(238, 238, 238));
			panel_information.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			frame.getContentPane().add(panel_information);
			
			JLabel label_license_plate = new JLabel("������ȣ");
			label_license_plate.setHorizontalAlignment(SwingConstants.CENTER);
			label_license_plate.setBounds(4, 4, 129, 27);
			label_license_plate.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_information.add(label_license_plate);

			JLabel label_car_type = new JLabel("����");
			label_car_type.setHorizontalAlignment(SwingConstants.CENTER);
			label_car_type.setBounds(4, 30, 129, 27);
			label_car_type.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_information.add(label_car_type);
			
			JLabel label_car_model = new JLabel("����");
			label_car_model.setHorizontalAlignment(SwingConstants.CENTER);
			label_car_model.setBounds(4, 56, 129, 27);
			label_car_model.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_information.add(label_car_model);
			
			JLabel label_car_year = new JLabel("�𵨿���");
			label_car_year.setHorizontalAlignment(SwingConstants.CENTER);
			label_car_year.setBounds(4, 82, 129, 27);
			label_car_year.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_information.add(label_car_year);
			
			JLabel label_owner_name = new JLabel("�̸�");
			label_owner_name.setHorizontalAlignment(SwingConstants.CENTER);
			label_owner_name.setBounds(4, 108, 129, 27);
			label_owner_name.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_information.add(label_owner_name);

			JLabel label_owner_id = new JLabel("�ֹε�Ϲ�ȣ");
			label_owner_id.setHorizontalAlignment(SwingConstants.CENTER);
			label_owner_id.setBounds(4, 134, 129, 27);
			label_owner_id.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_information.add(label_owner_id);

			JLabel label_owner_address = new JLabel("�ּ�");
			label_owner_address.setHorizontalAlignment(SwingConstants.CENTER);
			label_owner_address.setBounds(4, 160, 129, 54);
			label_owner_address.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_information.add(label_owner_address);

			JLabel label_handicap = new JLabel("����� ���");
			label_handicap.setHorizontalAlignment(SwingConstants.CENTER);
			label_handicap.setBounds(4, 213, 129, 27);
			label_handicap.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_information.add(label_handicap);
			
			JLabel label_parking_lot = new JLabel("������ ���");
			label_parking_lot.setHorizontalAlignment(SwingConstants.CENTER);
			label_parking_lot.setBounds(4, 239, 129, 27);
			label_parking_lot.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_information.add(label_parking_lot);
			
			JTextField textfield_license_plate = new JTextField();
			textfield_license_plate.setBounds(132, 4, 260, 27);
			textfield_license_plate.setEditable(false);
			textfield_license_plate.setBackground(Color.WHITE);
			textfield_license_plate.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_information.add(textfield_license_plate);
			
			JTextField textfield_car_type = new JTextField();
			textfield_car_type.setBounds(132, 30, 260, 27);
			textfield_car_type.setEditable(false);
			textfield_car_type.setBackground(Color.WHITE);
			textfield_car_type.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_information.add(textfield_car_type);
			
			JTextField textfield_car_model = new JTextField();
			textfield_car_model.setBounds(132, 56, 260, 27);
			textfield_car_model.setEditable(false);
			textfield_car_model.setBackground(Color.WHITE);
			textfield_car_model.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_information.add(textfield_car_model);
			
			JTextField textfield_car_year = new JTextField();
			textfield_car_year.setBounds(132, 82, 260, 27);
			textfield_car_year.setEditable(false);
			textfield_car_year.setBackground(Color.WHITE);
			textfield_car_year.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_information.add(textfield_car_year);
			
			JTextField textfield_owner_name = new JTextField();
			textfield_owner_name.setBounds(132, 108, 260, 27);
			textfield_owner_name.setEditable(false);
			textfield_owner_name.setBackground(Color.WHITE);
			textfield_owner_name.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_information.add(textfield_owner_name);
			
			JTextField textfield_owner_id = new JTextField();
			textfield_owner_id.setBounds(132, 134, 260, 27);
			textfield_owner_id.setEditable(false);
			textfield_owner_id.setBackground(Color.WHITE);
			textfield_owner_id.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_information.add(textfield_owner_id);
			
			/*
			JTextArea textarea_owner_address = new JTextArea();
			textarea_owner_address.setBounds(132, 160, 260, 54);
			textarea_owner_address.setEditable(false);
			textarea_owner_address.setBackground(Color.WHITE);
			textarea_owner_address.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			textarea_owner_address.setLineWrap(true);
			panel_information.add(textarea_owner_address);
			*/
			
			JTextField textfield_owner_address = new JTextField();
			textfield_owner_address.setBounds(132, 160, 260, 54);
			textfield_owner_address.setEditable(false);
			textfield_owner_address.setBackground(Color.WHITE);
			textfield_owner_address.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_information.add(textfield_owner_address);
			
			JTextField textfield_handicap = new JTextField();
			textfield_handicap.setBounds(132, 213, 260, 27);
			textfield_handicap.setEditable(false);
			textfield_handicap.setBackground(Color.WHITE);
			textfield_handicap.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_information.add(textfield_handicap);
			
			JTextField textfield_registration = new JTextField();
			textfield_registration.setBounds(132, 239, 260, 27);
			textfield_registration.setEditable(false);
			textfield_registration.setBackground(Color.WHITE);
			textfield_registration.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_information.add(textfield_registration);
			
			// ���̺��� Į�� �̸� ����
			String [] headings = new String[] {"����", "������ȣ", "�����ð�", "�����ð�", "����� ����", "����", "������ ���", "���"};
			
			// ��ü ���� ����� ����ϱ� ���� �г� ����
			JPanel panel_all = new JPanel();

			// ���� ��� ���̺� ��θ� �����´�
			String query_all = "select * from parking_record";
			
			// ���̺� �� ��ü ����, ���̺� ��� �� ���� ���� �Ұ��� ����
			DefaultTableModel dtm_all = new DefaultTableModel(headings, 0) {
				private static final long serialVersionUID = 1L;
				
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
				
			JTable table_all = new JTable(dtm_all);
			frame.getContentPane().add(table_all);
			
			// ���̺� ��ũ�� ��ü ���� �� Ÿ��Ʋ �� �׵θ� ����
			JScrollPane scrollpane_all = new JScrollPane(table_all);
			
			scrollpane_all.setBorder(BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder(), "��ü ���� ���", TitledBorder.CENTER, TitledBorder.TOP));
			
			// table_setting �޼ҵ带 ȣ���Ͽ� ���̺� �г�, ��ũ��, ���̺� ���� ����, Ȯ�� ���� ������Ʈ �� �г�, ��ư ����
			table_setting(stmt, 
					panel_all, table_all, scrollpane_all, 
					textfield_license_plate, textfield_car_type, textfield_car_model, textfield_car_year, 
					textfield_owner_name, textfield_owner_id, textfield_owner_address, textfield_handicap, textfield_registration, 
					panel_cctv, panel_video, 
					empc_cctv, empc_video, 
					button_video_stop, 
					f_video, empc_expansion_video, p_video);
			
			// table_making �޼ҵ带 ȣ���Ͽ� ���������� �ش� ���ǹ��� ���ϴ� ���� ��ϸ� �����ͼ� ���̺� ���
			table_making(stmt, dtm_all, query_all); 
			
			panel_all.setVisible(false);
			
			// ���� ���� ��� ����� ���� �гη� �������� �����ϰ� ���� ������ ����
			JPanel panel_today = new JPanel();
			
			// ���� ��¥�� ���� �̻��̰� ���÷� ���� �Ϸ縦 ���� ���� ��¥ �̸��� ��� �� ���� ���� ����� �����´�
			String query_today = "select * from parking_record where parking_in_date >= curdate() and parking_in_date < date_add(curdate(), interval 1 day)";
			
			DefaultTableModel dtm_today = new DefaultTableModel(headings, 0) {
				private static final long serialVersionUID = 1L;
				
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			
			JTable table_today = new JTable(dtm_today);
			frame.getContentPane().add(table_today);
			
			JScrollPane scrollpane_today = new JScrollPane(table_today);
			
			scrollpane_today.setBorder(BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder(), "���� ���� ���", TitledBorder.CENTER, TitledBorder.TOP));
			
			table_setting(stmt, 
					panel_today, table_today, scrollpane_today, 
					textfield_license_plate, textfield_car_type, textfield_car_model, textfield_car_year, 
					textfield_owner_name, textfield_owner_id, textfield_owner_address, textfield_handicap, textfield_registration, 
					panel_cctv, panel_video, 
					empc_cctv, empc_video, 
					button_video_stop, 
					f_video, empc_expansion_video, p_video);
			
			table_making(stmt, dtm_today, query_today); 
				
			panel_today.setVisible(false);
			
			// ���� ���� ��� ����� ���� �гη� �������� �����ϰ� ���� ������ ����
			JPanel panel_now = new JPanel();
			
			// ���� �ð��� ��ϵ��� ���� ���� ����� ���� ���� ���� ���� ������� �����ϰ� �����´�
			String query_now = "select * from parking_record where parking_out_date is null";
			
			DefaultTableModel dtm_now = new DefaultTableModel(headings, 0) {
				private static final long serialVersionUID = 1L;
				
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
				
			JTable table_now = new JTable(dtm_now);
			frame.getContentPane().add(table_now);
			
			JScrollPane scrollpane_now = new JScrollPane(table_now);
			
			scrollpane_now.setBorder(BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder(), "���� ���� ��Ȳ", TitledBorder.CENTER, TitledBorder.TOP));
			
			table_setting(stmt, 
					panel_now, table_now, scrollpane_now, 
					textfield_license_plate, textfield_car_type, textfield_car_model, textfield_car_year, 
					textfield_owner_name, textfield_owner_id, textfield_owner_address, textfield_handicap, textfield_registration, 
					panel_cctv, panel_video, 
					empc_cctv, empc_video, 
					button_video_stop, 
					f_video, empc_expansion_video, p_video);
			
			table_making(stmt, dtm_now, query_now); 
			
			panel_now.setVisible(true);
			
			// ���� ���� ��� ����� ���� �гη� �������� �����ϰ� ���� ������ ����
			JPanel panel_violation = new JPanel();

			// ���� ��� ���̺��� ���� Į���� �����̶�� ��ϵǾ� �ִ� ���� ����� �����´�
			String query_violation = "select * from parking_record where violation = '����'";
			
			DefaultTableModel dtm_violation = new DefaultTableModel(headings, 0) {
				private static final long serialVersionUID = 1L;
				
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
				
			JTable table_violation = new JTable(dtm_violation);
			frame.getContentPane().add(table_violation);
			
			JScrollPane scrollpane_violation = new JScrollPane(table_violation);
			
			scrollpane_violation.setBorder(BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder(), "���� ���� ���", TitledBorder.CENTER, TitledBorder.TOP));
			
			table_setting(stmt, 
					panel_violation, table_violation, scrollpane_violation, 
					textfield_license_plate, textfield_car_type, textfield_car_model, textfield_car_year, 
					textfield_owner_name, textfield_owner_id, textfield_owner_address, textfield_handicap, textfield_registration, 
					panel_cctv, panel_video, 
					empc_cctv, empc_video, 
					button_video_stop, 
					f_video, empc_expansion_video, p_video);
			
			table_making(stmt, dtm_violation, query_violation); 
			
			panel_violation.setVisible(false);
			
			// �޺��ڽ��� �Է� ���� ��¥�� ���� ����� ������ �гη� ���� ������ ������ table_making �޼ҵ�� ȣ������ �ʴ´�
			JPanel panel_search = new JPanel();
			panel_search.setLayout(null);
					
			DefaultTableModel dtm_search = new DefaultTableModel(headings, 0) {
				private static final long serialVersionUID = 1L;
				
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			
			JTable table_search = new JTable(dtm_search);
			frame.getContentPane().add(table_search);
			
			JScrollPane scrollpane_search = new JScrollPane(table_search);

			scrollpane_search.setBorder(BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder(), "��¥ �˻� ���", TitledBorder.CENTER, TitledBorder.TOP));
			
			table_setting(stmt,
					panel_search, table_search, scrollpane_search, 
					textfield_license_plate, textfield_car_type, textfield_car_model, textfield_car_year, 
					textfield_owner_name, textfield_owner_id, textfield_owner_address, textfield_handicap, textfield_registration, 
					panel_cctv, panel_video, 
					empc_cctv, empc_video, 
					button_video_stop, 
					f_video, empc_expansion_video, p_video);
			
			panel_search.setVisible(false);
			
			// ���� ��¥�� �ð��� �����ӿ� ����ϱ� ���� ��¥ �г� ����
			JPanel panel_date = new JPanel();
			panel_date.setBounds(4, 3, 672, 75);
			panel_date.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			panel_date.setLayout(null);
			frame.getContentPane().add(panel_date);
			
			JLabel label_date = new JLabel();
			label_date.setBounds(276, 12, 120, 25);
			label_date.setHorizontalAlignment(SwingConstants.CENTER);
			label_date.setFont(new Font("Ariel", Font.BOLD, 23));
			label_date.setForeground(new Color(255, 255, 255));
			panel_date.add(label_date);
	        
			JLabel label_time = new JLabel();
			label_time.setBounds(276, 41, 120, 25);
			label_time.setHorizontalAlignment(SwingConstants.CENTER);
			label_time.setFont(new Font("Ariel", Font.BOLD, 21));
			label_time.setForeground(new Color(255, 255, 255));
			panel_date.add(label_time);
			
	        DateFormat dateformat_date = new SimpleDateFormat("YYYY-MM-dd");
	        DateFormat dateformat_time = new SimpleDateFormat("HH:mm:ss");
	        
	        ActionListener TimeListener = new ActionListener()
	        {
	            public void actionPerformed(ActionEvent e)
	            {
	                Date date_both = new Date();
	                
	                String date = dateformat_date.format(date_both);
	                label_date.setText(date);
	                
	                String time = dateformat_time.format(date_both);
	                label_time.setText(time);
	            }
	        };
	        
	        // 1�� ���� ���� ���� TimerListener�� ȣ���Ͽ� ���� ��¥�� �ð��� �޾� ��¥ �� �ð� �󺧿� ����Ѵ�
	        Timer timer = new Timer(1000, TimeListener);
	        timer.setInitialDelay(0);
	        timer.start();
	        
			ImageIcon background = new ImageIcon("background.jpg");
			JLabel label_menu = new JLabel(background);
			label_menu.setBounds(4, 4, 663, 66);
			panel_date.add(label_menu);
			
			// ���� ��ư ����� ���� �г� ����
			JPanel panel_center = new JPanel();
			panel_center.setBounds(4, 417, 672, 134);
			panel_center.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			panel_center.setLayout(null);
			frame.getContentPane().add(panel_center);
			
			/*
			panel_menu.addMouseListener(new MouseAdapter() { 
				public void mousePressed(MouseEvent me) { 
					table_all.clearSelection();
					table_today.clearSelection();
					table_now.clearSelection();
					table_violation.clearSelection();
					table_search.clearSelection();
					textfield_search.setText("  ��: 2019-01-01");
		        } 
		    });
			*/
	
			// �Ʒ��� ��ü, ����, ����, ���� ���� ����� ������ ��ư �����̴� �� ��ư�� Ŭ���ϸ� �ش��ϴ� ���̺� �г��� true�� ����ϰ� �������� false�� ������� �ʴ´�
			JButton button_all = new JButton("��ü ���� ��� ��ȸ");
			button_all.setBounds(4, 4, 187, 29);
			button_all.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			button_all.setBackground(new Color(238, 238, 238));
			button_all.setFocusPainted(false);
			panel_center.add(button_all);
			
			button_all.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					panel_all.setVisible(true);
					panel_today.setVisible(false);
					panel_now.setVisible(false);
					panel_violation.setVisible(false);
					panel_search.setVisible(false);
				}
			});
			
			JButton button_today = new JButton("���� ���� ��� ��ȸ");
			button_today.setBounds(194, 4, 187, 29);
			button_today.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			button_today.setBackground(new Color(238, 238, 238));
			button_today.setFocusPainted(false);
			panel_center.add(button_today);
			
			button_today.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					panel_all.setVisible(false);
					panel_today.setVisible(true);
					panel_now.setVisible(false);
					panel_violation.setVisible(false);
					panel_search.setVisible(false);
				}
			});
			
			JButton button_now = new JButton("���� ���� ��Ȳ ��ȸ");
			button_now.setBounds(4, 36, 187, 29);
			button_now.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			button_now.setBackground(new Color(238, 238, 238));
			button_now.setFocusPainted(false);
			panel_center.add(button_now);
			
			button_now.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					panel_all.setVisible(false);
					panel_today.setVisible(false);
					panel_now.setVisible(true);
					panel_violation.setVisible(false);
					panel_search.setVisible(false);
				}
			});
			
			JButton button_violation = new JButton("���� ���� ��� ��ȸ");
			button_violation.setBounds(194, 36, 187, 29);
			button_violation.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			button_violation.setBackground(new Color(238, 238, 238));
			button_violation.setFocusPainted(false);
			panel_center.add(button_violation);
			
			button_violation.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					panel_all.setVisible(false);
					panel_today.setVisible(false);
					panel_now.setVisible(false);
					panel_violation.setVisible(true);
					panel_search.setVisible(false);
				}
			});

			/*
			JTextField textfield_search = new JTextField();
			textfield_search.setBounds(381, 477, 207, 30);
			textfield_search.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			textfield_search.setText("  ��: 2019-01-01");
			panel_center.add(textfield_search);
			
			textfield_search.addMouseListener(new MouseAdapter() {
				  public void mouseClicked(MouseEvent e) {
					  textfield_search.setText("");
				  }
			});
			*/
			
			// ��¥�� �Է� �ޱ� ���� �޺��ڽ��� ��, ��, �� ������ ��ġ�Ͽ��� year�� 2010�� ���� 1�� �� �÷� ���ر��� ����Ʈ�� ��Ұ� month�� day�� ��Ʈ������ �Է��Ͽ���
			ArrayList<String> arraylist_year = new ArrayList<String>();
			arraylist_year.add("��");
			for(int years = 2010; years<=Calendar.getInstance().get(Calendar.YEAR); years++) {
				arraylist_year.add(years+"");
			}
			
			@SuppressWarnings({ "rawtypes", "unchecked" })
			JComboBox combobox_year = new JComboBox(arraylist_year.toArray());
			combobox_year.setBounds(384, 4, 69, 29);
			panel_center.add(combobox_year);
			
			String[] month = {"��", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
			
			@SuppressWarnings({ "rawtypes", "unchecked" })
			JComboBox combobox_month = new JComboBox(month);
			combobox_month.setBounds(453, 4, 69, 29);
			panel_center.add(combobox_month);
			
			String[] day = {"��", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", 
					"11", "12", "13", "14", "15", "16", "17", "18", "19", "20"
					, "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
			
			@SuppressWarnings({ "rawtypes", "unchecked" })
			JComboBox combobox_day = new JComboBox(day);
			combobox_day.setBounds(522, 4, 69, 29);
			panel_center.add(combobox_day);
			
			JButton button_search = new JButton("�˻�");
			button_search.setBounds(594, 4, 73, 29);
			button_search.setBackground(new Color(238, 238, 238));
			button_search.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			button_search.setFocusPainted(false);
			panel_center.add(button_search);
			
			// �޺��ڽ��κ��� ���� year, month, day�� year-month-day ������ ��Ʈ�� ������ �ְ� table_refreshing �޼ҵ带 ȣ���Ͽ� �ش� ��¥�� ���� ��ϸ� ���̺� ����Ѵ�
			button_search.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//String date = textfield_search.getText();
					
					String year = (String)combobox_year.getSelectedItem();
					String month = (String)combobox_month.getSelectedItem();
					String day = (String)combobox_day.getSelectedItem();
					
					String date = "" + year + "-" + month + "-" + day + "";
					table_refreshing(stmt, dtm_search, panel_all, panel_today, panel_now, panel_violation, panel_search, date);
				}
			});
			
			/*
			Action search = new AbstractAction() {
				private static final long serialVersionUID = 1L;
				public void actionPerformed(ActionEvent arg0) {
					//String date = textfield_search.getText();

					String year = (String)combobox_year.getSelectedItem();
					String month = (String)combobox_month.getSelectedItem();
					String day = (String)combobox_day.getSelectedItem();
					
					String date = "" + year + "-" + month + "-" + day + "";
					System.out.println(date);
					table_refreshing(stmt, dtm_search, panel_all, panel_today, panel_now, panel_violation, panel_search, date);
				}
			};
		 		
			KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
			textfield_search.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, "ENTER");
			textfield_search.getActionMap().put("ENTER", search);
			*/
			
			String parking_lot[]= {" ������"};
			
			@SuppressWarnings({ "rawtypes", "unchecked" })
			JComboBox combobox_cctv = new JComboBox(parking_lot);
			combobox_cctv.setBounds(384, 36, 207, 29);
			combobox_cctv.setBorder(null);
			panel_center.add(combobox_cctv);
		
			UIManager.put("ComboBox.background", Color.white);
			UIManager.put("ComboBox.border", Boolean.FALSE);
			combobox_cctv.updateUI();
			combobox_year.updateUI();
			combobox_month.updateUI();
			combobox_day.updateUI();
			
			JButton button_cctv = new JButton("����");
			button_cctv.setBounds(594, 36, 73, 29);
			button_cctv.setBackground(new Color(238, 238, 238));
			button_cctv.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			button_cctv.setFocusPainted(false);
			panel_center.add(button_cctv);
			
			// ���� ��ư�� Ŭ���ϸ� ������ ���� �������� ����ϴ� �г��� false�� ���߰� ��Ʈ������ ����ϴ� �г��� true�� �����ش� �׸��� ������Ʈ�� ���Ͽ� ����Ѵ�
			button_cctv.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String number = combobox_cctv.getSelectedItem().toString();
					if (number.equals(" ������")) {
						panel_cctv.setVisible(true);
						panel_video.setVisible(false);
						
						empc_video.mediaPlayer().controls().stop();
						
						empc_cctv.mediaPlayer().media().play("http://" + stream_addr + ":8000/?action=stream");
						
						button_cctv_stop.setText("����");
						
						count_cctv = 1;						
					}
				}
			});
			
			// ���� ���� ��� ������, �г�, ��, �ؽ�Ʈ�ʵ�, �޺��ڽ�, ��ư ����
			JFrame f_add = new JFrame("���� ���� ���");
			f_add.setBounds(100, 100, 445, 425);
			f_add.getContentPane().setLayout(null);
			f_add.setResizable(false);
			f_add.setLocationRelativeTo(null);
			f_add.setVisible(false);
			f_add.setIconImage(icon.getImage());
			
			JPanel p_add = new JPanel();
			p_add.setBounds(11, 5, 418, 383);
			p_add.setLayout(null);
			p_add.setBackground(new Color(238, 238, 238));
			p_add.setBorder(BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder(), "���� ���� ���", TitledBorder.CENTER, TitledBorder.TOP));
			f_add.getContentPane().add(p_add);

			JLabel label_add_license_plate = new JLabel("���� ��ȣ");
			label_add_license_plate.setBounds(16, 23, 131, 27);
			label_add_license_plate.setHorizontalAlignment(SwingConstants.CENTER);
			label_add_license_plate.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			p_add.add(label_add_license_plate);

			JLabel label_add_type = new JLabel("����");
			label_add_type.setBounds(16, 49, 131, 27);
			label_add_type.setHorizontalAlignment(SwingConstants.CENTER);
			label_add_type.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			p_add.add(label_add_type);
			
			JLabel label_add_model = new JLabel("����");
			label_add_model.setBounds(16, 75, 131, 27);
			label_add_model.setHorizontalAlignment(SwingConstants.CENTER);
			label_add_model.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			p_add.add(label_add_model);

			JLabel label_add_year = new JLabel("�𵨿���");
			label_add_year.setBounds(16, 101, 131, 27);
			label_add_year.setHorizontalAlignment(SwingConstants.CENTER);
			label_add_year.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			p_add.add(label_add_year);
			
			JLabel label_add_name = new JLabel("�̸�");
			label_add_name.setBounds(16, 127, 131, 27);
			label_add_name.setHorizontalAlignment(SwingConstants.CENTER);
			label_add_name.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			p_add.add(label_add_name);
			
			JLabel label_add_id = new JLabel("�ֹε�Ϲ�ȣ");
			label_add_id.setBounds(16, 153, 131, 27);
			label_add_id.setHorizontalAlignment(SwingConstants.CENTER);
			label_add_id.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			p_add.add(label_add_id);
			
			JLabel label_add_address = new JLabel("�ּ�");
			label_add_address.setBounds(16, 179, 131, 54);
			label_add_address.setHorizontalAlignment(SwingConstants.CENTER);
			label_add_address.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			p_add.add(label_add_address);

			JLabel label_add_handicap = new JLabel("����� ���");
			label_add_handicap.setBounds(16, 232, 131, 27);
			label_add_handicap.setHorizontalAlignment(SwingConstants.CENTER);
			label_add_handicap.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			p_add.add(label_add_handicap);
			
			JLabel label_add_parking_lot = new JLabel("������ ���");
			label_add_parking_lot.setBounds(16, 258, 131, 27);
			label_add_parking_lot.setHorizontalAlignment(SwingConstants.CENTER);
			label_add_parking_lot.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			p_add.add(label_add_parking_lot);
			
			JTextField textfield_add_license_plate = new JTextField();
			textfield_add_license_plate.setBounds(147, 23, 255, 28);
			p_add.add(textfield_add_license_plate);
			
			String type[]= {" �����ϼ���", "����", "������", "������", "������"};
			
			@SuppressWarnings({ "rawtypes", "unchecked" })
			JComboBox combobox_add_type = new JComboBox(type);
			combobox_add_type.setBounds(147, 49, 254, 27);
			p_add.add(combobox_add_type);
			
			JTextField textfield_add_model = new JTextField();
			textfield_add_model.setBounds(147, 75, 255, 28);
			p_add.add(textfield_add_model);
			
			ArrayList<String> tmp_add = new ArrayList<String>();
			tmp_add.add(" �����ϼ���");
			for(int years_add = 1900; years_add<=Calendar.getInstance().get(Calendar.YEAR); years_add++) {
				tmp_add.add(years_add+"");
			}
	
			@SuppressWarnings({ "rawtypes", "unchecked" })
			JComboBox combobox_add_year = new JComboBox(tmp_add.toArray());
			combobox_add_year.setBounds(147, 101, 254, 27);
			p_add.add(combobox_add_year);
			
			JTextField textfield_add_name = new JTextField();
			textfield_add_name.setBounds(147, 127, 255, 28);
			p_add.add(textfield_add_name);
			
			JTextField textfield_add_id = new JTextField();
			textfield_add_id.setBounds(147, 153, 255, 28);
			p_add.add(textfield_add_id);
			
			JTextArea textarea_add_address = new JTextArea();
			textarea_add_address.setBounds(147, 179, 254, 55);
			textarea_add_address.setBorder(new LineBorder(new java.awt.Color(144, 157, 169)));
			textarea_add_address.setLineWrap(true);
			p_add.add(textarea_add_address);
			
			String handicap_add[]= {" �����ϼ���", "�����", "�������"};
			
			@SuppressWarnings({ "rawtypes", "unchecked" })
			JComboBox combobox_add_handicap = new JComboBox(handicap_add);
			combobox_add_handicap.setBounds(147, 232, 254, 27);
			p_add.add(combobox_add_handicap);
			
			String registration_add[]= {" �����ϼ���", "���", "�̵��"};
			
			@SuppressWarnings({ "rawtypes", "unchecked" })
			JComboBox combobox_add_registration = new JComboBox(registration_add);
			combobox_add_registration.setBounds(147, 258, 254, 27);
			p_add.add(combobox_add_registration);
			
			JButton button_registration_confirm = new JButton("���");
			button_registration_confirm.setBounds(144, 297, 129, 29);
			button_registration_confirm.setBackground(new Color(238, 238, 238));
			button_registration_confirm.setFocusPainted(false);
			p_add.add(button_registration_confirm);
			
			// ��� Ȯ�� ��ư�� ��� ��ư �׼� �޼ҵ� ������ ������ JOptionPane �ߺ� ������ �ذ� 
			button_registration_confirm.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						String add_license_plate = textfield_add_license_plate.getText();
						String add_car_model = combobox_add_type.getSelectedItem().toString();
						String add_car_name = textfield_add_model.getText();
						String add_car_year = combobox_add_year.getSelectedItem().toString();
						String add_car_owner_name = textfield_add_name.getText();
						String add_car_owner_id = textfield_add_id.getText();
						String add_car_owner_address = textarea_add_address.getText();
						String add_handicap_check = combobox_add_handicap.getSelectedItem().toString();
						String add_registration = combobox_add_registration.getSelectedItem().toString();

						String lpn = "select * from car_information where license_plate_number = '" + add_license_plate + "'";

						ResultSet rs = stmt.executeQuery(lpn);
						
						String id = "";
							
						while(rs.next()) {
							id = rs.getString("license_plate_number");
						}
					
						if (add_car_model.equals(" �����ϼ���") || add_handicap_check.equals(" �����ϼ���") || add_registration.equals(" �����ϼ���") || add_car_year.equals(" �����ϼ���") ||
								add_license_plate.equals("") || add_car_name.equals("") ||
								add_car_owner_name.equals("") || add_car_owner_id.equals("") || add_car_owner_address.equals("")) {
							// �Է� ������ �ϳ��� �Է����� ���� ��� �˸� �޽����� ����Ѵ�
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showMessageDialog(null, "�Է� ������ Ȯ���ϼ���", "�˸�", JOptionPane.ERROR_MESSAGE);
						} else {
							if (id.equals(add_license_plate)) {
								Toolkit.getDefaultToolkit().beep();
								JOptionPane.showMessageDialog(null, "�̹� ��ϵ� ������ȣ�Դϴ�", "�˸�", JOptionPane.ERROR_MESSAGE);
							} else {
								// ��ϵ��� ���� ���� ��ȣ�� ��� �Է� ���� ��� ������ �״�� ���� ���� ���̺� �����Ѵ�
								String add = "insert into car_information (license_plate_number, car_model, car_name, car_year, car_owner_name, car_owner_id, car_owner_address, handicap_check, registration) "
										+ "values ('"+add_license_plate+"','"+add_car_model+"','"+add_car_name+"','"+add_car_year+"','"+add_car_owner_name+"','"+add_car_owner_id+"','"+add_car_owner_address+"','"+add_handicap_check+"','"+add_registration+"')";  
								stmt.executeUpdate(add);
								
								// ����� ������ �˸��� ���ο� ����� ���� �ؽ�Ʈ �ʵ�, �޺��ڽ� ���� �Է� �� �ʱⰪ���� ����Ѵ�
								Toolkit.getDefaultToolkit().beep();
								JOptionPane.showMessageDialog(null, "���� ������ ��ϵǾ����ϴ�", "�˸�", JOptionPane.INFORMATION_MESSAGE);
						
								textfield_add_license_plate.setText("");
								combobox_add_type.setSelectedIndex(0);						
								textfield_add_model.setText("");							
								combobox_add_year.setSelectedIndex(0);							
								textfield_add_name.setText("");
								textfield_add_id.setText("");
								textarea_add_address.setText("");							
								combobox_add_handicap.setSelectedIndex(0);							
								combobox_add_registration.setSelectedIndex(0);	
							}
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			});
			
			JButton button_add_cancel = new JButton("���");
			button_add_cancel.setBounds(144, 338, 129, 29);
			button_add_cancel.setBackground(new Color(238, 238, 238));
			button_add_cancel.setFocusPainted(false);
			p_add.add(button_add_cancel);
			
			// ��� ��ư�� Ŭ���ϸ� ����� �������� false�� ������
			button_add_cancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {	
					f_add.setVisible(false);
					
					textfield_add_license_plate.setText("");
					combobox_add_type.setSelectedIndex(0);						
					textfield_add_model.setText("");							
					combobox_add_year.setSelectedIndex(0);							
					textfield_add_name.setText("");
					textfield_add_id.setText("");
					textarea_add_address.setText("");							
					combobox_add_handicap.setSelectedIndex(0);							
					combobox_add_registration.setSelectedIndex(0);	
				}
			});
			
			UIManager.put("ComboBox.background", Color.white);
			UIManager.put("ComboBox.border", Boolean.FALSE);
			combobox_add_type.updateUI();
			combobox_add_year.updateUI();
			combobox_add_handicap.updateUI();
			combobox_add_registration.updateUI();
			
			f_add.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					f_add.setVisible(false);
					
					textfield_add_license_plate.setText("");
					combobox_add_type.setSelectedIndex(0);						
					textfield_add_model.setText("");							
					combobox_add_year.setSelectedIndex(0);							
					textfield_add_name.setText("");
					textfield_add_id.setText("");
					textarea_add_address.setText("");							
					combobox_add_handicap.setSelectedIndex(0);							
					combobox_add_registration.setSelectedIndex(0);	
				}
			});
			
			JButton button_vehicle_registration = new JButton("���� ���� ���");
			button_vehicle_registration.setBounds(4, 68, 187, 29);
			button_vehicle_registration.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			button_vehicle_registration.setBackground(new Color(238, 238, 238));
			button_vehicle_registration.setFocusPainted(false);
			panel_center.add(button_vehicle_registration);
			
			// ���� ���� ��� ��ư�� ������ ���ο� �������� ����
			button_vehicle_registration.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					f_add.setVisible(true);
				}		
			});
			
			// ���� ���� ��ȸ ������, �г� �� ����
			JFrame f_print = new JFrame("���� ���� ��ȸ");
			f_print.setBounds(100, 100, 445, 343);
			f_print.getContentPane().setLayout(null);
			f_print.setResizable(false);
			f_print.setLocationRelativeTo(null);
			f_print.setVisible(false);
			f_print.setIconImage(icon.getImage());
			
			JPanel panel_print = new JPanel();
			panel_print.setBounds(11, 5, 418, 300);
			panel_print.setLayout(null);
			panel_print.setBackground(new Color(238, 238, 238));
			panel_print.setBorder(BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder(), "���� ���� ��ȸ", TitledBorder.CENTER, TitledBorder.TOP));
			f_print.getContentPane().add(panel_print);
			
			JLabel label_license_plate_print = new JLabel("������ȣ");
			label_license_plate_print.setHorizontalAlignment(SwingConstants.CENTER);
			label_license_plate_print.setBounds(14, 23, 131, 27);
			label_license_plate_print.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_print.add(label_license_plate_print);

			JLabel label_car_type_print = new JLabel("����");
			label_car_type_print.setHorizontalAlignment(SwingConstants.CENTER);
			label_car_type_print.setBounds(14, 49, 131, 27);
			label_car_type_print.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_print.add(label_car_type_print);
			
			JLabel label_car_model_print = new JLabel("����");
			label_car_model_print.setHorizontalAlignment(SwingConstants.CENTER);
			label_car_model_print.setBounds(14, 75, 131, 27);
			label_car_model_print.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_print.add(label_car_model_print);
			
			JLabel label_car_year_print = new JLabel("�𵨿���");
			label_car_year_print.setHorizontalAlignment(SwingConstants.CENTER);
			label_car_year_print.setBounds(14, 101, 131, 27);
			label_car_year_print.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_print.add(label_car_year_print);
			
			JLabel label_owner_name_print = new JLabel("�̸�");
			label_owner_name_print.setHorizontalAlignment(SwingConstants.CENTER);
			label_owner_name_print.setBounds(14, 127, 131, 27);
			label_owner_name_print.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_print.add(label_owner_name_print);

			JLabel label_owner_id_print = new JLabel("�ֹε�Ϲ�ȣ");
			label_owner_id_print.setHorizontalAlignment(SwingConstants.CENTER);
			label_owner_id_print.setBounds(14, 153, 131, 27);
			label_owner_id_print.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_print.add(label_owner_id_print);

			JLabel label_owner_address_print = new JLabel("�ּ�");
			label_owner_address_print.setHorizontalAlignment(SwingConstants.CENTER);
			label_owner_address_print.setBounds(14, 179, 131, 54);
			label_owner_address_print.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_print.add(label_owner_address_print);

			JLabel label_handicap_print = new JLabel("����� ���");
			label_handicap_print.setHorizontalAlignment(SwingConstants.CENTER);
			label_handicap_print.setBounds(14, 232, 131, 27);
			label_handicap_print.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_print.add(label_handicap_print);
			
			JLabel label_parking_lot_print = new JLabel("������ ���");
			label_parking_lot_print.setHorizontalAlignment(SwingConstants.CENTER);
			label_parking_lot_print.setBounds(14, 258, 131, 27);
			label_parking_lot_print.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_print.add(label_parking_lot_print);
			
			JTextField textfield_license_plate_print = new JTextField();
			textfield_license_plate_print.setBounds(144, 23, 259, 27);
			textfield_license_plate_print.setEditable(false);
			textfield_license_plate_print.setBackground(Color.WHITE);
			textfield_license_plate_print.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_print.add(textfield_license_plate_print);
			
			JTextField textfield_car_type_print = new JTextField();
			textfield_car_type_print.setBounds(144, 49, 259, 27);
			textfield_car_type_print.setEditable(false);
			textfield_car_type_print.setBackground(Color.WHITE);
			textfield_car_type_print.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_print.add(textfield_car_type_print);
			
			JTextField textfield_car_model_print = new JTextField();
			textfield_car_model_print.setBounds(144, 75, 259, 27);
			textfield_car_model_print.setEditable(false);
			textfield_car_model_print.setBackground(Color.WHITE);
			textfield_car_model_print.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_print.add(textfield_car_model_print);
			
			JTextField textfield_car_year_print = new JTextField();
			textfield_car_year_print.setBounds(144, 101, 259, 27);
			textfield_car_year_print.setEditable(false);
			textfield_car_year_print.setBackground(Color.WHITE);
			textfield_car_year_print.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_print.add(textfield_car_year_print);
			
			JTextField textfield_owner_name_print = new JTextField();
			textfield_owner_name_print.setBounds(144, 127, 259, 27);
			textfield_owner_name_print.setEditable(false);
			textfield_owner_name_print.setBackground(Color.WHITE);
			textfield_owner_name_print.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_print.add(textfield_owner_name_print);
			
			JTextField textfield_owner_id_print = new JTextField();
			textfield_owner_id_print.setBounds(144, 153, 259, 27);
			textfield_owner_id_print.setEditable(false);
			textfield_owner_id_print.setBackground(Color.WHITE);
			textfield_owner_id_print.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_print.add(textfield_owner_id_print);
			
			/*
			JTextArea textarea_owner_address_print = new JTextArea();
			textarea_owner_address_print.setBounds(144, 179, 259, 54);
			textarea_owner_address_print.setEditable(false);
			textarea_owner_address_print.setBackground(Color.WHITE);
			textarea_owner_address_print.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			textarea_owner_address_print.setLineWrap(true);
			panel_print.add(textarea_owner_address_print);
			*/
			
			JTextField textfield_owner_address_print = new JTextField();
			textfield_owner_address_print.setBounds(144, 179, 259, 54);
			textfield_owner_address_print.setEditable(false);
			textfield_owner_address_print.setBackground(Color.WHITE);
			textfield_owner_address_print.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_print.add(textfield_owner_address_print);
			
			JTextField textfield_handicap_print = new JTextField();
			textfield_handicap_print.setBounds(144, 232, 259, 27);
			textfield_handicap_print.setEditable(false);
			textfield_handicap_print.setBackground(Color.WHITE);
			textfield_handicap_print.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_print.add(textfield_handicap_print);
			
			JTextField textfield_registration_print = new JTextField();
			textfield_registration_print.setBounds(144, 258, 259, 27);
			textfield_registration_print.setEditable(false);
			textfield_registration_print.setBackground(Color.WHITE);
			textfield_registration_print.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_print.add(textfield_registration_print);
			
			f_print.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					f_print.setVisible(false);
				}
			});
			
			JButton button_vehicle_information = new JButton("���� ���� ��ȸ");
			button_vehicle_information.setBounds(194, 68, 187, 29);
			button_vehicle_information.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			button_vehicle_information.setBackground(new Color(238, 238, 238));
			button_vehicle_information.setFocusPainted(false);
			panel_center.add(button_vehicle_information);
			
			button_vehicle_information.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String input_info = "";
					String search_info = "";
					
					ResultSet rs_info;
					
					String lpn_info = "";
					String cm_info = "";
					String cn_info = "";
					String cy_info = "";
					String con_info = "";
					String coi_info = "";
					String coa_info = "";
					String hc_info = "";
					String r_info = "";
					
					// ���� ���� ��ȸ ��ư�� Ŭ���ϸ� ���� ��ȣ�� �Է� ���� â�� ��� ��ư�� Ŭ���� ������ ��� ���� �� ���� ��ȣ�� �Է����� �ʰų� db�� ��ϵ��� ���� ��ȣ�� �Է��ϸ� ��� �Է� â�� ��µȴ�
					while (true) {
						input_info = JOptionPane.showInputDialog(null, "���� ��ȣ�� �Է��ϼ���", "���� ���� ��ȸ", JOptionPane.QUESTION_MESSAGE);
						
						search_info = "select * from car_information where license_plate_number = '" + input_info + "'";
						
						try {
							rs_info = stmt.executeQuery(search_info);
							
							// �Է� ���� ���� ��ȣ�� �����͵��� �����´�
							while(rs_info.next()) {
								lpn_info = rs_info.getString("license_plate_number");
								cm_info = rs_info.getString("car_model");
								cn_info = rs_info.getString("car_name");
								cy_info = rs_info.getString("car_year");
								con_info = rs_info.getString("car_owner_name");
								coi_info = rs_info.getString("car_owner_id");
								coa_info = rs_info.getString("car_owner_address");
								hc_info = rs_info.getString("handicap_check");
								r_info = rs_info.getString("registration");
							}
							
							if (input_info == null) {
								// ��� ��ư�� Ŭ���ϸ� �ݺ����� �����
								break;
							}
							else if (lpn_info.equals("")) {
								// ���� ��ȣ�� ��ȸ���� ������ �˸� �޽����� ����Ѵ�
								Toolkit.getDefaultToolkit().beep();
								JOptionPane.showMessageDialog(null, "��ϵ��� ���� �����Դϴ�", "�˸�", JOptionPane.ERROR_MESSAGE);
							} else {
								// ������ �����͸� �ؽ�Ʈ�ʵ忡 ����Ѵ� �� �� �ݺ����� �����
								textfield_license_plate_print.setText("  " + lpn_info);
								textfield_car_type_print.setText("  " + cm_info);
								textfield_car_model_print.setText("  " + cn_info);
								textfield_car_year_print.setText("  " + cy_info);
								textfield_owner_name_print.setText("  " + con_info);
								textfield_owner_id_print.setText("  " + coi_info);
								textfield_owner_address_print.setText("  " + coa_info);
								textfield_handicap_print.setText("  " + hc_info);
								textfield_registration_print.setText("  " + r_info);
								
								f_print.setVisible(true);
								
								break;
				            }
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				}
	        });
			
			JFrame f_modify = new JFrame("���� ���� ����");
			f_modify.setBounds(100, 100, 445, 425);
			f_modify.getContentPane().setLayout(null);
			f_modify.setResizable(false);
			f_modify.setLocationRelativeTo(null);
			f_modify.setVisible(false);
			f_modify.setIconImage(icon.getImage());
			
			JPanel panel_registration_modify = new JPanel();
			panel_registration_modify.setBounds(11, 5, 418, 383);
			panel_registration_modify.setLayout(null);
			panel_registration_modify.setBackground(new Color(238, 238, 238));
			panel_registration_modify.setBorder(BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder(), "���� ���� ����", TitledBorder.CENTER, TitledBorder.TOP));
			f_modify.getContentPane().add(panel_registration_modify);
			
			JLabel label_license_plate_modify = new JLabel("���� ��ȣ");
			label_license_plate_modify.setBounds(16, 23, 131, 27);
			label_license_plate_modify.setHorizontalAlignment(SwingConstants.CENTER);
			label_license_plate_modify.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_registration_modify.add(label_license_plate_modify);

			JLabel label_type_modify = new JLabel("����");
			label_type_modify.setBounds(16, 49, 131, 27);
			label_type_modify.setHorizontalAlignment(SwingConstants.CENTER);
			label_type_modify.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_registration_modify.add(label_type_modify);
			
			JLabel label_model_modify = new JLabel("����");
			label_model_modify.setBounds(16, 75, 131, 27);
			label_model_modify.setHorizontalAlignment(SwingConstants.CENTER);
			label_model_modify.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_registration_modify.add(label_model_modify);

			JLabel label_year_modify = new JLabel("�𵨿���");
			label_year_modify.setBounds(16, 101, 131, 27);
			label_year_modify.setHorizontalAlignment(SwingConstants.CENTER);
			label_year_modify.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_registration_modify.add(label_year_modify);
			
			JLabel label_name_modify = new JLabel("�̸�");
			label_name_modify.setBounds(16, 127, 131, 27);
			label_name_modify.setHorizontalAlignment(SwingConstants.CENTER);
			label_name_modify.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_registration_modify.add(label_name_modify);
			
			JLabel label_id_modify = new JLabel("�ֹε�Ϲ�ȣ");
			label_id_modify.setBounds(16, 153, 131, 27);
			label_id_modify.setHorizontalAlignment(SwingConstants.CENTER);
			label_id_modify.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_registration_modify.add(label_id_modify);
			
			JLabel label_address_modify = new JLabel("�ּ�");
			label_address_modify.setBounds(16, 179, 131, 54);
			label_address_modify.setHorizontalAlignment(SwingConstants.CENTER);
			label_address_modify.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_registration_modify.add(label_address_modify);

			JLabel label_handicap_modify = new JLabel("����� ���");
			label_handicap_modify.setBounds(16, 232, 131, 27);
			label_handicap_modify.setHorizontalAlignment(SwingConstants.CENTER);
			label_handicap_modify.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_registration_modify.add(label_handicap_modify);
			
			JLabel label_parking_lot_modify = new JLabel("������ ���");
			label_parking_lot_modify.setBounds(16, 258, 131, 27);
			label_parking_lot_modify.setHorizontalAlignment(SwingConstants.CENTER);
			label_parking_lot_modify.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			panel_registration_modify.add(label_parking_lot_modify);
			
			JTextField textfield_license_plate_modify = new JTextField();
			textfield_license_plate_modify.setBounds(147, 23, 255, 28);
			panel_registration_modify.add(textfield_license_plate_modify);
			
			String type_modify[]= {" �����ϼ���", "����", "������", "������", "������"};
			
			@SuppressWarnings({ "rawtypes", "unchecked" })
			JComboBox combobox_type_modify = new JComboBox(type_modify);
			combobox_type_modify.setBounds(147, 49, 254, 27);
			panel_registration_modify.add(combobox_type_modify);
			
			JTextField textfield_model_modify = new JTextField();
			textfield_model_modify.setBounds(147, 75, 255, 28);
			panel_registration_modify.add(textfield_model_modify);
			
			ArrayList<String> tmp_modify = new ArrayList<String>();
			tmp_modify.add(" �����ϼ���");
			for(int years = 1900; years<=Calendar.getInstance().get(Calendar.YEAR); years++) {
				tmp_modify.add(years+"");
			}		
			
			@SuppressWarnings({ "rawtypes", "unchecked" })
			JComboBox combobox_year_modify = new JComboBox(tmp_modify.toArray());
			combobox_year_modify.setBounds(147, 101, 254, 27);
			panel_registration_modify.add(combobox_year_modify);
			
			JTextField textfield_name_modify = new JTextField();
			textfield_name_modify.setBounds(147, 127, 255, 28);
			panel_registration_modify.add(textfield_name_modify);
			
			JTextField textfield_id_modify = new JTextField();
			textfield_id_modify.setBounds(147, 153, 255, 28);
			panel_registration_modify.add(textfield_id_modify);
			
			JTextArea textarea_address_modify = new JTextArea();
			textarea_address_modify.setBounds(147, 179, 254, 55);
			textarea_address_modify.setBorder(new LineBorder(new java.awt.Color(144, 157, 169)));
			textarea_address_modify.setLineWrap(true);
			panel_registration_modify.add(textarea_address_modify);
			
			String handicap_modify[]= {" �����ϼ���", "�����", "�������"};
					
			@SuppressWarnings({ "rawtypes", "unchecked" })
			JComboBox combobox_handicap_modify = new JComboBox(handicap_modify);
			combobox_handicap_modify.setBounds(147, 232, 254, 27);
			panel_registration_modify.add(combobox_handicap_modify);
			
			String registration_modify[]= {" �����ϼ���", "���", "�̵��"};
			
			@SuppressWarnings({ "rawtypes", "unchecked" })
			JComboBox combobox_registration_modify = new JComboBox(registration_modify);
			combobox_registration_modify.setBounds(147, 258, 254, 27);
			panel_registration_modify.add(combobox_registration_modify);
			
			UIManager.put("ComboBox.background", Color.white);
			UIManager.put("ComboBox.border", Boolean.FALSE);
			combobox_type_modify.updateUI();
			combobox_year_modify.updateUI();
			combobox_handicap_modify.updateUI();
			combobox_registration_modify.updateUI();
			
			JButton button_registration_modify = new JButton("����");
			button_registration_modify.setBounds(144, 297, 129, 29);
			button_registration_modify.setBackground(new Color(238, 238, 238));
			button_registration_modify.setFocusPainted(false);
			panel_registration_modify.add(button_registration_modify);
			
			JButton button_cancel_modify = new JButton("���");
			button_cancel_modify.setBounds(144, 338, 129, 29);
			button_cancel_modify.setBackground(new Color(238, 238, 238));
			button_cancel_modify.setFocusPainted(false);
			panel_registration_modify.add(button_cancel_modify);
			
			// ��� ��ư Ŭ�� �� ������ false
			button_cancel_modify.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {	
					f_modify.setVisible(false);
				}
			});
			
			f_modify.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					f_modify.setVisible(false);
				}
			});
			
			JButton button_vehicle_correction = new JButton("���� ���� ����");
			button_vehicle_correction.setBounds(4, 100, 187, 29);
			button_vehicle_correction.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			button_vehicle_correction.setBackground(new Color(238, 238, 238));
			button_vehicle_correction.setFocusPainted(false);
			panel_center.add(button_vehicle_correction);
				
			button_vehicle_correction.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String search_modify = "";
					
					ResultSet rs_modify;

					String lpn_modify = "";
					String cn_modify = "";
					String con_modify = "";
					String coi_modify = "";
					String coa_modify = "";
					
					while(true) {
						input_modify = JOptionPane.showInputDialog(null, "���� ��ȣ�� �Է��ϼ���", "���� ���� ����", JOptionPane.QUESTION_MESSAGE);
						
						if (input_modify == null) {
							break;
						} else {
							search_modify = "select * from car_information where license_plate_number = '" + input_modify + "'";
							
							try {
								rs_modify = stmt.executeQuery(search_modify);
								
								while(rs_modify.next()) {
									lpn_modify = rs_modify.getString("license_plate_number");
									cn_modify = rs_modify.getString("car_name");
									con_modify = rs_modify.getString("car_owner_name");
									coi_modify = rs_modify.getString("car_owner_id");
									coa_modify = rs_modify.getString("car_owner_address");
								}
								
								if (lpn_modify.equals("")) {
									// ���� ��ȣ�� ��ȸ�� ���� ������ �˸� �޽����� ����
									Toolkit.getDefaultToolkit().beep();
									JOptionPane.showMessageDialog(null, "��ϵ��� ���� �����Դϴ�", "�˸�", JOptionPane.ERROR_MESSAGE);
								} else {
									// ���� ��ȣ�� ��ȸ �Ǹ� �ݺ����� ���߰� ������ �����͸� �ؽ�Ʈ�ʵ忡 �״�� ����Ѵ�
									textfield_license_plate_modify.setText(lpn_modify);
									combobox_type_modify.setSelectedIndex(0);
									textfield_model_modify.setText(cn_modify);
									combobox_year_modify.setSelectedIndex(0);
									textfield_name_modify.setText(con_modify);
									textfield_id_modify.setText(coi_modify);
									textarea_address_modify.setText(coa_modify);
									combobox_handicap_modify.setSelectedIndex(0);							
									combobox_registration_modify.setSelectedIndex(0);
									
									f_modify.setVisible(true);
									
									break;
								}
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
	        });
			
			button_registration_modify.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String license_plate_modify = textfield_license_plate_modify.getText();
					String car_model_modify = combobox_type_modify.getSelectedItem().toString();
					String car_name_modify = textfield_model_modify.getText();
					String car_year_modify = combobox_year_modify.getSelectedItem().toString();
					String car_owner_name_modify = textfield_name_modify.getText();
					String car_owner_id_modify = textfield_id_modify.getText();
					String car_owner_address_modify = textarea_address_modify.getText();
					String handicap_check_modify = combobox_handicap_modify.getSelectedItem().toString();
					String registration_modify = combobox_registration_modify.getSelectedItem().toString();
					
					// ���� ��ư�� ������ �ٽ� �ѹ� �˸�â�� ��� Ȯ���Ѵ�
					if (car_model_modify.equals(" �����ϼ���") || handicap_check_modify.equals(" �����ϼ���") || registration_modify.equals(" �����ϼ���") || car_year_modify.equals(" �����ϼ���") ||
							license_plate_modify.equals("") || car_name_modify.equals("") ||
							car_owner_name_modify.equals("") || car_owner_id_modify.equals("") || car_owner_address_modify.equals("")) {
						Toolkit.getDefaultToolkit().beep();
						JOptionPane.showMessageDialog(null, "�Է� ������ Ȯ���ϼ���", "�˸�", JOptionPane.ERROR_MESSAGE);
					} else {
						int modify = JOptionPane.showConfirmDialog(null, "������ �����Ͻðڽ��ϱ�?", "Ȯ��", JOptionPane.WARNING_MESSAGE);
						
						if (modify == 0) {
							try {
							String delete_modify = "delete from car_information where license_plate_number = '"+input_modify+"'"; 
							stmt.executeUpdate(delete_modify);
							
							String add = "insert into car_information (license_plate_number, car_model, car_name, car_year, car_owner_name, car_owner_id, car_owner_address, handicap_check, registration) values ('"+license_plate_modify+"','"+car_model_modify+"','"+car_name_modify+"','"+car_year_modify+"','"+car_owner_name_modify+"','"+car_owner_id_modify+"','"+car_owner_address_modify+"','"+handicap_check_modify+"','"+registration_modify+"')";  
							stmt.executeUpdate(add);
							
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showMessageDialog(null, "���� ��ȣ '" + input_modify + "'�� ���� ������ �����Ǿ����ϴ�", "�˸�", JOptionPane.INFORMATION_MESSAGE);

							f_modify.setVisible(false);
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
						} 
					}
				}
			});
			
			JButton button_vehicle_deletion = new JButton("���� ���� ����");
			button_vehicle_deletion.setBounds(194, 100, 187, 29);
			button_vehicle_deletion.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			button_vehicle_deletion.setBackground(new Color(238, 238, 238));
			button_vehicle_deletion.setFocusPainted(false);
			panel_center.add(button_vehicle_deletion);
			
			button_vehicle_deletion.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String input = "";
					
					String search = "";
					
					ResultSet rs;
					
					String lpn = "";
					
					// ���������� �ùٸ� ���� ��ȣ�� �Է��ϰų� ��� ��ư�� Ŭ���� ������ ��� ���� ��ȣ �Է�â�� ����
					while (true) {
						input = JOptionPane.showInputDialog(null, "���� ��ȣ�� �Է��ϼ���", "���� ���� ����", JOptionPane.QUESTION_MESSAGE);
						
						search = "select * from car_information where license_plate_number = '" + input + "'";
						
						try {
							rs = stmt.executeQuery(search);
							
							while(rs.next()) {
								lpn = rs.getString("license_plate_number");
							}
							
							if (input == null) {
								// ��� ��ư Ŭ�� �� �ݺ��� ����
								break;
							} else if (lpn.equals("")) {
								// ���� ��ȣ ��ȸ �� �� �� �˸� �޽��� ���
								Toolkit.getDefaultToolkit().beep();
								JOptionPane.showMessageDialog(null, "��ϵ��� ���� �����Դϴ�", "�˸�", JOptionPane.ERROR_MESSAGE);
							} else {
								// ���� ��ȣ ��ȸ ���� �� �ٽ� �ѹ� ���� ������ ����� Ȯ�� ��ư�� Ŭ���ϸ� �ش� ���� ��ȣ�� �����͵��� ��� ���� �� �˸� �޽����� ���� �ݺ����� �����
								int confirm = JOptionPane.showConfirmDialog(null, "������ �����Ͻðڽ��ϱ�?", "Ȯ��", JOptionPane.WARNING_MESSAGE);
								
								if (confirm == 0) {
									String delete = "delete from car_information where license_plate_number = '"+input+"'"; 
									stmt.executeUpdate(delete);
									
									Toolkit.getDefaultToolkit().beep();
									JOptionPane.showMessageDialog(null, "���� ��ȣ '" + input + "'�� ���� ������ �����Ǿ����ϴ�", "�˸�", JOptionPane.INFORMATION_MESSAGE);
									
									break;
								} else {
									break;
								}
							}
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				}
	        });
			
	        JButton button_server = new JButton("���� ����");
	        button_server.setBounds(384, 68, 283, 29);
	        button_server.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
	        button_server.setBackground(new Color(238, 238, 238));
	        button_server.setFocusPainted(false);
			panel_center.add(button_server);
	        
			// ���� ���� ��ư�� Ŭ���ϸ� ������ ���� �˸� �޽����� ����� �� �ش� ���� ��ü�� �ٸ� Ŭ������ �Ű� ó���Ѵ�
			button_server.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						server = new ServerSocket(7000);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(null, "������ ���Ƚ��ϴ�", "�˸�", JOptionPane.INFORMATION_MESSAGE);
					
					Runnable r1 = new b_server_test(server, dtm_all, dtm_today, dtm_now, dtm_violation);
					Thread t1 = new Thread(r1);
					t1.start();
				}
			});
			
			JButton button_exit = new JButton("����");
			button_exit.setBounds(384, 100, 283, 29);
			button_exit.setBorder(new LineBorder(new java.awt.Color(196, 196, 196)));
			button_exit.setBackground(new Color(238, 238, 238));
			button_exit.setFocusPainted(false);
			panel_center.add(button_exit);

			// ���� ��ư Ŭ�� �� ���α׷� ����
			button_exit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
		} else {		
			// �Է� ���� id�� ��й�ȣ�� login �޼ҵ�� ������ db ��ȸ �� ��ġ���� ������ �˸� �޽��� ���
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, "ID �Ǵ� ��й�ȣ�� Ʋ�Ƚ��ϴ�", "�˸�", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public int login(Statement stmt, String textfield_id, String textfield_password) {
		String login = "select * from manager where id = '" + textfield_id + "'";
		
		String id = "";
		String pw = "";
		
		ResultSet rs;
		try {
			// id�� ��й�ȣ�� �Է� �ް� id�� ���ؼ� manager ���̺��� �ش� id�� password�� �����´�
			rs = stmt.executeQuery(login);
			
			while(rs.next()) {
				id = rs.getString("id");
				pw = rs.getString("pw");
			}
			
			// id�� ���� ���� id�� ������ ��й�ȣ�� �ٸ� ��� 0�� �����ϰ� id�� ��й�ȣ�� ��ġ�� ��� 1�� �����Ѵ�
			if (id.equals("")) {
				return 0;
			} else {
				if (!pw.equals(textfield_password)) {
					return 0;
				} else {
					return 1;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public void table_setting(Statement stmt, 
			JPanel panel, JTable table, JScrollPane scrollpane, 
			JTextField textfield_license_plate, JTextField textfield_car_type, JTextField textfield_car_model, JTextField textfield_car_year, 
			JTextField textfield_owner_name, JTextField textfield_owner_id, JTextField textfield_owner_address, 
			JTextField textfield_handicap, JTextField textfield_registration, 
			JPanel panel_cctv, JPanel panel_video,
			EmbeddedMediaPlayerComponent empc_cctv, EmbeddedMediaPlayerComponent empc_video, 
			JButton button_video_stop, 
			JFrame f_video, EmbeddedMediaPlayerComponent empc_expansion_video, JPanel p_video) {
		// �гΰ� ��ũ���� ����� ����
		panel.setBounds(1, 77, 676, 339);
		
		scrollpane.setBounds(1, 1, 676, 339);
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		panel.add(scrollpane);
		
		frame.getContentPane().add(panel);
		
		table.setFillsViewportHeight(true); // ���̺� �� ä���� ���
		table.getTableHeader().setReorderingAllowed(false); // ���̺� Į�� �̵� ����
		table.setFocusable(false); // ���� �ƴ϶� �� ������ Ŭ���� �� �ֵ��� �� ��Ŀ�� ����
		
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked (MouseEvent e) {
	            if (e.getClickCount() == 2) {
	            	if (table.getSelectedRow() != -1) {
						try {
							// ���̺��� �� ���� ���� Ŭ���ϸ� �ش� ���� ������ ��ȣ, ���� ��ȣ, ���� �ð��� �޾� ��Ʈ�� ������ �־��ش�
							String parking_lot_number = table.getValueAt(table.getSelectedRow(), 0).toString();
			            	String parked_car_license_plate_number = table.getValueAt(table.getSelectedRow(), 1).toString();
			            	String parking_in_date = table.getValueAt(table.getSelectedRow(), 2).toString();
			            	
			            	// ���� �ð��� ���Ϸ� �����ϱ� ���� Ư�����ڸ� �����ϰ� ������ ��ȯ���ش�
			    			Date pid;
							pid = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(parking_in_date);
			    			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			    			parking_in_date = sdf.format(pid);
			    			
			    			String video_file_name = parking_lot_number + " - " + parking_in_date + " - " + parked_car_license_plate_number;
			    			
			    			String license_plate = "";
							String car_model = "";
							String car_name = "";
							String car_year = "";
							String owner_name = "";
							String owner_id = "";
							String owner_address = "";
							String handicap = "";
							String registration = "";
							
							ResultSet rs = stmt.executeQuery("select * from car_information where license_plate_number = '" + parked_car_license_plate_number + "'");
							
							// ���̺� �� ���� Ŭ�� �� ������ ������ȣ�� ���� ���� ���̺��� ��ȸ�Ͽ� �����͸� �����´�
							while(rs.next()) {
								license_plate = rs.getString("license_plate_number");
								car_model = rs.getString("car_model");
								car_name = rs.getString("car_name");
								car_year = rs.getString("car_year");
								owner_name = rs.getString("car_owner_name");
								owner_id = rs.getString("car_owner_id");
								owner_address = rs.getString("car_owner_address");
								handicap = rs.getString("handicap_check");
								registration = rs.getString("registration");
							}
							
							if (parked_car_license_plate_number.equals("���繰")) {
			            		// ���繰�� ��� ���� ������ ������� �ʱ� ���� ��� ��ĭ���� ����Ѵ�
								textfield_license_plate.setText("");
								textfield_car_type.setText("");
								textfield_car_model.setText("");
								textfield_car_year.setText("");
								textfield_owner_name.setText("");
								textfield_owner_id.setText("");
								textfield_owner_address.setText("");
								textfield_handicap.setText("");
								textfield_registration.setText("");
			            	} else {
			            		// ������ ��� �ؽ�Ʈ�ʵ忡 ������ ������ �����͸� ��� ����Ѵ�
								textfield_license_plate.setText("  " + license_plate);
								textfield_car_type.setText("  " + car_model);
								textfield_car_model.setText("  " + car_name);
								textfield_car_year.setText("  " + car_year);
								textfield_owner_name.setText("  " + owner_name);
								textfield_owner_id.setText("  " + owner_id);
								textfield_owner_address.setText("  " + owner_address);
								textfield_handicap.setText("  " + handicap);
								textfield_registration.setText("  " + registration);
			            	}
							
							panel_cctv.setVisible(false);
							panel_video.setVisible(true);
							
							empc_cctv.mediaPlayer().controls().stop();
							empc_video.mediaPlayer().media().play(""+video_file_name+".avi");
							empc_video.mediaPlayer().controls().setRepeat(true);
							
							// ��� ���̴ϱ� count_video = 1�̰� ��ư �ؽ�Ʈ�� "�Ͻ�����"
							count_video = 1;
							button_video_stop.setText("�Ͻ�����");
							
							empc_video.videoSurfaceComponent().addMouseListener(new MouseAdapter() {
							    public void mouseClicked(MouseEvent e) {
							    	if (e.getClickCount() == 2) {
							    		f_video.setVisible(true);
							    		empc_expansion_video.mediaPlayer().media().play(""+video_file_name+".avi");
							    		empc_expansion_video.mediaPlayer().controls().setRepeat(true);
							    	}
							    }
							});
						} catch (ParseException | SQLException e1) {
							e1.printStackTrace();
						}
	            	}
	            }
			}
	    });

		// �Ʒ����ʹ� ���̺�� Į��, ��ũ�ѹ� ���� �ʺ�, �� ���� �����ϴ� ����
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		
		TableColumn parking_lot_number = table.getColumnModel().getColumn(0);
		parking_lot_number.setMaxWidth(50);
		parking_lot_number.setMinWidth(50);
		parking_lot_number.setCellRenderer(centerRenderer);

		TableColumn parked_car_license_plate_number = table.getColumnModel().getColumn(1);
		parked_car_license_plate_number.setMaxWidth(70);
		parked_car_license_plate_number.setMinWidth(70);
		parked_car_license_plate_number.setCellRenderer(centerRenderer);
		
		TableColumn parking_in_date = table.getColumnModel().getColumn(2);
		parking_in_date.setMaxWidth(130);
		parking_in_date.setMinWidth(130);
		parking_in_date.setCellRenderer(centerRenderer);
		
		TableColumn parking_out_date = table.getColumnModel().getColumn(3);
		parking_out_date.setMaxWidth(130);
		parking_out_date.setMinWidth(130);
		parking_out_date.setCellRenderer(centerRenderer);
		
		TableColumn handicap = table.getColumnModel().getColumn(4);
		handicap.setMaxWidth(80);
		handicap.setMinWidth(80);
		handicap.setCellRenderer(centerRenderer);
		
		TableColumn violation = table.getColumnModel().getColumn(5);
		violation.setMaxWidth(50);
		violation.setMinWidth(50);
		violation.setCellRenderer(centerRenderer);
		
		TableColumn registration = table.getColumnModel().getColumn(6);
		registration.setMaxWidth(80);
		registration.setMinWidth(80);
		registration.setCellRenderer(centerRenderer);
		
		TableColumn fee = table.getColumnModel().getColumn(7);
		fee.setCellRenderer(centerRenderer);
		panel.setLayout(null);
		
		scrollpane.setBackground(new java.awt.Color(238, 238, 238));
		UIManager.put("ScrollBar.track", new ColorUIResource(new java.awt.Color(238, 238, 238)));
		UIManager.put("ScrollBar.thumb", new ColorUIResource(new java.awt.Color(238, 238, 238)));
		UIManager.put("ScrollBar.thumbDarkShadow", new ColorUIResource(new java.awt.Color(238, 238, 238)));
		UIManager.put("ScrollBar.thumbHighlight", new ColorUIResource(new java.awt.Color(122, 138, 153)));
		UIManager.put("ScrollBar.thumbShadow", new ColorUIResource(new java.awt.Color(122, 138, 153)));
		scrollpane.getVerticalScrollBar().setUI(new BasicScrollBarUI());
	}
	
	public void table_making(Statement stmt, DefaultTableModel dtm, String query) {
		try {		
			ResultSet rs = stmt.executeQuery(query);
			// ��ü, ����, ����, ���� ������ �������� �Ű� ������ �ް� ���ǿ� �´� ���� ��ϸ� �����ͼ� ���̺� �࿡ �߰��Ѵ�
			
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
					// ���� ����� ���� ��� ���� �ð�, ���� �ð� ��Ʈ������ �߰�
					String _pod = df.format(pod);
					String[] data = {pln, pclpn, _pid, _pod, h, v, r, f};
					dtm.addRow(data);
				} 
				else {
					// ���� ����� ���� ��� ���� �ð�, ���� �ð� ��¥ �������� �߰�
					Object[] data = {pln, pclpn, _pid, pod, h, v, r, f};
					dtm.addRow(data);
				}   
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}	
	}
	
	public void table_refreshing(Statement stmt, DefaultTableModel dtm, 
			JPanel panel_all, JPanel panel_today, JPanel panel_now, JPanel panel_violation, JPanel panel_search, 
			String date) {
		try {
			// ��¥ ǥ�� ������ �ٲٰ� ��Ʈ�� ������ ����
			Date converted_date = new SimpleDateFormat("yyyy-mm-dd").parse(date);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
			date = sdf.format(converted_date);
		} catch (ParseException e) {
			return;
		}  
		
		if (!date.equals("")) {
			// �Է� ���� ��¥ �̻� ~ �Է� ���� ��¥ + �Ϸ� �̸� �� �� �ش� ��¥���� ���� ����� �����´�
			String search_search = "select * from parking_record where parking_in_date >="+"'"+date+"' and parking_in_date < date_add('"+date+"', interval 1 day)";
			
			// ���̺� ����
			while(dtm.getRowCount() > 0){
				dtm.removeRow(0);
			}

			try {
				ResultSet rs = stmt.executeQuery(search_search);
				
				// �Ʒ��� table_making �޼ҵ�� �����ϴ�
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
						String _pod = df.format(pod);
						String[] data = {pln, pclpn, _pid, _pod, h, v, r, f};
						dtm.addRow(data);
					} 
					else {
						Object[] data = {pln, pclpn, _pid, pod, h, v, r, f};
						dtm.addRow(data);
					}   
				}				
			} catch (SQLException e1) {
				return;
			}	
			
			// �˻� ��� ���̺��� ������ �г��� �����ϰ� ��� false
			panel_all.setVisible(false);
			panel_today.setVisible(false);
			panel_now.setVisible(false);
			panel_violation.setVisible(false);
			panel_search.setVisible(true);	
		}
	}
}
