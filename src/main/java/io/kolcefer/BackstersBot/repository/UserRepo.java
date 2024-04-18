package io.kolcefer.BackstersBot.repository;

import io.kolcefer.BackstersBot.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Users,Long> {



}
