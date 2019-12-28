## 怎样理解IO, BIO，NIO，AIO
在讲Netty之前，先重点理解下IO,BIO, NIO

**从计算机架构角度理解IO**, 任何涉及到计算机核心(CPU和内存)与其他设备间的数据转移过程就是IO。

**从编程的角度理解IO**, 应用程序的IO分为2个动作，IO调用和IO执行。IO调用是进程发起的，而IO执行是操作系统的工作。以一个进程的输入类型的IO调用为例子，完成的工作如下：
- 进程向操作系统请求外部数据
- 操作系统将外部数据加载到内核缓冲区
- 操作系统将数据从内核缓冲区copy到进程缓冲区
- 进程读取数据

**BIO和NIO** 阻塞和非阻塞是指进程对于操作系统IO是否处于就绪状态的处理方式。进程发起了读取数据的IO调用，操作系统需要将外部数据拷贝到进程缓冲区，在有数据拷贝到进程缓冲区前，进程缓冲区处于不可读状态，我们称之为操作系统IO未就绪。如果操作系统IO处于未就绪状态，当前进程或线程如果一直等待直到其就绪，该种IO方式为阻塞IO。如果进程或线程并不一直等待其就绪，而是可以做其他事情，这种方式为非阻塞IO。所以对于非阻塞IO，我们编程时需要经常去轮询就绪状态，也就是需要selector去不断轮训

**AIO** 操作系统连接后，通知application，不需要轮训了。AIO NIO底层都是用epoll实现，netty是封装了NIO，API更像AIO
## Netty 架构设计
### 1. 模块组件
- Bootstrap、ServerBootstrap，引导类
- Future、ChannelFuture
- Channel: 网络通信组件
- Selector: Netty 基于 Selector 对象实现 I/O 多路复用，通过 Selector 一个线程可以监听多个连接的 Channel 事件
- NioEventLoop: 维护了一个线程和任务队列，支持异步提交执行任务。
- NioEventLoopGroup，主要管理 eventLoop 的生命周期，可以理解为一个线程池，内部维护了一组线程，每个线程(NioEventLoop)负责处理多个 Channel 上的事件，而一个 Channel 只对应于一个线程。
- ChannelHandler
### 2. Channel/ChannelHandler详解
https://www.cnblogs.com/krcys/p/9297092.html

ChannelPipeline当做流水线,ChannelHandler当做流水线工人.源头的组件当做event,如read,write等

在Netty 中每个Channel 都有且仅有一个ChannelPipeline 与之对应

### 3. Netty inbound and outbound 
https://wallenwang.com/2019/06/understand-netty-inbound-outbound/

区别inbound和outbound是看event起源
- inbound: 由外部触发的事件是inbound, 外部是application之外的，比如：某个socket上读取数据进来了。

inbound 事件传播方法：

```
fireChannelRegistered()
fireChannelActive()
fireChannelRead(Object)
fireChannelReadComplete()
fireExceptionCaught(Throwable)
fireUserEventTriggered(Object)
fireChannelWritabilityChanged()
fireChannelInactive()
fireChannelUnregistered()
```

- outbound: application主动请求而触发的事件。

outbound 事件传播方法：

```
bind(SocketAddress, ChannelPromise)
connect(SocketAddress, SocketAddress, ChannelPromise)
write(Object, ChannelPromise)
flush()
read()
disconnect(ChannelPromise)
close(ChannelPromise)
deregister(ChannelPromise)
```
如果pipeline:

```
ChannelPipeline} p = ...;
 p.addLast("1", new InboundHandlerA());
 p.addLast("2", new InboundHandlerB());
 p.addLast("3", new OutboundHandlerA());
 p.addLast("4", new OutboundHandlerB());
 p.addLast("5", new InboundOutboundHandlerX());
```
一个inbound事件的处理顺序是1,2,5, 一个outbound事件的处理顺序是5,4,3

## 参考
1. https://www.cnblogs.com/imstudy/p/9908791.html