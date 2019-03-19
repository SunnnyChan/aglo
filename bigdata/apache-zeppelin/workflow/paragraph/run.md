# RUN_PARAGRAPH

## WebSocket
### Request 
* RUN_PARAGRAPH
### Responses
* SAVE_NOTE_FORMS
* PARAGRAPH
* SAVE_NOTE_FORMS
* PARAGRAPH
* SAVE_NOTE_FORMS
* PARAGRAPH
* PARAGRAPH_UPDATE_OUTPUT
* PARAGRAPH_APPEND_OUTPUT
* SAVE_NOTE_FORMS
* PARAGRAPH

## Code
1. NotebookServer runParagraph()
```md
onMessage(NotebookSocket conn, String msg) 
runParagraph(conn, messagereceived)
```
```java
  private void runParagraph(NotebookSocket conn,
                            Message fromMessage) throws IOException {
    String paragraphId = (String) fromMessage.get("id");
    String noteId = connectionManager.getAssociatedNoteId(conn);
    String text = (String) fromMessage.get("paragraph");
    String title = (String) fromMessage.get("title");
    Map<String, Object> params = (Map<String, Object>) fromMessage.get("params");
    Map<String, Object> config = (Map<String, Object>) fromMessage.get("config");
    getNotebookService().runParagraph(noteId, paragraphId, title, text, params, config,
        false, false, getServiceContext(fromMessage),
        new WebSocketServiceCallback<Paragraph>(conn) {
          @Override
          public void onSuccess(Paragraph p, ServiceContext context) throws IOException {
            super.onSuccess(p, context);
            if (p.getNote().isPersonalizedMode()) {
              Paragraph p2 = p.getNote().clearPersonalizedParagraphOutput(paragraphId,
                  context.getAutheInfo().getUser());
              connectionManager.unicastParagraph(p.getNote(), p2, context.getAutheInfo().getUser());
            }
            // if it's the last paragraph and not empty, let's add a new one
            boolean isTheLastParagraph = p.getNote().isLastParagraph(paragraphId);
            if (!(Strings.isNullOrEmpty(p.getText()) ||
                Strings.isNullOrEmpty(p.getScriptText())) &&
                isTheLastParagraph) {
              Paragraph newPara = p.getNote().addNewParagraph(p.getAuthenticationInfo());
              broadcastNewParagraph(p.getNote(), newPara);
            }
          }
        });
  }
```
```md
根据 WebSocket 连接管理器 来获取关联的 noteId，（可以看出 一个 note 最多对应一个 WebSocket连接，
而实际上一个 WebSocket 连接可以关联 多个 note）。

然后主要是调用 NoteBookService 的 runParagraph()，同时注册 WebSocketService 回调接口，
用来向客户端 发送执行结果。
```
2. org.apache.zeppelin.service.NotebookService runParagraph()
```md
先获取 Note note = notebook.getNote(noteId); 
在根据 paragraphId 从 Note 中获取 Paragraph p = note.getParagraph(paragraphId);
Note 中的 Paragraph 信息，是在 Note 实例化时，从持久化设备中反序列化而来。
（以上可以参考 Note 的 Create 流程）
```
```java
  public boolean runParagraph(String noteId,
                              String paragraphId,
                              String title,
                              String text,
                              Map<String, Object> params,
                              Map<String, Object> config,
                              boolean failIfDisabled,
                              boolean blocking,
                              ServiceContext context,
                              ServiceCallback<Paragraph> callback) throws IOException {

    if (!checkPermission(noteId, Permission.RUNNER, Message.OP.RUN_PARAGRAPH, context, callback)) {
      return false;
    }

    Note note = notebook.getNote(noteId);
    if (note == null) {
      callback.onFailure(new NoteNotFoundException(noteId), context);
      return false;
    }
    Paragraph p = note.getParagraph(paragraphId);
    if (p == null) {
      callback.onFailure(new ParagraphNotFoundException(paragraphId), context);
      return false;
    }
    if (failIfDisabled && !p.isEnabled()) {
      callback.onFailure(new IOException("paragraph is disabled."), context);
      return false;
    }
    p.setText(text);
    p.setTitle(title);
    p.setAuthenticationInfo(context.getAutheInfo());
    p.settings.setParams(params);
    p.setConfig(config);

    if (note.isPersonalizedMode()) {
      p = note.getParagraph(paragraphId);
      p.setText(text);
      p.setTitle(title);
      p.setAuthenticationInfo(context.getAutheInfo());
      p.settings.setParams(params);
      p.setConfig(config);
    }

    try {
      notebook.saveNote(note, context.getAutheInfo());
      boolean result = note.run(p.getId(), blocking);
      callback.onSuccess(p, context);
      return result;
    } catch (Exception ex) {
      LOGGER.error("Exception from run", ex);
      p.setReturn(new InterpreterResult(InterpreterResult.Code.ERROR, ex.getMessage()), ex);
      p.setStatus(Job.Status.ERROR);
      // don't call callback.onFailure, we just need to display the error message
      // in paragraph result section instead of pop up the error window.
      return false;
    }
  }
```
```java
主要是调 Note 的 run() 方法，执行 Paragraph。

set 方法的参数 都来至调用函数：
    String text = (String) fromMessage.get("paragraph");
    String title = (String) fromMessage.get("title");
    Map<String, Object> params = (Map<String, Object>) fromMessage.get("params");
    Map<String, Object> config = (Map<String, Object>) fromMessage.get("config");
可以看出是从，Websocket 请求消息 “data” 节点中读取的。

重点关注下 text 的处理：
在调用 setText(） 方法时，再调用 parseText() 解析 Text。
```
* 2.1 org.apache.zeppelin.notebook.Paragraph parseText()
```java
  public void parseText() {
    // parse text to get interpreter component
    if (this.text != null) {
      // clean localProperties, otherwise previous localProperties will be used for the next run
      this.localProperties.clear();
      Matcher matcher = REPL_PATTERN.matcher(this.text);
      if (matcher.matches()) {
        String headingSpace = matcher.group(1);
        setIntpText(matcher.group(2));

        if (matcher.groupCount() == 3 && matcher.group(3) != null) {
          String localPropertiesText = matcher.group(3);
          String[] splits = localPropertiesText.substring(1, localPropertiesText.length() -1)
              .split(",");
          for (String split : splits) {
            String[] kv = split.split("=");
            if (StringUtils.isBlank(split) || kv.length == 0) {
              continue;
            }
            if (kv.length > 2) {
              throw new RuntimeException("Invalid paragraph properties format: " + split);
            }
            if (kv.length == 1) {
              localProperties.put(kv[0].trim(), kv[0].trim());
            } else {
              localProperties.put(kv[0].trim(), kv[1].trim());
            }
          }
          this.scriptText = this.text.substring(headingSpace.length() + intpText.length() +
              localPropertiesText.length() + 1).trim();
        } else {
          this.scriptText = this.text.substring(headingSpace.length() + intpText.length() + 1).trim();
        }
      } else {
        setIntpText("");
        this.scriptText = this.text.trim();
      }
    }
  }
```
```java
为 获取 解释器 组件 解析 text 首先用 text 匹配正则表达式：
  private static Pattern REPL_PATTERN = Pattern.compile("(\\s*)%([\\w\\.]+)(\\(.*?\\))?.*", Pattern.DOTALL);
匹配成功，解析 Text 字符串，然后设置 localProperties，intpText，scriptText
不成功，则 intpText = ""，scriptText = text。
```
* 3. org.apache.zeppelin.notebook.Note run()
```java
// run
  public boolean run(String paragraphId, boolean blocking) {
    Paragraph p = getParagraph(paragraphId);
    p.setListener(this.paragraphJobListener);
    return p.execute(blocking);
  }
```
* 4. org.apache.zeppelin.notebook.Paragraph 
```java
// execute
this.interpreter = getBindedInterpreter();
interpreter.getScheduler().submit(this);
```

* 4.1 getBindedInterpreter()
```java
 public Interpreter getBindedInterpreter() throws InterpreterNotFoundException {
    return this.note.getInterpreterFactory().getInterpreter(user, note.getId(), intpText,
        note.getDefaultInterpreterGroup());
  }
```
```md
InterpreterFactory 实例 在 NoteBook 类中注入。
而 NoteBook 实例是在 NotebookServer 中注入。

最终获取的是一个  RemoteInterpreter 实例。
```

* 4.2 org.apache.zeppelin.scheduler.AbstractScheduler (RemoteScheduler)  submit()
```java
  @Override
  public void submit(Job job) {
    job.setStatus(Job.Status.PENDING);
    queue.add(job);
    jobs.put(job.getId(), job);
  }
```
```md
放到调度队列，等待执行，默认 FIFO。
```

* 5. [Scheduler 调度流程](../scheduler/README.md)

* 6. org.apache.zeppelin.notebook.Paragraph 
```java
  @Override
  protected InterpreterResult jobRun() throws Throwable {
    this.runtimeInfos.clear();
    this.interpreter = getBindedInterpreter();
    if (this.interpreter == null) {
      LOGGER.error("Can not find interpreter name " + intpText);
      throw new RuntimeException("Can not find interpreter for " + intpText);
    }
    LOGGER.info("Run paragraph [paragraph_id: {}, interpreter: {}, note_id: {}, user: {}]",
        getId(), this.interpreter.getClassName(), note.getId(), subject.getUser());
    InterpreterSetting interpreterSetting = ((ManagedInterpreterGroup)
        interpreter.getInterpreterGroup()).getInterpreterSetting();
    if (interpreterSetting != null) {
      interpreterSetting.waitForReady();
    }
    if (this.user != null) {
      if (subject != null && !interpreterSetting.isUserAuthorized(subject.getUsersAndRoles())) {
        String msg = String.format("%s has no permission for %s", subject.getUser(), intpText);
        LOGGER.error(msg);
        return new InterpreterResult(Code.ERROR, msg);
      }
    }

    for (Paragraph p : userParagraphMap.values()) {
      p.setText(getText());
    }

    // inject form
    String script = this.scriptText;
    if (interpreter.getFormType() == FormType.NATIVE) {
      settings.clear();
    } else if (interpreter.getFormType() == FormType.SIMPLE) {
      // inputs will be built from script body
      LinkedHashMap<String, Input> inputs = Input.extractSimpleQueryForm(script, false);
      LinkedHashMap<String, Input> noteInputs = Input.extractSimpleQueryForm(script, true);
      final AngularObjectRegistry angularRegistry =
          interpreter.getInterpreterGroup().getAngularObjectRegistry();
      String scriptBody = extractVariablesFromAngularRegistry(script, inputs, angularRegistry);

      settings.setForms(inputs);
      if (!noteInputs.isEmpty()) {
        if (!note.getNoteForms().isEmpty()) {
          Map<String, Input> currentNoteForms =  note.getNoteForms();
          for (String s : noteInputs.keySet()) {
            if (!currentNoteForms.containsKey(s)) {
              currentNoteForms.put(s, noteInputs.get(s));
            }
          }
        } else {
          note.setNoteForms(noteInputs);
        }
      }
      script = Input.getSimpleQuery(note.getNoteParams(), scriptBody, true);
      script = Input.getSimpleQuery(settings.getParams(), script, false);
    }
    LOGGER.debug("RUN : " + script);
    try {
      InterpreterContext context = getInterpreterContext();
      InterpreterContext.set(context);
      InterpreterResult ret = interpreter.interpret(script, context);

      if (interpreter.getFormType() == FormType.NATIVE) {
        note.setNoteParams(context.getNoteGui().getParams());
        note.setNoteForms(context.getNoteGui().getForms());
      }

      if (Code.KEEP_PREVIOUS_RESULT == ret.code()) {
        return getReturn();
      }

      context.out.flush();
      List<InterpreterResultMessage> resultMessages = context.out.toInterpreterResultMessage();
      resultMessages.addAll(ret.message());
      InterpreterResult res = new InterpreterResult(ret.code(), resultMessages);
      Paragraph p = getUserParagraph(getUser());
      if (null != p) {
        p.setResult(res);
        p.settings.setParams(settings.getParams());
      }

      // After the paragraph is executed,
      // need to apply the paragraph to the configuration in the
      // `interpreter-setting.json` config
      if (this.configSettingNeedUpdate) {
        this.configSettingNeedUpdate = false;
        InterpreterSettingManager intpSettingManager
            = this.note.getInterpreterSettingManager();
        if (null != intpSettingManager) {
          InterpreterGroup intpGroup = interpreter.getInterpreterGroup();
          if (null != intpGroup && intpGroup instanceof ManagedInterpreterGroup) {
            String name = ((ManagedInterpreterGroup) intpGroup).getInterpreterSetting().getName();
            Map<String, Object> config
                = intpSettingManager.getConfigSetting(name);
            applyConfigSetting(config);
          }
        }
      }

      return res;
    } finally {
      InterpreterContext.remove();
    }
  }
```
```md
InterpreterResult ret = interpreter.interpret(script, context);
调 RemoteInterpreter interpret() 方法执行
```
* 7. org.apache.zeppelin.interpreter.remote.RemoteInterpreter
```java
  @Override
  public InterpreterResult interpret(final String st, final InterpreterContext context)
      throws InterpreterException {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("st:\n{}", st);
    }

    final FormType form = getFormType();
    RemoteInterpreterProcess interpreterProcess = null;
    try {
      interpreterProcess = getOrCreateInterpreterProcess();
    } catch (IOException e) {
      throw new InterpreterException(e);
    }
    this.lifecycleManager.onInterpreterUse(this.getInterpreterGroup(), sessionId);
    return interpreterProcess.callRemoteFunction(
        new RemoteInterpreterProcess.RemoteFunction<InterpreterResult>() {
          @Override
          public InterpreterResult call(Client client) throws Exception {

            RemoteInterpreterResult remoteResult = client.interpret(
                sessionId, className, st, convert(context));
            Map<String, Object> remoteConfig = (Map<String, Object>) gson.fromJson(
                remoteResult.getConfig(), new TypeToken<Map<String, Object>>() {
                }.getType());
            context.getConfig().clear();
            if (remoteConfig != null) {
              context.getConfig().putAll(remoteConfig);
            }
            GUI currentGUI = context.getGui();
            GUI currentNoteGUI = context.getNoteGui();
            if (form == FormType.NATIVE) {
              GUI remoteGui = GUI.fromJson(remoteResult.getGui());
              GUI remoteNoteGui = GUI.fromJson(remoteResult.getNoteGui());
              currentGUI.clear();
              currentGUI.setParams(remoteGui.getParams());
              currentGUI.setForms(remoteGui.getForms());
              currentNoteGUI.setParams(remoteNoteGui.getParams());
              currentNoteGUI.setForms(remoteNoteGui.getForms());
            } else if (form == FormType.SIMPLE) {
              final Map<String, Input> currentForms = currentGUI.getForms();
              final Map<String, Object> currentParams = currentGUI.getParams();
              final GUI remoteGUI = GUI.fromJson(remoteResult.getGui());
              final Map<String, Input> remoteForms = remoteGUI.getForms();
              final Map<String, Object> remoteParams = remoteGUI.getParams();
              currentForms.putAll(remoteForms);
              currentParams.putAll(remoteParams);
            }

            InterpreterResult result = convert(remoteResult);
            return result;
          }
        }
    );

  }
```
```md
重点看 
  interpreterProcess = getOrCreateInterpreterProcess();

  RemoteInterpreterResult remoteResult = client.interpret(
                sessionId, className, st, convert(context));
```
* 7.1 org.apache.zeppelin.interpreter.remote.RemoteInterpreter
```java
RemoteInterpreter类 相当于 RemoteInterpreter 相关操作API。
```
```java
  public synchronized RemoteInterpreterProcess getOrCreateInterpreterProcess() throws IOException {
    if (this.interpreterProcess != null) {
      return this.interpreterProcess;
    }
    ManagedInterpreterGroup intpGroup = getInterpreterGroup();
    this.interpreterProcess = intpGroup.getOrCreateInterpreterProcess(getUserName(), properties);
    return interpreterProcess;
  }
```
```md
以上 代码 主要是获取 thrift Client，这里是 RemoteInterpreterProcess 实例。
```
* 7.1.1 org.apache.zeppelin.interpreter.ManagedInterpreterGroup
```java
  public synchronized RemoteInterpreterProcess getOrCreateInterpreterProcess(String userName,
                                                                             Properties properties)
      throws IOException {
    if (remoteInterpreterProcess == null) {
      LOGGER.info("Create InterpreterProcess for InterpreterGroup: " + getId());
      remoteInterpreterProcess = interpreterSetting.createInterpreterProcess(id, userName,
          properties);
      remoteInterpreterProcess.start(userName);
      interpreterSetting.getLifecycleManager().onInterpreterProcessStarted(this);
      getInterpreterSetting().getRecoveryStorage()
          .onInterpreterClientStart(remoteInterpreterProcess);
    }
    return remoteInterpreterProcess;
  }
```
* 7.1.1.1 org.apache.zeppelin.interpreter.InterpreterSetting
```java
  synchronized RemoteInterpreterProcess createInterpreterProcess(String interpreterGroupId,
                                                                 String userName,
                                                                 Properties properties)
      throws IOException {
    if (launcher == null) {
      createLauncher();
    }
    InterpreterLaunchContext launchContext = new
        InterpreterLaunchContext(properties, option, interpreterRunner, userName,
        interpreterGroupId, id, group, name, interpreterEventServer.getPort(), interpreterEventServer.getHost());
    RemoteInterpreterProcess process = (RemoteInterpreterProcess) launcher.launch(launchContext);
    recoveryStorage.onInterpreterClientStart(process);
    return process;
  }
```
```md
实际创建客户端的地方，Interpreter服务端也可能在这里启动。
```
* 7.1.1.1.1 org.apache.zeppelin.interpreter.launcher.StandardInterpreterLauncher
```java
  @Override
  public InterpreterClient launch(InterpreterLaunchContext context) throws IOException {
    LOGGER.info("Launching Interpreter: " + context.getInterpreterSettingGroup());
    this.properties = context.getProperties();
    InterpreterOption option = context.getOption();
    InterpreterRunner runner = context.getRunner();
    String groupName = context.getInterpreterSettingGroup();
    String name = context.getInterpreterSettingName();
    int connectTimeout = getConnectTimeout();

    if (option.isExistingProcess()) {
      return new RemoteInterpreterRunningProcess(
          context.getInterpreterSettingName(),
          connectTimeout,
          option.getHost(),
          option.getPort());
    } else {
      // try to recover it first
      if (zConf.isRecoveryEnabled()) {
        InterpreterClient recoveredClient =
            recoveryStorage.getInterpreterClient(context.getInterpreterGroupId());
        if (recoveredClient != null) {
          if (recoveredClient.isRunning()) {
            LOGGER.info("Recover interpreter process: " + recoveredClient.getHost() + ":" +
                recoveredClient.getPort());
            return recoveredClient;
          } else {
            LOGGER.warn("Cannot recover interpreter process: " + recoveredClient.getHost() + ":"
                + recoveredClient.getPort() + ", as it is already terminated.");
          }
        }
      }

      // create new remote process
      String localRepoPath = zConf.getInterpreterLocalRepoPath() + "/"
          + context.getInterpreterSettingId();
      return new RemoteInterpreterManagedProcess(
          runner != null ? runner.getPath() : zConf.getInterpreterRemoteRunnerPath(),
          context.getZeppelinServerRPCPort(), context.getZeppelinServerHost(), zConf.getInterpreterPortRange(),
          zConf.getInterpreterDir() + "/" + groupName, localRepoPath,
          buildEnvFromProperties(context), connectTimeout, name,
          context.getInterpreterGroupId(), option.isUserImpersonate());
    }
  }
```
```md
如果 Interpreter 已经启动，则直接创建一个 RemoteInterpreterRunningProcess 客户端连接。
然后优先尝试恢复，失败后 构建 RemoteInterpreterManagedProcess，调 start 方法 通过 bin/interpreter.sh 脚本创建。
```
* 7.1.2 org.apache.zeppelin.interpreter.ManagedInterpreterGroup
```java
  @Override
  public void start(String userName) throws IOException {
    // start server process
    CommandLine cmdLine = CommandLine.parse(interpreterRunner);
    cmdLine.addArgument("-d", false);
    cmdLine.addArgument(interpreterDir, false);
    cmdLine.addArgument("-c", false);
    cmdLine.addArgument(zeppelinServerRPCHost, false);
    cmdLine.addArgument("-p", false);
    cmdLine.addArgument(String.valueOf(zeppelinServerRPCPort), false);
    cmdLine.addArgument("-r", false);
    cmdLine.addArgument(interpreterPortRange, false);
    cmdLine.addArgument("-i", false);
    cmdLine.addArgument(interpreterGroupId, false);
    if (isUserImpersonated && !userName.equals("anonymous")) {
      cmdLine.addArgument("-u", false);
      cmdLine.addArgument(userName, false);
    }
    cmdLine.addArgument("-l", false);
    cmdLine.addArgument(localRepoDir, false);
    cmdLine.addArgument("-g", false);
    cmdLine.addArgument(interpreterSettingName, false);

    executor = new DefaultExecutor();

    ByteArrayOutputStream cmdOut = new ByteArrayOutputStream();
    ProcessLogOutputStream processOutput = new ProcessLogOutputStream(logger);
    processOutput.setOutputStream(cmdOut);

    executor.setStreamHandler(new PumpStreamHandler(processOutput));
    watchdog = new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);
    executor.setWatchdog(watchdog);

    try {
      Map procEnv = EnvironmentUtils.getProcEnvironment();
      procEnv.putAll(env);

      logger.info("Run interpreter process {}", cmdLine);
      executor.execute(cmdLine, procEnv, this);
    } catch (IOException e) {
      running.set(false);
      throw new RuntimeException(e);
    }

    try {
      synchronized (running) {
        if (!running.get()) {
          running.wait(getConnectTimeout());
        }
      }
      if (!running.get()) {
        throw new IOException(new String(
            String.format("Interpreter Process creation is time out in %d seconds",
                getConnectTimeout()/1000) + "\n" + "You can increase timeout threshold via " +
                "setting zeppelin.interpreter.connect.timeout of this interpreter.\n" +
                cmdOut.toString()));
      }
    } catch (InterruptedException e) {
      logger.error("Remote interpreter is not accessible");
    }
    processOutput.setOutputStream(null);
  }
```
* 8 org.apache.zeppelin.interpreter.thrift.RemoteInterpreterService
```java
    public RemoteInterpreterResult interpret(String sessionId, String className, String st, RemoteInterpreterContext interpreterContext) throws org.apache.thrift.TException
    {
      send_interpret(sessionId, className, st, interpreterContext);
      return recv_interpret();
    }
```

* 9 接下来就是 thrift server 端的处理

