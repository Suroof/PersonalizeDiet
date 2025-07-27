package com.itheima.zhinengti.repository;

import com.itheima.zhinengti.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
    
    @Query("SELECT up FROM UserPreference up WHERE up.user.id = :userId")
    UserPreference findByUserId(@Param("userId") Long userId);
}