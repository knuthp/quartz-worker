package com.herokuapp.worker;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatMinutelyForever;
import static org.quartz.TriggerBuilder.newTrigger;

public class SchedulerMain {

	private static final int ONE_MINUTE = 1;
	private static final int FIVE_MINUTES = 5;
	final static Logger logger = LoggerFactory.getLogger(SchedulerMain.class);
	private Scheduler scheduler;

	public static void main(String[] args) throws Exception {
		SchedulerMain schedulerMain = new SchedulerMain();
		schedulerMain.scheduleJobs();
	}

	private void scheduleJobs() throws SchedulerException {
		schedulePollReisetider(FIVE_MINUTES);
		schedulePollMicroservices(ONE_MINUTE);
	}

	private void schedulePollReisetider(int minutes) throws SchedulerException {
		JobDetail jobDetail = newJob(PollReisetiderJob.class).build();
		Trigger trigger = newTrigger().startNow()
				.withSchedule(repeatMinutelyForever(minutes)).build();
		scheduler.scheduleJob(jobDetail, trigger);
	}

	private void schedulePollMicroservices(int minutes) throws SchedulerException {
		JobDetail jobDetail = newJob(PollMicroservices.class).build();
		Trigger trigger = newTrigger().startNow()
				.withSchedule(repeatMinutelyForever(minutes)).build();
		scheduler.scheduleJob(jobDetail, trigger);
	}

	public SchedulerMain() throws SchedulerException {
		scheduler = StdSchedulerFactory.getDefaultScheduler();
		scheduler.start();
	}

}
