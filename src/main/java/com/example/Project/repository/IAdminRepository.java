package com.example.Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Project.model.Admin;

public interface IAdminRepository extends JpaRepository<Admin, Long> {

}
