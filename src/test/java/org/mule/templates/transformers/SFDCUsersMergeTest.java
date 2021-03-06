/**
 * Mule Anypoint Template
 * Copyright (c) MuleSoft, Inc.
 * All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.templates.transformers.SFDCUsersMerge;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class SFDCUsersMergeTest {
	private static final String QUERY_COMPANY_A = "usersFromOrgA";
	private static final String QUERY_COMPANY_B = "usersFromOrgB";

	@Mock
	private MuleContext muleContext;

	@Test
	public void testMerge() throws TransformerException {
		List<Map<String, String>> usersA = createUserLists("A", 0, 1);
		List<Map<String, String>> usersB = createUserLists("B", 1, 2);

		MuleMessage message = new DefaultMuleMessage(null, muleContext);
		message.setInvocationProperty(QUERY_COMPANY_A, usersA.iterator());
		message.setInvocationProperty(QUERY_COMPANY_B, usersB.iterator());

		SFDCUsersMerge transformer = new SFDCUsersMerge();
		List<Map<String, String>> mergedList = (List<Map<String, String>>) transformer.transform(message, "UTF-8");

		System.out.println(mergedList);
		Assert.assertEquals("The merged list obtained is not as expected", createExpectedList(), mergedList);

	}

	private List<Map<String, String>> createExpectedList() {
		Map<String, String> user0 = new HashMap<String, String>();
		user0.put("IDInA", "0");
		user0.put("IDInB", "");
		user0.put("Email", "some.email.0@fakemail.com");
		user0.put("Name", "SomeName_0");
		user0.put("UserNameInA", "username_0_A");
		user0.put("UserNameInB", "");

		Map<String, String> user1 = new HashMap<String, String>();
		user1.put("IDInA", "1");
		user1.put("IDInB", "1");
		user1.put("Email", "some.email.1@fakemail.com");
		user1.put("Name", "SomeName_1");
		user1.put("UserNameInA", "username_1_A");
		user1.put("UserNameInB", "username_1_B");

		Map<String, String> user2 = new HashMap<String, String>();
		user2.put("IDInA", "");
		user2.put("IDInB", "2");
		user2.put("Email", "some.email.2@fakemail.com");
		user2.put("Name", "SomeName_2");
		user2.put("UserNameInA", "");
		user2.put("UserNameInB", "username_2_B");

		List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
		userList.add(user0);
		userList.add(user1);
		userList.add(user2);

		return userList;

	}

	private List<Map<String, String>> createUserLists(String orgId, int start, int end) {
		List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
		for (int i = start; i <= end; i++) {
			userList.add(createUser(orgId, i));
		}
		return userList;
	}

	private Map<String, String> createUser(String orgId, int sequence) {
		Map<String, String> user = new HashMap<String, String>();

		user.put("Id", new Integer(sequence).toString());
		user.put("Username", "username_" + sequence + "_" + orgId);
		user.put("Name", "SomeName_" + sequence);
		user.put("Email", "some.email." + sequence + "@fakemail.com");

		return user;
	}
}
