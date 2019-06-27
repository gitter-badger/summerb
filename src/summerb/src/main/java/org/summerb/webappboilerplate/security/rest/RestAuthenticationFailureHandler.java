package org.summerb.webappboilerplate.security.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.summerb.approaches.validation.FieldValidationException;
import org.summerb.approaches.validation.ValidationErrors;
import org.summerb.utils.exceptions.ExceptionUtils;
import org.summerb.utils.exceptions.dto.ExceptionInfo;
import org.summerb.utils.exceptions.dto.GenericServerErrorResult;
import org.summerb.utils.exceptions.translator.ExceptionTranslator;
import org.summerb.utils.json.JsonResponseWriter;
import org.summerb.utils.json.JsonResponseWriterGsonImpl;

public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {
	private JsonResponseWriter jsonResponseHelper;
	private ExceptionTranslator exceptionTranslator;

	public RestAuthenticationFailureHandler() {
		jsonResponseHelper = new JsonResponseWriterGsonImpl();
	}

	public RestAuthenticationFailureHandler(JsonResponseWriter jsonResponseHelper) {
		this.jsonResponseHelper = jsonResponseHelper;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		FieldValidationException fve = ExceptionUtils.findExceptionOfType(exception, FieldValidationException.class);
		if (fve != null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			jsonResponseHelper.writeResponseBody(new ValidationErrors(fve.getErrors()), response);
			return;
		}

		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		GenericServerErrorResult responseBody = new GenericServerErrorResult(
				exceptionTranslator.buildUserMessage(exception, LocaleContextHolder.getLocale()),
				new ExceptionInfo(exception));
		jsonResponseHelper.writeResponseBody(responseBody, response);
	}

	public ExceptionTranslator getExceptionTranslator() {
		return exceptionTranslator;
	}

	@Autowired
	public void setExceptionTranslator(ExceptionTranslator exceptionTranslator) {
		this.exceptionTranslator = exceptionTranslator;
	}
}
