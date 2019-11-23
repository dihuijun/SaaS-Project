package com.itheima.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.commons.utils.BeanMapUtils;
import com.itheima.domain.cargo.*;
import com.itheima.domain.vo.ExportProductVo;
import com.itheima.domain.vo.ExportResult;
import com.itheima.domain.vo.ExportVo;
import com.itheima.service.cargo.ContractService;
import com.itheima.service.cargo.ExportProductService;
import com.itheima.service.cargo.ExportService;
import com.itheima.web.controller.BaseController;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cargo/export")
public class ExportController extends BaseController {

    @Reference
    private ExportService exportService;

    @Reference
    private ContractService contractService;

    @Reference
    private ExportProductService exportProductService;

    //根据状态为1也就是查询购销合同已经提交的分页显示到页面上
    @RequestMapping("/contractList")
    public String contractList(
            @RequestParam(defaultValue = "1")int page,
            @RequestParam(defaultValue = "5")int size
    ){
        ContractExample contractExample = new ContractExample();
        ContractExample.Criteria criteria = contractExample.createCriteria();
        criteria.andStateEqualTo(1);
        criteria.andCompanyIdEqualTo(companyId);
        PageInfo pageInfo = contractService.findAll(contractExample, page, size);
        request.setAttribute("page",pageInfo);
        return "cargo/export/export-contractList";
    }


    //点击报运 然后转向页面
    @RequestMapping("/toExport")
    public String toExport(String id){
        request.setAttribute("id",id);
        return "cargo/export/export-toExport";
    }


    //根据报运上来的id去添加其需要在海关那里存在的信息 比如这个商品的毛重等等
    @RequestMapping("/edit")
    public String edit(Export export,String contractids){
        //传过来的需要报运的合同的合同id集合
        export.setContractIds(contractids);
        export.setCompanyId(companyId);
        export.setCompanyName(companyName);

        //判断一下传过来的报运单id是否为空
        if (StringUtils.isEmpty(export.getId())){
            exportService.save(export);
        }else {
            exportService.update(export);
        }

        //将报运单填写完整后再次转到报运的页面
        return "redirect:/cargo/export/list.do";
    }

    //查询所有的报运单，分页显示
    @RequestMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1")int page,
            @RequestParam(defaultValue = "5")int size
    ){
        ExportExample exportExample = new ExportExample();
        ExportExample.Criteria criteria = exportExample.createCriteria();
        criteria.andCompanyIdEqualTo(companyId);
        PageInfo pageInfo = exportService.findAll(exportExample, page, size);
        request.setAttribute("page",pageInfo);
        return "cargo/export/export-list";
    }

    @RequestMapping(value = "/toUpdate")
    public String toUpdate(String id){
        Export export = exportService.findById(id);
        request.setAttribute("export", export);

        ExportProductExample exportProductExample = new ExportProductExample();
        ExportProductExample.Criteria criteria = exportProductExample.createCriteria();
        criteria.andExportIdEqualTo(id);
        List<ExportProduct> list = exportProductService.findAll(exportProductExample);
        request.setAttribute("eps", list);

        return "cargo/export/export-update";
    }
    @RequestMapping(value = "/toView")
    public String toView(String id){
        Export export = exportService.findById(id);
        request.setAttribute("export", export);
        return "cargo/export/export-view";
    }

    @RequestMapping("/cancel")
    public String cancel(String id){
        //根据报运单id去查询报运单的实体
        Export export = exportService.findById(id);
        //设置报运状态为1
        export.setState(0);
        //更新报运单
        exportService.update(export);
        //重定向页面
        return "redirect:/cargo/export/list.do";
    }


    @RequestMapping("/submit")
    public String submit(String id){
        //根据报运单id去查询报运单的实体
        Export export = exportService.findById(id);
        //设置报运状态为1
        export.setState(1);
        //更新报运单
        exportService.update(export);
        //重定向页面
        return "redirect:/cargo/export/list.do";
    }

    //电子保运的处理方法
    @RequestMapping("/exportE")
    public String exportE(String id){
        //通过id查询到报运单的实体
        Export export = exportService.findById(id);
        //通过实体转换成海关需要的数据  主数据
        //报运单中的数据通过BEanUtils复制
        ExportVo exportVo = new ExportVo();
        BeanUtils.copyProperties(export,exportVo);
        //报运单的货物
        ExportProductExample exportProductExample = new ExportProductExample();
        ExportProductExample.Criteria criteria = exportProductExample.createCriteria();
        criteria.andExportIdEqualTo(export.getId());
        List<ExportProduct> exportProductList = exportProductService.findAll(exportProductExample);
        List<ExportProductVo> exportProductVos = new ArrayList<>();
        //报运单的货物转换为海关需要的
        for (ExportProduct exportProduct : exportProductList) {
            ExportProductVo exportProductVo = new ExportProductVo();
            BeanUtils.copyProperties(exportProduct,exportProductVo);
            exportProductVo.setExportId(id);
            exportProductVo.setExportProductId(exportProduct.getId());
            exportProductVos.add(exportProductVo);
        }
        //主数据和商品数据合并
        exportVo.setProducts(exportProductVos);

        //海关的接口调用，将数据传递过去
        WebClient wc = WebClient.create("http://localhost:8088/ws/export/user");
        wc.post(exportVo);
        //调用海关接口，第二次，id去查海关返回的数据
        wc = WebClient.create("http://localhost:8088/ws/export/user/"+id);
        ExportResult result = wc.get(ExportResult.class);

        //更新数据
        exportService.updateE(result);
        return "redirect:/cargo/export/list.do";
    }


    //报运表根据模板下载
    @RequestMapping("/exportPdf")
    public void exportPdf(String id) throws Exception{
        //通过要下载的id查询出报运表的内容
        Export export = exportService.findById(id);

        //将实体类转化为map,使用工具类
        Map<String ,Object> exportMap = BeanMapUtils.beanToMap(export);

        //通过id查询出来报运的商品信息
        ExportProductExample exportProductExample = new ExportProductExample();
        ExportProductExample.Criteria criteria = exportProductExample.createCriteria();
        criteria.andExportIdEqualTo(id);
        List<ExportProduct> list = exportProductService.findAll(exportProductExample);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);

        String path = session.getServletContext().getRealPath("/")+"/jasper/export.jsper";
        JasperPrint jasperPrint = JasperFillManager.fillReport(path,exportMap,dataSource);
        JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());
    }


}
