package org.summerb.approaches.jdbccrud.rest.commonpathvars;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.PathVariable;
import org.summerb.approaches.jdbccrud.rest.EasyCrudRestControllerBase;

/**
 * It's similar to {@link PathVariable}, but to be applied at the whole
 * controller level.
 * 
 * <p>
 * 
 * Designed to be used to annotate controllers, like subclasses of
 * {@link EasyCrudRestControllerBase} and then use variable of type
 * {@link PathVariablesMap} for each request method where you need this. This
 * variable will be resolved by the {@link PathVariablesMapArgumentResolver},
 * which is expected to be registered in site servlet, i.e.
 * 
 * <p>
 * 
 * <code>
 * &lt;mvc:annotation-driven&gt;
	&lt;mvc:argument-resolvers&gt;
		&lt;bean class=&quot;org.summerb.approaches.jdbccrud.rest.PathVariablesMapArgumentResolver&quot; /&gt;
	&lt;/mvc:argument-resolvers&gt;
&lt;/mvc:annotation-driven&gt;
 * </code>
 * 
 * In case Swagger is being used then
 * {@link org.summerb.approaches.jdbccrud.rest.commonpathvars.CommonPathVariableOperationBuilderPlugin}
 * needs to be registered as a Spring bean in the context (Swagger will locate
 * it automatically)
 * 
 * @author sergeyk
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HasCommonPathVariable {
	/**
	 * The URI template variable to bind to.
	 */
	String name();

	Class<?> type();

	String defaultValue() default "";
}
