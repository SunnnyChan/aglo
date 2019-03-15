```java
package com.gemantic.analyse.thrift.index;

import org.apache.thrift.TException;

/**
 * 服务实现
 */
public class IndexNewsOperatorServicesImpl implements IndexNewsOperatorServices.Iface{

    @Override
    public boolean deleteArtificiallyNews(int id) throws TException {
        System.out.println("method deleteArtificiallyNews success !!  id is :"+id);
        return true;
    }

    @Override
    public String indexNews(NewsModel indexNews) throws TException {
        System.out.println("method indexNews success !!  data  is :");
        System.out.println(indexNews);
        return "客户端小婊砸你好，你发送的标题为【" + indexNews.getTitle() + "】的新闻已处理，请耐心等待。";
    }

}
```