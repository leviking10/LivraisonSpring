package com.casamancaise.exeption;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestException extends RuntimeException {
    String message;
    HttpStatus status;
}
