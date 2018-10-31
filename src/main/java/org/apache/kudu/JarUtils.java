package org.apache.kudu;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class JarUtils {
  /**
   * Convert a resource URI to a jar file path name URL.
   *
   * <p>For example, will convert {code jar:file:/path/to/package.jar!log4j.properties} to
   * {code /path/to/package.jar}
   */
  public static File jarFileFromResourceURI(URI jarResourceURI) throws IOException {
    assert ("jar".equals(jarResourceURI.getScheme()));

    URI fileURI;
    try {
      fileURI = new URI(jarResourceURI.getSchemeSpecificPart()); // Strip off leading "jar:", leaving us with "file:".
      assert ("file".equals(fileURI.getScheme()));
    } catch (URISyntaxException ex) {
      throw new IOException("Unable to extract JAR filename from resource URI " + jarResourceURI, ex);
    }
    String path = fileURI.getSchemeSpecificPart(); // Strip off the "file:" scheme.

    // TODO(mpercy): should we use some supported API instead of just splitting on "!" ?
    int idx = path.indexOf("!"); // Resource URIs from jar files contain a ! to separate JAR filename from entry.
    if (idx != -1) {
      path = path.substring(0, idx);
    }
    return new File(path);
  }

  // TODO(mpercy): Use something off-the-shelf to unpack JAR files because maintaining permissions is platform-specific.
  public static void unpackJar(File jarFile, File destDir) throws IOException {
    if (!destDir.isDirectory()) {
      throw new IllegalArgumentException("destination " + destDir + " is not a directory");
    }
    try (ZipFile jar = new ZipFile(jarFile)) {
      Enumeration<? extends ZipEntry> entries = jar.entries();
      while (entries.hasMoreElements()) {
        ZipEntry entry = entries.nextElement();
        File file = new File(destDir.toString() + "/" + entry.getName());
        if (entry.isDirectory() && !file.exists()) {
          if (!file.mkdirs()) {
            throw new IOException("Unable to create directory " + file);
          }
          continue;
        } else if (!entry.isDirectory()) {
          if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
              throw new IOException("Unable to create directory " + file.getParentFile());
            }
          }
          try (BufferedInputStream inputStream = new BufferedInputStream(jar.getInputStream(entry))) {
            try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
              int lastReadSize = 0;
              byte[] buf = new byte[4096];
              while ((lastReadSize = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, lastReadSize);
              }
              outputStream.flush();
            }
          }
        }
      }
    }
  }

  public static URI findJarResource(String resourceName) throws IOException {
    Enumeration<URL> resources = JarUtils.class.getClassLoader().getResources(resourceName);
    while (resources.hasMoreElements()) {
      URL url = resources.nextElement();
      try {
        URI uri = url.toURI();
        if ("jar".equals(uri.getScheme())) {
          return uri;
        }
      } catch (URISyntaxException ex) {
        continue;
      }
    }
    throw new FileNotFoundException("unable to find jar file containing resource " + resourceName);
  }

  public static File jarFileFromResourceName(String resourceName) throws IOException {
    return jarFileFromResourceURI(findJarResource(resourceName));
  }

  public static void unpackJarFromResource(String resourceName, File destDir) throws IOException {
    unpackJar(jarFileFromResourceName(resourceName), destDir);
  }

  /**
   * Print the entries in a JAR file
   */
  public static void printJarEntries(File jarFile) throws IOException {
    ZipInputStream zip = new ZipInputStream(jarFile.toURL().openStream());
    ZipEntry entry;
    while ((entry = zip.getNextEntry()) != null) {
      System.out.println(entry.getName());
    }
  }


  public static void listJarFromResource(String resourceName) throws IOException {
    printJarEntries(jarFileFromResourceName(resourceName));
  }
}
