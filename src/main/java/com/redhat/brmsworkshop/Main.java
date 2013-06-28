package com.redhat.brmsworkshop;

import org.drools.KnowledgeBase;
import org.drools.WorkingMemory;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.definition.type.FactType;
import org.drools.event.AgendaEventListener;
import org.drools.event.DefaultAgendaEventListener;
import org.drools.event.RuleFlowGroupActivatedEvent;
import org.drools.impl.StatefulKnowledgeSessionImpl;
import org.drools.io.ResourceChangeScannerConfiguration;
import org.drools.io.ResourceFactory;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;

import com.redhat.brmsworkshop.entities.CustomerPOJO;

public class Main {

	public static void main(String[] args) throws Exception {
		new Main().launch();
	}

	private void launch() throws Exception {

		KnowledgeAgent knowledgeAgent = KnowledgeAgentFactory
				.newKnowledgeAgent("MyAgent");
		knowledgeAgent.applyChangeSet(new ClassPathResource("changeset.xml"));
		KnowledgeBase knowledgeBase = knowledgeAgent.getKnowledgeBase();
		StatefulKnowledgeSession ksession = knowledgeBase
				.newStatefulKnowledgeSession();

		startScannerService();
		configureRulesFirePolicy(ksession);

		CustomerPOJO customer = createCustomer();
		ksession.insert(customer);
		
		
//		FactType factType = knowledgeBase.getFactType("cleartech", "Customer");
//		Object fact = factType.newInstance();
//		factType.set(fact, "age", 18);
//		factType.set(fact, "monthlyIncome", 5000);
//		factType.set(fact, "cpf", "999999999999");
//		ksession.insert(fact);

		ksession.getWorkItemManager().registerWorkItemHandler("SCPC",
				new SPCPWorkItemHandler(ksession));
		ksession.getWorkItemManager().registerWorkItemHandler("Approver",
				new ApproverWorkItemHandler(ksession));

		ksession.startProcess("cleartech.CreditProcess");

		System.out.println("Aprovado: " + customer.getApproved());
		System.out.println("Credit: " + customer.getCredit());
	}
	
	private CustomerPOJO createCustomer() { 
		CustomerPOJO customer = new CustomerPOJO();
		customer.setAge(18);
		customer.setCpf("99999999999");
		customer.setMonthlyIncome(5000);
		return customer;
	}
	
	
	@SuppressWarnings("unused")
	private void launchWithFact() throws Exception {

		KnowledgeAgent knowledgeAgent = KnowledgeAgentFactory
				.newKnowledgeAgent("MyAgent");
		knowledgeAgent.applyChangeSet(new ClassPathResource("changeset.xml"));
		KnowledgeBase knowledgeBase = knowledgeAgent.getKnowledgeBase();
		StatefulKnowledgeSession ksession = knowledgeBase
				.newStatefulKnowledgeSession();

		startScannerService();
		configureRulesFirePolicy(ksession);

		FactType factType = knowledgeBase.getFactType("cleartech", "Customer");
		Object fact = factType.newInstance();
		factType.set(fact, "age", 18);
		factType.set(fact, "monthlyIncome", 5000);
		factType.set(fact, "cpf", "999999999999");
		ksession.insert(fact);

		ksession.getWorkItemManager().registerWorkItemHandler("SCPC",
				new SPCPWorkItemHandler(ksession));
		ksession.getWorkItemManager().registerWorkItemHandler("Approver",
				new ApproverWorkItemHandler(ksession));

		ksession.startProcess("cleartech.CreditProcess");

		System.out.println("aprovado? -> " + factType.get(fact, "approved"));
	}

	private void startScannerService() {
		ResourceChangeScannerConfiguration configuration = ResourceFactory
				.getResourceChangeScannerService()
				.newResourceChangeScannerConfiguration();
		configuration.setProperty("drools.resource.scanner.interval", "10");
		ResourceFactory.getResourceChangeScannerService().configure(
				configuration);
		ResourceFactory.getResourceChangeNotifierService().start();
		ResourceFactory.getResourceChangeScannerService().start();
	}

	private void configureRulesFirePolicy(
			StatefulKnowledgeSession knowledgeSession) {
		final AgendaEventListener agendaEventListener = new DefaultAgendaEventListener() {
			public void afterRuleFlowGroupActivated(
					RuleFlowGroupActivatedEvent event,
					WorkingMemory workingMemory) {
				workingMemory.fireAllRules();
			}
		};
		((StatefulKnowledgeSessionImpl) knowledgeSession).session
				.addEventListener(agendaEventListener);
	}

}
