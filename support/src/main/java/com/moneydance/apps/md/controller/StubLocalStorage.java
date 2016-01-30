// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2016 Florian J. Breunig. All rights reserved.

package com.moneydance.apps.md.controller;

import com.infinitekind.moneydance.model.LocalStorage;
import com.infinitekind.tiksync.SyncRecord;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Florian J. Breunig
 */
public final class StubLocalStorage extends LocalStorage {

    private static final long serialVersionUID = 1L;

    StubLocalStorage() {
        this.authenticationCache = new SyncRecord();
    }

    @Override
    public long getLastModified(final String arg0) {
        return 0;
    }

    @Override
    public boolean delete(final String arg0) {
        return true;
    }

    @Override
    public boolean exists(final String arg0) {
        return true;
    }

    @Override
    public boolean isFolder(final String arg0) {
        return false;
    }

    @Override
    public String[] listFiles(final String arg0) {
        return new String[0];
    }

    @Override
    public String[] listSubfolders(final String arg0) {
        return new String[0];
    }

    @Override
    public void moveFile(final String arg0, final String arg1) {
        // ignore
    }

    @Override
    public InputStream openFileForReading(final String arg0) {
        return null;
    }

    @Override
    public OutputStream openFileForWriting(final String arg0) {
        return null;
    }

    @Override
    public long readFromFile(final String arg0, final OutputStream arg1) {
        return 0;
    }

    @Override
    public boolean save() {
        return true;
    }

    @Override
    public long writeToFile(final String arg0, final InputStream arg1) {
        return 0;
    }

    @Override
    public void writeToFileAtomically(final byte[] arg0, final String arg1) {
        // ignore
    }
}
