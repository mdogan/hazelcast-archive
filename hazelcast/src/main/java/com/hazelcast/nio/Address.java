/*
 * Copyright (c) 2008-2012, Hazel Bilisim Ltd. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.nio;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public final class Address implements DataSerializable {

    private static final long serialVersionUID = -7626390274220424603L;
    private static final byte IPv4 = 4;
    private static final byte IPv6 = 6;

    private int port = -1;
    private String host;
    private byte type;
    private transient String scopeId;

    public Address() {
    }

    public Address(InetSocketAddress inetSocketAddress) {
        this(inetSocketAddress.getAddress(), inetSocketAddress.getPort());
    }

    public Address(String host, int port) throws UnknownHostException {
        this(InetAddress.getByName(host), port);
    }

    public Address(InetAddress inetAddress, int port) {
        this.port = port;
        this.type = (inetAddress instanceof Inet4Address) ? IPv4 : IPv6;
        String[] host = inetAddress.getHostAddress().split("\\%");
        this.host = host[0];
        if (host.length == 2) {
            scopeId = host[1];
        }
    }

    public Address(Address address) {
        this.host = address.host;
        this.port = address.port;
        this.type = address.type;
        this.scopeId = address.scopeId;
    }

    public void writeData(DataOutput out) throws IOException {
        out.writeInt(port);
        out.write(type);
        byte[] address = host.getBytes();
        out.writeInt(address.length);
        out.write(address);
    }

    public void readData(DataInput in) throws IOException {
        port = in.readInt();
        type = in.readByte();
        int len = in.readInt();
        byte[] address = new byte[len];
        in.readFully(address);
        host = new String(address);
    }

    public void writeObject(ByteBuffer buffer) {
        buffer.putInt(port);
        buffer.put(type);
        byte[] address = host.getBytes();
        buffer.putInt(address.length);
        buffer.put(address);
    }

    public void readObject(ByteBuffer buffer) {
        port = buffer.getInt();
        type = buffer.get();
        int len = buffer.getInt();
        byte[] address = new byte[len];
        buffer.get(address);
        host = new String(address);
    }

    public String getHost() {
        return host;
    }

    @Override
    public String toString() {
        return "Address[" + getHost() + "]:" + port;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getInetAddress() throws UnknownHostException {
        return InetAddress.getByName(getScopedHost());
    }

    public InetSocketAddress getInetSocketAddress() throws UnknownHostException {
        return new InetSocketAddress(getInetAddress(), port);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        final Address address = (Address) o;
        return port == address.port && this.type == address.type && this.host.equals(address.host);
    }

    @Override
    public int hashCode() {
        return hash(host.getBytes()) * 29 + port;
    }

    private int hash(byte[] bytes) {
        int hash = 0;
        for (byte b : bytes) {
            hash = (hash * 29) + b;
        }
        return hash;
    }

    public boolean isIPv4() {
        return type == IPv4;
    }

    public boolean isIPv6() {
        return type == IPv6;
    }

    public String getScopeId() {
        return isIPv6() ? scopeId : null;
    }

    public void setScopeId(final String scopeId) {
        if (isIPv6()) {
            this.scopeId = scopeId;
        }
    }

    public String getScopedHost() {
        return (isIPv4() || scopeId == null) ? getHost()
                : getHost() + "%" + scopeId;
    }
}
