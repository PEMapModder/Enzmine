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
import lombok.Setter;
import net.enzmine.Server;

@RequiredArgsConstructor
public class Task implements Comparable<Task>{
	@Getter @Setter private boolean repeating;
	@Getter @Setter private int interval;

	@Getter private int nextTick;

	@Getter private final Server server;
	@Getter private final Runnable runnable;

	public Task(Server server){
		this(server, null);
	}

	@Override
	public int compareTo(Task t){
		return ((Integer) t.nextTick).compareTo(nextTick);
	}

	public final void execute0(){
		execute();
		int tick = nextSchedule();
		if(tick != -1){
			nextTick = tick;
			server.getHeart().addTask(this);
		}
	}

	protected int nextSchedule(){
		int currentTick = server.getHeart().getCurrentTick();
		if(repeating){
			return currentTick + interval;
		}
		return 0;
	}

	protected void execute(){
		if(runnable != null){
			runnable.run();
		}
	}
}
