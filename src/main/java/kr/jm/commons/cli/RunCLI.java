package kr.jm.commons.cli;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public interface RunCLI {

	public static Logger logger = Logger.getLogger(RunCLI.class);
	
	public boolean run(List<String> command);

	public boolean run(String command);

	public String getResultOut();

	public boolean statusAfterRunning();
	
	public void setEnviorment(Map<String, String> environment);

}