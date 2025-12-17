package org.example.web_service_v2.auth.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.web_service_v2.auth.dto.signup.SignupRequest;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, SignupRequest> {

    @Override
    public boolean isValid(SignupRequest req, ConstraintValidatorContext ctx) {
        if (req == null) return true;

        String pw = req.getPassword();
        String confirm = req.getPasswordMatch();

        if (pw == null || pw.isBlank() || confirm == null || confirm.isBlank()) {
            return true;
        }

        boolean ok = pw.equals(confirm);
        if (!ok) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate("비밀번호가 일치하지 않습니다.")
                    .addPropertyNode("password_match") // 필드명 정확히
                    .addConstraintViolation();
        }

        return ok;
    }
}
