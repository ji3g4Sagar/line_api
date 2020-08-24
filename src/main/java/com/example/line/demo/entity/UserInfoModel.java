package com.example.line.demo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 機構資料
 * @author MikeyJou
 */
@Data
@Entity
@Table(
    name = "user",
    schema = "dev"
)
public class UserInfoModel implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * IDENTITY key.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private long id;

  @Column(name = "user_id", nullable = false, columnDefinition = "CHAR(32)")
  private String userId = null;

  @Column(name = "account", columnDefinition = "VARCHAR(20)")
  private String account = null; // 帳號

  @Column(name = "password", columnDefinition = "VARCHAR(128)")
  private String password = null; // 密碼SHA3-512處理

  @Column(name = "name", columnDefinition = "TEXT")
  private String name = null; // 名稱

  @Column(name = "line_user_id", columnDefinition = "TEXT")
  private String lineUserId = null; 

  @Column(name = "is_logical_delete", columnDefinition = "INT(1)")
  private Integer isLogicalDelete = 0;

  @Column(name = "update_time")
  private LocalDateTime updateTime = null;

  @Column(name = "create_time")
  private LocalDateTime createTime = null;
}
