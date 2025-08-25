package co.com.powerup.api.mapper;

import org.mapstruct.Mapper;

import co.com.powerup.api.dtos.request.RequestTypeCreateDTO;
import co.com.powerup.api.dtos.response.RequestTypeResponse;
import co.com.powerup.model.requesttype.RequestType;

@Mapper(componentModel = "spring")
public interface RequestTypeDTOMapper {

    RequestTypeResponse toResponse(RequestType requestType);
    RequestType toModel(RequestTypeCreateDTO requestTypeDTO);

}
