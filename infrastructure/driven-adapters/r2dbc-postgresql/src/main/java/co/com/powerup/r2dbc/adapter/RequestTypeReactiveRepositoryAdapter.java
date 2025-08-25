package co.com.powerup.r2dbc.adapter;

import co.com.powerup.model.requesttype.RequestType;
import co.com.powerup.model.requesttype.gateways.RequestTypeRepository;
import co.com.powerup.r2dbc.entity.RequestTypeEntity;
import co.com.powerup.r2dbc.helper.ReactiveAdapterOperations;
import co.com.powerup.r2dbc.repository.RequestTypeReactiveRepository;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RequestTypeReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    RequestType/* change for domain model */,
    RequestTypeEntity/* change for adapter model */,
    Long,
    RequestTypeReactiveRepository
> implements RequestTypeRepository{
    public RequestTypeReactiveRepositoryAdapter(RequestTypeReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, RequestType.class/* change for domain model */));
    }

}
