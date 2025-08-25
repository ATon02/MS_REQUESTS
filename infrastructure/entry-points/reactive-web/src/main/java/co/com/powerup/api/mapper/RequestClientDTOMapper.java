package co.com.powerup.api.mapper;

import org.mapstruct.Mapper;

import co.com.powerup.api.dtos.request.RequestClientCreateDTO;
import co.com.powerup.api.dtos.response.RequestClientResponse;
import co.com.powerup.model.requestclient.RequestClient;


@Mapper(componentModel = "spring")
public interface RequestClientDTOMapper {

    RequestClientResponse toResponse(RequestClient requestClient);
    RequestClient toModel(RequestClientCreateDTO requestClientCreateDTO);

}
