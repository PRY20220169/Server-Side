package com.tp.pry20220169.domain.repository;

import com.tp.pry20220169.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Long> {
}
