package co.com.powerup.model.userinfo.gateways;

import co.com.powerup.model.userinfo.UserInfo;
import reactor.core.publisher.Mono;

public interface UserInfoRepository {

    Mono<UserInfo> findByEmail(String email, String authorization);
    Mono<UserInfo> selfSearch(String authorization);
}
