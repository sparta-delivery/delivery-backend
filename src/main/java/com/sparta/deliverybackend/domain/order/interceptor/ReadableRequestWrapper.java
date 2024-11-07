package com.sparta.deliverybackend.domain.order.interceptor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

// HttpServletRequest 의 inputStream 은 한번 읽으면 다시 읽을 수 없다.
// OrderStatusValidationInterceptor 에서 reqDto 를 읽게 되면, Controller 에서 사용 불가 (stream 이 닫힌 상황)
// 아래 클래스를 통해 데이터를 request 에서 읽고 복사 한뒤 inputStream 관련 요청시 마다 복사된 데이터를 컨트롤러에 넘김
public class ReadableRequestWrapper extends HttpServletRequestWrapper {

	private final Charset encoding;
	private byte[] rawData;

	public ReadableRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);

		String characterEncoding = request.getCharacterEncoding();
		if (StringUtils.isBlank(characterEncoding)) {
			characterEncoding = StandardCharsets.UTF_8.name();
		}
		this.encoding = Charset.forName(characterEncoding);

		try {
			InputStream inputStream = request.getInputStream();
			this.rawData = IOUtils.toByteArray(inputStream);
		} catch (IOException e) {
			throw e;
		}
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.rawData);
		ServletInputStream servletInputStream = new ServletInputStream() {
			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {

			}

			public int read() throws IOException {
				return byteArrayInputStream.read();
			}
		};
		return servletInputStream;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(this.getInputStream(), this.encoding));
	}

	@Override
	public ServletRequest getRequest() {
		return super.getRequest();
	}
}

// 추후 코드 분석 필요