package com.casamancaise.exeptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class apiexception {
    String message;
    HttpStatus status;
    LocalDateTime timestamp;
}
