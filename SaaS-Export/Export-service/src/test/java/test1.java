import com.itheima.domain.system.Dept;
import com.itheima.service.system.DeptService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/spring/applicationContext-*.xml")
public class test1 {

    @Autowired
    private DeptService deptService;

    @Test
    public void test2(){
        Dept dept = deptService.findDeptById("100");
        System.out.println(dept);
    }

}
