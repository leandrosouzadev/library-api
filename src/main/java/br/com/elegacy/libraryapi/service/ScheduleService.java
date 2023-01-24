package br.com.elegacy.libraryapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.elegacy.libraryapi.model.entity.Loan;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {

	private static final String CRON_LATE_LOANS = "0 0 0 1/1 * ?";

	@NonNull
	private final LoanService loanService;
	
	@NonNull
	private final EmailService emailService;
	
	@Value("${application.mail.lateloans.message}")
	private String message;
	
	@Scheduled(cron = CRON_LATE_LOANS)
	public void sendMailToLateLoans() {
		List<Loan> allLateLoans = loanService.getAllLateLoans();

		List<String> mailsList = allLateLoans.stream()
				.map(Loan::getCustomerEmail)
				.toList();

		emailService.sendMails(message, mailsList);
	}
}
