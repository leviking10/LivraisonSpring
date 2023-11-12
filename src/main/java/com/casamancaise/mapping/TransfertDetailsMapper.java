package com.casamancaise.mapping;
import com.casamancaise.DTO.TransferDetailsDto;
import com.casamancaise.Entities.TransferDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransfertDetailsMapper {
    TransferDetailsDto transfertDetailsToTransfertDetailsDto(TransferDetails transfertDetails);
    TransferDetails transfertDetailsDtoToTransfertDetails(TransferDetailsDto transfertDetailsDto);
}
