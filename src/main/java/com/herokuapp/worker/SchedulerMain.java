package com.herokuapp.worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;

public class SchedulerMain {

	final static Logger logger = LoggerFactory.getLogger(SchedulerMain.class);

	public static void main(String[] args) throws Exception {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		scheduler.start();
		JobDetail jobDetail = newJob(HelloJob.class).build();

		Trigger trigger = newTrigger().startNow()
				.withSchedule(repeatSecondlyForever(5)).build();

		scheduler.scheduleJob(jobDetail, trigger);
	}

	public static class HelloJob implements Job {
		private final String USER_AGENT = "Mozilla/5.0";

		public void execute(JobExecutionContext jobExecutionContext)
				throws JobExecutionException {
			try {
				String url = "http://spartid-server.herokuapp.com/";

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

}
