/*******************************************************************************
 * Copyright 2015-2021 Sergey Karpushin
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package integr.org.summerb.users.impl.dao.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;
import org.springframework.test.annotation.SystemProfileValueSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.summerb.users.api.UserService;
import org.summerb.users.api.dto.User;
import org.summerb.users.api.dto.UserFactory;
import org.summerb.users.api.exceptions.UserNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-users-app-context.xml")
@ProfileValueSourceConfiguration(SystemProfileValueSource.class)
@Transactional
public class UserCachedTest {
	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@Autowired
	@Qualifier("userServiceNoncached")
	private UserService userServiceNoncached;

	@Test
	public void testGetUserByUuid_expectReferencesEqualityForReturnedDtos() throws Exception {
		User userToCreate = UserFactory.createNewUserTemplate();
		userToCreate = userService.createUser(userToCreate);

		User foundUser = userService.getUserByUuid(userToCreate.getUuid());
		User foundUserCached = userService.getUserByUuid(userToCreate.getUuid());
		assertTrue(foundUser == foundUserCached);
	}

	@Test
	public void testGetUserByEmail_expectReferencesEqualityForReturnedDtos() throws Exception {
		User userToCreate = UserFactory.createNewUserTemplate();
		userToCreate = userService.createUser(userToCreate);

		User foundUser = userService.getUserByEmail(userToCreate.getEmail());
		User foundUserCached = userService.getUserByEmail(userToCreate.getEmail());
		assertTrue(foundUser == foundUserCached);
	}

	@Test
	public void testUpdateUser_expectReferencesNonEqualityForReturnedDtos() throws Exception {
		User userToCreate = UserFactory.createNewUserTemplate();
		userToCreate.setDisplayName("Display name");
		userToCreate = userService.createUser(userToCreate);

		User foundUser = userService.getUserByUuid(userToCreate.getUuid());

		userToCreate.setDisplayName("Another display name");
		userService.updateUser(userToCreate);

		User foundUserAgain = userService.getUserByUuid(userToCreate.getUuid());

		assertTrue(foundUser != foundUserAgain);
		assertTrue(foundUserAgain.getDisplayName().equals("Another display name"));
	}

	@Test // (expected=UserNotFoundException.class)
	public void testDeleteUser_expectUserNotFoundException() throws Exception {
		User userToCreate = UserFactory.createNewUserTemplate();
		userToCreate = userService.createUser(userToCreate);

		userService.getUserByUuid(userToCreate.getUuid());

		userService.deleteUserByUuid(userToCreate.getUuid());

		try {
			userService.getUserByUuid(userToCreate.getUuid());
			fail();
		} catch (UserNotFoundException e) {

		}
	}

	@Test
	public void testPerformance_expectCacheFaster() throws Exception {
		User userToCreate = UserFactory.createNewUserTemplate();
		userToCreate = userService.createUser(userToCreate);
		int cycles = 1000;

		long before = new Date().getTime();
		for (int i = 0; i < cycles; i++) {
			userService.getUserByUuid(userToCreate.getUuid());
		}
		long after = new Date().getTime() - before;

		long beforeNonCached = new Date().getTime();
		for (int i = 0; i < cycles; i++) {
			userServiceNoncached.getUserByUuid(userToCreate.getUuid());
		}
		long afterNonCached = new Date().getTime() - beforeNonCached;

		System.out.println("Cached: " + after + "ms");
		System.out.println("Noncached: " + afterNonCached + "ms");
		assertTrue(afterNonCached / 5 > after);
	}

}
