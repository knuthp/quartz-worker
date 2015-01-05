package com.herokuapp.worker;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PollReisetiderJob implements Job {
	final static Logger logger = LoggerFactory
			.getLogger(PollReisetiderJob.class);

	public void execute(JobExecutionContext jobExecutionContext)
			throws JobExecutionException {
		String url = "http://spartid-server.herokuapp.com/";

		try {
			InputStream in = new URL(url).openStream();
			IOUtils.toString(in);
			logger.info("Polled: " + url);
		} catch (MalformedURLException e) {
			logger.error("Trouble with url: " + url, e);
		} catch (IOException e) {
			logger.error("Trouble with IO: " + url, e);
		}
	}

}