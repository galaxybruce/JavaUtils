package com.galaxybruce.parsebyte.m2;

import com.galaxybruce.writebyte.ByteUtils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


/**
 * @date 2022/4/4 09:49
 * @author bruce.zhang
 * @description 参考Smack代码。
 * [XMPPTCPConnection](https://github.com/igniterealtime/Smack/blob/master/smack-tcp/src/main/java/org/jivesoftware/smack/tcp/XMPPTCPConnection.java)
 * <p>
 * modification history:
 */
class PacketReader {
    private Thread readerThread;
    private ExecutorService listenerExecutor;
    private InputStream reader;
	private boolean done;

	public PacketReader() {
		init();
	}
	private void init(){
		done = false;
		readerThread = new Thread(){
			
			public void run() {
				parsePackets(this);
			}
		};
		readerThread.setName("IM Packet Reader");
		readerThread.setDaemon(true);

		// Create an executor to deliver incoming packets to listeners. We'll use a single
        // thread with an unbounded queue.
        listenerExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {

            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable,"IM Listener Processor");
                thread.setDaemon(true);
                return thread;
            }
        });
        resetParser();
	}
	  /**
     * Resets the parser using the latest connection's reader. Reseting the parser is necessary
     * when the plain connection has been secured or when a new opening stream element is going
     * to be sent by the server.
     */
    private void resetParser() {
        try {
//        	reader = connection.socket.getInputStream();
        }
        catch (Exception xppe) {
            xppe.printStackTrace();
        }
    }
	public void startup(){
        readerThread.start();
	}

	public void parsePackets(Thread thread) {
		// 循环按照协议读取数据
		while (!done && thread == readerThread) {
			try {
				onReceive(reader);
			} catch (IOException e) {
//				if (!(done || connection.isSocketClosed())) {
//					e.printStackTrace();
//					notifyConnectionError(e);
//				}
			}
		}
	}

	private void onReceive(InputStream data) throws IOException{
		DataInputStream dataInputStream = new DataInputStream(data);
		byte [] first4Bytes = new byte[4];
		dataInputStream.readFully(first4Bytes);

		byte messageType = dataInputStream.readByte();
		if (messageType < 0 || messageType > 8) {
			throw new IOException("Packet Header(messageType) Error.");
		}
		
		byte messageFormat = dataInputStream.readByte();
		if (messageFormat < 0 || messageFormat > 1) {
			throw new IOException("Packet Header(messageFormat) Error.");
		}

		int bodyLen = ByteUtils.byte2int(first4Bytes);
		byte [] body = new byte[bodyLen];
		dataInputStream.readFully(body);

		// 解析body
	}

	
	public void shutdown() {
		done = true;
        // Shut down the listener executor.
        listenerExecutor.shutdown();
	}

}