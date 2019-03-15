# RemoteInterpreterServer
```java
  public static void main(String[] args)
      throws TTransportException, InterruptedException, IOException {
    String zeppelinServerHost = null;
    int port = Constants.ZEPPELIN_INTERPRETER_DEFAUlT_PORT;
    String portRange = ":";
    String interpreterGroupId = null;
    if (args.length > 0) {
      zeppelinServerHost = args[0];
      port = Integer.parseInt(args[1]);
      interpreterGroupId = args[2];
      if (args.length > 3) {
        portRange = args[3];
      }
    }
    RemoteInterpreterServer remoteInterpreterServer =
        new RemoteInterpreterServer(zeppelinServerHost, port, interpreterGroupId, portRange);
    remoteInterpreterServer.start();

    // add signal handler
    Signal.handle(new Signal("TERM"), new SignalHandler() {
      @Override
      public void handle(Signal signal) {
        try {
          remoteInterpreterServer.shutdown();
        } catch (TException e) {
          logger.error("Error on shutdown RemoteInterpreterServer", e);
        }
      }
    });

    remoteInterpreterServer.join();
    System.exit(0);
  }
```