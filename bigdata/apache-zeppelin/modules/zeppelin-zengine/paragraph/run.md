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