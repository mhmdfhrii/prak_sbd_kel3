package com.example.sbd_modul2_kel03.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MahasiswaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public void softDelete(String nim){
        String sql = "UPDATE mahasiswa SET deleted = 1 WHERE nim=?";
        jdbcTemplate.update(sql, nim);

    }

}
