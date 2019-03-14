# org.apache.zeppelin.service.NoteBookService
```md
Service class for Notebook related operations.
It use {@link Notebook} which provides high level api to access notes.
 
In most of methods, this class will check permission first and whether this note existed.
If the operation succeeed, {@link ServiceCallback#onSuccess(Object, ServiceContext)} should be
called, otherwise {@link ServiceCallback#onFailure(Exception, ServiceContext)} should be called.
```
