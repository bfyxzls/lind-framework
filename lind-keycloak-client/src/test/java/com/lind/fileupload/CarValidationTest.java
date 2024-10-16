package com.lind.fileupload;

import com.lind.fileupload.dto.UpdateUserModifyPasswordDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.testng.AssertJUnit.assertEquals;

public class CarValidationTest {

	private static Validator validator;

	@BeforeEach
	public void setUpValidator() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void testRegistrationRequiredFields() {

		UpdateUserModifyPasswordDTO userModifyPasswordDTO = new UpdateUserModifyPasswordDTO();
		userModifyPasswordDTO.setUserName("lind");
		userModifyPasswordDTO.setPassword("123456");
		Set<ConstraintViolation<UpdateUserModifyPasswordDTO>> constraintViolations = validator
				.validate(userModifyPasswordDTO);

		assertEquals(1, constraintViolations.size());

	}

}
