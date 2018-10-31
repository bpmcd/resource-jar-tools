package org.apache.kudu;

import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import static org.junit.Assert.assertEquals;

public class ListJarFromResourceTest {
  @Test
  public void testFindLocalResource() throws IOException {
    Enumeration<URL> resources = JarUtils.class.getClassLoader().getResources("blah.txt");
    int numFound = 0;
    while (resources.hasMoreElements()) {
      URL url = resources.nextElement();
      System.out.println(url);
      numFound++;
    }
    assertEquals(1, numFound);
  }

  @Test
  public void testListJarFromResource() throws IOException {
    JarUtils.listJarFromResource("LICENSE-junit.txt");
  }
}
