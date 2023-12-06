package com.casamancaise.mapping;
import com.casamancaise.dto.DotationDto;
import com.casamancaise.entities.Dotation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ArticleMapper.class, MouvementMapper.class})
public interface DotationMapper extends EntityMapper<DotationDto, Dotation> {
    @Mapping(source = "article.idArticle", target = "articleId")
    @Override
    DotationDto toDto(Dotation dotation);
    @Mapping(source = "articleId", target = "article")
    @Override
    Dotation toEntity(DotationDto dotationDto);

    default Dotation mapToDotation(Long id) {
        if (id == null) {
            return null;
        }
        Dotation dotation = new Dotation();
        dotation.setId(id);
        return dotation;
    }

}