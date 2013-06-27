package com.redhat.brmsworkshop;

import org.drools.KnowledgeBase;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.definition.type.FactType;
import org.drools.io.ResourceChangeScannerConfiguration;
import org.drools.io.ResourceFactory;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;

public class Main {

	public static void main(String[] args) throws Exception {
		new Main().launch();
	}

	private void launch() throws Exception {
		
		KnowledgeAgent knowledgeAgent = KnowledgeAgentFactory.newKnowledgeAgent("MyAgent");
		knowledgeAgent.applyChangeSet(new ClassPathResource("changeset.xml"));
		KnowledgeBase knowledgeBase = knowledgeAgent.getKnowledgeBase();
		StatefulKnowledgeSession ksession = knowledgeBase.newStatefulKnowledgeSession();
		
		FactType factType = knowledgeBase.getFactType("cleartech", "Customer");
		Object fact = factType.newInstance();
		factType.set(fact, "age", 28);
		ksession.insert(fact);

		ksession.getWorkItemManager().registerWorkItemHandler("SCPC", new SPCPWorkItemHandler());
		
		ksession.startProcess("cleartech.CreditProcess");
		
		
	}

	private void startScannerService() throws Exception {
		ResourceChangeScannerConfiguration configuration = ResourceFactory.getResourceChangeScannerService().newResourceChangeScannerConfiguration();
		configuration.setProperty("drools.resource.scanner.interval", "10");
		ResourceFactory.getResourceChangeScannerService().configure(configuration);
		ResourceFactory.getResourceChangeNotifierService().start();
		ResourceFactory.getResourceChangeScannerService().start();
	}
	

}
