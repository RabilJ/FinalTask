package com.example.bukmacher.Repository;

import com.example.bukmacher.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    public User findByUsername(String username);

    public User findByActivationKey(String activationKey);
}
