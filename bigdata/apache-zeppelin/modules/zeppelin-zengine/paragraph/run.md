# RUN_PARAGRAPH

* NotebookServer
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
```java
org.apache.zeppelin.service.NotebookService
// runParagraph
```
```md
先获取 Note note = notebook.getNote(noteId); 
在根据 paragraphId 从 Note 中获取 Paragraph p = note.getParagraph(paragraphId);
Note 中的 Paragraph 信息，是在 Note 实例化时，从持久化设备中反序列化而来。
（以上可以参考 Note 的 Create 流程）
```
```java
      notebook.saveNote(note, context.getAutheInfo());
      boolean result = note.run(p.getId(), blocking);
      callback.onSuccess(p, context);
      return result;
```
```md
主要是调 Note 的 run() 方法，执行 Paragraph。
```
```java
org.apache.zeppelin.notebook.Note
// run
  public boolean run(String paragraphId, boolean blocking) {
    Paragraph p = getParagraph(paragraphId);
    p.setListener(this.paragraphJobListener);
    return p.execute(blocking);
  }
```
```java
org.apache.zeppelin.notebook.Paragraph
// execute
this.interpreter = getBindedInterpreter();
interpreter.getScheduler().submit(this);
```
```java
 public Interpreter getBindedInterpreter() throws InterpreterNotFoundException {
    return this.note.getInterpreterFactory().getInterpreter(user, note.getId(), intpText,
        note.getDefaultInterpreterGroup());
  }
```
```md
InterpreterFactory 实例 在 NoteBook 类中注入。
而 NoteBook 实例是在 NotebookServer 中注入。
```
```java
package org.apache.zeppelin.interpreter.InterpreterFactory;

  public Interpreter getInterpreter(String user,
                                    String noteId,
                                    String replName,
                                    String defaultInterpreterSetting)
```
