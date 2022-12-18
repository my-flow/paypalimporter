package com.moneydance.apps.md.controller;

import com.infinitekind.moneydance.model.LocalStorage;
import com.infinitekind.tiksync.SyncRecord;

import java.io.InputStream;
import java.io.OutputStream;

public final class StubLocalStorage extends LocalStorage {

    private static final long serialVersionUID = 1L;

    StubLocalStorage() {
        super();
        this.authenticationCache = new SyncRecord();
    }

    @Override
    public long getLastModified(final String var1) {
        return 0;
    }

    @Override
    public boolean delete(final String var1) {
        return true;
    }

    @Override
    public boolean exists(final String var1) {
        return true;
    }

    @Override
    public boolean isFolder(final String var1) {
        return false;
    }

    @Override
    public String[] listFiles(final String var1) {
        return new String[0];
    }

    @Override
    public String[] listSubfolders(final String var1) {
        return new String[0];
    }

    @Override
    public void moveFile(final String var1, final String var2) {
        // ignore
    }

    @Override
    public InputStream openFileForReading(final String var1) {
        return new InputStream() {
            @Override
            public int read() {
                return 0;
            }
        };
    }

    @Override
    public OutputStream openFileForWriting(final String var1) {
        return new OutputStream() {
            @Override
            public void write(final int data) {
                // ignore
            }
        };
    }

    @Override
    public long readFromFile(final String var1, final OutputStream var2) {
        return 0;
    }

    @Override
    public boolean save() {
        return true;
    }

    @Override
    public long writeToFile(final String var1, final InputStream var2) {
        return 0;
    }

    @Override
    public void writeToFileAtomically(final byte[] var1, final String var2) {
        // ignore
    }
}
