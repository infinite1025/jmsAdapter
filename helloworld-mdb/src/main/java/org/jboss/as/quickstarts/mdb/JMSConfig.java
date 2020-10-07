package org.jboss.as.quickstarts.mdb;

import com.ibm.mq.jms.MQQueue;
import com.ibm.msg.client.wmq.WMQConstants;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.UUID;

public class JMSConfig {
    public static String send() throws NamingException, JMSException {
        String requestQueue = "ql.VALARQC.req";
        String responseQueue = "ql.VALARQC.res";
        String mensaje = "Hola mundo desde mi nueva config";
        String userId = "USUARIOSAEX";
        int timeout = 20000;

        QueueConnectionFactory qcfReq = null;
        QueueConnectionFactory qcfRes = null;
        InitialContext context = new InitialContext();
        //Context context = new InitialContext();
        QueueConnectionFactory req = (QueueConnectionFactory) context.lookup("java:/jboss/ConnectionFactory");

        //qcfReq = openConnection(context, Constants.CONN_TYPE_REQUEST);
        QueueConnection connectionReq = req.createQueueConnection();
        QueueSession queueSession =  connectionReq.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        MQQueue queuePut = new MQQueue(requestQueue);

        queuePut.setBooleanProperty(WMQConstants.WMQ_MQMD_WRITE_ENABLED, true);
        queuePut.setIntProperty(WMQConstants.WMQ_MQMD_MESSAGE_CONTEXT, WMQConstants.WMQ_MDCTX_SET_IDENTITY_CONTEXT);
        QueueSender sender =  queueSession.createSender(queuePut);
        TextMessage message = queueSession.createTextMessage(mensaje);

        String correlationId = UUID.randomUUID().toString();

        Destination destination = new MQQueue(responseQueue);
        message.setJMSReplyTo(destination);
        message.setJMSCorrelationID(correlationId);
        message.setStringProperty(WMQConstants.JMS_IBM_MQMD_USERIDENTIFIER, userId);
        sender.send(message);

        QueueConnectionFactory res = (QueueConnectionFactory) context.lookup("java:/jboss/ConnectionFactory");

        QueueConnection connectionRes = res.createQueueConnection();

        queueSession =  connectionRes.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        connectionRes.start();

        MQQueue queueGet = new MQQueue(responseQueue);
        QueueReceiver receiver = queueSession.createReceiver(queueGet, "JMSCorrelationID = '"+correlationId+"'");

        TextMessage messageGet =  (TextMessage)receiver.receive(timeout);
        return messageGet.getText();

    }
}
