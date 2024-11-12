package com.lind.hibernate.controller;

import com.lind.hibernate.dto.UpdateUserModifyPasswordDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * @author lind
 * @date 2024/10/15 14:07
 * @since 1.0.0
 */

@Controller
@RequestMapping("/reset-password")
public class PasswordResetController {

	@ModelAttribute("passwordResetForm")
	public UpdateUserModifyPasswordDTO passwordReset() {
		return new UpdateUserModifyPasswordDTO();
	}

	@GetMapping
	public String showPasswordReset(Model model) {
		return "reset-password";
	}

	@PostMapping
	public String handlePasswordReset(@ModelAttribute("passwordResetForm") @Valid UpdateUserModifyPasswordDTO form,
			BindingResult result) {

		if (result.hasErrors()) {
			return "reset-password";
		}

		// save/updaate form here

		return "redirect:/login?resetSuccess";
	}

}
