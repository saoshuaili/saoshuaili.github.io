package com.coachhe.annotations.追踪项目示例;

import java.util.List;

/**
 * @Author: CoachHe
 * @Date: 2023/1/27 14:02
 */
public class PasswordUtils {
    @UseCase(id = 47, description = "Passwords must contain at least one number")
    public boolean validatePassword(String password) {
        return password.matches("\\w*\\d\\w*");
    }

    @UseCase(id = 48)
    public String encryptPassword(String password) {
        return new StringBuilder(password).reverse().toString();
    }

    @UseCase(id = 49, description = "New passwords can't equal previously used ones")
    public boolean checkForNewPassword(List<String> prevPasswords, String password) {
        return !prevPasswords.contains(password);
    }
}
