package com.kostya.habittracker.security;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kostya.habittracker.filter.CorrelationIdFilter;
import com.kostya.habittracker.model.ErrorResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
			throws IOException, ServletException {
		String path = request.getRequestURI();
		String cid = Optional.ofNullable(MDC.get(CorrelationIdFilter.CORRELATION_ID_HEADER)).orElse(null);
		String method = request.getMethod();
		log.warn("403 Forbidden: method={} path={}", method, path);

		ErrorResponse body = new ErrorResponse();
		body.setStatus(HttpServletResponse.SC_FORBIDDEN);
		body.setError("Forbidden");
		body.setMessage("Not allowed");
		body.setPath(path);
		body.setCorrelationId(cid);
		body.setTimestamp(Instant.now());

		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType("application/json");
		objectMapper.writeValue(response.getOutputStream(), body);
	}
}


