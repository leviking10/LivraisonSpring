package com.casamancaise.mapping;
import com.casamancaise.dto.DetailsDotationDto;
import com.casamancaise.entities.DetailsDotation;
import com.casamancaise.enums.Etat;
import org.mapstruct.*;
@Mapper(componentModel = "spring", uses = {ArticleMapper.class, DotationMapper.class})
public interface DetailsDotationMapper extends EntityMapper<DetailsDotationDto, DetailsDotation> {
    @Mapping(source = "dotation.id", target = "dotationId")
    @Mapping(source = "article.idArticle", target = "articleId")
    DetailsDotationDto toDto(DetailsDotation entity);

    @Mapping(source = "dotationId", target = "dotation")
    @Mapping(source = "articleId", target = "article")
    DetailsDotation toEntity(DetailsDotationDto detailsDotationDto);
    default Etat map(String etat) {
        return Etat.valueOf(etat);
    }
    @AfterMapping
    default void updateFromDto(DetailsDotationDto dto, @MappingTarget DetailsDotation entity) {
        if (dto.getQuantity() != null) {
            entity.setQuantity(dto.getQuantity());
        }
    }
}

