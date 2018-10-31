# resource-jar-tools

Some simple tools to demonstrate listing and unpacking JAR files on the classpath

## To Build

```
mvn package
```

## Usage

### List the contents of a JAR found on the classpath

`ListJarFromResource` will find a JAR file on the classpath containing a
resource you specify (for example, a directory inside the JAR) and print a
listing of the contents of that JAR to stdout.

Example:

```
java -cp target/ResourceJarTools-1.0-SNAPSHOT.jar:$HOME/src/kudu/build/minicluster/apache-kudu-1.9.0-SNAPSHOT.jar org.apache.kudu.ListJarFromResource apache-kudu-1.9.0-SNAPSHOT
```

### Unpack a JAR found on the classpath

`UnpackJarFromResource` will find a JAR file on the classpath containing a
resource you specify (for example, a directory inside the JAR) and unpack the
JAR into the destination directory you specify.

Example:

```
java -cp target/ResourceJarTools-1.0-SNAPSHOT.jar:$HOME/src/kudu/build/minicluster/apache-kudu-1.9.0-SNAPSHOT.jar org.apache.kudu.UnpackJarFromResource apache-kudu-1.9.0-SNAPSHOT out
```
