
* [$ cat notebook/2E6PUMJYQ/note.json]()
```json
{
"paragraphs":[...],
"name":"sunnychan/spark",
"id":"2E6PUMJYQ",
"noteParams":{
},
"noteForms":{
},
"angularObjects":{
"md:shared_process":[
],
"sh:shared_process":[
],
"spark:shared_process":[
]
},
"config":{
"isZeppelinNotebookCronEnable":false,
"looknfeel":"default",
"personalizedMode":"false"
},
"info":{
}
}
```
```md
以上json实际是与Note的“非transient”字段一一对应的。
```
```md
为什么Note将replLoader、jobListenerFactory、repo、index和delayedPersist设置为transient？
是因为这些字段（状态）可以通过Note剩下的字段以及与该Note实例相关的其他class实例的状态重建。
```

