package co.com.powerup.r2dbc.repository;

import java.util.List;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import co.com.powerup.r2dbc.entity.RequestClientEntity;
import reactor.core.publisher.Flux;

@Repository
public interface RequestClientReactiveRepository extends ReactiveCrudRepository<RequestClientEntity, Long>, ReactiveQueryByExampleExecutor<RequestClientEntity> {
    @Query("SELECT * FROM request_client WHERE status_id IN (:statusIds) ORDER BY id DESC LIMIT :size OFFSET :offset")
    Flux<RequestClientEntity> findByStatusIds(@Param("statusIds") List<Long> statusIds, @Param("offset") int offset, @Param("size") int size);

    Flux<RequestClientEntity> findByEmailAndStatusId(String email, Long statusId);

}
