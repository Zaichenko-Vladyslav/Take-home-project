package com.spatial.laser.repository;

import com.spatial.laser.model.Houses;
import com.spatial.laser.model.HousesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HousesRepository extends JpaRepository<Houses, HousesId> {

    @Query(nativeQuery = true, value = "select * from table_a")
    List<Houses> getTableA();

    @Query(nativeQuery = true, value = "select * from table_b")
    List<Houses> getTableB();

    @Query(nativeQuery = true, value = "select * from table_a union all select * from table_b")
    List<Houses> getTableAAndB();
}