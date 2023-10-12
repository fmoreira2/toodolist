package br.com.fms.toodolist.user;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<UserModel, UUID>{

    UserModel findByUserName(String userName);
    List<UserModel> findAllByOrderByNameAsc();
    
}
