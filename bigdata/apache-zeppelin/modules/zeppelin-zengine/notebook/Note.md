# org.apache.zeppelin.notebook.Note
```md
是zeppelin管理的最小单位，无论是做权限控制、共享、还是持久化，都是以Note为粒度的。
```
```md
从类关系上看，Note是由一些列的有序Paragraph组成。
因此其绝大部分职责都是与管理Paragraph有关：
1. Paragraph的CRUD、相对顺序控制 
2. 与处理前后端数据双向推送的AngularObject的管理 
3. 整体和单个Paragraph 执行，以及执行过程的基于Observer模式的执行过程Hook 
4. Note基本的样式外观控制 
```
```md
为了“分离关注点”，其他的功能，如： 
1. Note相关的Interpreter加载和初始化 
2. 持久化与反持久化，包括延迟持久化 
3. 权限控制 
```

# Create

```java
org.apache.zeppelin.service.NoteBookService

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
```java
org.apache.zeppelin.notebook.NoteManager

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
```java
org.apache.zeppelin.notebook.NoteManager.Forld

    // noteName -> NoteNode
    private Map<String, NoteNode> notes = new HashMap<>();
    // folderName -> Folder
    private Map<String, Folder> subFolders = new HashMap<>();

    public NoteNode getNote(String noteName) {
      return this.notes.get(noteName);
    }
```
```java
org.apache.zeppelin.notebook.NoteManager.NoteNode

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