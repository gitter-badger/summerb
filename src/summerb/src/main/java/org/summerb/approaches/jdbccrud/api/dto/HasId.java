package org.summerb.approaches.jdbccrud.api.dto;

import org.summerb.approaches.jdbccrud.api.EasyCrudService;
import org.summerb.utils.DtoBase;

/**
 * Mark your DTO with this interface and clarify primary key type.
 * {@link EasyCrudService} works only with DTOs which impl this interface.
 * 
 * @author sergey.karpushin
 *
 * @param <TId>
 *            type of primary key
 */
public interface HasId<TId> extends DtoBase {
	public static final String FN_ID = "id";

	TId getId();

	void setId(TId id);
}
