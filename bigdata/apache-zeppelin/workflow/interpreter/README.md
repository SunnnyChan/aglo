# interpreter

## getInterpreter
* org.apache.zeppelin.interpreter.InterpreterFactory
```java
  public Interpreter getInterpreter(String user,
                                    String noteId,
                                    String replName,
                                    String defaultInterpreterSetting)

    if (StringUtils.isBlank(replName)) {
      // Get the default interpreter of the defaultInterpreterSetting
      InterpreterSetting defaultSetting =
          interpreterSettingManager.getByName(defaultInterpreterSetting);
      return defaultSetting.getDefaultInterpreter(user, noteId);
    }

    String[] replNameSplits = replName.split("\\.");
    if (replNameSplits.length == 2) {
      String group = replNameSplits[0];
      String name = replNameSplits[1];
      InterpreterSetting setting = interpreterSettingManager.getByName(group);
      if (null != setting) {
        Interpreter interpreter = setting.getInterpreter(user, noteId, name);
        if (null != interpreter) {
          return interpreter;
        }
        throw new InterpreterNotFoundException("No such interpreter: " + replName);
      }
      throw new InterpreterNotFoundException("No interpreter setting named: " + group);

    } else if (replNameSplits.length == 1){
      // first assume group is omitted
      InterpreterSetting setting =
          interpreterSettingManager.getByName(defaultInterpreterSetting);
      if (setting != null) {
        Interpreter interpreter = setting.getInterpreter(user, noteId, replName);
        if (null != interpreter) {
          return interpreter;
        }
      }

      // then assume interpreter name is omitted
      setting = interpreterSettingManager.getByName(replName);
      if (null != setting) {
        return setting.getDefaultInterpreter(user, noteId);
      }
    }

    throw new InterpreterNotFoundException("No such interpreter: " + replName);
  }
```
```md
主要是根据 text 解析结果 获取 interpreter，4种情况：
1. replName （上文提及的 intpText） 为空，则获取 zeppelin.interpreter.group.default 配置的默认 interpreter。
2. replName 为 group.name格式，则把 replName 做解析为 group 和 name，根据 group 找到相关解释器配置管理器
3. replName 不包含 . 分割的字段，从默认配置中，获取符合 replName 的 解释器。
4. 其他情况下，则用 replName 找相关解释器配置管理器，再获取默认的解释器

注意 都是先找到 解释器配置管理器，再获取解释器。
```
* org.apache.zeppelin.interpreter.InterpreterSetting
```java
 public Interpreter getDefaultInterpreter(String user, String noteId) {
    return getOrCreateSession(user, noteId).get(0);
  }

```
* org.apache.zeppelin.interpreter.ManagedInterpreterGroup
```java
  public synchronized List<Interpreter> getOrCreateSession(String user, String sessionId) {
    if (sessions.containsKey(sessionId)) {
      return sessions.get(sessionId);
    } else {
      List<Interpreter> interpreters = interpreterSetting.createInterpreters(user, id, sessionId);
      for (Interpreter interpreter : interpreters) {
        interpreter.setInterpreterGroup(this);
      }
      LOGGER.info("Create Session: {} in InterpreterGroup: {} for user: {}", sessionId, id, user);
      sessions.put(sessionId, interpreters);
      return interpreters;
    }
  }
```
```java
  List<Interpreter> createInterpreters(String user, String interpreterGroupId, String sessionId) {
    List<Interpreter> interpreters = new ArrayList<>();
    List<InterpreterInfo> interpreterInfos = getInterpreterInfos();
    Properties intpProperties = getJavaProperties();
    for (InterpreterInfo info : interpreterInfos) {
      Interpreter interpreter = new RemoteInterpreter(intpProperties, sessionId,
          info.getClassName(), user, lifecycleManager);
      if (info.isDefaultInterpreter()) {
        interpreters.add(0, interpreter);
      } else {
        interpreters.add(interpreter);
      }
      LOGGER.info("Interpreter {} created for user: {}, sessionId: {}",
          interpreter.getClassName(), user, sessionId);
    }

    // TODO(zjffdu) this kind of hardcode is ugly. For now SessionConfInterpreter is used
    // for livy, we could add new property in interpreter-setting.json when there's new interpreter
    // require SessionConfInterpreter
    if (group.equals("livy")) {
      interpreters.add(
          new SessionConfInterpreter(intpProperties, sessionId, interpreterGroupId, this));
    } else {
      interpreters.add(new ConfInterpreter(intpProperties, sessionId, interpreterGroupId, this));
    }
    return interpreters;
  }
```