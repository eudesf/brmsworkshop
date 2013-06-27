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
		ResourceChangeScannerConfiguration configuration = ResourceFactory.getResourceChangeScannerService().newResourceChangeScannerConfiguration();
		configuration.setProperty("drools.resource.scanner.interval", "10");
		ResourceFactory.getResourceChangeScannerService().configure(configuration);
		
		KnowledgeAgent knowledgeAgent = KnowledgeAgentFactory.newKnowledgeAgent("MyAgent");
		knowledgeAgent.applyChangeSet(new ClassPathResource("changeset.xml"));
		KnowledgeBase knowledgeBase = knowledgeAgent.getKnowledgeBase();
		StatefulKnowledgeSession ksession = knowledgeBase.newStatefulKnowledgeSession();

		ResourceFactory.getResourceChangeNotifierService().start();
		ResourceFactory.getResourceChangeScannerService().start();
		
		FactType factType = knowledgeBase.getFactType("cleartech", "Customer");
		Object fact = factType.newInstance();
		factType.set(fact, "age", 28);
		
		ksession.insert(fact);
		ksession.fireAllRules();
		System.out.println("approved? -> " + factType.get(fact, "approved"));
		
		Thread.sleep(60000);
		
		Object fact2 = factType.newInstance();
		factType.set(fact2, "age", 28);
		
		knowledgeBase = knowledgeAgent.getKnowledgeBase();
		ksession = knowledgeBase.newStatefulKnowledgeSession();
		ksession.insert(fact2);
		ksession.fireAllRules();
		System.out.println("aprovado? -> " + factType.get(fact2, "approved") );
	}
	

}
