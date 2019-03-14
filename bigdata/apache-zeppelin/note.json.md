## 简版
```json
{
  "paragraphs":[],
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
## 详细
```json
{
  "paragraphs": [
    {
      "text": "sc.version\n",
      "user": "anonymous",
      "dateUpdated": "2019-03-11 21:26:26.288",
      "config": {
        "colWidth": 12.0,
        "fontSize": 9.0,
        "enabled": true,
        "results": {},
        "editorSetting": {
          "language": "scala",
          "editOnDblClick": false,
          "completionKey": "TAB",
          "completionSupport": true
        },
        "editorMode": "ace/mode/scala",
        "tableHide": false
      },
      "settings": {
        "params": {},
        "forms": {}
      },
      "results": {
        "code": "SUCCESS",
        "msg": [
          {
            "type": "TEXT",
            "data": "res36: String \u003d 2.4.0\n"
          }
        ]
      },
      "apps": [],
      "jobName": "paragraph_1551171249437_-1857577060",
      "id": "20190226-165409_698627529",
      "dateCreated": "2019-02-26 16:54:09.437",
      "dateStarted": "2019-03-11 21:26:26.306",
      "dateFinished": "2019-03-11 21:26:26.603",
      "status": "FINISHED",
      "progressUpdateIntervalMs": 500
    },
    {
      "text": "select 1;",
      "user": "anonymous",
      "dateUpdated": "2019-03-13 16:37:26.505",
      "config": {
        "colWidth": 12.0,
        "fontSize": 9.0,
        "enabled": true,
        "results": {},
        "editorSetting": {
          "language": "scala",
          "editOnDblClick": false,
          "completionKey": "TAB",
          "completionSupport": true
        },
        "editorMode": "ace/mode/scala",
        "tableHide": false
      },
      "settings": {
        "params": {},
        "forms": {}
      },
      "results": {
        "code": "ERROR",
        "msg": [
          {
            "type": "TEXT",
            "data": "\u003cconsole\u003e:1: error: \u0027;\u0027 expected but integer literal found.\nselect 1;\n       ^\n"
          }
        ]
      },
      "apps": [],
      "jobName": "paragraph_1551171272558_-1701054050",
      "id": "20190226-165432_545226500",
      "dateCreated": "2019-02-26 16:54:32.558",
      "dateStarted": "2019-03-13 16:37:26.535",
      "dateFinished": "2019-03-13 16:37:26.547",
      "status": "ERROR",
      "progressUpdateIntervalMs": 500
    },
    {
      "text": "sc.textFile(\"hdfs://10.96.111.130:9000/wordcountdemo/input/wc.input\").flatMap(_.split(\" \")).filter(!_.isEmpty).map((_,1)).reduceByKey(_+_).collect().foreach(println)",
      "user": "anonymous",
      "dateUpdated": "2019-03-13 16:37:51.018",
      "config": {
        "colWidth": 12.0,
        "fontSize": 9.0,
        "enabled": true,
        "results": {
          "0": {
            "graph": {
              "mode": "table",
              "height": 119.0,
              "optionOpen": false
            }
          }
        },
        "editorSetting": {
          "language": "scala",
          "editOnDblClick": false,
          "completionKey": "TAB",
          "completionSupport": true
        },
        "editorMode": "ace/mode/scala",
        "tableHide": false,
        "lineNumbers": false,
        "title": false
      },
      "settings": {
        "params": {},
        "forms": {}
      },
      "results": {
        "code": "SUCCESS",
        "msg": [
          {
            "type": "TEXT",
            "data": "(hive,2)\n(mapreduce,1)\n(sqoop,1)\n(spark,2)\n(hadoop,3)\n(storm,1)\n(hbase,1)\n"
          }
        ]
      },
      "runtimeInfos": {
        "jobUrl": {
          "propertyName": "jobUrl",
          "label": "SPARK JOB",
          "tooltip": "View in Spark web UI",
          "group": "spark",
          "values": [
            "http://3bc470a167dd:4040/jobs"
          ],
          "interpreterSettingId": "spark"
        }
      },
      "apps": [],
      "jobName": "paragraph_1551171286654_13340978",
      "id": "20190226-165446_748418719",
      "dateCreated": "2019-02-26 16:54:46.654",
      "dateStarted": "2019-03-13 16:37:51.036",
      "dateFinished": "2019-03-13 16:37:52.078",
      "status": "FINISHED",
      "progressUpdateIntervalMs": 500
    },
    {
      "text": "import sqlContext.implicits._\n\nval ip2region \u003d sc.textFile(\"hdfs://10.96.111.130:9000/wordcountdemo/input/ipdata_code_with_maxmind.txt\")\n\ncase class Ip(ipstart:Long, ipend:Long, country:String, province: String, city : String, county: String)\n\nval ip_map \u003d ip2region.map(s\u003d\u003es.split(\",\")).filter(s\u003d\u003e(s.length\u003d\u003d9)).map(\ns\u003d\u003eIp(s(0).toLong,\ns(1).toLong, s(2), s(3), s(4), s(5))\n).toDF()\n\nip_map.registerTempTable(\"ip2region\")",
      "user": "anonymous",
      "dateUpdated": "2019-03-11 21:29:34.424",
      "config": {
        "colWidth": 12.0,
        "fontSize": 9.0,
        "enabled": true,
        "results": {},
        "editorSetting": {
          "language": "scala",
          "editOnDblClick": false,
          "completionKey": "TAB",
          "completionSupport": true
        },
        "editorMode": "ace/mode/scala",
        "tableHide": false,
        "editorHide": true
      },
      "settings": {
        "params": {},
        "forms": {}
      },
      "results": {
        "code": "SUCCESS",
        "msg": [
          {
            "type": "TEXT",
            "data": "warning: there was one deprecation warning; re-run with -deprecation for details\nimport sqlContext.implicits._\nip2region: org.apache.spark.rdd.RDD[String] \u003d hdfs://10.96.111.130:9000/wordcountdemo/input/ipdata_code_with_maxmind.txt MapPartitionsRDD[219] at textFile at \u003cconsole\u003e:22\ndefined class Ip\nip_map: org.apache.spark.sql.DataFrame \u003d [ipstart: bigint, ipend: bigint ... 4 more fields]\n"
          }
        ]
      },
      "apps": [],
      "jobName": "paragraph_1551258704324_672497027",
      "id": "20190227-171144_1849896810",
      "dateCreated": "2019-02-27 17:11:44.324",
      "dateStarted": "2019-03-11 21:29:34.447",
      "dateFinished": "2019-03-11 21:29:36.275",
      "status": "FINISHED",
      "progressUpdateIntervalMs": 500
    },
    {
      "text": "%sql\nselect distinct country, count(1) as sum from ip2region group by country order by country",
      "user": "anonymous",
      "dateUpdated": "2019-03-11 21:31:42.683",
      "config": {
        "colWidth": 12.0,
        "fontSize": 9.0,
        "enabled": true,
        "results": {
          "0": {
            "graph": {
              "mode": "table",
              "height": 300.0,
              "optionOpen": true,
              "setting": {
                "table": {
                  "tableGridState": {
                    "columns": [
                      {
                        "name": "country",
                        "visible": true,
                        "width": "*",
                        "sort": {},
                        "filters": [
                          {}
                        ],
                        "pinned": ""
                      },
                      {
                        "name": "sum",
                        "visible": true,
                        "width": "*",
                        "sort": {},
                        "filters": [
                          {}
                        ],
                        "pinned": ""
                      }
                    ],
                    "scrollFocus": {},
                    "selection": [],
                    "grouping": {
                      "grouping": [],
                      "aggregations": [],
                      "rowExpandedStates": {}
                    },
                    "treeView": {},
                    "pagination": {
                      "paginationCurrentPage": 1.0,
                      "paginationPageSize": 250.0
                    }
                  },
                  "tableColumnTypeState": {
                    "updated": false,
                    "names": {
                      "country": "string",
                      "sum": "string"
                    }
                  },
                  "updated": false,
                  "initialized": false,
                  "tableOptionSpecHash": "[{\"name\":\"useFilter\",\"valueType\":\"boolean\",\"defaultValue\":false,\"widget\":\"checkbox\",\"description\":\"Enable filter for columns\"},{\"name\":\"showPagination\",\"valueType\":\"boolean\",\"defaultValue\":false,\"widget\":\"checkbox\",\"description\":\"Enable pagination for better navigation\"},{\"name\":\"showAggregationFooter\",\"valueType\":\"boolean\",\"defaultValue\":false,\"widget\":\"checkbox\",\"description\":\"Enable a footer for displaying aggregated values\"}]",
                  "tableOptionValue": {
                    "useFilter": false,
                    "showPagination": false,
                    "showAggregationFooter": false
                  }
                },
                "stackedAreaChart": {
                  "rotate": {
                    "degree": "-45"
                  },
                  "xLabelStatus": "default"
                },
                "lineChart": {
                  "rotate": {
                    "degree": "-45"
                  },
                  "xLabelStatus": "default"
                },
                "multiBarChart": {
                  "rotate": {
                    "degree": "-45"
                  },
                  "xLabelStatus": "default"
                },
                "pieChart": {}
              },
              "commonSetting": {},
              "keys": [],
              "groups": [
                {
                  "name": "country",
                  "index": 0.0,
                  "aggr": "sum"
                }
              ],
              "values": [
                {
                  "name": "sum",
                  "index": 1.0,
                  "aggr": "sum"
                }
              ]
            },
            "helium": {}
          }
        },
        "editorSetting": {
          "language": "sql",
          "editOnDblClick": false,
          "completionKey": "TAB",
          "completionSupport": true
        },
        "editorMode": "ace/mode/sql",
        "tableHide": false
      },
      "settings": {
        "params": {},
        "forms": {}
      },
      "results": {
        "code": "SUCCESS",
        "msg": [
          {
            "type": "TABLE",
            "data": "country\tsum\nAU\t2\nCN\t1\n"
          },
          {
            "type": "TEXT",
            "data": ""
          }
        ]
      },
      "runtimeInfos": {
        "jobUrl": {
          "propertyName": "jobUrl",
          "label": "SPARK JOB",
          "tooltip": "View in Spark web UI",
          "group": "spark",
          "values": [
            "http://3bc470a167dd:4040/jobs"
          ],
          "interpreterSettingId": "spark"
        }
      },
      "apps": [],
      "jobName": "paragraph_1551255329665_1903770005",
      "id": "20190227-161529_2066146074",
      "dateCreated": "2019-02-27 16:15:29.665",
      "dateStarted": "2019-03-11 21:27:17.826",
      "dateFinished": "2019-03-11 21:28:01.227",
      "status": "FINISHED",
      "progressUpdateIntervalMs": 500
    },
    {
      "user": "anonymous",
      "config": {},
      "settings": {
        "params": {},
        "forms": {}
      },
      "apps": [],
      "jobName": "paragraph_1551259623076_150076614",
      "id": "20190227-172703_696983389",
      "dateCreated": "2019-02-27 17:27:03.076",
      "status": "READY",
      "progressUpdateIntervalMs": 500
    }
  ],
  "name": "sunnychan/spark",
  "id": "2E6PUMJYQ",
  "noteParams": {},
  "noteForms": {},
  "angularObjects": {
    "md:shared_process": [],
    "sh:shared_process": [],
    "spark:shared_process": []
  },
  "config": {
    "isZeppelinNotebookCronEnable": false,
    "looknfeel": "default",
    "personalizedMode": "false"
  },
  "info": {}
}
```