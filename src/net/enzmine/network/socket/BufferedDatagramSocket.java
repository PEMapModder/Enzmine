package net.enzmine.network.socket;

/*
 * This file is part of Enzmine.
 *
 * Enzmine is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Enzmine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Enzmine.  If not, see <http://www.gnu.org/licenses/>.
 */

import lombok.Getter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.LinkedList;

public final class BufferedDatagramSocket extends Thread{
	@Getter private final DatagramSocket realSocket;
	@Getter private boolean running;

	@Getter private final LinkedList<WrappedPacket> recvBuffer = new LinkedList<>();
	@Getter private final LinkedList<WrappedPacket> sendBuffer = new LinkedList<>();

	public BufferedDatagramSocket(SocketAddress addr) throws SocketException{
		realSocket = new DatagramSocket(addr);
		realSocket.setReceiveBufferSize((1 << 20) * 8);
		realSocket.setSendBufferSize((1 << 20) * 8);
		realSocket.setSoTimeout(0);
	}

	@Override
	public void run(){
		while(running){
			DatagramPacket pk = new DatagramPacket(new byte[65535], 65535);
			try{
				realSocket.setSoTimeout(10);
				realSocket.receive(pk);

			}catch(IOException e){
				e.printStackTrace();
			}
		}
		realSocket.close();
	}

	private void sendQueued(){

	}

	public void close(){
		running = false;
	}
}
