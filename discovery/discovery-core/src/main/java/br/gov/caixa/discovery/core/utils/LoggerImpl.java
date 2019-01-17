package br.gov.caixa.discovery.core.utils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerImpl {
	static private FileHandler fileTxt;
	static private SimpleFormatter formatterTxt;

	private static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	static public void configurarFileLog() {
		configurarFileLog(Paths.get(System.getProperty("user.dir")));
	}

	static public void configurarFileLog(Level logFile) {
		configurarFileLog(Paths.get(System.getProperty("user.dir")), "discovery.%g.log", logFile);
	}

	static public void configurarFileLog(Path logFolder) {
		configurarFileLog(logFolder, "app.%g.log");
	}

	static public void configurarFileLog(Path logFolder, String logFile) {
		configurarFileLog(logFolder, logFile, Level.INFO, 30000000, 5, true);
	}

	static public void configurarFileLog(Path logFolder, String logFile, Level logLevel) {
		configurarFileLog(logFolder, logFile, logLevel, 30000000, 5, true);
	}

	static public void configurarFileLog(Path logFolder, String logFile, Level logLevel, int logLimit) {
		configurarFileLog(logFolder, logFile, logLevel, logLimit, 5, true);
	}

	static public void configurarFileLog(Path logFolder, String logFile, Level logLevel, int logLimit, int logCount) {
		configurarFileLog(logFolder, logFile, logLevel, logLimit, logCount, true);
	}

	static public void configurarFileLog(Path logFolder, String logFile, Level logLevel, int logLimit, int logCount,
			boolean logAppend) {
		logger.setUseParentHandlers(false);
		logger.setLevel(Level.ALL);
		try {
			fileTxt = new FileHandler(logFile.toString(), logLimit, logCount, logAppend);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}

		fileTxt.setLevel(logLevel);

		// create a TXT formatter
		formatterTxt = new SimpleFormatter();
		fileTxt.setFormatter(formatterTxt);

		logger.addHandler(fileTxt);
	}

	static public void printTrace(Exception e) {
	}

}
