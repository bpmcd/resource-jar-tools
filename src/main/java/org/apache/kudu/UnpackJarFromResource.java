package org.apache.kudu;

import java.io.File;
import java.io.IOException;

public class UnpackJarFromResource {
  public static void main(String[] args) throws IOException {
    if (args.length != 2) {
      System.err.println("usage: UnpackJarFromResource resource_name dest_path");
      System.exit(1);
    }

    String resourceName = args[0];
    String destPath = args[1];
    JarUtils.unpackJarFromResource(resourceName, new File(destPath));
  }
}
