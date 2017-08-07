package org.summerb.approaches.jdbccrud.rest.commonpathvars;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Optional;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

/**
 * This bean will help Swagger to discovered path variables described using
 * {@link HasCommonPathVariable} and {@link HasCommonPathVariables} annotations
 * 
 * @author sergeyk
 *
 */
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
public class CommonPathVariableOperationBuilderPlugin implements OperationBuilderPlugin {
	protected Logger log = Logger.getLogger(getClass());

	@Autowired
	private TypeResolver typeResolver;

	public CommonPathVariableOperationBuilderPlugin() {
	}

	@Override
	public boolean supports(DocumentationType delimiter) {
		return true;
	}

	@Override
	public void apply(OperationContext opCtx) {
		List<Parameter> ret = new ArrayList<Parameter>();
		Optional<HasCommonPathVariable> annSingle = opCtx.findControllerAnnotation(HasCommonPathVariable.class);
		if (annSingle.isPresent()) {
			ret.add(addParameter(annSingle.get()));
		}

		Optional<HasCommonPathVariables> annPlural = opCtx.findControllerAnnotation(HasCommonPathVariables.class);
		if (annPlural.isPresent()) {
			for (HasCommonPathVariable ann : annPlural.get().value()) {
				ret.add(addParameter(ann));
			}
		}
		opCtx.operationBuilder().parameters(ret);
	}

	private Parameter addParameter(HasCommonPathVariable ann) {
		ParameterBuilder pb = new ParameterBuilder();
		pb.parameterType("path").name(ann.name()).type(typeResolver.resolve(ann.type()));
		pb.modelRef(new ModelRef("string"));
		pb.required(true);
		if (!"".equals(ann.defaultValue())) {
			pb.defaultValue(ann.defaultValue());
		}
		return pb.build();
	}
}
