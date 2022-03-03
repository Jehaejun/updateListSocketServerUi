package updateListSocketServerUi.socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import updateListSocketServerUi.MainFrame;

public class FileSocket implements Runnable {
	private File file;
	private String filePath;
	private ServerSocket ss;

	public FileSocket(ServerSocket ss, String filePath) {
		this.ss = ss;
		this.filePath = filePath;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		file = new File(filePath);

		if (!file.isFile()) {
			MainFrame.appendLog(filePath + " 해당 경로에 파일이 존재하지 않습니다.");
		}
		
		while (true) {
			try (Socket socket = ss.accept();
					DataOutputStream dout = new DataOutputStream(
					new BufferedOutputStream(socket.getOutputStream()));
					BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file));) {

				//System.out.printf("file download - client connected: %s%n", socket.getInetAddress());
				MainFrame.appendLog("file download - client connected: " + socket.getInetAddress());

				long fileSize = file.length();
				dout.writeLong(fileSize); // 전송할 데이터 크기를 미리 전달한다.
				byte[] b = new byte[10000];
				int readed = -1;

				while ((readed = bin.read(b)) > 0) {
					dout.write(b, 0, readed);
				}

				dout.flush();

				//System.out.println("response message: " + br.readLine()); // 클라이언트 메시지 출력
				MainFrame.appendLog(socket.getInetAddress() + " => response message: " + br.readLine());

			} catch (IOException e) {
				e.printStackTrace();
				
				MainFrame.appendLog(e);
			}
		}
	
	}
}
