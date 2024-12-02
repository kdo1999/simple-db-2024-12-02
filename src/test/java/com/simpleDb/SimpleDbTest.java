package com.simpleDb;

import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class SimpleDbTest {
	private static SimpleDb simpleDb;

	@BeforeAll
	public static void beforeAll() {
		simpleDb = new SimpleDb("localhost", "root", "test1234", "simpleDb__test");
		simpleDb.setDevMode(true);

		createArticleTable();
	}

	@BeforeEach
	public void beforeEach() {
		truncateArticleTable();
		makeArticleTestData();
	}

	private static void createArticleTable() {
		simpleDb.run("DROP TABLE IF EXISTS article");

		simpleDb.run("""
			CREATE TABLE article (
			    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
			    PRIMARY KEY(id),
			    createdDate DATETIME NOT NULL,
			    modifiedDate DATETIME NOT NULL,
			    title VARCHAR(100) NOT NULL,
			    `body` TEXT NOT NULL,
			    isBlind BIT(1) NOT NULL DEFAULT 0
			)
			""");
	}

	private void makeArticleTestData() {
		IntStream.rangeClosed(1, 6).forEach(no -> {
			boolean isBlind = no > 3;
			String title = "제목%d".formatted(no);
			String body = "내용%d".formatted(no);

			simpleDb.run("""
				INSERT INTO article
				SET createdDate = NOW(),
				modifiedDate = NOW(),
				title = ?,
				`body` = ?,
				isBlind = ?
				""", title, body, isBlind);
		});
	}

	private void truncateArticleTable() {
        simpleDb.run("TRUNCATE article");
    }

}