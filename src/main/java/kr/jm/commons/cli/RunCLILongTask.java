package kr.jm.commons.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

public class RunCLILongTask extends RunCLISimple {

	protected void stdOutAndErr(BufferedReader stdOut, BufferedReader stdErr)
			throws IOException {
		Thread stdOutThread = getStdOutThread(stdOut);
		Thread stdErrThread = getStdErrThread(stdErr);
		stdOutThread.start();
		stdErrThread.start();
			try {
				stdOutThread.join();
				stdErrThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
				writeErrLog(e.toString());
			}
	}

	private Thread getStdOutThread(final BufferedReader stdOut) {
		return new Thread(new Runnable() {
			public void run() {
				String resultLine;
				try {
					while ((resultLine = stdOut.readLine()) != null) {
						writeStdOutLog(resultLine);
					}
				} catch (IOException e) {
					e.printStackTrace();
					writeErrLog(e.toString());
				}
			}
		});
	}

	private Thread getStdErrThread(final BufferedReader stdErr) {
		return new Thread(new Runnable() {
			public void run() {
				String resultLine;
				try {
					while ((resultLine = stdErr.readLine()) != null) {
						writeErrLog(resultLine);
					}
				} catch (IOException e) {
					e.printStackTrace();
					writeErrLog(e.toString());
				}
			}
		});
	}

	synchronized protected void writeErrLog(String line) {
		super.writeErrLog(line);
	}

	synchronized protected void writeStdOutLog(String line) {
		super.writeStdOutLog(line);
	}

}
