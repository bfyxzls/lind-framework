package com.lind.minio.util;

import com.lind.minio.model.FileModel;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class MinioUtil {

	private final MinioClient minioClient;

	/**
	 * 创建一个桶
	 */
	public void createBucket(String bucket) throws Exception {
		boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
		if (!found) {
			minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
		}
	}

	/**
	 * 上传一个文件
	 */
	public void uploadFile(InputStream stream, String bucket, String objectName) throws Exception {
		minioClient.putObject(
				PutObjectArgs.builder().bucket(bucket).object(objectName).stream(stream, -1, 10485760).build());
	}

	/**
	 * 列出所有的桶
	 */
	public List<String> listBuckets() throws Exception {
		List<Bucket> list = minioClient.listBuckets();
		List<String> names = new ArrayList<>();
		list.forEach(b -> {
			names.add(b.name());
		});
		return names;
	}

	/**
	 * 列出一个桶中的所有文件和目录
	 */
	public List<FileModel> listFiles(String bucket) throws Exception {
		Iterable<Result<Item>> results = minioClient
				.listObjects(ListObjectsArgs.builder().bucket(bucket).recursive(true).build());

		List<FileModel> infos = new ArrayList<>();
		results.forEach(r -> {
			FileModel info = new FileModel();
			try {
				Item item = r.get();
				info.setFilename(item.objectName());
				info.setDirectory(item.isDir());
				infos.add(info);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		});
		return infos;
	}

	/**
	 * 下载一个文件
	 */
	public InputStream download(String bucket, String objectName) throws Exception {
		InputStream stream = minioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(objectName).build());
		return stream;
	}

	/**
	 * 删除一个桶
	 */
	public void deleteBucket(String bucket) throws Exception {
		minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucket).build());
	}

	/**
	 * 删除一个对象
	 */
	public void deleteObject(String bucket, String objectName) throws Exception {
		minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(objectName).build());
	}

	/**
	 * 从输入流中获取字节数组
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public byte[] fromStreamToBytes(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);// 将字节数组buffer,off索引开始,len个字节写入到字节输出流bos的"字节数组"中,bos.write内部方法为System.arraycopy(b,
			// off, buf, count, len);
		}
		bos.close();
		return bos.toByteArray();
	}

}
