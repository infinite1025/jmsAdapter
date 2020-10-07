package org.jboss.as.quickstarts.mdb;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.jboss.ejb3.annotation.Pool;
import org.jboss.ejb3.annotation.ResourceAdapter;
import org.jboss.logging.Logger;


/**
 * A simple MDB (Message Driven Bean) that asynchronously receives and processes the
 * messages that are sent to the JMS Provider.
 * Pre-requisites : jboss-ejb3.xml is mandatory which needs to be packaged along with this code as war/ear/jar.
 * @author Abu Davis
 */
 
 
public class HelloWorldQueueMDB implements MessageListener {


	private final static Logger LOGGER = Logger.getLogger(HelloWorldQueueMDB.class.toString());
				
  	public void onMessage(Message rcvMessage) {
		TextMessage msg = null;
		try {
			String msjOut = JMSConfig.send();
			System.out.println(msjOut);
			if (rcvMessage instanceof TextMessage) {
				msg = (TextMessage) rcvMessage;
				LOGGER.info("Received Message from queue: " + msg.getText());
			} else {
				LOGGER.info("Message of wrong type: "
						+ rcvMessage.getClass().getName());
			}
		} catch (JMSException | NamingException e) {
			LOGGER.info("interrupted");
			throw new RuntimeException(e);
			
		}
	}

}
