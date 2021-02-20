package victor.training.ddd.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Email {
	@Id
	@GeneratedValue
	private Long id;

	private String toAddress;
	private String fromAddress;
	private String subject;
	private String body;
}
