package org.apache.kudu;

import java.io.IOException;

public class ListJarFromResource {

  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      System.err.println("usage: ListJarFromResource resource_name");
      System.exit(1);
    }

    JarUtils.listJarFromResource(args[0]);
  }
}
