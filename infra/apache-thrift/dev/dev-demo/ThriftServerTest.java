package com.gemantic.analyse.thrift.index;

import java.net.InetSocketAddress;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadPoolServer.Args;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportFactory;

/**
 * 服务端
 */
public class ThriftServerTest {
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void main(String[] args) {
        // 创建 Processer
        IndexNewsOperatorServices.Processor processor
             = new IndexNewsOperatorServices.Processor(new IndexNewsOperatorServicesImpl());
        try {
           // 创建 Transport
            TServerTransport serverTransport = new TServerSocket(new InetSocketAddress("127.0.0.1", 9981));
            Args trArgs = new Args(serverTransport);
            trArgs.processor(processor);
            // 创建 Protocol 使用二进制来编码应用层的数据
            trArgs.protocolFactory(new TBinaryProtocol.Factory(true, true));
            // 使用普通的socket来传输数据
            trArgs.transportFactory(new TTransportFactory());
            // 创建Server
            TServer server = new TThreadPoolServer(trArgs);
            // 启动Server
            server.serve();
 
            server.stop();
        }
        catch (Exception e) {
            throw new RuntimeException("index thrift server start failed!!" + "/n" + e.getMessage());
        }
    }
}