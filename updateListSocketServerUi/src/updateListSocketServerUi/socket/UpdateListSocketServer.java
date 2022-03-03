package updateListSocketServerUi.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateListSocketServer {
	private String filePath;
	private String infoPath;

	private ServerSocket fileServerSocket;
	private ServerSocket infoServerSocket;
	
	private final int _FILE_SOCKET_PORT = 10081;
	private final int _INFO_SOCKET_PORT = 10080;

	private final int _THREAD_CNT = 3;
	private ExecutorService threadPool;
	private ExecutorService threadPool2;
	
	Thread fileThread;

	public UpdateListSocketServer(String filePath, String infoPath) throws IOException {
		this.filePath = filePath;
		this.infoPath = infoPath;

		fileServerSocket = new ServerSocket(_FILE_SOCKET_PORT);
		infoServerSocket = new ServerSocket(_INFO_SOCKET_PORT);
	}

	public static class Builder {
		private String filePath;
		private String infoPath;

		public Builder setFilePath(String filePath) {
			this.filePath = filePath;
			return this;
		}

		public Builder setInfoPath(String infoPath) {
			this.infoPath = infoPath;
			return this;
		}

		public UpdateListSocketServer bulid() throws IOException {
			return new UpdateListSocketServer(filePath, infoPath);
		}
	}

	public void setInfoPath(String infoPath) {
		this.infoPath = infoPath;
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public void run() throws IOException {
		threadPool = Executors.newFixedThreadPool(_THREAD_CNT);
		threadPool2 = Executors.newFixedThreadPool(_THREAD_CNT);

		FileSocket fileSocket = new FileSocket(fileServerSocket, filePath);
		Thread fileThread = new Thread(fileSocket);
		threadPool.submit(fileThread);
		
		InfoSocket dateSocket = new InfoSocket(infoServerSocket, infoPath);
		Thread dateThread = new Thread(dateSocket);
		threadPool2.submit(dateThread);
	}

	public void destroy() {
		threadPool.shutdown();
		threadPool2.shutdown();
	}
}
