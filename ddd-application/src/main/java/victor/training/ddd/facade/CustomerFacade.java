package victor.training.ddd.facade;

import lombok.RequiredArgsConstructor;
import victor.training.ddd.MyException;
import victor.training.ddd.MyException.ErrorCode;
import victor.training.ddd.facade.dto.CustomerSearchCriteria;
import victor.training.ddd.facade.dto.CustomerSearchResult;
import victor.training.ddd.model.Customer.CustomerId;
import victor.training.ddd.repo.CustomerSearchRepo;
import victor.training.ddd.service.EmailSender;
import victor.training.ddd.model.Email;
import victor.training.ddd.facade.dto.CustomerDto;
import victor.training.ddd.model.Customer;
import victor.training.ddd.repo.CustomerRepo;
import victor.training.ddd.repo.EmailRepo;
import victor.training.ddd.repo.SiteRepo;
import victor.training.ddd.service.QuotingService;

import java.text.SimpleDateFormat;
import java.util.List;

@Facade
@RequiredArgsConstructor
public class CustomerFacade {
	private final CustomerRepo customerRepo;
	private final EmailSender emailSender;
	private final EmailRepo emailRepo;
	private final SiteRepo siteRepo;
	private final CustomerSearchRepo searchRepo;

	public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
		return searchRepo.search(searchCriteria);
	}

	public CustomerDto findById(long customerId) {
		Customer customer = customerRepo.findById(new CustomerId(customerId)).get();
		CustomerDto dto = new CustomerDto();
		dto.name = customer.name();
		dto.email = customer.email();
		dto.creationDateStr = new SimpleDateFormat("yyyy-MM-dd").format(customer.getCreationDate());
		dto.id = customer.id().value();
		return dto;
	}

	public void registerCustomer(CustomerDto dto) {
		CustomerId id = customerRepo.newId();
		Customer customer = new Customer(id, dto.name, null, "a@b.com", dto.siteId);
//		customer.setEmail(dto.email);
//		customer.setName(dto.name);
//		customer.setSite(siteRepo.getReference(dto.countryId)); // JPA link
//		customer.setSiteId(dto.siteId);

		if (customer.name().trim().length() <= 5) {
			throw new MyException(ErrorCode.CUSTOMER_NAME_TOO_SHORT, customer.name());
		}
		
//		if (customerRepo.customerExistsWithEmail(customer.getEmail())) {
//			throw new IllegalArgumentException("Email already registered");
//		}

		// Heavy logic
		// Heavy logic
		customerRepo.save(customer);
		// Heavy logic

		sendRegistrationEmail(customer.email());
	}

	private void sendRegistrationEmail(String emailAddress) {
		System.out.println("Sending activation link via email to "+ emailAddress);
		Email email = new Email();
		email.setToAddress(emailAddress);
		email.setFromAddress("noreply");
		email.setSubject("Welcome!");
		email.setBody("You'll like it! Sincerely, Team");
		
//		if (!emailRepo.emailWasSentBefore(email.hashCode())) {
//			emailClient.sendEmail(email.getFrom(), email.getTo(), email.getSubject(), email.getBody());
//			emailRepo.saveSentEmail(email);
//		}
	}
}
