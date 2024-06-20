package ru.hse.store.userApi.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.hse.store.common.config.MapStructConfig;
import ru.hse.store.restApi.dto.user.ShortUserResponse;
import ru.hse.store.restApi.dto.user.UserRequest;
import ru.hse.store.restApi.dto.user.UserResponse;
import ru.hse.store.userApi.entity.User;

@Mapper(config = MapStructConfig.class)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", expression = "java(encoder.encode(dto.password()))")
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "permissions", ignore = true)
//    @Mapping(target = "userRefreshTokens", ignore = true)
    User map(UserRequest dto, PasswordEncoder encoder);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "permissions", ignore = true)
//    @Mapping(target = "userRefreshTokens", ignore = true)
    User map(UserRequest dto);

    UserResponse map(User user);
    ShortUserResponse mapToShort(User user);
}
