package com.imhungry.backend.model;

import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="searches")
public class SearchQuery extends AuditModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@UpdateTimestamp
	private LocalDateTime updateDateTime;

	private Long userId;

	private String searchTerm;

	private int amount;

	private int radius;

	private String[] collageList;

}
