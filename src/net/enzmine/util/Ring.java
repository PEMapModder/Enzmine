package net.enzmine.util;

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

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Iterator;

@RequiredArgsConstructor
public class Ring<T> implements Iterable<T>{
	private final T[] data;

	private int startOffset;
	private int size;

	public boolean add(T t){
		if(size < data.length){
			data[size++] = t;
		}else{
			data[startOffset++] = t;
		}
		return true;
	}

	public T get(int publicOffset){
		if(publicOffset >= size){
			throw new IndexOutOfBoundsException("The Ring is not that big!");
		}
		return data[(startOffset + publicOffset) % data.length];
	}

	public int size(){
		return size;
	}

	public Object[] toArray(){
		if(size < data.length){
			return Arrays.copyOf(data, size);
		}
		assert data.length == size;
		Object[] array = new Object[size];
		System.arraycopy(data, startOffset, array, 0, size - startOffset);
		System.arraycopy(data, 0, array, size - startOffset, startOffset);
		return array;
	}

	public T[] toArray(T[] array){
		if(array.length < size){
			return Arrays.copyOf(data, size);
		}
		assert data.length == size;
		System.arraycopy(data, startOffset, array, 0, size - startOffset);
		System.arraycopy(data, 0, array, size - startOffset, startOffset);
		return array;
	}

	public void clear(){
		if(size == 0){
			return;
		}
		size = 0;
		Arrays.fill(data, null);
	}

	@Override
	public Iterator<T> iterator(){
		return new RingIterator();
	}

	private class RingIterator implements Iterator<T>{
		private final T[] data;
		private final int start, end;
		private int next = 0;
		private boolean first = true;

		public RingIterator(){
			data = Ring.this.data;
			start = startOffset;
			end = size < data.length ? size : startOffset == 0 ? data.length - 1 : startOffset - 1;
		}

		@Override
		public T next(){
			if(first || next > start || next < end){
				first = false;
				if(next == data.length){
					next = 0;
				}
				return data[next++];
			}
			return null;
		}

		@Override
		public boolean hasNext(){
			return first && size > 0 || next > start || next < end;
		}
	}
}
