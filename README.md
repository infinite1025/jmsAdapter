# jmsAdapter

Parámetros de sistema
=====================
/system-property=jms.connectionfactory.jndi:write-attribute(value=java:/jboss/ConnectionFactory)
/system-property=jms.dest.queue.jndi:add(value=java:/queue/ticketOrderQueue)
/system-property=jms.queue.manager.channel:add(value=....SRVCONN)
/system-property=jms.queue.manager.host:add(value=localhost)
/system-property=jms.queue.manager.name:add(value=QMGR1)
/system-property=jms.queue.manager.port:add(value=1425)
/system-property=jms.queue.name:add(value=QL....RESP)

Parámetros del EJB3
=====================
/subsystem=ejb3:write-attribute(name=default-resource-adapter-name, value=wmq.jmsra.rar)
/subsystem=ejb3:write-attribute(name=enable-statistics, value=true)

Parámetros del adaptador MQ
============================
/subsystem=resource-adapters/resource-adapter=wmq.jmsra.rar:add(archive=wmq.jmsra.rar,transaction-support=NoTransaction)
/subsystem=resource-adapters/resource-adapter=wmq.jmsra.rar/admin-objects=QueuePool:add(class-name=com.ibm.mq.connector.outbound.MQQueueProxy,jndi-name=${jms.dest.queue.jndi},use-java-context=false,enabled=true)
/subsystem=resource-adapters/resource-adapter=wmq.jmsra.rar/admin-objects=QueuePool/config-properties=baseQueueName:add(value=${jms.queue.name})
/subsystem=resource-adapters/resource-adapter=wmq.jmsra.rar/admin-objects=QueuePool/config-properties=baseQueueManagerName:add(value=${jms.queue.manager.name})
/subsystem=resource-adapters/resource-adapter=wmq.jmsra.rar/connection-definitions=connection-definition:add(class-name=com.ibm.mq.connector.outbound.ManagedConnectionFactoryImpl,jndi-name=${jms.connectionfactory.jndi}, use-java-context=false)
/subsystem=resource-adapters/resource-adapter=wmq.jmsra.rar/connection-definitions=connection-definition/config-properties=hostName:add(value=${jms.queue.manager.host})
/subsystem=resource-adapters/resource-adapter=wmq.jmsra.rar/connection-definitions=connection-definition/config-properties=queueManager:add(value=${jms.queue.manager.name})
/subsystem=resource-adapters/resource-adapter=wmq.jmsra.rar/connection-definitions=connection-definition/config-properties=port:add(value=${jms.queue.manager.port})
/subsystem=resource-adapters/resource-adapter=wmq.jmsra.rar/connection-definitions=connection-definition/config-properties=channel:add(value=${jms.queue.manager.channel})
/subsystem=resource-adapters/resource-adapter=wmq.jmsra.rar/connection-definitions=connection-definition/config-properties=transportType:add(value=CLIENT)

:reload
