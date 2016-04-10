package net.enzmine.heart;

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
import lombok.RequiredArgsConstructor;
import net.enzmine.Server;
import net.enzmine.util.Ring;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

@RequiredArgsConstructor
@Getter
public class Heart{
	private final static long MILLIS_TO_NANOS = 1000000L;
	private final static long STD_TICK_FREQUENCY_MILLIS = 50;
	private final static int TICK_LOAD_LOG_SIZE = 100;
	private final Server server;

	private boolean running = true;
	private int currentTick = 0;

	private final Ring<Long> lastTickLoads = new Ring<>(new Long[TICK_LOAD_LOG_SIZE]);

	private final List<Runnable> shutdownTasks = new ArrayList<>();

	private final Queue<Task> tasks = new PriorityQueue<>();

	public void run(){
		while(running){
			long start = System.nanoTime();
			heartBeat();
			long end = System.nanoTime();
			long load = end - start;
			lastTickLoads.add(load);
			long sleepNanos = MILLIS_TO_NANOS * STD_TICK_FREQUENCY_MILLIS - load;
			long sleepMillis = sleepNanos / MILLIS_TO_NANOS;
			sleepNanos %= MILLIS_TO_NANOS;
			if(sleepNanos < 0){
				sleepNanos += MILLIS_TO_NANOS;
			}
			try{
				Thread.sleep(sleepMillis, (int) sleepNanos);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		shutdownTasks.forEach(Runnable::run);
	}

	private void heartBeat(){
		Task next;
		while((next = tasks.peek()).getNextTick() <= currentTick){
			next.execute();
		}
		currentTick++;
	}

	public void addTask(Task task){
		tasks.add(task);
	}

	public long averageTickLoad(){
		long sum = 0;
		for(Long load : lastTickLoads){
			sum += load;
		}
		return sum / lastTickLoads.size();
	}

	public void stop(){
		running = false;
	}
}
