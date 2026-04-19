package com.example.service.repository;

import com.example.service.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    Optional<Song> findByFileName(String fileName);
    @Query("SELECT s FROM Song s JOIN FETCH s.user")
    List<Song> findAllWithUser();

    @Query("SELECT s FROM Song s JOIN FETCH s.user WHERE s.user.id = :userId")
    List<Song> findByUserIdWithUser(Long userId);

    @Query("SELECT s FROM Song s JOIN FETCH s.user WHERE s.user.id = :userId")
    List<Song> findByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Song s SET s.views = s.views + :count WHERE s.id = :id")
    void incrementView(@Param("id") Long id, @Param("count") int count);
}