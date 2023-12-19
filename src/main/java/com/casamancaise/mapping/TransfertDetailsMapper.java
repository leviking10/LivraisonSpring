package com.casamancaise.mapping;
import com.casamancaise.dto.MouvementSortieDetailDto;
import com.casamancaise.dto.TransferDetailsDto;
import com.casamancaise.entities.MouvementSortieDetail;
import com.casamancaise.entities.TransferDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ArticleMapper.class,TransfertMapper.class})
public interface TransfertDetailsMapper {
        @Mapping(source = "articleId", target = "article.idArticle")
        @Mapping(source = "transfertId", target = "transfert.id")
        TransferDetails toEntity(TransferDetailsDto dto);
        @Mapping(source = "article.idArticle", target = "articleId")
        @Mapping(source = "transfert.id", target = "transfertId")
        TransferDetailsDto toDto(TransferDetails entity);
    }
