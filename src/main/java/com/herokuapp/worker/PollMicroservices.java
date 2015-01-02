package com.herokuapp.worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PollMicroservices implements Job {
	private final String USER_AGENT = "Mozilla/5.0";
	final static Logger logger = LoggerFactory.getLogger(PollMicroservices.class);

	public void execute(JobExecutionContext jobExecutionContext)
			throws JobExecutionException {
		try {
			String url = "http://trainstations-rt.herokuapp.com/";

			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(url);

			// add request header
			request.addHeader("User-Agent", USER_AGENT);

			HttpResponse response = client.execute(request);

			logger.info("\nSending 'GET' request to URL : " + url);
			logger.info("Response Code : "
					+ response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			logger.info(result.toString());
		} catch (IOException e) {
			logger.error("Got exection", e);
		}
	}

}