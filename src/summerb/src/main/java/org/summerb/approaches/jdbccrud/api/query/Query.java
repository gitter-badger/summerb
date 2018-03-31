package org.summerb.approaches.jdbccrud.api.query;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.PropertyAccessor;
import org.summerb.approaches.jdbccrud.api.EasyCrudDao;
import org.summerb.approaches.jdbccrud.api.EasyCrudService;
import org.summerb.approaches.jdbccrud.api.QueryToNativeSqlCompiler;
import org.summerb.approaches.jdbccrud.api.query.restrictions.BooleanEqRestriction;
import org.summerb.approaches.jdbccrud.api.query.restrictions.IsNullRestriction;
import org.summerb.approaches.jdbccrud.api.query.restrictions.NumberBetweenRestriction;
import org.summerb.approaches.jdbccrud.api.query.restrictions.NumberEqRestriction;
import org.summerb.approaches.jdbccrud.api.query.restrictions.NumberGreaterOrEqualRestriction;
import org.summerb.approaches.jdbccrud.api.query.restrictions.NumberOneOfRestriction;
import org.summerb.approaches.jdbccrud.api.query.restrictions.StringBetweenRestriction;
import org.summerb.approaches.jdbccrud.api.query.restrictions.StringContainsRestriction;
import org.summerb.approaches.jdbccrud.api.query.restrictions.StringEqRestriction;
import org.summerb.approaches.jdbccrud.api.query.restrictions.StringLengthBetweenRestriction;
import org.summerb.approaches.jdbccrud.api.query.restrictions.StringOneOfRestriction;

/**
 * This class is used to build very simple queries to {@link EasyCrudService}
 * (nothing fancy -- no aggregation, etc).
 * 
 * I.e.: <code>
 * Query.n().eq(HasId.FN_ID, 123)
 * </code>
 * 
 * It's also capable of evaluating if any in-memory DTO matches this query.
 * 
 * Each impl of {@link EasyCrudDao} supposed to be injected with it's own impl
 * of {@link QueryToNativeSqlCompiler} that can convert this abstracted Query to
 * query native to actual data source.
 * 
 * @author sergey.karpushin
 * 
 */
public class Query {
	private List<Restriction<PropertyAccessor>> list;

	public Query() {
		list = new LinkedList<Restriction<PropertyAccessor>>();
	}

	/**
	 * Shortcut for creating new instance of this class
	 */
	public static Query n() {
		return new Query();
	}

	public boolean isMeet(PropertyAccessor subjectValue) {
		for (Restriction<PropertyAccessor> r : list) {
			if (!r.isMeet(subjectValue)) {
				return false;
			}
		}
		return true;
	}

	public List<Restriction<PropertyAccessor>> getRestrictions() {
		return list;
	}

	public Query or(Query... subqueries) {
		list.add(new DisjunctionCondition(subqueries));
		return this;
	}

	public Query isNull(String fieldName) {
		list.add(new FieldCondition(fieldName, new IsNullRestriction()));
		return this;
	}

	public Query isNotNull(String fieldName) {
		IsNullRestriction r = new IsNullRestriction();
		r.setNegative(true);
		list.add(new FieldCondition(fieldName, r));
		return this;
	}

	public Query eq(String fieldName, String value) {
		list.add(new FieldCondition(fieldName, new StringEqRestriction(value)));
		return this;
	}

	public Query ne(String fieldName, String value) {
		StringEqRestriction r = new StringEqRestriction(value);
		r.setNegative(true);
		list.add(new FieldCondition(fieldName, r));
		return this;
	}

	public Query lengthBetween(String fieldName, int minLength, int maxLength) {
		list.add(new FieldCondition(fieldName, new StringLengthBetweenRestriction(minLength, maxLength)));
		return this;
	}

	public Query in(String fieldName, String... values) {
		list.add(new FieldCondition(fieldName, new StringOneOfRestriction(values)));
		return this;
	}

	public Query notIn(String fieldName, String... values) {
		StringOneOfRestriction notIn = new StringOneOfRestriction(values);
		notIn.setNegative(true);
		list.add(new FieldCondition(fieldName, notIn));
		return this;
	}

	public Query contains(String fieldName, String value) {
		list.add(new FieldCondition(fieldName, new StringContainsRestriction(value)));
		return this;
	}

	public Query notContains(String fieldName, String value) {
		StringContainsRestriction r = new StringContainsRestriction(value);
		r.setNegative(true);
		list.add(new FieldCondition(fieldName, r));
		return this;
	}

	public Query isTrue(String fieldName) {
		list.add(new FieldCondition(fieldName, new BooleanEqRestriction(true)));
		return this;
	}

	public Query isFalse(String fieldName) {
		list.add(new FieldCondition(fieldName, new BooleanEqRestriction(false)));
		return this;
	}

	public Query ne(String fieldName, boolean value) {
		BooleanEqRestriction r = new BooleanEqRestriction(value);
		r.setNegative(true);
		list.add(new FieldCondition(fieldName, r));
		return this;
	}

	public Query eq(String fieldName, long value) {
		list.add(new FieldCondition(fieldName, new NumberEqRestriction(value)));
		return this;
	}

	public Query ne(String fieldName, long value) {
		NumberEqRestriction r = new NumberEqRestriction(value);
		r.setNegative(true);
		list.add(new FieldCondition(fieldName, r));
		return this;
	}

	public Query in(String fieldName, Long... values) {
		list.add(new FieldCondition(fieldName, new NumberOneOfRestriction(values)));
		return this;
	}

	public Query notIn(String fieldName, Long... values) {
		NumberOneOfRestriction r = new NumberOneOfRestriction(values);
		r.setNegative(true);
		list.add(new FieldCondition(fieldName, r));
		return this;
	}

	public Query between(String fieldName, long lowerBound, long upperBound) {
		list.add(new FieldCondition(fieldName, new NumberBetweenRestriction(lowerBound, upperBound)));
		return this;
	}

	public Query between(String fieldName, String lowerBound, String upperBound) {
		list.add(new FieldCondition(fieldName, new StringBetweenRestriction(lowerBound, upperBound)));
		return this;
	}

	public Query notBetween(String fieldName, long lowerBound, long upperBound) {
		NumberBetweenRestriction r = new NumberBetweenRestriction(lowerBound, upperBound);
		r.setNegative(true);
		list.add(new FieldCondition(fieldName, r));
		return this;
	}

	public Query ge(String fieldName, long value) {
		list.add(new FieldCondition(fieldName, new NumberGreaterOrEqualRestriction(value)));
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((list == null) ? 0 : list.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Query other = (Query) obj;
		if (list == null) {
			if (other.list != null)
				return false;
		} else if (!list.equals(other.list))
			return false;
		return true;
	}

}
