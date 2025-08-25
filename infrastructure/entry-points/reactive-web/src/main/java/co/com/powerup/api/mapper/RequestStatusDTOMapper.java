package co.com.powerup.api.mapper;

import org.mapstruct.Mapper;

import co.com.powerup.api.dtos.request.RequestStatusCreateDTO;
import co.com.powerup.api.dtos.response.RequestStatusResponse;
import co.com.powerup.model.requeststatus.RequestStatus;



@Mapper(componentModel = "spring")
public interface RequestStatusDTOMapper {

    RequestStatusResponse toResponse(RequestStatus RequestStatus);
    RequestStatus toModel(RequestStatusCreateDTO RequestStatusCreateDTO);

}
