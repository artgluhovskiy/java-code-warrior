package org.art.web.warrior.client.config.validation;

import org.art.web.warrior.client.dto.UserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

   @Override
   public boolean isValid(Object userDto, ConstraintValidatorContext context) {
      return userDto.getPassword().equals(userDto.getMatchingPassword());
   }
}
