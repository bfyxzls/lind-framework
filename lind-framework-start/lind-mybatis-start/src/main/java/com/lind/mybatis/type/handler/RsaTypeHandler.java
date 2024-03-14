package com.lind.mybatis.type.handler;

import com.lind.common.encrypt.RSAUtils;
import com.lind.common.core.util.file.FileUtils;
import lombok.SneakyThrows;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 添加@Component之后，可以获取到springboot配置信息.
 * 加载pkulaw-mybatis-start包下的资源文件，因为使用的RsaTypeHandler的类加载器. jvm的类加载机制,自上而下加载,自下而上检查。
 * 最开始是由BootStrap ClassLoader加载rt.jar下的文件，也就是java最最核心的部分；然后由Extension
 * ClassLoader加载ext下的文件；再有App ClassLoader加载用户自己的文件。
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(String.class)
public class RsaTypeHandler extends BaseTypeHandler<String> {

	static String publicKey;
	static String privateKey;
	static Logger logger = LoggerFactory.getLogger(RsaTypeHandler.class);

	/**
	 * 加载公钥
	 * @return
	 */
	public static String getPublicKey() {
		if (publicKey == null) {
			InputStream inputStream = RsaTypeHandler.class.getClassLoader().getResourceAsStream("public.key");
			publicKey = FileUtils.readTxtFile(inputStream);
		}
		return publicKey;
	}

	/**
	 * 加载私钥
	 * @return
	 */
	public static String getPrivateKey() {
		if (privateKey == null) {
			InputStream inputStream = RsaTypeHandler.class.getClassLoader().getResourceAsStream("private.key");
			privateKey = FileUtils.readTxtFile(inputStream);
		}
		return privateKey;
	}

	@SneakyThrows
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
			throws SQLException {
		if (parameter == null) {
			ps.setString(i, null);
			return;
		}

		String encrypt = RSAUtils.encrypt(parameter, RSAUtils.getPublicKey(getPublicKey()));
		ps.setString(i, encrypt);
	}

	@SneakyThrows
	@Override
	public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return RSAUtils.decrypt(rs.getString(columnName), RSAUtils.getPrivateKey(getPrivateKey()));
	}

	@SneakyThrows
	@Override
	public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return RSAUtils.decrypt(rs.getString(columnIndex), RSAUtils.getPrivateKey(privateKey));
	}

	@SneakyThrows
	@Override
	public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return RSAUtils.decrypt(cs.getString(columnIndex), RSAUtils.getPrivateKey(privateKey));
	}

}
