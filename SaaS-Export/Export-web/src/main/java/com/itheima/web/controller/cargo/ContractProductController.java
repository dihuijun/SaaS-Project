package com.itheima.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.commons.utils.DownloadUtil;
import com.itheima.commons.utils.UploadUtil;
import com.itheima.domain.cargo.*;
import com.itheima.domain.vo.ContractProductVo;
import com.itheima.service.cargo.ContractProductService;
import com.itheima.service.cargo.FactoryService;
import com.itheima.service.cargo.ProductMsgService;
import com.itheima.web.controller.BaseController;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("/cargo/contractProduct")
public class ContractProductController extends BaseController {

    @Reference
    private FactoryService factoryService;

    @Reference
    private ContractProductService contractProductService;

    @Reference
    private ProductMsgService productMsgService;



    //查询当前合同的所有货物
    @RequestMapping("/list")
    public String list(String contractId,
                       @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "5") int size
    ) {

        //查询所有厂家，而且cType必须为货物的
        FactoryExample factoryExample = new FactoryExample();
        FactoryExample.Criteria criteria = factoryExample.createCriteria();
        criteria.andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList", factoryList);

        //查询所有货号并且封装到前台页面
        List<ProductMsg> productMsgList = productMsgService.findAll();
        System.out.println(productMsgList);
        request.setAttribute("productList",productMsgList);


        //查询所有货物
        ContractProductExample contractProductExample = new ContractProductExample();
        ContractProductExample.Criteria criteria1 = contractProductExample.createCriteria();
        criteria1.andContractIdEqualTo(contractId);
        PageInfo info = contractProductService.findAll(contractProductExample, page, size);
        request.setAttribute("page", info);
        request.setAttribute("contractId", contractId);
        return "/cargo/product/product-list";
    }

    //根据货号查询货物的详情封装到前台
    @RequestMapping("/findById")
    public @ResponseBody String findById(String productNo){
        ProductMsg productMsg = productMsgService.findByProductId(productNo);
        String loadingRate = productMsg.getLoadingRate();
        System.out.println(loadingRate);
        return loadingRate;
    }

    @RequestMapping("/edit")
    public String edit(String contractId, ContractProduct contractProduct, MultipartFile productPhoto) throws IOException {
        contractProduct.setCompanyId(companyId);
        contractProduct.setCompanyName(companyName);

        if (!productPhoto.isEmpty()) {
            String imgUrl = new UploadUtil().upLoad(productPhoto.getBytes());
            contractProduct.setProductImage(imgUrl);
        }


        if (StringUtils.isEmpty(contractProduct.getId())) {
            contractProductService.save(contractProduct);
        } else {
            contractProductService.update(contractProduct);
        }

        return "redirect:/cargo/contractProduct/list.do?contractId=" + contractId;
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        FactoryExample factoryExample = new FactoryExample();
        FactoryExample.Criteria criteria = factoryExample.createCriteria();
        criteria.andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(factoryExample);

        request.setAttribute("factoryList", factoryList);

        ContractProduct contractProduct = contractProductService.findById(id);
        request.setAttribute("contractProduct", contractProduct);
        return "cargo/product/product-update";
    }

    @RequestMapping(value = "/delete")
    public String delete(String id, String contractId) {
        contractProductService.delete(id);
        return "redirect:/cargo/contractProduct/list.do?contractId=" + contractId;
    }


    //上传货物，通过Excel表
    @RequestMapping("/toImport")
    public String toImport(String contractId) {
        request.setAttribute("contractId", contractId);
        return "cargo/product/product-import";
    }


    @RequestMapping("/import")
    public String importExcel(String contractId, MultipartFile file) throws IOException {

        //先接受一个用户上传的Excel表的流化文件
        //创建工作簿
        Workbook wb = new XSSFWorkbook(file.getInputStream());//2007及以上版本
        //创建页
        Sheet sheet = wb.getSheetAt(0);//从0开始为第一页

        Object[] strings = new Object[10];
        //循环其中的数据
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            for (int j = 1; j < 10; j++) {
                Cell cell = row.getCell(j);
                strings[j] = getCellValue(cell);
            }
        }

        ContractProduct contractProduct = new ContractProduct(strings, companyId, companyName);
        contractProduct.setContractId(contractId);
        contractProductService.save(contractProduct);


        return "redirect:/cargo/contractProduct/list.do?contractId=" + contractId;
    }

    public Object getCellValue(Cell cell) {
        Object object = new Object();

        switch (cell.getCellType()) {
            case STRING:
                object = cell.getStringCellValue();
                break;
            case BOOLEAN:
                object = cell.getBooleanCellValue();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    object = new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
                } else {
                    object = cell.getNumericCellValue();
                }
                break;
            case FORMULA:
                break;
        }

        return object;
    }

    /*//大标题的样式
    public CellStyle bigTitle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);//字体加粗
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);                //横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);        //纵向居中
        style.setBorderTop(BorderStyle.THIN);                        //上细线
        style.setBorderBottom(BorderStyle.THIN);                    //下细线
        style.setBorderLeft(BorderStyle.THIN);                        //左细线
        style.setBorderRight(BorderStyle.THIN);                        //右细线
        return style;
    }

    //小标题的样式
    public CellStyle title(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);                //横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);        //纵向居中
        style.setBorderTop(BorderStyle.THIN);                        //上细线
        style.setBorderBottom(BorderStyle.THIN);                    //下细线
        style.setBorderLeft(BorderStyle.THIN);                        //左细线
        style.setBorderRight(BorderStyle.THIN);                        //右细线
        return style;
    }

    //文字样式
    public CellStyle text(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 10);

        style.setFont(font);

        style.setAlignment(HorizontalAlignment.LEFT);                //横向居左
        style.setVerticalAlignment(VerticalAlignment.CENTER);        //纵向居中
        style.setBorderTop(BorderStyle.THIN);                        //上细线
        style.setBorderBottom(BorderStyle.THIN);                    //下细线
        style.setBorderLeft(BorderStyle.THIN);                        //左细线
        style.setBorderRight(BorderStyle.THIN);                        //右细线

        return style;
    }*/
}