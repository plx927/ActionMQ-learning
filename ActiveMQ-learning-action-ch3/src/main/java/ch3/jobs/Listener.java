package ch3.jobs;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * mvn exec:java -Dexec.mainClass=org.apache.activemq.book.ch3.jobs.Consumer
 */
public class Listener implements MessageListener {

	private String job;
	
	public Listener(String job) {
		this.job = job;
	}

	public void onMessage(Message message) {
		try {
			//do something here
			System.out.println(job + " id:" + ((ObjectMessage)message).getObject());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
