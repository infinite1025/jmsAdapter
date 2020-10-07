package org.jboss.as.quickstarts.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.jms.QueueSession;
import javax.ejb.Stateless;
import java.util.UUID;

import com.ibm.mq.jms.MQQueue;
import com.ibm.msg.client.wmq.WMQConstants;
import org.jboss.logging.Logger;

//import javax.naming.Context;

/**
 * A simple servlet 3 as client that sends several messages to a queue.
 * 
 * @author Abu Davis
 */

@WebServlet("/HelloWorldMDBServletClient")
public class HelloWorldMDBServletClient extends HttpServlet {

	private final static Logger LOGGER = Logger.getLogger(HelloWorldMDBServletClient.class.toString());

	private static final long serialVersionUID = -8314035702649252239L;

	private static final int MSG_COUNT = 5;

	QueueConnectionFactory qcfReq = null;
	QueueConnectionFactory qcfRes = null;


	@Resource(mappedName = "java:/jboss/ConnectionFactory")
	private ConnectionFactory connectionFactory;

	@Resource(mappedName = "java:/queue/ticketOrderQueue")
	private Queue queue;


	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		Connection connection = null;
		out.write(
				"<h1>Quickstart: Example demonstrates the use of <strong>JMS 1.1</strong> and <strong>EJB 3.1 Message-Driven Bean using IBM MQ JMS Provider </strong> in JBoss Enterprise Application 6.x/7.x</h1>");
		try {
			Destination destination;
			destination = queue;
			LOGGER.info("<p>Sending messages to <em>" + destination + "</em></p>");
			out.write("<p>Sending messages to <em>" + destination + "</em></p>");
			connection = connectionFactory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer messageProducer = session.createProducer(destination);
			connection.start();
			out.write("<h2>Following messages will be send to the destination:</h2>");
			TextMessage message = session.createTextMessage();
			for (int i = 0; i < MSG_COUNT; i++) {
				message.setText("This is message " + (i + 1));
				messageProducer.send(message);
				LOGGER.info("Sent Message (" + i + "): " + message.getText() + "</br>");
				out.write("Sent Message (" + i + "): " + message.getText() + "</br>");
			}
			out.write(
					"<p><i>Go to your JBoss Application Server console or Server log to see the result of messages processing</i></p>");

		} catch (JMSException e) {
			e.printStackTrace();
			out.write("<h2>A problem occurred during the delivery of this message</h2>");
			out.write("</br>");
			out.write(
					"<p><i>Go your the JBoss Application Server console or Server log to see the error stack trace</i></p>");
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				out.close();
			}
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
