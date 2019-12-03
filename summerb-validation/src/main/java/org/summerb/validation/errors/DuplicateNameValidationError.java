/*******************************************************************************
 * Copyright 2015-2019 Sergey Karpushin
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
package org.summerb.validation.errors;

import org.summerb.validation.ValidationError;

public class DuplicateNameValidationError extends ValidationError {
	private static final long serialVersionUID = -537217996301287218L;

	/**
	 * @deprecated used only for serialization
	 */
	@Deprecated
	public DuplicateNameValidationError() {
		super();
	}

	public DuplicateNameValidationError(String fieldToken) {
		super("validation.duplicateName", fieldToken);
	}
}