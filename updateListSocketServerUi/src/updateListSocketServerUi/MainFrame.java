package updateListSocketServerUi;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import updateListSocketServerUi.socket.UpdateListSocketServer;

public class MainFrame {
	private JFrame mainFrame;
	private JLabel labelFilePath;
	private JLabel labelInfoPath;
	private JTextField txtFieldFilePath;
	private JTextField txtFieldInfoPath;
	private JButton btnFileOpen;
	private JButton btnInfoOpen;
	private JButton btnRunServer;
	private JButton btnStopServer;
	private JLabel labelLog;
	private static JTextArea txtAreaLog;

	private JMenuBar menuBar; // 메뉴바 선언
	private JMenu menu; // 메뉴 선언
	private JMenuItem menuItem; // 메뉴 항목 선언

	private GridBagConstraints constraints;
	private GridBagLayout layout;

	UpdateListSocketServer socketServer;

	public MainFrame() {
		mainFrame = new JFrame("socket server");
		mainFrame.setSize(570, 450);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		prepareGUI();
		setMenuBar();
		mainFrame.setVisible(true);
		mainFrame.setLocationRelativeTo(null); // 프레임 실행시 위치 중앙
		mainFrame.setResizable(false);
		buttonAction();

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MainFrame();

	}

	private void prepareGUI() {
		ImageIcon fileSelectorImg = new ImageIcon(getClass().getResource("/image/icon_file.png"));
		
		layout = new GridBagLayout();
		mainFrame.setLayout(layout); // GridBagLayout을 설정
		constraints = new GridBagConstraints(); // GridBagLayout에 배치할 컴포넌트 위치 정보 등을 담을 객체 준비
		constraints.fill = GridBagConstraints.BOTH; // GridBagConstraints.fill: 컴포넌트의 디스플레이 영역이 컴포넌트가 요청한 크기보다 클 때,
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;

		labelFilePath = new JLabel("File Path : ");
		gridBagAdd(labelFilePath, 0, 0, 3, 1, 1.0);

		txtFieldFilePath = new JTextField("C:\\updateListSocket\\updateList.jar");
		gridBagAdd(txtFieldFilePath, 3, 0, 20, 1, 1.0);

		btnFileOpen = new JButton();
		btnFileOpen.setIcon(fileSelectorImg);
		gridBagAdd(btnFileOpen, 23, 0, 1, 1, 1.0);

		gridBagAdd(new JLabel(), 24, 0, 1, 2, 1.0);

		JSeparator separator2 = new JSeparator(SwingConstants.VERTICAL);
		gridBagAdd(separator2, 25, 0, 1, 2, 1.0);

		btnRunServer = new JButton("실행");
		// btnRunServer.setEnabled(false);
		gridBagAdd(btnRunServer, 26, 0, 6, 1, 1.0);

		labelInfoPath = new JLabel("Info Path : ");
		gridBagAdd(labelInfoPath, 0, 1, 3, 1, 1.0);

		txtFieldInfoPath = new JTextField("C:\\updateListSocket\\update_data.json");
		gridBagAdd(txtFieldInfoPath, 3, 1, 20, 1, 1.0);

		btnInfoOpen = new JButton();
		btnInfoOpen.setIcon(fileSelectorImg);
		gridBagAdd(btnInfoOpen, 23, 1, 1, 1, 1.0);

		btnStopServer = new JButton("중지");
		btnStopServer.setEnabled(false);
		gridBagAdd(btnStopServer, 26, 1, 6, 1, 1.0);

		gridBagAdd(new JLabel(), 0, 2, 32, 1, 2.0);

		JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
		gridBagAdd(separator, 0, 3, 32, 1, 1.0);

		labelLog = new JLabel("Server log");
		gridBagAdd(labelLog, 0, 4, 2, 1, 1.0);

		gridBagAdd(new JLabel(), 2, 4, 30, 1, 1.0);

		txtAreaLog = new JTextArea(10, 1);
		txtAreaLog.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(txtAreaLog);
		gridBagAdd(scrollPane, 0, 5, 32, 1, 30);

		for (int i = 0; i < 32; i++) {
			gridBagAdd(new JLabel(), i, 6, 1, 1, 1);
		}
	}

	private void gridBagAdd(Component component, int x, int y, int w, int h, double weight) {
		constraints.gridx = x;
		constraints.gridy = y;
		// 가장 왼쪽 위 gridx, gridy값은 0
		constraints.gridwidth = w; // 넓이
		constraints.gridheight = h; // 높이
		constraints.weighty = weight; // 가중치

		layout.setConstraints(component, constraints); // 컴포넌트를 컴포넌트 위치+크기 정보에 따라 GridBagLayout에 배치

		mainFrame.add(component);

	}

	private void setMenuBar() {
		menuBar = new JMenuBar(); // 메뉴바 초기화
		menu = new JMenu("Help");

		menuBar.add(menu);

		JMenu subMenu = new JMenu("문의");
		menuItem = new JMenuItem("제해준 주임");
		subMenu.add(menuItem);

		menu.add(subMenu);

		mainFrame.setJMenuBar(menuBar); // 프레임에 메뉴바 설정
	}

	private void buttonAction() {
		btnFileOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter fiter = new FileNameExtensionFilter(".jar", "jar");
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileFilter(fiter);
				chooser.setMultiSelectionEnabled(false);// 다중 선택 불가

				int result = chooser.showOpenDialog(null);

				if (result == JFileChooser.APPROVE_OPTION) {
					txtFieldFilePath.setText(chooser.getSelectedFile().getPath());

					if (!"".equals(txtFieldFilePath.getText()) && !"".equals(txtFieldInfoPath.getText())) {
						btnRunServer.setEnabled(true);
					}

				} else if (result == JFileChooser.CANCEL_OPTION) {

				}
			}
		});

		btnInfoOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter fiter = new FileNameExtensionFilter(".json", "json");
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileFilter(fiter);
				chooser.setMultiSelectionEnabled(false);// 다중 선택 불가

				int result = chooser.showOpenDialog(null);

				if (result == JFileChooser.APPROVE_OPTION) {
					txtFieldInfoPath.setText(chooser.getSelectedFile().getPath());

					if (!"".equals(txtFieldFilePath.getText()) && !"".equals(txtFieldInfoPath.getText())) {
						btnRunServer.setEnabled(true);
					}
				} else if (result == JFileChooser.CANCEL_OPTION) {

				}
			}
		});

		btnRunServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (socketServer == null) {
					try {
						socketServer = new UpdateListSocketServer
								.Builder()
								.setFilePath(txtFieldFilePath.getText())
								.setInfoPath(txtFieldInfoPath.getText())
								.bulid();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						
						MainFrame.appendLog(e1);
					}
				} else {
					socketServer.setFilePath(txtFieldFilePath.getText());
					socketServer.setInfoPath(txtFieldInfoPath.getText());
				}

				try {
					socketServer.run();
					appendLog("server start...");

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					
					MainFrame.appendLog(e1);
				}

				btnRunServer.setEnabled(false);
				btnStopServer.setEnabled(true);

			}
		});

		btnStopServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				socketServer.destroy();

				appendLog("server stop");
				btnRunServer.setEnabled(true);
				btnStopServer.setEnabled(false);
			}
		});
	}

	public static synchronized void appendLog(String log) {
		String currnetDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
		txtAreaLog.append((txtAreaLog.getText().length() == 0 ?  "" : "\n") + "[" + currnetDate + "] " + log);

	}

	public static synchronized void appendLog(Exception e) {
		String currnetDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
		
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		
		txtAreaLog.append((txtAreaLog.getText().length() == 0 ?  "" : "\n") + "[" + currnetDate + "] " + errors.toString());

	}
}
