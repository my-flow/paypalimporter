package com.moneydance.apps.md.controller;

import com.infinitekind.moneydance.model.AccountBook;
import com.infinitekind.moneydance.model.LocalStorage;
import com.infinitekind.tiksync.SyncRecord;
import com.infinitekind.tiksync.SyncStorage;

import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Test stub for LocalStorage.
 */
public final class StubLocalStorage extends LocalStorage {

    private static final long serialVersionUID = 1L;

    /**
     * Minimal stub implementation of SyncStorage for testing.
     */
    private static final class StubSyncStorage implements SyncStorage {
        @Override
        public String getUnderlyingPathOrURL() {
            return "";
        }

        @Override
        public String getSyncTypeID() {
            return "stub";
        }

        @Override
        public BufferedSource readUnencrypted(final String path) {
            return new Buffer();
        }

        @Override
        public void writeUnencryptedFile(final String path, final BufferedSource source) {
            // ignore
        }

        @Override
        public byte[] encryptBytes(final byte[] data) {
            return data;
        }

        @Override
        public BufferedSource readFile(final String path) {
            return new Buffer();
        }

        @Override
        public com.infinitekind.tiksync.SyncFolder folderForPath(final String path) {
            return this;
        }

        @Override
        public void createParents() {
            // ignore
        }

        @Override
        public boolean exists(final String path) {
            return false;
        }

        @Override
        public boolean isFolder(final String path) {
            return false;
        }

        @Override
        public List<String> listFiles(final String path) {
            return Collections.emptyList();
        }

        @Override
        public List<String> listSubfolders(final String path) {
            return Collections.emptyList();
        }

        @Override
        public BufferedSource openFileForReading(final String path) {
            return new Buffer();
        }

        @Override
        public BufferedSink openFileForWriting(final String path) {
            return new Buffer();
        }

        @Override
        public void writeFile(final String path, final BufferedSource source) {
            // ignore
        }

        @Override
        public long readFile(final String path, final BufferedSink sink) {
            return 0;
        }

        @Override
        public void delete(final String path) {
            // ignore
        }

        @Override
        public void writeToFileAtomically(final byte[] data, final String path) {
            // ignore
        }

        @Override
        public void moveFile(final String fromPath, final String toPath) {
            // ignore
        }

        @Override
        public Date getModified(final String path) {
            return new Date(0);
        }
    }

    /**
     * Constructs a StubLocalStorage.
     *
     * @param book the account book
     */
    StubLocalStorage(final AccountBook book) {
        super(book, new StubSyncStorage());
        this.authenticationCache = new SyncRecord();
    }

    @Override
    public void delete(final String var1) throws Exception {
        // ignore
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
    public List<String> listFiles(final String var1) {
        return Collections.emptyList();
    }

    @Override
    public List<String> listSubfolders(final String var1) {
        return Collections.emptyList();
    }

    @Override
    public void moveFile(final String var1, final String var2) throws IOException {
        // ignore
    }

    @Override
    public BufferedSource openFileForReading(final String var1) {
        return new Buffer();
    }

    @Override
    public BufferedSink openFileForWriting(final String var1) {
        return new Buffer();
    }

    @Override
    public long readFile(final String var1, final OutputStream var2) throws Exception {
        return 0;
    }

    @Override
    public boolean save() {
        return true;
    }

    @Override
    public void writeFile(final String var1, final BufferedSource var2) throws Exception {
        // ignore
    }

    @Override
    public void writeFile(final String var1, final InputStream var2) throws Exception {
        // ignore
    }

    @Override
    public void writeToFileAtomically(final byte[] var1, final String var2) throws Exception {
        // ignore
    }
}
