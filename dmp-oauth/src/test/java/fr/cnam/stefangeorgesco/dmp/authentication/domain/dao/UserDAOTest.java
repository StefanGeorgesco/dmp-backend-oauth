package fr.cnam.stefangeorgesco.dmp.authentication.domain.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;

@TestPropertySource("/application-test.properties")
@SpringBootTest
@SqlGroup({ @Sql(scripts = "/sql/create-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class UserDAOTest {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private User user;

	@BeforeEach
	public void setupBeforeEach() {
		user.setId("D000");
		user.setUsername("user0");
		user.setPassword("passwd");
		user.setSecurityCode("code");
	}

	@Test
	public void testUserDAOExistsById() {
		assertTrue(userDAO.existsById("D001"));
		assertFalse(userDAO.existsById("D000"));
	}

	@Test
	public void testUserDAOSave() {
		assertFalse(userDAO.existsById("D000"));

		userDAO.save(user);

		assertTrue(userDAO.existsById("D000"));
	}

	@Test
	public void testUserDAOExistsByUserName() {
		assertTrue(userDAO.existsByUsername("user"));
		assertFalse(userDAO.existsByUsername("username"));
	}

	@Test
	public void testUserDAOFindByUserName() {
		assertTrue(userDAO.findByUsername("user").isPresent());
		assertEquals("D001", userDAO.findByUsername("user").get().getId());
		assertFalse(userDAO.findByUsername("username").isPresent());
	}

	@Test
	public void testUserDAODeleteByIdSuccess() {
		assertTrue(userDAO.existsById("P001"));

		assertDoesNotThrow(() -> userDAO.deleteById("P001"));

		assertFalse(userDAO.existsById("P001"));
	}

	@Test
	public void testUserDAODeleteByIdFailureUserDoesNotExist() {
		assertFalse(userDAO.existsById("D002"));

		assertThrows(RuntimeException.class, () -> userDAO.deleteById("D002"));

	}
}
