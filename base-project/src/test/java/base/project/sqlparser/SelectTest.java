package base.project.sqlparser;

import com.tang.project.entry.UserDemo;
import com.tang.project.service.UserDemoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SelectTest {

    @Autowired
    private UserDemoService userDemoService;

    @Test
    public void selectTest() {
        List<UserDemo> list = userDemoService.list();
        for (UserDemo userDemo : list) {

            System.out.println(userDemo);
        }

    }


}
