package co.com.powerup.r2dbc.repository;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import co.com.powerup.r2dbc.entity.RequestTypeEntity;

@Repository
public interface RequestTypeReactiveRepository extends ReactiveCrudRepository<RequestTypeEntity, Long>, ReactiveQueryByExampleExecutor<RequestTypeEntity> {

}
