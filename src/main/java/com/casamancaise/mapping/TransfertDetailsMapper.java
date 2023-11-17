package com.casamancaise.mapping;
import com.casamancaise.dto.TransferDetailsDto;
import com.casamancaise.entities.TransferDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransfertDetailsMapper {
    TransferDetailsDto transfertDetailsToTransfertDetailsDto(TransferDetails transfertDetails);
    TransferDetails transfertDetailsDtoToTransfertDetails(TransferDetailsDto transfertDetailsDto);
}
