package co.com.powerup.r2dbc.adapter;

import co.com.powerup.model.requeststatus.RequestStatus;
import co.com.powerup.model.requeststatus.gateways.RequestStatusRepository;
import co.com.powerup.r2dbc.entity.RequestStatusEntity;
import co.com.powerup.r2dbc.helper.ReactiveAdapterOperations;
import co.com.powerup.r2dbc.repository.RequestStatusReactiveRepository;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RequestStatusReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    RequestStatus/* change for domain model */,
    RequestStatusEntity/* change for adapter model */,
    Long,
    RequestStatusReactiveRepository
> implements RequestStatusRepository {
    public RequestStatusReactiveRepositoryAdapter(RequestStatusReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, RequestStatus.class/* change for domain model */));
    }

}
