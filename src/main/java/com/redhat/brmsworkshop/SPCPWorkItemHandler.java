package com.redhat.brmsworkshop;

import org.drools.process.instance.WorkItemHandler;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;

public class SPCPWorkItemHandler implements WorkItemHandler{

	public void abortWorkItem(WorkItem arg0, WorkItemManager arg1) {
		// TODO Auto-generated method stub
		
	}

	public void executeWorkItem(WorkItem workItem, WorkItemManager workItemManager) {
		System.out.println("Executing work item");
		workItemManager.completeWorkItem(workItem.getId(), null);
	}

}
