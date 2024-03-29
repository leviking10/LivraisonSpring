package com.casamancaise.myexeptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiException {
    String message;
    HttpStatus status;
    LocalDateTime timestamp;
}
