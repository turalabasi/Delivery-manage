package delivery.management.system.model.enums;

import com.fasterxml.jackson.core.JsonParseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum Exceptions {
    TOKEN_EXPIRED_EXCEPTION("exception.unauthorized.token-expired", HttpStatus.UNAUTHORIZED),
    ID_NOT_FOUND_EXCEPTION("exception.id-not-found", HttpStatus.BAD_REQUEST),
    USER_MUST_NOT_BE_NULL("exception.null-pointer.user", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_MUST_NOT_BE_NULL("exception.null-pointer.refresh-token", HttpStatus.BAD_REQUEST),
    USER_NULL_POINTER_EXCEPTION("exception.null-pointer", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND("product not found", HttpStatus.NOT_FOUND),
    PRODUCT_QUANTITY_INSUFFICIENT("product quantity insufficient",HttpStatus.BAD_REQUEST),
    NOT_FOUND_CART("cart not found", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND_IN_CART("product not found in cart",HttpStatus.NOT_FOUND),
    INSUFFICIENT_PRODUCT_COUNT("Insufficient product count in cart",HttpStatus.BAD_REQUEST),
    CART_NOT_FOUND_FOR_USER("no cart found for user",HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND("category.not.found.exception",HttpStatus.NOT_FOUND),
    INVALID_CATEGORY_IDS("invalid.category.ids.exception",HttpStatus.BAD_REQUEST),
    CATEGORY_ALREADY_INACTIVE("category.already.inactive.exception",HttpStatus.BAD_REQUEST),
    DRIVER_NOT_FOUND("driver not found",HttpStatus.NOT_FOUND),
    DRIVER_APPEALS_EMPTY("driver appeals empty",HttpStatus.NOT_FOUND),
    DRIVER_STATUS_NOT_FOUND("driver status not found",HttpStatus.NOT_FOUND),
    DRIVER_LIST_EMPTY("driver list is empty",HttpStatus.NOT_FOUND),
    DRIVER_TYPE_EMPTY("driver type is empty",HttpStatus.NOT_FOUND),
    DRIVER_USER_NOT_FOUND("driver.user not found ",HttpStatus.NOT_FOUND),
    FILE_NOT_FOUND("FILE NOT FOUND",HttpStatus.NOT_FOUND),
    PAYMENT_INSUFFICIENT("payment insufficient",HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND("order not found",HttpStatus.NOT_FOUND),
    NOT_FOUND("order status not found ",HttpStatus.NOT_FOUND),
    PRODUCT_COUNT_UPDATE_FAILED("product count update failed",HttpStatus.BAD_REQUEST),
    NOT_FOUND_CUSTOMER("exception.not.found.cart", HttpStatus.NOT_FOUND);


    private final String message;
    private final HttpStatus httpStatus;
    Exceptions(String message,HttpStatus  httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
