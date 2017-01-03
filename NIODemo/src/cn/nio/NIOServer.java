package cn.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * NIO Server
 * @author haofan
 */
public class NIOServer {
    private Selector selector;

    public void initServer(int port) throws IOException {

        /**
        * Get ServerSocket channel and init for this ServerSocket Channel
         **/
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress(port));

        //get channel mange selector
        this.selector = Selector.open();
        //bind between channel mange and this channel
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    /**
     * apply polling methods to listen whether have event need to be process
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void listen() throws IOException {
        System.out.println("server success to start");
        while (true) {
            selector.select();
            Iterator ite = this.selector.selectedKeys().iterator();
            while (ite.hasNext()) {
                SelectionKey key = (SelectionKey) ite.next();
                ite.remove();
                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key
                            .channel();
                    SocketChannel channel = server.accept();
                    channel.configureBlocking(false);
                    //send message to client
                    channel.write(ByteBuffer.wrap(new String("Hello Client").getBytes()));
                    channel.register(this.selector, SelectionKey.OP_READ);

                } else if (key.isReadable()) {
                    read(key);
                }

            }

        }
    }
    /**
     * process the message that reading from client
     * @param key
     * @throws IOException
     */
    public void read(SelectionKey key) throws IOException{
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(10);
        channel.read(buffer);
        byte[] data = buffer.array();
        String msg = new String(data).trim();
        System.out.println("Server receive the message: "+msg);
        ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());
        channel.write(outBuffer);//send message to client
    }

    /**
     * test
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        NIOServer server = new NIOServer();
        server.initServer(8000);
        server.listen();
    }
}