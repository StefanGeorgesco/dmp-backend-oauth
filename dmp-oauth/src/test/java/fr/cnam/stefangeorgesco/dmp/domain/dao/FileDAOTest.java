package fr.cnam.stefangeorgesco.dmp.domain.dao;

import fr.cnam.stefangeorgesco.dmp.domain.model.File;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application-test.properties")
@DataJpaTest
@SqlGroup({ @Sql(scripts = "/sql/create-specialties.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-files.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-files.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-specialties.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class FileDAOTest {

	@Autowired
	private FileDAO fileDAO;

	@Test
	public void testFileDAOExistsById() {
		assertTrue(fileDAO.existsById("D001"));
		assertTrue(fileDAO.existsById("P001"));
		assertFalse(fileDAO.existsById("P002"));
	}

	@Test
	public void testFileDAOFindById() {

		Optional<File> optionalFile = fileDAO.findById("D001");

		assertTrue(optionalFile.isPresent());

		File file = optionalFile.get();

		assertEquals(file.getFirstname(), "John");
		assertEquals(file.getLastname(), "Smith");
	}

}
