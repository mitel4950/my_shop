package alien.marshmallow.auth_service.mapper;

import alien.marshmallow.auth_service.domain.dto.SignUpRequest;
import alien.marshmallow.auth_service.domain.entity.UserAuthEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserAuthEntity toEntity(SignUpRequest dto);

}
