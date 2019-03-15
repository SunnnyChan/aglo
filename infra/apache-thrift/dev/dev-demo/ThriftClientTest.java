package com.gemantic.analyse.thrift.index;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
/**
 * 客户端
 */
public class ThriftClientTest
{
    public static void main(String[] args) throws TException
    {
        // 创建 Transport
        TTransport transport = new TSocket("127.0.0.1", 9981);
        long start = System.currentTimeMillis();
        // 创建 Protocol
        TProtocol protocol = new TBinaryProtocol(transport);

        // 基于 Potocol 创建 Client
        IndexNewsOperatorServices.Client client = new IndexNewsOperatorServices.Client(protocol);

        // 打开 Transport
        transport.open();

        // 调用服务
        // deleteArtificiallyNews
        client.deleteArtificiallyNews(123456);

        // indexNews
        NewsModel newsModel = new NewsModel();
        newsModel.setId(789456);
        newsModel.setTitle("好诗");
        newsModel.setContent("锄禾日当午，清明上河图");
        newsModel.setAuthor("ddc");
        newsModel.setMedia_from("新华08");
        String callback = client.indexNews(newsModel);
        System.out.println("==>" + callback);

        transport.close();

        System.out.println("耗时：" + (System.currentTimeMillis() - start));
        System.out.println("client sucess!");
    }

}