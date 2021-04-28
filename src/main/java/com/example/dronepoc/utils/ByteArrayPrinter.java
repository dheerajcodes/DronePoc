package com.example.dronepoc.utils;

import javax.servlet.ServletOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

public class ByteArrayPrinter {
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private final PrintWriter pw = new PrintWriter(baos);
    private final ServletOutputStream sos = new ByteArrayServletStream(baos);

    public PrintWriter getWriter() {
        return pw;
    }

    public ServletOutputStream getStream() {
        return sos;
    }

    public byte[] toByteArray() {
        return baos.toByteArray();
    }
}
