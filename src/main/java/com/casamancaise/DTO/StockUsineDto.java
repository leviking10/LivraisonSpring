package com.casamancaise.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockUsineDto {
    private int id;
    private int entrepotId; // Référence à l'ID de l'entrepôt associé
    private String quart; // Quart de travail pendant lequel le stock est enregistré
    private Date dateEntree; // Date d'entrée du stock

    // Si vous souhaitez inclure les détails du stock, vous pouvez ajouter une liste de DetailsStockDTO
    // Notez que vous aurez besoin de convertir DetailsStock en DetailsStockDTO avant de les ajouter
    private List<DetailsStockDto> detailsStocks;

     private String nomEntrepot;

}
