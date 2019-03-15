# RemoteInterpreterProcess

```java
  public <T> T callRemoteFunction(RemoteFunction<T> func) {
    Client client = null;
    boolean broken = false;
    try {
      client = getClient();
      if (client != null) {
        return func.call(client);
      }
    } catch (TException e) {
      broken = true;
      throw new RuntimeException(e);
    } catch (Exception e1) {
      throw new RuntimeException(e1);
    } finally {
      if (client != null) {
        releaseClient(client, broken);
      }
    }
    return null;
  }

  /**
   *
   * @param <T>
   */
  public interface RemoteFunction<T> {
    T call(Client client) throws Exception;
  }
```