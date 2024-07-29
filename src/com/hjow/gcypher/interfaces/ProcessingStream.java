package com.hjow.gcypher.interfaces;

/** 스트림 처리 이벤트 */
public interface ProcessingStream {
    public boolean processing(byte[] buffer, int sizes);
}
