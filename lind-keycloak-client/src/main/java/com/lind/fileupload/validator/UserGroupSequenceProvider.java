package com.lind.fileupload.validator;

import com.lind.fileupload.dto.UpdateUserModifyPasswordDTO;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * 一组验证规则的顺序设置
 *
 * @author lind
 * @date 2024/10/15 14:28
 * @since 1.0.0
 */
public class UserGroupSequenceProvider implements DefaultGroupSequenceProvider<UpdateUserModifyPasswordDTO> {


	@Override
	public List<Class<?>> getValidationGroups(UpdateUserModifyPasswordDTO object) {
		List<Class<?>> defaultGroupSequence = new ArrayList<>();
		defaultGroupSequence.add(UpdateUserModifyPasswordDTO.class); // 注意这里不要写错
		if (object.getUserName() != null && object.getUserName().equals("admin")) {
			defaultGroupSequence.add(UpdateUserModifyPasswordDTO.First.class);
		}
		// defaultGroupSequence.add(UpdateUserModifyPasswordDTO.Second.class);
		return defaultGroupSequence;
	}

}
