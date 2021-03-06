package com.lec.spring;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.lec.spring.domain.WriteDTO;

public class WriteValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		
		return WriteDTO.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		System.out.println("[WriteValidator::validate() 호출]");
		WriteDTO dto = (WriteDTO)target;
		
		String subject = dto.getSubject();
		if(subject == null || subject.trim().isEmpty()) {
			errors.rejectValue("subject", "empty subject");
		}
		
		String name = dto.getName();
		if(name == null || name.trim().isEmpty()) {
			errors.rejectValue("name", "empty name");
		}
	}

}
