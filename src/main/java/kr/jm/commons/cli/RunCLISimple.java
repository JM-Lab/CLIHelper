package kr.jm.commons.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

public class RunCLISimple implements RunCLI {

	protected StringBuffer resultOut;

	protected boolean checkError;

	protected ProcessBuilder processBuilder;

	protected Process process;

	protected Map<String, String> environment;

	protected void runInit(String command) {

		processBuilder = new ProcessBuilder(command.split(" "));

		if (environment != null && environment.size() > 0) {
			processBuilder.environment().putAll(environment);
		}

		resultOut = new StringBuffer();

		checkError = true;
	}

	public boolean run(String command) {

		logger.info("Command: " + command);

		runInit(command);

		try {

			process = processBuilder.start();

			BufferedReader stdOut = new BufferedReader(new InputStreamReader(
					process.getInputStream()));

			BufferedReader stdErr = new BufferedReader(new InputStreamReader(
					process.getErrorStream()));

			stdOutAndErr(stdOut, stdErr);
			
			return process.waitFor() == 0 ? checkError:false;
			
		} catch (IOException e) {
			occurProcessExceptions(e);
			return false;
		} catch (InterruptedException e) {
			occurProcessExceptions(e);
			return false;
		}
	}

	private void occurProcessExceptions(Exception e) {
		checkError = false;
		e.printStackTrace();
		logger.fatal(e.getMessage());
	}

	protected void stdOutAndErr(BufferedReader stdOut, BufferedReader stdErr)
			throws IOException {
		String resultLine;

		while ((resultLine = stdOut.readLine()) != null) {
			writeStdOutLog(resultLine);
		}

		while ((resultLine = stdErr.readLine()) != null) {
			writeErrLog(resultLine);
			
		}
	}

	protected void writeErrLog(String line) {
		checkError = false;
		resultOut.append(line).append('\n');
		logger.fatal(line);
	}

	protected void writeStdOutLog(String line) {
		resultOut.append(line).append('\n');
		logger.info(line);
	}

	public String getResultOut() {
		return resultOut.toString();
	}

	public boolean statusAfterRunning() {
		return checkError;
	}

	public void setEnviorment(Map<String, String> environment) {
		this.environment = environment;
	}

}
