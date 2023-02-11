package microservices.examples.common;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CodeDAO {

    @Autowired
    DataSource dataSource;
    
    public List<Code> findByCodeTypeIn(List<String> codeTypes) {
        
        List<Code> result = new ArrayList<>();

        String sql = "select * from tb_code where code_type = ANY(?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            Array tagIdsInArray = connection.createArrayOf("varchar", codeTypes.toArray());
            ps.setArray(1, tagIdsInArray);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Code c = new Code();
                    c.setCode(rs.getString("code"));
                    c.setCodeType(rs.getString("code_type"));
                    c.setValue(rs.getString("value"));
                    result.add(c);
                }
            }

        } catch (SQLException e) {
            log.error("Unknown error : {}", e);
        }

        return result;
        
    }

    
    
}
