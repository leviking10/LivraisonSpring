package com.casamancaise.myexeptions;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EntityNotFoundException extends RuntimeException {
     String message;
}
