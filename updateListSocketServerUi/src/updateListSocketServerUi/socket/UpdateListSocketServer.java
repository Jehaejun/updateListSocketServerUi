package updateListSocketServerUi.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateListSocketServer {
	private final int _FILE_SOCKET_PORT = 10081;
	private final int _INFO_SOCKET_PORT = 10080;
	private ServerSocket fileServerSocket;
	private ServerSocket infoServerSocket;
	
	private final int _THREAD_CNT = 3;
	private ExecutorService threadPool;
	private ExecutorService threadPool2;
	
	private String filePath;
	private String infoPath;

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

		if (fileServerSocket.isClosed()) {
			fileServerSocket = new ServerSocket(_FILE_SOCKET_PORT);
		}
		if (infoServerSocket.isClosed()) {
			infoServerSocket = new ServerSocket(_INFO_SOCKET_PORT);
		}
		
		FileSocket fileSocket = new FileSocket(fileServerSocket, filePath);
		threadPool.submit(new Thread(fileSocket));

		InfoSocket dateSocket = new InfoSocket(infoServerSocket, infoPath);
		threadPool2.submit(new Thread(dateSocket));
	}

	public void destroy() throws IOException {
		threadPool.shutdown();
		threadPool2.shutdown();

		this.fileServerSocket.close();
		this.infoServerSocket.close();
	}
}
