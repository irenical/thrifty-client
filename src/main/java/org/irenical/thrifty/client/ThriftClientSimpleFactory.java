package org.irenical.thrifty.client;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class ThriftClientSimpleFactory<THRIFT_CLIENT extends TServiceClient> {

  private final Class<THRIFT_CLIENT> serviceClientClass;
  private final String host;
  private final int port;

  public ThriftClientSimpleFactory(Class<THRIFT_CLIENT> serviceClientClass, String host, int port) {
    this.host = host;
    this.port = port;
    this.serviceClientClass = serviceClientClass;
  }

  public THRIFT_CLIENT create() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, TTransportException {
    TTransport transport = new TSocket(host, port);
    transport = new TFramedTransport(transport);
    TProtocol protocol = new TBinaryProtocol(transport);
    Constructor<THRIFT_CLIENT> constructor = serviceClientClass.getConstructor(TProtocol.class);
    THRIFT_CLIENT result = constructor.newInstance(protocol);
    transport.open();
    return result;
  }

}