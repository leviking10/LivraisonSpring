package com.casamancaise.dto;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {
    private Long idArticle;
    @NotNull(message = "Le champ référence ne peut pas être vide.")
    @Size(min = 3, max = 100, message = "La référence doit comporter entre 3 et 100 caractères.")
    private String refArti;
    @NotNull(message = "Le champ libellé ne peut pas être vide.")
    private String libArti;
    @NotNull(message = "Le champ cout de production ne peut pas être vide.")
    private Double coutProd;
    @NotNull(message = "Le champ unité ne peut pas être vide.")
    private String unite;
    @NotNull(message = "Le champ marge securitaire ne peut pas être vide.")
    private Double margeSecu;
    @NotNull(message = "Le champ tonage ne peut pas être vide.")
    private Double tonage;

}
