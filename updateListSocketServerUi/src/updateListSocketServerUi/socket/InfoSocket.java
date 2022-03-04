package updateListSocketServerUi.socket;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import updateListSocketServerUi.MainFrame;

public class InfoSocket implements Runnable {
	private String filePath;
	private String versionData = "";
	private ServerSocket ss;

	public InfoSocket(ServerSocket ss, String filePath) {
		this.ss = ss;
		this.filePath = filePath;
		readVersionFile();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			if (ss.isClosed()) {
				break;
			}
			
			try (Socket socket = ss.accept()) {
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

				MainFrame.appendLog("client connected: " + socket.getInetAddress());

				bw.write(versionData);
				bw.flush();
			}  catch (SocketException se) {
				//MainFrame.appendLog("info socket close");
				
			} catch (IOException e) {
				e.printStackTrace();

				MainFrame.appendLog(e);
			}
		}

	}

	private void readVersionFile() {
		// 파일 객체 생성
		Path path = Paths.get(filePath);
		// 캐릭터셋 지정
		Charset cs = StandardCharsets.UTF_8;
		// 파일 내용담을 리스트
		List<String> list = new ArrayList<String>();
		try {
			list = Files.readAllLines(path, cs);
		} catch (IOException e) {
			e.printStackTrace();
			
			MainFrame.appendLog(e);
		}
		for (String readLine : list) {
			versionData += readLine;
		}

	}
}
