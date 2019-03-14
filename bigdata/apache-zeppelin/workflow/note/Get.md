# get Note Workflow

## 从 存储 文件 反序列化
* org.apache.zeppelin.service.NoteBook
```java
  public Note getNote(String id) {
    try {
      Note note = noteManager.getNote(id);
      if (note == null) {
        return null;
      }
      note.setInterpreterFactory(replFactory);
      note.setInterpreterSettingManager(interpreterSettingManager);
      note.setParagraphJobListener(paragraphJobListener);
      note.setNoteEventListeners(noteEventListeners);
      note.setCredentials(credentials);
      for (Paragraph p : note.getParagraphs()) {
        p.setNote(note);
      }
      return note;
    } catch (IOException e) {
      LOGGER.warn("Fail to get note: " + id, e);
      return null;
    }
  }
```
```md
调用 set 方法的部分，是没有做持久化存储的 Note 属性。
这些 属性的值 都是通过 HK2 方式注入的。
```
* org.apache.zeppelin.notebook.NoteManager
```java
  public Note getNote(String noteId) throws IOException {
    String notePath = this.notesInfo.get(noteId);
    if (notePath == null) {
      return null;
    }
    NoteNode noteNode = getNoteNode(notePath);
    return noteNode.getNote();
  }

  private NoteNode getNoteNode(String notePath) throws IOException {
    String[] tokens = notePath.split("/");
    Folder curFolder = root;
    for (int i = 0; i < tokens.length - 1; ++i) {
      if (!StringUtils.isBlank(tokens[i])) {
        curFolder = curFolder.getFolder(tokens[i]);
        if (curFolder == null) {
          throw new IOException("Can not find note: " + notePath);
        }
      }
    }
    NoteNode noteNode = curFolder.getNote(tokens[tokens.length - 1]);
    if (noteNode == null) {
      throw new IOException("Can not find note: " + notePath);
    }
    return noteNode;
  }
```
```md
NoteManager 类在实例化时会调用 init() 方法，init() 再调用 会调用addOrUpdateNoteNode() 方法，
从 Note 的存储中 读取 路径信息，保存在 notesInfo 中，同时会 保存 noteName -> NoteNode 的映射关系
至 org.apache.zeppelin.notebook.NoteManager.Forld 中的 notes 中。

当读取 构建 Note 实例时，先查找 Note 的路径，然后在从持久化 设备中 读取 Note 的信息。
```
* org.apache.zeppelin.notebook.NoteManager.Forld
```java
    // noteName -> NoteNode
    private Map<String, NoteNode> notes = new HashMap<>();
    // folderName -> Folder
    private Map<String, Folder> subFolders = new HashMap<>();

    public NoteNode getNote(String noteName) {
      return this.notes.get(noteName);
    }
```
* org.apache.zeppelin.notebook.NoteManager.NoteNode
```java
  public synchronized Note getNote() throws IOException {
      if (!note.isLoaded()) {
        note = notebookRepo.get(note.getId(), note.getPath(), AuthenticationInfo.ANONYMOUS);
        if (parent.toString().equals("/")) {
          note.setPath("/" + note.getName());
        } else {
          note.setPath(parent.toString() + "/" + note.getName());
        }
        note.setLoaded(true);
      }
      return note;
    }
```

```java
Interface : org.apache.zeppelin.notebook.repo.NotebookRepo
org.apache.zeppelin.notebook.repo.VFSNotebookRepo

  @Override
  public Note get(String noteId, String notePath, AuthenticationInfo subject) throws IOException {
    FileObject noteFile = rootNotebookFileObject.resolveFile(buildNoteFileName(noteId, notePath),
        NameScope.DESCENDENT);
    String json = IOUtils.toString(noteFile.getContent().getInputStream(),
        conf.getString(ConfVars.ZEPPELIN_ENCODING));
    Note note = Note.fromJson(json);
    // setPath here just for testing, because actually NoteManager will setPath
    note.setPath(notePath);
    return note;
  }
```
```md
大致是，先 Note 的路径，通过路径找到 NoteNode 节点，再找节点下的 Note。
```