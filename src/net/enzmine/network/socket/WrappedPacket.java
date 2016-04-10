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
import lombok.RequiredArgsConstructor;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

@RequiredArgsConstructor
public class WrappedPacket{
	@Getter private final ByteBuffer data;
	@Getter private final SocketAddress source;
}
