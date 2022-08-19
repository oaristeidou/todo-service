package org.oaristeidou.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.openapitools.model.StatusType;

@Entity
@Table(name = "items")
public class ItemEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(name = "description")

  private String description;

  @Enumerated(EnumType.STRING)
  private StatusType status;

  @Column(name = "creationDate", columnDefinition = "TIMESTAMP")
  private LocalDateTime creationDate;

  @Column(name = "dueDate", columnDefinition = "TIMESTAMP")
  private LocalDateTime  dueDate;

  @Column(name = "doneDate", columnDefinition = "TIMESTAMP")
  private LocalDateTime doneDate;

  public ItemEntity(){

  }

  public ItemEntity(String description, StatusType status, LocalDateTime creationDate,
      LocalDateTime dueDate, LocalDateTime doneDate) {
    this.description = description;
    this.status = status;
    this.creationDate = creationDate;
    this.dueDate = dueDate;
    this.doneDate = doneDate;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    if (description != null){
      this.description = description;
    }
  }

  public StatusType getStatus() {
    return status;
  }

  public void setStatus(StatusType status) {
    if (status != null) {
      this.status = status;
    }
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDateTime creationDate) {
    if (creationDate != null && this.creationDate == null){
      this.creationDate = creationDate;
    }
  }

  public LocalDateTime getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDateTime dueDate) {
    if (dueDate != null && this.dueDate == null) {
      this.dueDate = dueDate;
    }
  }

  public LocalDateTime getDoneDate() {
    return doneDate;
  }

  public void setDoneDate(LocalDateTime markDoneDate) {
    if (markDoneDate != null && this.getDoneDate() == null) {
      this.doneDate = markDoneDate;
    }
  }
}
