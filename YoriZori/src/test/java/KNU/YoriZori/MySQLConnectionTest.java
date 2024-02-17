package KNU.YoriZori;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.Assert.assertTrue;

@SpringBootTest
public class MySQLConnectionTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testConnection() {
        // MySQL 데이터베이스와의 연결 테스트
        assertTrue(jdbcTemplate.queryForObject("SELECT 1", Integer.class) == 1);
    }
}
