package co.com.powerup.r2dbc.adapter;

import co.com.powerup.model.requestclient.RequestClient;
import co.com.powerup.model.requestclient.gateways.RequestClientRepository;
import co.com.powerup.r2dbc.entity.RequestClientEntity;
import co.com.powerup.r2dbc.helper.ReactiveAdapterOperations;
import co.com.powerup.r2dbc.repository.RequestClientReactiveRepository;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RequestClientReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    RequestClient/* change for domain model */,
    RequestClientEntity/* change for adapter model */,
    Long,
    RequestClientReactiveRepository
> implements RequestClientRepository{
    public RequestClientReactiveRepositoryAdapter(RequestClientReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, RequestClient.class/* change for domain model */));
    }

}
