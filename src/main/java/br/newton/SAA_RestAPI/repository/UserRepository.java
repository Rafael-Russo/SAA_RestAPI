package br.newton.SAA_RestAPI.repository;

import br.newton.SAA_RestAPI.model.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {
    UserEntity findByUsername(String username);
}