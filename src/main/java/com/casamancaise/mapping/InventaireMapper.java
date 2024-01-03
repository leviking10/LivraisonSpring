package com.casamancaise.mapping;

import com.casamancaise.dto.InventaireDto;
import com.casamancaise.entities.Inventaire;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventaireMapper extends EntityMapper<InventaireDto, Inventaire> {

    @Override
    @Mapping(source = "entrepot.idEntre", target = "entrepotId")
    @Mapping(source = "article.idArticle", target = "articleId")
    InventaireDto toDto(Inventaire inventaire);

    @Override
    @Mapping(target = "entrepot.idEntre", source = "entrepotId")
    @Mapping(target = "article.idArticle", source = "articleId")
    Inventaire toEntity(InventaireDto dto);
    default Inventaire mapToInventaire(Long id) {
        if (id == null) {
            return null;
        }
        Inventaire inventaire = new Inventaire();
        inventaire.setId(id);
        return inventaire;
    }
}
