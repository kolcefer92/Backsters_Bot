package io.kolcefer.BackstersBot.repository;

import io.kolcefer.BackstersBot.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepo extends JpaRepository<Menu,String> {
}
