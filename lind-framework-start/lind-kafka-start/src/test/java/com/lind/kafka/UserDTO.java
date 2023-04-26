package com.lind.kafka;

import com.lind.kafka.entity.MessageEntityAware;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class UserDTO implements MessageEntityAware {

	private String title;

}
