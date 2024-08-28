package de.stella.agora_web.banned.model;

import de.stella.agora_web.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "banned")
public class Banned {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "ban_date")
  private Date banDate;

  @Column(name = "ban_reason")
  private String banReason;

  @Column(name = "data_retained")
  private boolean dataRetained;

  @Column(name = "eu_data_protection")
  private boolean euDataProtection;

  @ManyToOne
  @JoinColumn(name = "user_id", insertable = false, updatable = false)
  private User user;

  // Getters y setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Date getBanDate() {
    return banDate;
  }

  public void setBanDate(Date banDate) {
    this.banDate = banDate;
  }

  public String getBanReason() {
    return banReason;
  }

  public void setBanReason(String banReason) {
    this.banReason = banReason;
  }

  public boolean isDataRetained() {
    return dataRetained;
  }

  public void setDataRetained(boolean dataRetained) {
    this.dataRetained = dataRetained;
  }

  public boolean isEuDataProtection() {
    return euDataProtection;
  }

  public void setEuDataProtection(boolean euDataProtection) {
    this.euDataProtection = euDataProtection;
  }
}
