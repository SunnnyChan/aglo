
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

***paragraph***

```json
{
"text":"sc.version\n",
"user":"anonymous",
"dateUpdated":"2019-02-27 17:34:26.746",
"config":{
"colWidth":12.0,
"fontSize":9.0,
"enabled":true,
"results":{
},
"editorSetting":{
"language":"scala",
"editOnDblClick":false,
"completionKey":"TAB",
"completionSupport":true
},
"editorMode":"ace/mode/scala",
"tableHide":false
},
"settings":{
"params":{
},
"forms":{
}
},
"results":{
"code":"SUCCESS",
"msg":[
{
"type":"TEXT",
"data":"res11: String \u003d 2.4.0\n"
}
]
},
"apps":[
],
"jobName":"paragraph_1551171249437_-1857577060",
"id":"20190226-165409_698627529",
"dateCreated":"2019-02-26 16:54:09.437",
"dateStarted":"2019-02-27 17:34:26.778",
"dateFinished":"2019-02-27 17:34:27.457",
"status":"FINISHED",
"progressUpdateIntervalMs":500
}
```
