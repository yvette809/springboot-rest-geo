package com.example.springrestgeo.validation;

import jakarta.validation.Payload;

public @interface EmojiSymbol {
    String message() default  "Must be an emoji symbol";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}