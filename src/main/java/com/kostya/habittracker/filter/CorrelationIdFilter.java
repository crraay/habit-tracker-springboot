package com.kostya.habittracker.filter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

	public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";

	@Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
			throws ServletException, IOException {

		String correlationId = Optional.ofNullable(request.getHeader(CORRELATION_ID_HEADER))
				.filter(h -> !h.isBlank())
				.orElse(UUID.randomUUID().toString());

		try {
			MDC.put(CORRELATION_ID_HEADER, correlationId);
			response.setHeader(CORRELATION_ID_HEADER, correlationId);
			filterChain.doFilter(request, response);
		} finally {
			MDC.remove(CORRELATION_ID_HEADER);
		}
	}
}


