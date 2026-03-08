package com.example.sbd_modul2_kel03.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.sbd_modul2_kel03.model.Mahasiswa;

@Controller
public class MahasiswaController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ================= INDEX =================
    @GetMapping("/")
    public String index(Model model) {

        String sql = "SELECT * FROM mahasiswa";
        List<Mahasiswa> mahasiswa;

        try {
            mahasiswa = jdbcTemplate.query(
                    sql,
                    BeanPropertyRowMapper.newInstance(Mahasiswa.class)
            );
        } catch (DataAccessException ex) {
            mahasiswa = new ArrayList<>();
            model.addAttribute("error", "Database error: " + ex.getMessage());
        }

        model.addAttribute("mahasiswa", mahasiswa);
        return "index";
    }

    // ================= ADD =================
    @GetMapping("/add")
    public String add() {
        return "add";
    }

    @PostMapping("/add")
    public String add(Mahasiswa mahasiswa){

    String sql = "INSERT INTO mahasiswa VALUES(?,?,?,?,0)";

    jdbcTemplate.update(
            sql,
            mahasiswa.getNim(),
            mahasiswa.getNama(),
            mahasiswa.getAngkatan(),
            mahasiswa.getGender()
    );

    return "redirect:/";
}

    // ================= EDIT =================
    @GetMapping("/edit/{nim}")
    public String edit(@PathVariable("nim") String nim, Model model) {

        String sql = "SELECT * FROM mahasiswa WHERE nim = ?";

        try {
            Mahasiswa mahasiswa = jdbcTemplate.queryForObject(
                    sql,
                    BeanPropertyRowMapper.newInstance(Mahasiswa.class),
                    nim
            );

            model.addAttribute("mahasiswa", mahasiswa);
            return "edit";

        } catch (DataAccessException ex) {
            model.addAttribute("error", "Data tidak ditemukan");
            return "redirect:/";
        }
    }

    @PostMapping("/edit")
    public String edit(Mahasiswa mahasiswa, Model model) {

        String sql = "UPDATE mahasiswa SET nama = ?, angkatan = ?, gender = ? WHERE nim = ?";

        try {
            jdbcTemplate.update(sql,
                    mahasiswa.getNama(),
                    mahasiswa.getAngkatan(),
                    mahasiswa.getGender(),
                    mahasiswa.getNim()
            );

            return "redirect:/";

        } catch (DataAccessException ex) {
            model.addAttribute("error", "Gagal mengubah data");
            model.addAttribute("mahasiswa", mahasiswa);
            return "edit";
        }
    }

    // ================= DELETE =================
    @GetMapping("/delete/{nim}")
    public String delete(@PathVariable("nim") String nim) {

        String sql = "DELETE FROM mahasiswa WHERE nim = ?";
        jdbcTemplate.update(sql, nim);

        return "redirect:/";
    }

    // ================= DETAIL =================
    @GetMapping("/detail/{nim}")
    public String detail(@PathVariable("nim") String nim, Model model) {

    String sqlMahasiswa = "SELECT * FROM mahasiswa WHERE nim = ?";

    Mahasiswa mahasiswa = jdbcTemplate.queryForObject(
            sqlMahasiswa,
            BeanPropertyRowMapper.newInstance(Mahasiswa.class),
            nim
    );

    model.addAttribute("mahasiswa", mahasiswa);
    return "detail";
}

    @PostMapping("/multidelete")
    public String multiDelete(@RequestParam("nim") List<String> nims){

    for(String nim : nims){

        String sql = "DELETE FROM mahasiswa WHERE nim=?";
        jdbcTemplate.update(sql,nim);

    }

    return "redirect:/";
    }
}