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
package org.summerb.easycrud.api.query.restrictions;

import org.summerb.easycrud.api.query.Restriction;

import com.google.common.base.Preconditions;

public class NumberGreaterOrEqualRestriction extends NegativableRestrictionBase implements Restriction<Long> {
	private static final long serialVersionUID = 471006414190458384L;

	private long value;

	public NumberGreaterOrEqualRestriction() {
	}

	public NumberGreaterOrEqualRestriction(long value) {
		this.value = value;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	@Override
	public boolean isMeet(Long subjectValue) {
		Preconditions.checkArgument(subjectValue != null);
		return isNegative() ? subjectValue < value : subjectValue >= value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (value ^ (value >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		NumberGreaterOrEqualRestriction other = (NumberGreaterOrEqualRestriction) obj;
		if (value != other.value)
			return false;
		return true;
	}
}
