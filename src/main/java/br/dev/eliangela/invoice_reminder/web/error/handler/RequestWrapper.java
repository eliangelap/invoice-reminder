package br.dev.eliangela.invoice_reminder.web.error.handler;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class RequestWrapper extends HttpServletRequestWrapper {

    private String requestData = null;

    public RequestWrapper(HttpServletRequest request) {
        super(request);

        try {
            requestData = new String(request.getInputStream().readAllBytes(), StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            //
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStreamImpl(new StringReader(requestData));
    }

    private class ServletInputStreamImpl extends ServletInputStream {

        private final StringReader reader;

        public ServletInputStreamImpl(StringReader reader) {
            this.reader = reader;
        }

        @Override
        public int read() throws IOException {
            return reader.read();
        }

        @Override
        public void setReadListener(ReadListener listener) {
            ReadListener readListener = listener;

            try {
                if (!isFinished()) {
                    readListener.onDataAvailable();
                } else {
                    readListener.onAllDataRead();
                }
            } catch (IOException io) {
                //
            }
        }

        @Override
        public boolean isReady() {
            return isFinished();
        }

        @Override
        public boolean isFinished() {
            try {
                return reader.read() < 0;
            } catch (IOException e) {
                //
            }

            return false;
        }
    }

}
