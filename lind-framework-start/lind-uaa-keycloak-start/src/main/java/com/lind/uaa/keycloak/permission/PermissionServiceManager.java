package com.lind.uaa.keycloak.permission;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collections;
import java.util.List;

import static com.lind.uaa.keycloak.config.Constant.KC_PERMISSION;
import static com.lind.uaa.keycloak.config.Constant.KC_ROLE_PERMISSION;

/**
 * 获取数据仓库里的权限,会进行缓存的操作.
 */
@RequiredArgsConstructor
public class PermissionServiceManager {

	private final RedisTemplate<String, Object> template;

	private final PermissionService permissionService;

	private final KeycloakSpringBootProperties keycloakSpringBootProperties;

	@SneakyThrows
	public List<? extends ResourcePermission> getPermissionByRoleFromCache() {
		String key = KC_PERMISSION.concat(keycloakSpringBootProperties.getResource());

		if (template.hasKey(key)) {
			ObjectMapper om = new ObjectMapper();
			JsonParser jsonParser = om.getFactory().createParser(om.writeValueAsBytes(template.opsForValue().get(key)));
			return jsonParser.readValueAs(new TypeReference<List<ResourcePermission>>() {
			});
		}
		List<? extends ResourcePermission> resourcePermissions = permissionService.getAll();
		if (resourcePermissions != null) {
			template.opsForValue().set(key, resourcePermissions);
			return resourcePermissions;
		}
		return Collections.emptyList();
	}

	/**
	 * get data by roleId from cache
	 * @param roleId
	 * @return
	 */
	@SneakyThrows
	public List<? extends ResourcePermission> getPermissionByRoleFromCache(String roleId) {
		String key = KC_ROLE_PERMISSION.concat(keycloakSpringBootProperties.getResource()).concat("::").concat(roleId);

		if (template.hasKey(key)) {
			ObjectMapper om = new ObjectMapper();
			JsonParser jsonParser = om.getFactory().createParser(om.writeValueAsBytes(template.opsForValue().get(key)));
			return jsonParser.readValueAs(new TypeReference<List<ResourcePermission>>() {
			});
		}
		List<? extends ResourcePermission> resourcePermissions = permissionService.getByRoleId(roleId);
		if (resourcePermissions != null) {
			template.opsForValue().set(key, resourcePermissions);
			return resourcePermissions;
		}
		return Collections.emptyList();
	}

}
