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
package org.summerb.webappboilerplate.security.ve;

import org.summerb.spring.security.SecurityMessageCodes;
import org.summerb.users.api.dto.User;
import org.summerb.validation.ValidationError;

public class RegistrationActivationRequiredValidationError extends ValidationError {
	private static final long serialVersionUID = 4448690696428396195L;

	public RegistrationActivationRequiredValidationError() {
		super(SecurityMessageCodes.CANT_LOGIN_UNTIL_REGISTRATION_ACTIVATED, User.FN_EMAIL);
	}
}
