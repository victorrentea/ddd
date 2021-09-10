package victor.training.ddd.user.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
public class Email {
	@Id
	private String id = UUID.randomUUID().toString();

	private String toAddress;
	private String fromAddress;
	private String subject;
	private String body;

	public Email() {}
	public Email(String toAddress, String fromAddress, String subject, String body) {
		this.toAddress = toAddress;
		this.fromAddress = fromAddress;
		this.subject = subject;
		this.body = body;
	}

	public String toAddress() {
		return toAddress;
	}

	public String fromAddress() {
		return fromAddress;
	}

	public String body() {
		return body;
	}

	public String subject() {
		return subject;
	}

	public Email setBody(String body) {
		this.body = body;
		return this;
	}

	public Email setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
		return this;
	}

	public Email setSubject(String subject) {
		this.subject = subject;
		return this;
	}

	public Email setToAddress(String toAddress) {
		this.toAddress = toAddress;
		return this;
	}
}
