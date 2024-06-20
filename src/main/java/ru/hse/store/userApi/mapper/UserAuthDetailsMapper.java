package ru.hse.store.userApi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.GrantedAuthority;
import ru.hse.store.common.config.MapStructConfig;
import ru.hse.store.userApi.dto.UserAuthDetails;
import ru.hse.store.userApi.entity.User;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface UserAuthDetailsMapper {

    @Mapping(target = "authorities", source = "authorities")
    UserAuthDetails map(User user, List<GrantedAuthority> authorities);
}

