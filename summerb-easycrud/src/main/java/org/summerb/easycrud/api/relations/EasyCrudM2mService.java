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
package org.summerb.easycrud.api.relations;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.summerb.easycrud.api.EasyCrudService;
import org.summerb.easycrud.api.dto.HasId;
import org.summerb.easycrud.api.dto.relations.ManyToManyDto;
import org.summerb.security.api.exceptions.NotAuthorizedException;
import org.summerb.validation.FieldValidationException;

/**
 * Service for m2m references. It's based on {@link EasyCrudService}, but it's
 * more for compatibility reasons. Primarily methods of this interface should be
 * used.
 * 
 * it's assumed that there are many referencers, but few referencee
 * 
 * @author sergeyk
 *
 * @param <T1Id>
 *            id of referencer
 * @param <T1Dto>
 *            referencer. Dto that suppose to reference dictionary objects
 * @param <T2Id>
 *            id of referencee
 * @param <T2Dto>
 *            referencee dto. The one is referenced by many referencers
 */
public interface EasyCrudM2mService<T1Id, T1Dto extends HasId<T1Id>, T2Id, T2Dto extends HasId<T2Id>>
		extends EasyCrudService<Long, ManyToManyDto<T1Id, T2Id>> {

	List<T2Dto> findReferenceeByReferencer(T1Id referencerId);

	/**
	 * @return mapping between referencer id and list of referencee
	 */
	Map<T1Id, List<T2Dto>> findReferenceeByReferencers(Set<T1Id> referencerIds);

	ManyToManyDto<T1Id, T2Id> addReferencee(T1Id referencerId, T2Id referenceeId)
			throws FieldValidationException, NotAuthorizedException;

	void removeReferencee(T1Id referencerId, T2Id referenceeId) throws NotAuthorizedException;
}
