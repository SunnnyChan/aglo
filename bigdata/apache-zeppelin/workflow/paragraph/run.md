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

* 4.2 org.apache.zeppelin.scheduler  submit()
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
