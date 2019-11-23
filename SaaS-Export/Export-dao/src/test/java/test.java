import com.itheima.dao.cargo.ContractProductDao;
import com.itheima.dao.cargo.ProductMessageDao;
import com.itheima.dao.cargo.ProductMsgDao;
import com.itheima.dao.company.CompanyDao;
import com.itheima.dao.system.DeptDao;
import com.itheima.dao.system.UserDao;
import com.itheima.domain.cargo.*;
import com.itheima.domain.company.Company;
import com.itheima.domain.system.Dept;
import com.itheima.domain.system.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/applicationContext-*.xml")
public class test {

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DeptDao deptDao;

    @Autowired
    private ContractProductDao contractProductDao;

    @Autowired
    private ProductMessageDao productMessageDao;

    @Autowired
    private ProductMsgDao productMsgDao;

    @Test
    public void test1(){
        List<Company> list = companyDao.findAllCompany();
        System.out.println(list.toString());
    }

    @Test
    public  void test2(){
        List<Dept> list = deptDao.findAllDept("1");
        System.out.println(list);
    }

    @Test
    public void test3(){
        User user = userDao.findByEmail("lw@export.com");
        System.out.println(user);

    }

    @Test
    public void test4(){
       ProductMessageExample example = new ProductMessageExample();
        List<ProductMessage> list = productMessageDao.selectByExample(example);
        System.out.println(list);
    }
    }


