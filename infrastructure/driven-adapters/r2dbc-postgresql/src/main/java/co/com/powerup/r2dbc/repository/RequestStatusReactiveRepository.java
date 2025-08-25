package co.com.powerup.r2dbc.repository;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import co.com.powerup.r2dbc.entity.RequestStatusEntity;

@Repository
public interface RequestStatusReactiveRepository extends ReactiveCrudRepository<RequestStatusEntity, Long>, ReactiveQueryByExampleExecutor<RequestStatusEntity> {

}
