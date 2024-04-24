package delivery.management.system.model.enums;

import lombok.Getter;

@Getter
public enum SubjectType {
    REGISTRATION("Registration"),
    FORGET_PASSWORD("Forget password"),
    REGISTERED_SUCCESSFULLY("User registered successfully"),
    RENEW_PASSWORD("Renew password");

    private final String name;

    SubjectType(String name) {
        this.name = name;
    }
}
