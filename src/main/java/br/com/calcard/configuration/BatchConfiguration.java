package br.com.calcard.configuration;

import javax.sql.DataSource;
import br.com.calcard.mapper.PersonRowMapper;
import br.com.calcard.model.Person;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import br.com.calcard.processor.PersonItemProcessor;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	@Autowired
	private JobRegistry jobRegistry;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private JobBuilderFactory jobs;
	
	@Autowired
	private StepBuilderFactory steps;
	@Autowired
	private SimpleJobLauncher jobLauncher;

	@Scheduled(cron = "0 33 14 * * *") //second, minute, hour, day, month, weekday.
	public void migrateCustomersFromRisk() throws Exception
	{
		System.out.println(" Job Started at :"+ new Date());
		JobParameters param = new JobParametersBuilder().addString("JobID",
				String.valueOf(System.currentTimeMillis())).toJobParameters();
		JobExecution execution = jobLauncher.run(migrateCustomersJob(), param);
		System.out.println("Job finished at: "+ new Date() +" with status :" + execution.getStatus());
	}

	public Job migrateCustomersJob() {
		return jobs.get("Job Carga Inicial")
				.incrementer(new RunIdIncrementer())
				.start(step1())
				.build();
	}

	public Step step1() {
		return steps.get("Step Carga Inicial")
				.<Person, Person> chunk(10000)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.build();
	}
	
	@Bean
	public ItemStreamReader<Person> reader() {
		JdbcCursorItemReader<Person> reader = new JdbcCursorItemReader<Person>();
		reader.setDataSource(dataSource);

		StringBuilder query = new StringBuilder();
		query.append("select ");
		query.append(" r.LIMITE_VISA,r.LIMITE_SAQUE_RAPIDO, a.id_account, p.id_person,a.id_platform,r.ID_CONTA, p.name  ");
		query.append("  from migration.ELEGIVEIS_RISCO r ");
		query.append(" join account.account a on r.CPF = a.cpf " );
		query.append(" join person.person p on r.CPF = p.cpf ");
		query.append(" where a.id_platform = r.ID_CONTA ");

		reader.setSql(query.toString());

		reader.setRowMapper(new PersonRowMapper());
		return reader;
	}

	@Bean
	public ItemProcessor<Person, Person> processor() {
		return new PersonItemProcessor();
	}

	@Bean
	public ItemWriter<Person> writer() {
		JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<Person>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>());
		writer.setSql("INSERT INTO migration.customer " +
				" (name, status, id_account, id_person, withdraw_limit, limit) " +
				" VALUES (:name, :status, :idAccount, :idPerson, :limiteSaque, :limite); " +
				" insert into migration.customer_batch_history(id_customer, customer_status, limit, withdraw_limit, " +
				" created_date) VALUES (SCOPE_IDENTITY(), :status, :limite,:limiteSaque, getdate())");
		writer.setDataSource(dataSource);
		return writer;
	}
}
