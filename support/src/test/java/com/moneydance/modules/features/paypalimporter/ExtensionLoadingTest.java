package com.moneydance.modules.features.paypalimporter;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads the extension's main class from the packaged extension archive, with
 * only the libraries that Moneydance provides on the class path. Unlike the
 * other tests, which run with all dependencies on the class path, this
 * catches dependencies that are missing from the archive.
 */
public final class ExtensionLoadingTest {

    @Test
    public void testMainLoadsFromExtensionArchive() throws Exception {
        final List<URL> urls = new ArrayList<>();
        urls.add(new File(System.getProperty("paypalimporter.extension")).toURI().toURL());
        for (String path : System.getProperty("paypalimporter.moneydance").split(File.pathSeparator)) {
            urls.add(new File(path).toURI().toURL());
        }
        try (URLClassLoader classLoader = new URLClassLoader(
                urls.toArray(new URL[0]), ClassLoader.getPlatformClassLoader())) {
            final Class<?> mainClass = Class.forName(Main.class.getName(), true, classLoader);
            assertNotNull(mainClass.getDeclaredConstructor().newInstance());
        }
    }
}
