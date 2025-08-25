package co.com.powerup.r2dbc.repository;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import co.com.powerup.r2dbc.entity.RequestClientEntity;

@Repository
public interface RequestClientReactiveRepository extends ReactiveCrudRepository<RequestClientEntity, Long>, ReactiveQueryByExampleExecutor<RequestClientEntity> {

}
