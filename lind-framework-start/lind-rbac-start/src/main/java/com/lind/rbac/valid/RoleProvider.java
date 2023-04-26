package com.lind.rbac.valid;

import com.lind.rbac.dto.RoleDTO;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * 校验的依赖，当isManager为true为，PrefixAdmin注解才有效.
 */
public class RoleProvider implements DefaultGroupSequenceProvider<RoleDTO> {

	@Override
	public List<Class<?>> getValidationGroups(RoleDTO object) {
		List<Class<?>> defaultGroupSequence = new ArrayList<>();

		defaultGroupSequence.add(RoleDTO.class); // 注意这里不要写错
		if (object != null && object.isManager()) {
			defaultGroupSequence.add(RoleDTO.CheckManagerGroup.class);
		}
		return defaultGroupSequence;
	}

}
