package com.lind.common.lindbase;

import com.lind.common.minibase.Bytes;
import lombok.Getter;

import java.io.IOException;

@Getter
public class Entity implements Comparable<Entity> {

	// key长度
	public static final int RAW_KEY_LEN_SIZE = 4;

	// value长茺
	public static final int VAL_LEN_SIZE = 4;

	// id长度
	public static final int SEQ_ID_SIZE = 8;

	private byte[] key;

	private byte[] value;

	private long sequenceId;

	public Entity(byte[] key, byte[] value, long sequenceId) {
		assert key != null;
		assert value != null;
		assert sequenceId >= 0;
		this.key = key;
		this.value = value;
		this.sequenceId = sequenceId;
	}

	public static Entity create(byte[] key, byte[] value, long sequenceId) {
		return new Entity(key, value, sequenceId);
	}

	public static Entity parseFrom(byte[] bytes, int offset) throws IOException {
		if (bytes == null) {
			throw new IOException("buffer is null");
		}
		if (offset + RAW_KEY_LEN_SIZE + VAL_LEN_SIZE >= bytes.length) {
			throw new IOException("Invalid offset or len. offset: " + offset + ", len: " + bytes.length);
		}
		// Decode raw key length
		int pos = offset;
		int rawKeyLen = Bytes.toInt(Bytes.slice(bytes, pos, RAW_KEY_LEN_SIZE));
		pos += RAW_KEY_LEN_SIZE;

		// Decode value length
		int valLen = Bytes.toInt(Bytes.slice(bytes, pos, VAL_LEN_SIZE));
		pos += VAL_LEN_SIZE;

		// Decode key
		int keyLen = rawKeyLen - SEQ_ID_SIZE;
		byte[] key = Bytes.slice(bytes, pos, keyLen);
		pos += keyLen;

		// Decode sequenceId
		long sequenceId = Bytes.toLong(Bytes.slice(bytes, pos, SEQ_ID_SIZE));
		pos += SEQ_ID_SIZE;

		// Decode value.
		byte[] val = Bytes.slice(bytes, pos, valLen);
		return create(key, val, sequenceId);
	}

	/**
	 * 返回k/v的长度：key长度位+value长度位+key真实长度+value真实长度
	 * @return
	 */
	public int getSerializeSize() {
		return RAW_KEY_LEN_SIZE + VAL_LEN_SIZE + getRawKeyLen() + value.length;
	}

	/**
	 * 返回key的长度:key长度+操作长度+增量
	 * @return
	 */
	private int getRawKeyLen() {
		return key.length + SEQ_ID_SIZE;
	}

	@Override
	public int compareTo(Entity o) {
		if (o == null) {
			throw new IllegalArgumentException("kv to compare should be null");
		}
		int ret = Bytes.compare(this.key, o.key);
		if (ret != 0) {
			return ret;
		}
		if (this.sequenceId != o.sequenceId) {
			return this.sequenceId > o.sequenceId ? -1 : 1;
		}
		return 0;
	}

	public byte[] toBytes() throws IOException {
		int rawKeyLen = getRawKeyLen();
		int pos = 0;
		byte[] bytes = new byte[getSerializeSize()];

		// Encode raw key length
		byte[] rawKeyLenBytes = Bytes.toBytes(rawKeyLen);
		System.arraycopy(rawKeyLenBytes, 0, bytes, pos, RAW_KEY_LEN_SIZE);
		pos += RAW_KEY_LEN_SIZE;

		// Encode value length.
		byte[] valLen = Bytes.toBytes(value.length);
		System.arraycopy(valLen, 0, bytes, pos, VAL_LEN_SIZE);
		pos += VAL_LEN_SIZE;

		// Encode key
		System.arraycopy(key, 0, bytes, pos, key.length);
		pos += key.length;

		// Encode sequenceId
		byte[] seqIdBytes = Bytes.toBytes(sequenceId);
		System.arraycopy(seqIdBytes, 0, bytes, pos, seqIdBytes.length);
		pos += seqIdBytes.length;

		// Encode value
		System.arraycopy(value, 0, bytes, pos, value.length);
		return bytes;
	}

}
