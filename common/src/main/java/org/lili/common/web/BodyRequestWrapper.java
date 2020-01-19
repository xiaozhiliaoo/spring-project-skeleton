package org.lili.common.web;


import org.lili.common.util.RequestUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class BodyRequestWrapper extends HttpServletRequestWrapper {
	private final String body;

	public BodyRequestWrapper(HttpServletRequest request) {
		super(request);
		body = RequestUtils.getRequestJsonStr(request);
	}
	
	@Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes());
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }
    
    public String getBody() {
        return this.body.toString();
    }
}