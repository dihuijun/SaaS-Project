package com.itheima.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.commons.utils.DownloadUtil;
import com.itheima.domain.cargo.Contract;
import com.itheima.domain.cargo.ContractExample;
import com.itheima.domain.vo.ContractProductVo;
import com.itheima.service.cargo.ContractProductService;
import com.itheima.service.cargo.ContractService;
import com.itheima.web.controller.BaseController;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("/cargo/contract")
public class ContractController extends BaseController {

    @Reference
    private ContractService contractService;

    @Reference
    private ContractProductService contractProductService;


    @RequestMapping("/list")
    public String list(
                       @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "5") int size
                       ){
        ContractExample example = new ContractExample();
        ContractExample.Criteria criteria = example.createCriteria();
        criteria.andCompanyIdEqualTo(companyId);
        //查询的时候想要让每个创建合同的员工都只能看到自己所建立过的合同，并且主管可以看到其管理部门的
        //所有合同
        if (loginUser.getDegree()==3){
            //这个时候是主管,所以查询的时候就带上存在degree=3的这个记录
            criteria.andCreateDeptEqualTo(loginUser.getDeptId());
        }else if (loginUser.getDegree()==4){
            //如果只是一个普通的员工  那么就查询这个普通员工deptid下的合同
            criteria.andCreateDeptEqualTo(loginUser.getDeptId());
        }else if (loginUser.getDegree()==2){
            criteria.andCreateDeptEqualTo(loginUser.getDeptId()+"%");
        }
        PageInfo info = contractService.findAll(example, page, size);
        request.setAttribute("page",info);
        return "/cargo/contract/contract-list";
    }

    @RequestMapping("/toAdd")
    public String toAdd(){
        return "/cargo/contract/contract-add";
    }

    @RequestMapping("/edit")
    public String edit(Contract contract){
        contract.setCompanyId(companyId);
        contract.setCompanyName(companyName);

        if (StringUtils.isEmpty(contract.getId())){
            //新增的时候，想要控制新增合同的这个人员只能看到自己新增的合同以及历史记录，并不能看到别人的
            //写入数据库的时候将其现在登录的用户存到数据库中
            contract.setCreateBy(loginUser.getId());
            contract.setCreateDept(loginUser.getDeptId());
            contractService.save(contract);
        }else {
            contractService.update(contract);
        }

        return "redirect:/cargo/contract/list.do";

    }

    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        Contract contract = contractService.findById(id);
        request.setAttribute("contract",contract);
        return "/cargo/contract/contract-update";
    }

    @RequestMapping("/delete")
    public String delete(String id){
        contractService.delete(id);
        return "redirect:/cargo/contract/list.do";
    }

    @RequestMapping(value = "/submit")
    public String submit(String id){
        Contract contract = contractService.findById(id);
        contract.setState(1);
        contractService.update(contract);
        return "redirect:/cargo/contract/list.do";
    }

    @RequestMapping(value = "/cancel")
    public String cancel(String id){
        Contract contract = contractService.findById(id);
        contract.setState(0);
        contractService.update(contract);
        return "redirect:/cargo/contract/list.do";
    }

    //跳转到出货打印页面
    @RequestMapping("/print")
    public String print() {
        return "cargo/print/contract-print";
    }

    @RequestMapping("/printExcelByTemplate")
    public void printExcelByTemplate(String inputDate) throws IOException {

        //前台页面传的是一个inputDate数据，因为是根据日期来选择的
        List<ContractProductVo> vos = contractProductService.findByShipTime(inputDate);

        String path = session.getServletContext().getRealPath("/")+"/make/xlsprint/tOUTPRODUCT.xlsx";

        //打开模板的Excel表
        Workbook wb = new XSSFWorkbook(path);
        Sheet sheet = wb.getSheetAt(0);

        //大标题
        Row row = sheet.getRow(0);
        Cell cell = row.getCell(1);
        cell.setCellValue(inputDate.replaceAll("-","年")+"月份出货表");

        //内容
        row = sheet.getRow(2);
        CellStyle css[] = new CellStyle[9];

        for (int i = 1; i < 9; i++) {
            cell = row.getCell(i);
            css[i] = cell.getCellStyle();
        }

        int index = 2;
        for (ContractProductVo vo : vos) {

            //vo取数据
            Row row1 = sheet.createRow(index);
            //vo逐个进行给单元格赋值
            cell = row1.createCell(1);
            cell.setCellValue(vo.getCustomName());
            cell.setCellStyle(css[1]);

            cell = row1.createCell(2);
            cell.setCellValue(vo.getContractNo());
            cell.setCellStyle(css[2]);

            cell = row1.createCell(3);
            cell.setCellValue(vo.getProductNo());
            cell.setCellStyle(css[3]);


            cell = row1.createCell(4);
            if(vo.getCnumber()==null){
                cell.setCellValue(0);
            }else{
                cell.setCellValue(vo.getCnumber());
            }
            cell.setCellStyle(css[4]);


            cell = row1.createCell(5);
            cell.setCellValue(vo.getFactoryName());
            cell.setCellStyle(css[5]);

            cell = row1.createCell(6);
            cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(vo.getDeliveryPeriod()));
            cell.setCellStyle(css[6]);

            cell = row1.createCell(7);
            cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(vo.getShipTime()));
            cell.setCellStyle(css[7]);

            cell = row1.createCell(8);
            cell.setCellValue(vo.getTradeTerms());
            cell.setCellStyle(css[8]);


            index++;

        }
        //下载EXCEL表
        //ByteArrayOutputStream byteArrayOutputStream, HttpServletResponse response, String returnName
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        wb.write(outputStream);
        new DownloadUtil().download(outputStream, response, "出货表.xlsx");

    }
}
