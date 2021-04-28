package com.example.dronepoc.utils;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ByteArrayServletStream extends ServletOutputStream {
    ByteArrayOutputStream baos;

    public ByteArrayServletStream(ByteArrayOutputStream baos) {
        this.baos = baos;
    }

    @Override
    public void write(int b) throws IOException {
        baos.write(b);
    }
    
    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener listener) {

    }
}
