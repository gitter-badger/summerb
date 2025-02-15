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
package org.summerb.webappboilerplate.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Presentation model used to facilitate some operations needed while jsp
 * processing
 * 
 * @author sergey.karpushin
 * 
 */
public class ListPm<T> {
	/**
	 * Underlining items
	 */
	private List<T> items;

	public ListPm(List<T> items) {
		this.items = items;
	}

	public ListPm() {
		this(new ArrayList<T>());
	}

	public int getSize() {
		return items.size();
	}

	public boolean getHasItems() {
		return items.size() > 0;
	}

	public void add(T item) {
		items.add(item);
	}

	public List<T> getItems() {
		return items;
	}
}
